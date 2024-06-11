import org.junit.Test;
import static org.mockito.Mockito.*;
import org.quartz.JobExecutionContext;

public class DataArchiveDeleteJobTest {
    @Test
    public void test_execute() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);

        // Setup mock behavior for context.getResult method
        when(context.getResult()).thenReturn(new ScheduleTaskEntity()); // or some other setup depending on your needs

        try {
            dataArchiveDeleteJob.execute(context);
        } catch (Exception e) {
            fail("Error occurred: " + e.getMessage());
        }
    }
}