import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.Before;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private JobExecutionContext jobExecutionContext;
    
    @Before
    public void setup() {
        partitionPlanJob = new PartitionPlanJob();
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void test_execute_WithNullContext() {
        partitionPlanJob.execute(null);
    }

    @Test
    public void test_execute_WithNullScheduleId() {
        when(jobExecutionContext.getScheduleId()).thenReturn(null);
        partitionPlanJob.execute(jobExecutionContext);
    }

    @Test
    public void test_interrupt() {
        try {
            partitionPlanJob.interrupt();
        } catch (UnExpectedException ex) {
            assertEquals("UnExpectedException", ex.getMessage());
        }
    }
}

