import static org.mockito.Mockito.*;
import org.junit.Test;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute_ValidDataArchiveTaskId() {
        // Create mock objects and set up test data
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 123}");
        when(context.getResult()).thenReturn(taskEntity);

        // Call the method to be tested
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(context);

        // Verify the expected behavior
        // Add assertions here
    }
}