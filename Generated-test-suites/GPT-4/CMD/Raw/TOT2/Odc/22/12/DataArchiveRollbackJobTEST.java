import org.junit.*;
import org.mockito.*;
import org.quartz.*;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private Optional<ScheduleTaskEntity> dataArchiveTaskOption;
  
    @Test
    public void testExecute() {
       // Arrange
       Mockito.when(context.getResult()).thenReturn(taskEntity);
       Mockito.when(taskEntity.getParametersJson()).thenReturn("yourJsonString");
      
       // Adding further set up for mocking objects like `scheduleTaskRepository` etc
       
       // Act
       new DataArchiveRollbackJob().execute(context);
       
       // Assert
       // add your assertions here
    }
}