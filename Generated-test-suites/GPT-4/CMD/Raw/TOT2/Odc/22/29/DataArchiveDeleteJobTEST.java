import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import org.quartz.JobExecutionContext;

public class DataArchiveDeleteJobTest {

@Test
public void testExecute() {
    DataArchiveDeleteJob deleteJob = new DataArchiveDeleteJob();
    JobExecutionContext mockContext = Mockito.mock(JobExecutionContext.class);

    //  Typical use case where there is a data archive task
    Mockito.when(mockContext.getResult()).thenReturn(new ScheduleTaskEntity());
    
    // Mock edge case where  dataArchiveTaskOption is not present
    // Mock other edge cases
    // Mock error scenarios

    deleteJob.execute(mockContext);
    
    // Assert that the status was updated
    // Assert other outcomes
    // Repeat the process for edge cases and error scenarios
}
}