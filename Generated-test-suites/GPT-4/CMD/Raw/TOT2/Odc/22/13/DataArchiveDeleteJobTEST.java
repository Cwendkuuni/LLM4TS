import org.junit.Test;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.mock;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {
        // Prepare objects and mock services
        JobExecutionContext context = mock(JobExecutionContext.class);
        // calling real method with mocked context
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(context);
        // Assertions here
    }

    // When `dataArchiveTaskOption` is not present
    @Test (expected = NoSuchElementException.class)
    public void execute_dataArchiveTask_NotPresent_ShouldThrowException() {
        // Prepare objects and mock services that simulate 'dataArchiveTaskOption' not present
    }

    // When `dataArchiveTask.getStatus()` is not equal to DONE
    @Test
    public void execute_dataArchiveTaskStatus_NotDone_ShouldFail() {
        // Prepare objects and mock services that simulate 'dataArchiveTaskStatus' not equal to DONE
    }
}