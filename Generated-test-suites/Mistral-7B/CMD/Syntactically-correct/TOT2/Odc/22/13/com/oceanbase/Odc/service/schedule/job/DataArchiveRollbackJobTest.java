import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @SuppressWarnings("unchecked")
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_WhenScheduleTaskEntityExists_ThenRollbackTasksExecutedSuccessfully() throws Exception {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setId(1L);
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnit.setJobType(JobType.ROLLBACK);
        taskUnits.add(taskUnit);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));

        // When
        JobExecutionContext context = mock(JobExecutionContext.class);
        dataArchiveRollbackJob.execute(context);

        // Then
        verifyTasksExecutedSuccessfully(taskUnits);
    }

    @Test
    public void testExecute_WhenScheduleTaskEntityDoesNotExist_ThenRollbackJobFails() throws Exception {
        // Given
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        // When
        JobExecutionContext context = mock(JobExecutionContext.class);

        try {
            dataArchiveRollbackJob.execute(context);
            fail("Expected JobExecutionException");
        } catch (JobExecutionException e) {
            // Then
            verifyNoTasksExecuted(taskUnits);
        }

        verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
    }

    private void verifyTasksExecutedSuccessfully(List<DlmTask> taskUnits) {
        for (DlmTask taskUnit : taskUnits) {
            verify(scheduleTaskRepository).executeTask(taskUnit.getId(), taskUnit);
            verify(taskUnit).setStatus(TaskStatus.DONE);
        }
    }

    private void verifyNoTasksExecuted(List<DlmTask> taskUnits) {
        for (DlmTask taskUnit : taskUnits) {
            verify(scheduleTaskRepository, never()).executeTask(taskUnit.getId(), taskUnit);
        }
    }
}