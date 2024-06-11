import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import org.quartz.JobExecutionContext;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);       
        new DataArchiveDeleteJob().execute(context);
        
        verify(context).getResult();
    }

    @Test
    public void testExecuteWithNullTaskEntity() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(null);

        new DataArchiveDeleteJob().execute(context);
    }

    @Test
    public void testExecuteWithInvalidJson() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("INVALID_JSON");
        
        when(context.getResult()).thenReturn(taskEntity);

        new DataArchiveDeleteJob().execute(context);
    }

    @Test
    public void testExecuteWithTaskNotFound() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{}");
        
        when(context.getResult()).thenReturn(taskEntity);

        new DataArchiveDeleteJob().execute(context);
    }

    @Test
    public void testExecuteWithTaskNotDone() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{}");
        taskEntity.setStatus(TaskStatus.DOING);
        
        when(context.getResult()).thenReturn(taskEntity);

        new DataArchiveDeleteJob().execute(context);
    }

    @Test
    public void testExecuteWithInvalidTaskJson() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{}");
        taskEntity.setStatus(TaskStatus.DONE);
        
        when(context.getResult()).thenReturn(taskEntity);

        new DataArchiveDeleteJob().execute(context);
    }
}