
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import java.util.Collections;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import java.util.Optional;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Test
    public void testExecuteMethod_DataArchiveTaskNull() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        when(context.getResult()).thenReturn(taskEntity);

        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteMethod_DataArchiveTaskPresent() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();

        DlmTask dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.PREPARING);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setParametersJson(JsonUtils.toJson(Collections.singletonList(dlmTask)));

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class)); 
    }
}
