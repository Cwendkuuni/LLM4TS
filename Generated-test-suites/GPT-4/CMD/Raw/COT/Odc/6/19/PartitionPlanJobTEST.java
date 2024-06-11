import org.junit.Test;
import org.quartz.JobExecutionContext;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

public class PartitionPlanJobTest {

    @Test
    public void testExecute() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        JobExecutionContext context  = mock(JobExecutionContext.class);
        
        when(context.getScheduleId()).thenReturn(null);
        
        partitionPlanJob.execute(context);

        // add assertions and verify calls on your mock objects to complete the test
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteWithException() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        JobExecutionContext context  = mock(JobExecutionContext.class);
          
        doThrow(SchedulerException.class).when(context).terminate();
        
        partitionPlanJob.execute(context);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.interrupt();
    }
}