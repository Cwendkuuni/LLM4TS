import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.model.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Test class for DataArchiveRollbackJob
 */
@Slf4j
public class DataArchiveRollbackJobTest {

    private ScheduleTaskEntity scheduleTask;
    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        objectMapper = new ObjectMapper();
        dataArchiveRollbackJob = new DataArchiveRollbackJob() {
            @Override
            ScheduleTaskRepository getScheduleTaskRepository() {
                return scheduleTaskRepository;
            }
        };
    }

    /**
     * Test method for execute(JobExecutionContext)
     */
    @Test
    public void testExecute() {
        // Given
        ScheduleTaskEntity taskEntity = createTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTask));

        // When
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), eq(TaskStatus.DONE));
    }

    /**
     * Test method for execute(JobExecutionContext) with data archive task not found
     */
    @Test
    public void testExecuteWithDataArchiveTaskNotFound() {
        // Given
        ScheduleTaskEntity taskEntity = createTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // When
        dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), eq(TaskStatus.FAILED));
    }

    /**
     * Test method for executeTask(Long, List)
     */
    @Test
    public void testExecuteTask() {
        // Given
        ScheduleTaskEntity taskEntity = createTaskEntity();
        List<DlmTask> dlmTasks = new ArrayList<>();
        dataArchiveRollbackJob.executeTask(taskEntity.getId(), dlmTasks);

        // Then
        verify(dataArchiveRollbackJob, times(1)).execute(eq(taskEntity.getId()), eq(dlmTasks));
    }

    /**
     * Test method for getTaskStatus(List)
     */
    @Test
    public void testGetTaskStatus() {
        // Given
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setStatus(TaskStatus.DONE);
        dlmTasks.add(dlmTask1);
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setStatus(TaskStatus.FAILED);
        dlmTasks.add(dlmTask2);

        // When
        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

        // Then
        assertEquals(TaskStatus.FAILED, taskStatus);
    }

    /**
     * Helper method to create a ScheduleTaskEntity
     *
     * @return ScheduleTaskEntity
     */
    private ScheduleTaskEntity createTaskEntity() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        return scheduleTaskEntity;
    }
}