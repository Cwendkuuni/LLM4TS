import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("");
        taskEntity.setId(1L);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        when(context.getResult()).thenReturn(taskEntity);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(new ScheduleTaskEntity());
        when(dataArchiveTaskOption.isPresent()).thenReturn(true);
        when(scheduleTaskRepository.findById(2L)).thenReturn(dataArchiveTaskOption);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setResultJson("[]");

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTask.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask);

        dataArchiveTask.setResultJson("[{}]");
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {}))
                .thenReturn(taskUnits);

        dataArchiveRollbackJob.execute(context);

        TaskStatus expectedTaskStatus = TaskStatus.DONE;
        assertEquals(expectedTaskStatus, taskEntity.getStatus());
    }
}