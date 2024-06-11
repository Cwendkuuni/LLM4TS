
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity, dataArchiveTask;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        this.dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        DataArchiveRollbackParameters parameters = new DataArchiveRollbackParameters();
        parameters.setDataArchiveTaskId("TestTaskId");
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTask.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask);

        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(parameters));
        Mockito.when(taskEntity.getId()).thenReturn("TestEntityId");
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyString())).thenReturn(Optional.of(dataArchiveTask));
        Mockito.when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));

        dataArchiveRollbackJob.execute(context);
        
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyString(), Mockito.any());
    }

    @Test
    public void testExecuteNoArchiveTask() {
        DataArchiveRollbackParameters parameters = new DataArchiveRollbackParameters();
        parameters.setDataArchiveTaskId("NoTaskId");

        Mockito.when(context.getResult()).thenReturn(taskEntity);
        Mockito.when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(parameters));
        Mockito.when(scheduleTaskRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);
        
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(Mockito.anyString(), Mockito.eq(TaskStatus.FAILED));
    }
}
