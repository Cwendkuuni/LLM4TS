package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext context;
  
    @Before 
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        // setting up the mock context
        context = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute_NormalScenario() {
        // use mockito to mock returned scheduleTaskEntity and repository behaviours
        // call the method using the mocked context
        // assert that expected behaviours are called
    }

    @Test
    public void testExecute_TaskEntityNotPresent() {
        // use mockito to mock returned scheduleTaskEntity and repository behaviours
        // setup it in a way that no task entity is present within repository
        // call the method using the mocked context
        // assert that expected behaviours are called
    }

    @Test
    public void testExecute_JSONDeserializationError() {
        // use mockito to mock returned scheduleTaskEntity and repository behaviours
        // setup it in a way that JSON deserialization produces an error 
        // call the method using the mocked context
        // assert that expected exception is thrown
    }
}
