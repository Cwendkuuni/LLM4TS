package com.oceanbase.odc.service.schedule.job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobExecutionContext;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;
    
    @Mock
    private DataArchiveDeleteJob dataArchiveDeleteJob;
    
    @Mock
    private ScheduleTaskEntity taskEntity, dataArchiveTask;
    
    @Mock
    private DataArchiveClearParameters dataArchiveClearParameters;
          
    @Mock
    private Optional<ScheduleTaskEntity> dataArchiveTaskOption;
    
    

    @Test
    public void testExecuteWhenDataArchiveTaskNotPresent() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("someJson");
        when(dataArchiveTaskOption.isPresent()).thenReturn(false);
      
        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }
    
    @Test
    public void testExecuteWhenDataArchiveTaskNotDone() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("someJson");
        when(dataArchiveTaskOption.isPresent()).thenReturn(true);
        when(dataArchiveTaskOption.get()).thenReturn(dataArchiveTask);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);
      
        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }
    
    @Test
    public void testExecuteWhenDataArchiveTaskDone() {
        // Simulate the case when data archive task is done
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("someJson");
        when(dataArchiveTaskOption.isPresent()).thenReturn(true);
        when(dataArchiveTaskOption.get()).thenReturn(dataArchiveTask);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
      
        dataArchiveDeleteJob.execute(context);

        // assert behavior 
    }

    
}