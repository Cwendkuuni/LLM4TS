import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test method: execute
    @Test
    public void testExecute_TaskEntityPresent_RollbackSuccess() {
        // Given
        ScheduleTaskEntity taskEntity = createMockScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = createMockDlmTask();
        taskUnit1.setId(1L);
        taskUnit1.setSourceDatabaseId(2L);
        taskUnit1.setTargetDatabaseId(1L);
        taskUnit1.setJobType(JobType.BACKUP);
        taskUnits.add(taskUnit1);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), eq(TaskStatus.FAILED)))
                .thenReturn(true);

        // When
        dataArchiveRollbackJob.execute(null);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), eq(TaskStatus.DONE));
    }

    @Test
    public void testExecute_TaskEntityAbsent_RollbackFailed() {
        // Given
        ScheduleTaskEntity taskEntity = createMockScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        // When
        dataArchiveRollbackJob.execute(null);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), eq(TaskStatus.FAILED));
        verify(log, times(1)).warn("Data archive task not found,rollback task fast failed.dataArchiveTaskId=" + rollbackParameters.getDataArchiveTaskId());
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_JsonParsingFailed_RollbackFailed() {
        // Given
        ScheduleTaskEntity taskEntity = createMockScheduleTaskEntity();
        taskEntity.setParametersJson("{invalid json}");

        // When
        dataArchiveRollbackJob.execute(null);

        // Then
        verify(log, times(1)).warn("Failed to parse json data from ScheduleTaskEntity");
        throw new RuntimeException();
    }

    @Test
    public void testExecute_TaskUnitsUpdated() {
        // Given
        ScheduleTaskEntity taskEntity = createMockScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit1 = createMockDlmTask();
        taskUnit1.setId(1L);
        taskUnit1.setSourceDatabaseId(2L);
        taskUnit1.setTargetDatabaseId(1L);
        taskUnit1.setJobType(JobType.BACKUP);
        taskUnits.add(taskUnit1);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));

        // When
        dataArchiveRollbackJob.execute(null);

        // Then
        for (DlmTask task : taskUnits) {
            verify(task).setId(mock(Long.class));
            verify(task).setSourceDatabaseId(task.getTargetDatabaseId());
            verify(task).setTargetDatabaseId(task.getSourceDatabaseId());
            verify(task).setJobType(JobType.ROLLBACK);
            verify(task, times(1)).setStatus(eq(TaskStatus.DONE));
        }
    }

    private ScheduleTaskEntity createMockScheduleTaskEntity() {
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        return taskEntity;
    }

    private DlmTask createMockDlmTask() {
        DlmTask dlmTask = mock(DlmTask.class);
        return dlmTask;
    }
}