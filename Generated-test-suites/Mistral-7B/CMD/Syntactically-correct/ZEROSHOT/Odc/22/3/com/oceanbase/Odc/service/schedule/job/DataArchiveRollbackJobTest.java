import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobExecutionContext;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveRollbackJobTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

    @org.junit.Before
    public void setUp() {
        // Initialize mocks
    }

    @org.junit.Test
    public void testExecute_DataArchiveTaskNotFound() {
        // Given
        String dataArchiveTaskId = "dataArchiveTaskId";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters()));
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);

        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.empty());

        // When & Then
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        try {
            dataArchiveRollbackJob.execute(new JobExecutionContext());
            fail("Expected JobExecutionException to be thrown");
        } catch (JobExecutionException e) {
            verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
        }
    }

    @org.junit.Test
    public void testExecute_DataArchiveTaskFound() {
        // Given
        String dataArchiveTaskId = "dataArchiveTaskId";
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setResultJson(JsonUtils.toJsonString(new ArrayList<>()));
        dataArchiveTask.setJobName("jobName");
        dataArchiveTask.setJobGroup("jobGroup");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        dlmTask.setJobType(JobType.ROLLBACK);
        dlmTask.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask);

        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(1);

        // When
        DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
        dataArchiveRollbackJob.execute(new JobExecutionContext());

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveTask.getId(), TaskStatus.DONE);
    }

    @org.junit.Test
    public void testGetTaskStatus() {
        // Given
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setStatus(TaskStatus.DONE);
        dlmTasks.add(dlmTask1);

        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask2);

        // When
        TaskStatus taskStatus = new DataArchiveRollbackJob().getTaskStatus(dlmTasks);

        // Then
        assertEquals(TaskStatus.PREPARING, taskStatus);
    }
}