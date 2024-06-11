import org.quartz.JobExecutionContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;

public class DataArchiveDeleteJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        // Test when data archive task is not found
        TaskEntity taskEntity = new TaskEntity();
        DataArchiveClearParameters params = new DataArchiveClearParameters();
        params.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(params));
        
        when(scheduleTaskRepository.findById(params.getDataArchiveTaskId())).thenReturn(Optional.empty());
        
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
        
        // Test when data archive task is not done
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.IN_PROGRESS);
        when(scheduleTaskRepository.findById(params.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
        
        // Test successful execution
        dataArchiveTask.setStatus(TaskStatus.DONE);
        when(scheduleTaskRepository.findById(params.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        
        dataArchiveDeleteJob.execute(jobExecutionContext);
        
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }
}