import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    JobExecutionContext context;

    @Test
    public void executeTest() {
        PartitionPlanJob ppJob = new PartitionPlanJob();
        ppJob.execute(context);
    }

    @Test(expected = UnExpectedException.class)
    public void interruptTest() {
        PartitionPlanJob ppJob = new PartitionPlanJob();
        ppJob.interrupt();
    }
}