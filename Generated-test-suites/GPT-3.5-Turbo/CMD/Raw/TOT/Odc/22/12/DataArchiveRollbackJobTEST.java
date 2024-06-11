import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext context;
    
    private DataArchiveRollbackJob dataArchiveRollbackJob;
    
    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }
    
    @Test
    public void testExecute_SuccessfulRollback() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask taskUnit = new DlmTask();
        taskUnit.setSourceDatabaseId(1L);
        taskUnit.setTargetDatabaseId(2L);
        taskUnits.add(taskUnit);
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));
        
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveRollbackJob.execute(context);
        
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.DONE);
    }
    
    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
        
        dataArchiveRollbackJob.execute(context);
        
        verify(scheduleTaskRepository, times(1)).updateStatusById(0L, TaskStatus.FAILED);
    }
}