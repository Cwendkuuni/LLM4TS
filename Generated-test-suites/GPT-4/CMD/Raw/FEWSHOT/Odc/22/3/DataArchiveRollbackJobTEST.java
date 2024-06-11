import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;
    
    @Mock
    private ScheduleTaskEntity taskEntity;
    
    @Mock
    private DlmTask dlmTask;
    
    @Test
    public void testDataArchiveRollbackJob() {
        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getId()).thenReturn(UUID.randomUUID().toString());
        when(dlmTask.getSourceDatabaseId()).thenReturn(1L);
        when(dlmTask.getStatus()).thenReturn(TaskStatus.PREPARING);

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(dlmTask);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(UUID.randomUUID().toString());

        when(taskEntity.getParametersJson()).thenReturn(rollbackParameters.toJson());
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
        
        job.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), any(TaskStatus.class));
    }
}