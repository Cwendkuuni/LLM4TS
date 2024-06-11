package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {

        JobExecutionContext context = mock(JobExecutionContext.class);

        DataArchiveRollbackJob rollbackJob = new DataArchiveRollbackJob();

        rollbackJob.execute(context);

    }

    @Test(expected = NullPointerException.class)  
    public void testExecute_NullContext() {
        DataArchiveRollbackJob rollbackJob = new DataArchiveRollbackJob();
        rollbackJob.execute(null);
    } 

    @Test  
    public void testExecute_NullEntity() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(null);

        DataArchiveRollbackJob rollbackJob = new DataArchiveRollbackJob();
        rollbackJob.execute(context);
    }

    @Test
    public void testExecute_NoDataArchiveTaskAvailable() {
       // Test implementation here.
    }

}
