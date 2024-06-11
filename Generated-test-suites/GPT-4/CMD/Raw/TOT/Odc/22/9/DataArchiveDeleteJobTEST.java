import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DataArchiveDeleteJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Mock
    private JobExecutionContext context;
    
    @Mock
    private ScheduleTaskEntity taskEntity;
    
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecuteDataArchiveTaskNotFound() {   
        when(context.getResult()).thenReturn(taskEntity); 
        when(taskEntity.getParametersJson()).thenReturn("{dataArchiveTaskId: 'id1'}");    
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());
        
        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteDataArchiveTaskNotDone() {   
        when(context.getResult()).thenReturn(taskEntity); 
        when(taskEntity.getParametersJson()).thenReturn("{dataArchiveTaskId: 'id2'}");    
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.CREATED);
        
        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteHappyPath() {   
        when(context.getResult()).thenReturn(taskEntity); 
        when(taskEntity.getParametersJson()).thenReturn("{dataArchiveTaskId: 'id3'}");    
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), any());
    }
}