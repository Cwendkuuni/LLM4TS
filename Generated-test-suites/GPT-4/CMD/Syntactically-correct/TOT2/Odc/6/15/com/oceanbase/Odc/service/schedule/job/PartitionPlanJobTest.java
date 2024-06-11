package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;

public class PartitionPlanJobTest {

    @Test
    public void testJobExecute() {

        //create JobExecutionContext mock
        JobExecutionContext contextMock = Mockito.mock(JobExecutionContext.class);

        //initialize job
        PartitionPlanJob job = new PartitionPlanJob();
        
        //execute job
        job.execute(contextMock);
        
        //verify that the context was used
        Mockito.verify(contextMock);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {

        //initialize job
        PartitionPlanJob job = new PartitionPlanJob();
        
        //attempt to interrupt job, expecting UnExpectedException
        job.interrupt();
    }
}
