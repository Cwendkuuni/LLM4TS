import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute_successfulExecution() {
        // Mocking JobExecutionContext and ScheduleTaskEntity
        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);

        // Mocking data for rollbackParameters and dataArchiveTask
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(123);
        String parametersJson = "{...}"; // JSON data
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);

        // Mocking dataArchiveTask
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        List<DlmTask> taskUnits = new ArrayList<>();
        // Add taskUnits data
        when(dataArchiveTask.getResultJson()).thenReturn("{...}");

        // Mocking scheduleTaskRepository, executeTask, and getTaskStatus
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

        // Call execute method
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Verify the updateStatusById method is called with expected taskStatus
        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.DONE));
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound() {
        // Mocking JobExecutionContext and ScheduleTaskEntity
        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);

        // Mocking rollbackParameters
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(123);
        String parametersJson = "{...}";
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);

        // Mocking scheduleTaskRepository
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call execute method
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Verify the updateStatusById method is called with TaskStatus.FAILED
        verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

}