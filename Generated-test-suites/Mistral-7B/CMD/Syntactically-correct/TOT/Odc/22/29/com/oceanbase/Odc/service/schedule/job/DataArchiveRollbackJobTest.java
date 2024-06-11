import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_validDataArchiveTask_tasksExecutedSuccessfully() throws JobExecutionException {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> tasks = Collections.singletonList(new DlmTask());

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        doNothing().when(dataArchiveRollbackJob).executeTask(1L, tasks);

        // When
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.DONE);
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound_taskStatusUpdatedToFailed() {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // When
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_tasksExecutionFailed_taskStatusUpdatedToFailed() {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setResultJson(JsonUtils.toJsonString(Collections.singletonList(new DlmTask())));

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        List<DlmTask> tasks = Collections.singletonList(new DlmTask());
        tasks.get(0).setId(1L);
        tasks.get(0).setStatus(TaskStatus.FAILED);

        doThrow(new JobExecutionException("Test error"))
                .when(dataArchiveRollbackJob).executeTask(1L, tasks);

        // When
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);

        ObjectMapper objectMapper = new ObjectMapper();
        List<DlmTask> actualTasks = objectMapper.readValue(dataArchiveTask.getResultJson(),
                new TypeReference<List<DlmTask>>() {});
        assertEquals(tasks.get(0).getStatus(), actualTasks.get(0).getStatus());
    }

    @Test
    public void testExecute_tasksExecutionSuccessfully_taskStatusUpdatedToDone() {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setResultJson(JsonUtils.toJsonString(Collections.singletonList(new DlmTask())));

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        List<DlmTask> tasks = Collections.singletonList(new DlmTask());
        tasks.get(0).setId(1L);
        tasks.get(0).setStatus(TaskStatus.DONE);

        doNothing().when(dataArchiveRollbackJob).executeTask(1L, tasks);

        // When
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.DONE);

        ObjectMapper objectMapper = new ObjectMapper();
        List<DlmTask> actualTasks = objectMapper.readValue(dataArchiveTask.getResultJson(),
                new TypeReference<List<DlmTask>>() {});
        assertEquals(tasks.get(0).getStatus(), actualTasks.get(0).getStatus());
    }
}