
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

public class DataArchiveRollbackJobTest {

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private JobExecutionContext context;

    @Mock
    ScheduleTaskEntity taskEntity;
    
    @Mock
    ScheduleTaskEntity dataArchiveTask;
    
    @Mock
    DlmTask dlmTask;
    @Mock
    private DataArchiveRollbackParameters rollbackParameters;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteTaskWhenTaskNotFound() {
    	when(context.getResult()).thenReturn(taskEntity);
    	when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\": \"123\"}");
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());
        dataArchiveRollbackJob.execute(context);
        verify(scheduleTaskRepository).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteTaskWhenTaskIsFound() {
        when(context.getResult()).thenReturn(taskEntity);
    	when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\": \"123\"}");
        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
         
        List<DlmTask> dlmTasks = new ArrayList<>();
        dlmTasks.add(dlmTask);
        
        when(dlmTask.getSourceDatabaseId()).thenReturn(456L);
        when(dlmTask.getTargetDatabaseId()).thenReturn(123L);
          
        when(dataArchiveTask.getResultJson()).thenReturn(dlmTasks);
        when(dlmTask.getStatus()).thenReturn(TaskStatus.PREPARING);
        
        dataArchiveRollbackJob.execute(context);
        
        verify(dlmTask).setId(anyLong());
        verify(dlmTask).setSourceDatabaseId(123L);
        verify(dlmTask).setTargetDatabaseId(456L);
        verify(dlmTask).setJobType(JobType.ROLLBACK);
        verify(dlmTask).setStatus(TaskStatus.DONE);
        verify(scheduleTaskRepository).updateStatusById(anyString(), eq(TaskStatus.DONE));
    }
}
