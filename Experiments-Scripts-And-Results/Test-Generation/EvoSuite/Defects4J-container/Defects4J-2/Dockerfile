#Container image for running EvoSuite

FROM openjdk:8-jdk



#############################################################################

# Requirements

#############################################################################


# Java version

#ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64


#WORKDIR /usr/evosuite-code-coverage

WORKDIR /usr/ressources

#VOLUME /evosuite

COPY ressources /usr/ressources

RUN chmod +x /usr/ressources/*.sh


# Copy the evosuite jar from the builder to this container

#COPY Docker/EVOSUITE_VERSIONS/evosuite-master-1.2.1-SNAPSHOT-ALL.jar evosuite_all.jar



# The executable is evosuite

#CMD ["./script.sh"]

#RUN ./start.sh

ENTRYPOINT ["./start.sh"]
