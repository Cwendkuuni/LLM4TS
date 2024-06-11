import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static org.mockito.Mockito.mock;

public class PartitionPlanJobTest {

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        try {
            partitionPlanJob.execute(context);
        } catch (JobExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.interrupt();
    }
}