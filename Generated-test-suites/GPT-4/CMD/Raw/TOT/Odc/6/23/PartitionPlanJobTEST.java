
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private JobExecutionContext context;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
        context = Mockito.mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute() {
        try {
            partitionPlanJob.execute(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        verify(context, times(1)).getScheduleId();
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}
