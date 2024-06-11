import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ScheduleTaskEntity scheduleTaskEntity;
    private DataArchiveRollbackParameters rollbackParameters;

    @Test
    public void testExecute_DataArchiveTaskFound_TasksExecutedSuccessfully() throws Exception {
        // Arrange
        rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setStatus(TaskStatus.DONE);
        dlmTasks.add(dlmTask1);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class)))
                .thenReturn(true);

        // Act
        dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

        // Assert
        Assertions.assertFalse(scheduleTaskRepository.updateStatusById(anyLong(), TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound_TaskStatusUpdatedToFailed() throws Exception {
        // Arrange
        rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // Act
        dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

        // Assert
        Assertions.assertTrue(scheduleTaskRepository.updateStatusById(anyLong(), TaskStatus.FAILED));
    }

    @Test
    public void testExecute_TaskExecutionFailed() throws Exception {
        // Arrange
        rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setStatus(TaskStatus.FAILED);
        dlmTasks.add(dlmTask1);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class)))
                .thenReturn(true);

        // Act and Assert
        Assertions.assertThrows(JobExecutionException.class, () -> dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class)));
    }
}