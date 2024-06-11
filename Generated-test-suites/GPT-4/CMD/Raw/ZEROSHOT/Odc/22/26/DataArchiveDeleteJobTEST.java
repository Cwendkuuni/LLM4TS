import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import java.util.Arrays;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {
  
    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    
    @Mock
    private JobExecutionContext context;
  
    @Test
    public void testExecute() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }
  
    @Test
    public void testExecuteWithDataArchiveTask() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        DlmTask taskUnit = new DlmTask();
        taskUnit.setStatus(TaskStatus.PREPARING);
        dataArchiveTask.setResultJson(JsonUtils.toJson(Arrays.asList(taskUnit)));

        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), taskUnit.getStatus());
    }
}