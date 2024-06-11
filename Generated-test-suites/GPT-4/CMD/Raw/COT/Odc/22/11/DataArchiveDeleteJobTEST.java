
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {
    @Mock
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    
    @Mock
    private JobExecutionContext context;
    
    @Mock
    private ScheduleTaskEntity taskEntity;
    
    @Before
    public void setUp() {
        when(context.getResult()).thenReturn(taskEntity);
    }
    
    //Basic functionality test
    @Test
    public void testExecute() {
        dataArchiveDeleteJob.execute(context);
        verify(context, times(1)).getResult();
    }

    //Edge Case 1: JobExecutionContext context is null
    @Test(expected = NullPointerException.class)
    public void testExecuteWithNullContext() {
        dataArchiveDeleteJob.execute(null);
    }

    //Edge Case 2: ScheduleTaskEntity returned by context.getResult() is null
    @Test(expected = NullPointerException.class)
    public void testExecuteWithNullTaskEntity() {
        when(context.getResult()).thenReturn(null);
        dataArchiveDeleteJob.execute(context);
    }
    
    //Edge Case 3: ScheduleTaskEntity does not exist
    @Test(expected = NoSuchElementException.class)
    public void testExecuteWithNoScheduleTaskEntityFound() {
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(context);
    }
    
    //Edge Case 4: ScheduleTaskEntity's status is not DONE
    @Test
    public void testExecuteWithTaskNotDone() {
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING); // Not TaskStatus.DONE
        dataArchiveDeleteJob.execute(context);
    }
}
