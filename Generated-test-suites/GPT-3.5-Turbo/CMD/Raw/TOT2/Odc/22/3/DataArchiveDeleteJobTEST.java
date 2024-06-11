import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;
    
    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Test
    public void testExecute() {
        // Mock data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("TestGroup");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
        
        // Mock repository
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
        
        // Set up context
        when(context.getResult()).thenReturn(taskEntity);
        
        dataArchiveDeleteJob.scheduleTaskRepository = scheduleTaskRepository;
        dataArchiveDeleteJob.execute(context);
        
        // Add assertions based on expected behavior
    }
    
    // Add more test cases for other methods as needed

}