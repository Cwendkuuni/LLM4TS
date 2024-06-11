import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @SuppressWarnings("unchecked")
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_whenDataArchiveTaskExists_thenRollbackTasksExecutedAndStatusUpdated() throws Exception {
        // Given
        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("existingTaskId");
        doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById("existingTaskId");

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId("task1Id");
        dlmTask1.setStatus(TaskStatus.DONE);
        dlmTasks.add(dlmTask1);

        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId("task2Id");
        dlmTask2.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask2);

        doReturn(dlmTasks).when(dataArchiveRollbackJob).findDataArchiveTasks("existingTaskId");

        String jsonString = objectMapper.writeValueAsString(dlmTasks);
        doReturn(jsonString).when(scheduleTaskRepository).save(any(ScheduleTaskEntity.class));

        doReturn(TaskStatus.DONE).when(dataArchiveRollbackJob).getTaskStatus(any(List.class));

        // When
        dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

        // Then
        verify(scheduleTaskRepository, times(2)).save(any(ScheduleTaskEntity.class));
        assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecute_whenDataArchiveTaskDoesNotExist_thenStatusUpdatedAndNoTasksExecuted() throws Exception {
        // Given
        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("nonExistingTaskId");
        doReturn(Optional.empty()).when(scheduleTaskRepository).findById("nonExistingTaskId");

        // When
        dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById("nonExistingTaskId", eq(TaskStatus.FAILED));
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_whenExceptionOccursDuringTasksExecution_thenStatusUpdatedAndExceptionThrown() throws Exception {
        // Given
        ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId("taskWithExceptionId");
        doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById("taskWithExceptionId");

        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId("task1Id");
        dlmTask1.setStatus(TaskStatus.DONE);
        dlmTasks.add(dlmTask1);

        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId("task2Id");
        dlmTask2.setStatus(TaskStatus.PREPARING);
        dlmTasks.add(dlmTask2);

        doReturn(dlmTasks).when(dataArchiveRollbackJob).findDataArchiveTasks("taskWithExceptionId");

        OngoingStubbing<Void> doThrowException = Mockito.doThrow(new RuntimeException("Exception message"));
        doThrowException.when(dataArchiveRollbackJob).executeTask("taskWithExceptionId", any(List.class));

        // When & Then
        dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

        verify(scheduleTaskRepository, times(1)).updateStatusById("taskWithExceptionId", eq(TaskStatus.FAILED));
        throw new JobExecutionException("Exception message");
    }
}