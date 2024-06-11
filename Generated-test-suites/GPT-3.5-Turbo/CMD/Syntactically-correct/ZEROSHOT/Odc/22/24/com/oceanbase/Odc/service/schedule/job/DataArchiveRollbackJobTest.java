import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.quartz.JobExecutionContext;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @After
    public void tearDown() {
        dataArchiveRollbackJob = null;
    }

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(context.getResult()).thenReturn(taskEntity);

        dataArchiveRollbackJob.execute(context);

        verify(taskEntity, times(2)).getId();
        verify(taskEntity, times(1)).getJobName();
        verify(taskEntity, times(1)).getJobGroup();
        verify(taskEntity, times(1)).getResultJson();
        verify(taskEntity, times(1)).getStatus();
        verify(taskEntity, times(1)).setId(anyLong());
        verify(taskEntity, times(1)).setSourceDatabaseId(anyLong());
        verify(taskEntity, times(1)).setTargetDatabaseId(anyLong());
        verify(taskEntity, times(1)).setJobType(any());
        verify(taskEntity, times(1)).setStatus(any());

        assertNotNull(dataArchiveRollbackJob.jobThread);
    }
}