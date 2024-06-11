import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContextMock;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        jobExecutionContextMock = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecuteWithValidTask() {
        // Define test data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        // Mock data and behavior
        when(jobExecutionContextMock.getResult()).thenReturn(taskEntity);
        // Execute the method
        dataArchiveDeleteJob.execute(jobExecutionContextMock);
        // Verify the result
        verify(scheduleTaskRepository, times(1)).findById(1);
        // Add more verification based on expected behavior
    }

    @Test
    public void testExecuteWithNotFoundDataArchiveTask() {
        // Define test data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 999}");
        // Mock data and behavior
        when(jobExecutionContextMock.getResult()).thenReturn(taskEntity);
        // Execute the method
        dataArchiveDeleteJob.execute(jobExecutionContextMock);
        // Verify the result
        verify(scheduleTaskRepository, times(1)).findById(999);
        // Add more verification based on expected behavior
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotDone() {
        // Define test data
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        when(scheduleTaskRepository.findById(anyInt())).thenReturn(Optional.of(dataArchiveTask));
        // Execute the method
        dataArchiveDeleteJob.execute(jobExecutionContextMock);
        // Verify the result
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyInt(), eq(TaskStatus.FAILED));
        // Add more verification based on expected behavior
    }

    // Add more test cases for different scenarios

}