package com.oceanbase.odc.service.schedule.job;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {
   
    @InjectMocks
    private PartitionPlanJob partitionPlanJob;
    
    @Mock
    private JobExecutionContext context;
        
    @Before
    public void setup() throws Exception {
        // Setup the test by initializing all mocks 
    }
       
    @Test(expected = RuntimeException.class)
    public void testExecuteWithSchedulerException() {
        // Edge case: When the scheduler throws an exception, it should be caught and logged
        when(ScheduleTaskUtils.getScheduleId(any())).thenThrow(new SchedulerException());
        partitionPlanJob.execute(context);
    }
  
    @Test(expected = UnExpectedException.class)
    public void testInterruptThrowsException() {
        // The interrupt method is supposed to throw an UnExpectedException
        partitionPlanJob.interrupt();
    }
}