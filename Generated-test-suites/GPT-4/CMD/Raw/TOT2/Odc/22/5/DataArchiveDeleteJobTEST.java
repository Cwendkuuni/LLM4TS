import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute_nullScheduleTaskEntity() {
        // Arrange 
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        Mockito.when(context.getResult()).thenReturn(null);

        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        // Act
        job.execute(context);
        // Assert
        // Verify the logger (not shown here) logs expected warning message
    }
}