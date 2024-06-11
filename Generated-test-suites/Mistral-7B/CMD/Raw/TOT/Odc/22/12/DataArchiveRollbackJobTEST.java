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
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(Mockito.MockitoJUnit.MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobRepository jobRepository;

    @Test
    public void testExecute_ValidDataArchiveTask_ShouldRollbackTasksSuccessfully() throws JobExecutionException {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String jsonString = objectMapper.writeValueAsString(rollbackParameters);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(jsonString);
        when(jobRepository.findById(any(Long.class))).thenReturn(Optional.of(taskEntity));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setSourceDatabaseId(1L);
        dlmTask1.setTargetDatabaseId(2L);
        dlmTask1.setStatus(TaskStatus.DONE);
        dlmTasks.add(dlmTask1);
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setSourceDatabaseId(2L);
        dlmTask2.setTargetDatabaseId(1L);
        dlmTask2.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask2);

        when(jobRepository.executeTask(any(Long.class), any(List.class))).thenAnswer(invocation -> {
            // Simulate task execution
            return null;
        });

        // When
        dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

        // Then
        TaskStatus expectedTaskStatus = TaskStatus.DONE;
        Assertions.assertEquals(expectedTaskStatus, taskEntity.getStatus());
    }

    @Test
    public void testExecute_InvalidDataArchiveTask_ShouldReturnTaskFailed() {
        // Given
        when(jobRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(JobExecutionException.class, () -> dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class)));
        TaskStatus expectedTaskStatus = TaskStatus.FAILED;
        Assertions.assertEquals(expectedTaskStatus, taskEntity.getStatus());
    }

    @Test
    public void testExecute_DataArchiveTaskWithFailedTask_ShouldReturnTaskFailed() {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String jsonString = objectMapper.writeValueAsString(rollbackParameters);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(jsonString);
        when(jobRepository.findById(any(Long.class))).thenReturn(Optional.of(taskEntity));

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setSourceDatabaseId(1L);
        dlmTask1.setTargetDatabaseId(2L);
        dlmTask1.setStatus(TaskStatus.DONE);
        dlmTasks.add(dlmTask1);
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setSourceDatabaseId(2L);
        dlmTask2.setTargetDatabaseId(1L);
        dlmTask2.setStatus(TaskStatus.FAILED);
        dlmTasks.add(dlmTask2);

        when(jobRepository.executeTask(any(Long.class), any(List.class))).thenAnswer(invocation -> {
            // Simulate task execution
            return null;
        });

        // When
        Assertions.assertThrows(JobExecutionException.class, () -> dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class)));
        TaskStatus expectedTaskStatus = TaskStatus.FAILED;
        Assertions.assertEquals(expectedTaskStatus, taskEntity.getStatus());
    }
}