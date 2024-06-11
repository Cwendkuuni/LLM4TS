package com.oceanbase.odc.service.schedule.job;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;

import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute() {
        // Given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        DataArchiveClearParameters params = new DataArchiveClearParameters();
        Optional<ScheduleTaskEntity> optionalTask = Optional.of(dataArchiveTask);
        List<DlmTask> taskUnits = new ArrayList<>();
        
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(params));
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));
        when(scheduleTaskRepository.findById(any())).thenReturn(optionalTask);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        
        // When
        job.execute(context);
        
        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), any());
        // place more assertions here based on the expected behavior
    }

    @Test
    public void testExecute_NoDataArchiveTask() { // Edge case
        // Given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        DataArchiveClearParameters params = new DataArchiveClearParameters();
        Optional<ScheduleTaskEntity> emptyOptional = Optional.empty();
        
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(params));
        when(scheduleTaskRepository.findById(params.getDataArchiveTaskId())).thenReturn(emptyOptional);
        
        // When
        job.execute(context);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(any(), eq(TaskStatus.FAILED));
        // assertions here for no other repo calls or updates 
    }

    // Similarly, write some tests for other edge cases and exceptions
}