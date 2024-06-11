import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @InjectMocks 
    PartitionPlanJob partitionPlanJob;
    
    @Mock
    JobExecutionContext jobExecutionContext;

    @Mock
    PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    PartitionPlanService partitionPlanService;

    @Mock
    ScheduleService scheduleService;
   
    /* Rest of mock objects to be created here */

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        try {
            doNothing().when(scheduleService).terminate(any());
            doNothing().when(partitionPlanTaskService).executePartitionPlan(any(), any());
            partitionPlanJob.execute(jobExecutionContext);  
        } catch (Exception e) {
            fail("No exception should be thrown");
        }
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(any(), any());
    }
    
    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
    
    /* Repeat above steps to generate more tests for edge cases and exceptions */

}