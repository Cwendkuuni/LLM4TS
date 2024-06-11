package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;
import org.junit.BeforeClass;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {
    @Mock
    JobExecutionContext context;
    @InjectMocks
    DataArchiveRollbackJob dataArchiveRollbackJob;

    @BeforeClass 
    public static void init() { 
        context = mock(JobExecutionContext.class); 
        dataArchiveRollbackJob = new DataArchiveRollbackJob(); 
    }
    
    @Test
    public void testExecute() {
        // This is a basic use case where you would mock any dependencies and set up your expectations. 
        // You then invoke the method and verify that it behaved as expected
        
        //Here we don't have the actual object of ScheduleTaskEntity and other dependencies,
        //so we can't actually create an instance of it. We can't also mock it as the instance is created inside the method.
        
        //setup
        when(context.getResult()).thenReturn(new ScheduleTaskEntity()); 
        //action
        dataArchiveRollbackJob.execute(context);
        //verify
        verify(context, times(1)).getResult(); 
    }
}
