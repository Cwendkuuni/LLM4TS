import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        // mock jobExecutionContext
        JobExecutionContext context = mock(JobExecutionContext.class);
        
        // mock data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1);
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("TestGroup");
        taskEntity.setParametersJson("{'dataArchiveTaskId': 123}");
        taskEntity.setResultJson("[]");
        
        // mock repository and dataArchiveTask
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(eq(123L))).thenReturn(Optional.of(taskEntity));
        
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
        
        // execute
        dataArchiveRollbackJob.execute(context);
        
        // verify
        verify(scheduleTaskRepository).updateStatusById(eq(1L), any(TaskStatus.class));
        
        // additional assertions
        // Add additional assertions based on the logic in execute method, such as checking taskUnit modifications and taskStatus update
    }
}