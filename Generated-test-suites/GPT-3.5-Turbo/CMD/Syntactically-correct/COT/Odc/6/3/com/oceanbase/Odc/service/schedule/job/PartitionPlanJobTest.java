import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

public class PartitionPlanJobTest {

    @Mock
    private JobExecutionContext context;

    @Test
    public void testExecute() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        try {
            partitionPlanJob.execute(context);
        } catch (Exception e) {
            fail("Exception thrown during execution");
        }
    }
}