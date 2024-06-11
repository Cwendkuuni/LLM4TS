
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private JobExecutionContext context;

    @Before
    public void setup() {
        partitionPlanJob = new PartitionPlanJob();
        context = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute() {
        try {
            partitionPlanJob.execute(context);
        } catch (Exception e) {
            Assert.fail("Expected no exception to be thrown");
        }
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_withSchedulerException() {
        doThrow(new SchedulerException()).when(context).terminate(any());
        partitionPlanJob.execute(context);   
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }   
}
