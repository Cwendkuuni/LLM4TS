import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;

public class DataArchiveDeleteJobTest { 

    private DataArchiveDeleteJob dataArchiveDeleteJobInstance;
    private JobExecutionContext context;

    @Before
    public void setUp() {
        dataArchiveDeleteJobInstance = new DataArchiveDeleteJob();
        context = Mockito.mock(JobExecutionContext.class);
        // set-up dependencies here...
    }

    @Test
    public void testExecuteDataArchiveTaskNotFound() {
        // Set-up context and taskEntity
        // ...

        // Call the method
        dataArchiveDeleteJobInstance.execute(context);

        // Assertions to check if status is updated to FAILED.
    }

    @Test
    public void testExecuteDataArchiveTaskNotFinished() {
        // Set up context and taskEntity
        // ...

        // Call the method
        dataArchiveDeleteJobInstance.execute(context);

        // Assertions to check if status is updated to FAILED.
    }

    @Test
    public void testExecuteNormal() {
        // Set up context and taskEntity
        // ...

        // Call the method
        dataArchiveDeleteJobInstance.execute(context);

        // Assertions to check if the operation was successful.
    }
}