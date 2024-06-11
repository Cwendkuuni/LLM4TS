import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoJobExecutionContext;
import org.quartz.impl.JobDetail;
import org.quartz.impl.SimpleScheduleBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private StdSchedulerFactory schedulerFactory;
    private JobExecutionContext context;

    @Before
    public void setUp() {
        schedulerFactory = new StdSchedulerFactory();
        context = new MockitoJobExecutionContext();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        // Given
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJsonString(rollbackParameters));

        List<DlmTask> taskUnits = new ArrayList<>();
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        assertNotNull(jobThread);
        assertEquals(TaskStatus.DONE, getTaskStatus(taskUnits));
        verifyExecuteTaskCalled(taskEntity.getId(), taskUnits);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // Given
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJsonString(rollbackParameters));

        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        verifyLogWarningMessage("Data archive task not found,rollback task fast failed.dataArchiveTaskId=" + rollbackParameters.getDataArchiveTaskId());
        verifyTaskStatusUpdated(taskEntity.getId(), TaskStatus.FAILED);
    }

    // Add more test cases for other methods and edge cases as needed

    // Helper methods

    private void verifyExecuteTaskCalled(Long taskId, List<DlmTask> taskUnits) {
        // Add code to verify executeTask() method was called with the correct arguments
    }

    private void verifyLogWarningMessage(String message) {
        // Add code to verify a warning message was logged
    }

    private void verifyTaskStatusUpdated(Long taskId, TaskStatus expectedStatus) {
        // Add code to verify task status was updated in the repository
    }

    private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
        // Add code to get the current status of all tasks and determine the overall status
    }
}