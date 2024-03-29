def call(Map pipelineParams) {

    pipeline {
        agent any
           tools {
      // Install the Maven version configured as "M3" and add it to the path.
      maven "maven"
        }
        stages {
            stage('checkout git') {
                steps {
                    git branch: pipelineParams.branch , url: pipelineParams.Url
                }
            }

            stage('build') {
                steps {
                    sh 'mvn clean package -DskipTests=true'
                }
            }

            stage ('test') {
                steps {
                    parallel (
                        "unit tests": { sh 'mvn test' },
                        "integration tests": { sh 'mvn integration-test' }
                    )
                }
            }
             stage('email') {
                steps {
                    mail to: pipelineParams.email, subject: 'Pipeline failed', body: "hahahahahahahaha"
                }
            }

        // post {
        //     failure {
        //         mail to: pipelineParams.email, subject: 'Pipeline failed', body: "${env.BUILD_URL}"
        //     }
        // }
    }
}
}
