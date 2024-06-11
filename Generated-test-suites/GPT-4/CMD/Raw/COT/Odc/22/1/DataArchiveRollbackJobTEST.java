import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext contextMock;
    private ScheduleTaskEntity scheduleTaskEntityMock;
    private DataArchiveRollbackParameters dataArchiveRollbackParametersMock;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        contextMock = mock(JobExecutionContext.class);
        scheduleTaskEntityMock = mock(ScheduleTaskEntity.class);
        dataArchiveRollbackParametersMock = mock(DataArchiveRollbackParameters.class);
    }

    @Test
    public void testExecute() {
        when(contextMock.getResult()).thenReturn(scheduleTaskEntityMock);
        when(scheduleTaskEntityMock.getParametersJson()).thenReturn("{}");
        when(scheduleTaskEntityMock.getId()).thenReturn(1L);
        when(dataArchiveRollbackParametersMock.getDataArchiveTaskId()).thenReturn(1L);

        try {
            dataArchiveRollbackJob.execute(contextMock);
        } catch (Exception e) {
            fail("Execution of DataArchiveRollbackJob threw an Exception");
        }
    }

    @Test
    public void testExecuteWhenParametersIsNull() {
        when(contextMock.getResult()).thenReturn(scheduleTaskEntityMock);
        when(scheduleTaskEntityMock.getParametersJson()).thenReturn(null);

        try {
            dataArchiveRollbackJob.execute(contextMock);
            fail("Expected Exception not thrown when parameters is null");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testExecuteWhenParametersDoesNotExists() {
        // Configure the behaviour for repository
        // Configure the tasks behaviour
        // Assert the results
    }

    @Test
    public void testExecuteWhenTasksDoesNotExists() {      
        // Configure the behaviour for repository
        // Configure the tasks behaviour
        // Assert the results
    }
    
    @Test
    public void testTaskStatus() {
        // Configure the tasks behaviour
        // Assert the results
    }
}