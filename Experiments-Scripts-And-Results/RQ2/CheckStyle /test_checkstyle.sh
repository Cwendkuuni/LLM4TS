
#export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
#/usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar checkstyle-10.14.2-all.jar -c /chemin/vers/votre/fichier_de_configuration.xml /chemin/vers/votre/source/code



#java -jar checkstyle-10.14.2-all.jar -c /sun_checks.xml CommandLineTest.java
#java -jar checkstyle-10.14.2-all.jar -c /google_checks.xml CommandLineTest.java


#java -jar checkstyle-10.14.2-all.jar -c /sun_checks.xml AsyncSystemTaskExecutorTest.java
#java -jar checkstyle-10.14.2-all.jar -c /google_checks.xml AsyncSystemTaskExecutorTest.java


/usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar checkstyle-10.14.2-all.jar -c /sun_checks.xml CommandLineTest.java -o=output.txt
/usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar checkstyle-10.14.2-all.jar -c /google_checks.xml CommandLineTest.java


/usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar checkstyle-10.14.2-all.jar -c /sun_checks.xml AsyncSystemTaskExecutorTest.java
/usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar checkstyle-10.14.2-all.jar -c /google_checks.xml AsyncSystemTaskExecutorTest.java

/usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar checkstyle-10.14.2-all.jar
