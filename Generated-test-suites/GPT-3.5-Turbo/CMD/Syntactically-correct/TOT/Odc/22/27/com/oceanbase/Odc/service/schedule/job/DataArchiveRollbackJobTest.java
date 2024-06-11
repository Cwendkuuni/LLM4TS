import org.junit.Test;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Test
    public void testExecute_Success() {
        // Mock JobExecutionContext
        JobExecutionContext context = mock(JobExecutionContext.class);

        // Prepare test data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(101);

        // Mocking repository methods
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

        // Execute the method
        dataArchiveRollbackJob.execute(context);

        // Add assertions here for successful execution
    }

    @Test
    public void testExecute_TaskNotFound() {
        // Mock JobExecutionContext
        JobExecutionContext context = mock(JobExecutionContext.class);

        // Prepare test data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(101);

        // Mocking repository methods
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        // Execute the method
        dataArchiveRollbackJob.execute(context);

        // Add assertions here for task not found scenario
    }

    @Test
    public void testExecute_RollbackTaskFail() {
        // Mock JobExecutionContext
        JobExecutionContext context = mock(JobExecutionContext.class);

        // Prepare test data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(101);

        // Mocking repository methods
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getResultJson()).thenReturn(""); // Mocking empty resultJson scenario

        // Execute the method
        dataArchiveRollbackJob.execute(context);

        // Add assertions here for rollback task failure scenario
    }

    // Add more test cases for edge cases and error scenarios as required

}