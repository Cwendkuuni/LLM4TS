import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {
        // Setup
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        scheduleTaskEntity.setParametersJson(dataArchiveClearParameters.toString());
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        // Test
        dataArchiveDeleteJob.execute(context);

        // Verify
        // Add verification statements here
    }
}