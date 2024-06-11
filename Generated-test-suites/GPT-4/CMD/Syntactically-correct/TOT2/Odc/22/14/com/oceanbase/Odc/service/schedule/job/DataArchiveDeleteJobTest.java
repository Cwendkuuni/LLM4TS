package com.oceanbase.odc.service.schedule.job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {
    
    @Mock
    JobExecutionContext context;
    
    @Mock
    ScheduleTaskEntity taskEntity;
    
    @Mock
    DataArchiveDeleteJob dataArchiveDeleteJob;
    
    @Test
    public void testExecuteTaskNotFound() {
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }
    
    
    @Test
    public void testExecuteTaskStatusNotDone() {
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.PREPARING);
        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }
    
    @Test
    public void testExecuteSuccess() {
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        dataArchiveDeleteJob.execute(context);
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.DONE));
    }
}