import org.junit.Test;
import org.mockito.Mockito;

public class DataArchiveRollbackJobTest {

    @Test
    public void executeTest() {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        
        Mockito.when(context.getResult()).thenReturn(scheduleTaskEntity);
        new DataArchiveRollbackJob().execute(context);
        Mockito.verify(context, Mockito.times(1)).getResult();
    }

    @Test
    public void executeTest_taskEntityNull() {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        Mockito.when(context.getResult()).thenReturn(null);
        new DataArchiveRollbackJob().execute(context);
        Mockito.verify(context, Mockito.times(1)).getResult();
    }

    @Test
    public void executeTest_dataArchiveTaskOptionNotPresent() {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        
        Mockito.when(context.getResult()).thenReturn(scheduleTaskEntity);
        Mockito.when(scheduleTaskRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        new DataArchiveRollbackJob().execute(context);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).findById(Mockito.any());
    }
}