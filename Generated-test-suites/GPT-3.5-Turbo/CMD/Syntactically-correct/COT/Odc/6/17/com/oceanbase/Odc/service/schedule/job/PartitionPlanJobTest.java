
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.quartz.JobExecutionContext;

public class PartitionPlanJobTest {

    @Test
    public void testExecute() {
        PartitionPlanJob job = new PartitionPlanJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        job.execute(context);
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob job = new PartitionPlanJob();
        try {
            job.interrupt();
        } catch (Exception e) {
            assertEquals(UnExpectedException.class, e.getClass());
        }
    }
}
