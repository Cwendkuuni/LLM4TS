
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.quartz.JobExecutionContext;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        
        // Mock ScheduleTaskEntity
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(taskEntity);
        
        // Mock DataArchiveClearParameters
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        when(taskEntity.getParametersJson()).thenReturn("{ \"dataArchiveTaskId\": 1 }");
        
        // Mock dataArchiveTaskOption
        when(scheduleTaskRepository.findById(1)).thenReturn(Optional.of(taskEntity));

        // Test the execute method
        dataArchiveDeleteJob.execute(context);
        
        // Add assertions here
    }
}
