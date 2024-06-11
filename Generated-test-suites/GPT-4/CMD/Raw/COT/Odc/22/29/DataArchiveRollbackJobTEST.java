
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {
    
    @Mock
    private JobExecutionContext context;
    
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    
    @Before
    public void setUp() throws Exception {
       dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }
    
    @Test
    public void testExecute() {
        
        //Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("some parameters");
        
        when(context.getResult()).thenReturn(taskEntity);
        
        //When
        dataArchiveRollbackJob.execute(context);

        //Then
        verify(context).getResult();
        assertEquals("some parameters", taskEntity.getParametersJson());
    }
}
