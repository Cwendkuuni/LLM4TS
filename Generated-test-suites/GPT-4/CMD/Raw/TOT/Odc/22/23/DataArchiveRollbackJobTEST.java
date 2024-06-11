import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import java.util.Optional;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import java.util.ArrayList;


public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskEntity taskEntity;
    private JobExecutionContext context;
    private DataArchiveRollbackParameters rollbackParameters;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        taskEntity = mock(ScheduleTaskEntity.class);
        context = mock(JobExecutionContext.class);
        rollbackParameters = new DataArchiveRollbackParameters();
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("someParameterJson");
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
    }

    @Test
    public void testExecute() {
        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)). findById(any());       
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), any());
    }
}