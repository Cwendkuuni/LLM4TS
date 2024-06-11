package com.oceanbase.odc.service.schedule.job;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {
  
  @Test
  public void testExecute() {
    // mocking
    JobExecutionContext context = mock(JobExecutionContext.class);
    scheduleTaskRepository = mock(ScheduleTaskRepository.class);
    ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
    DataArchiveDeleteJob dataArchiveDeleteJob = spy(DataArchiveDeleteJob.class);
    dataArchiveDeleteJob.scheduleTaskRepository = scheduleTaskRepository;

    // setting up the mock behaviors
    when(context.getResult()).thenReturn(taskEntity);
    when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());
    
    // invoking the method to be tested
    dataArchiveDeleteJob.execute(context);
    
    // Verifying that updateStatusById() was executed only once with specific arguments
    verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    
    // Ensuring that no more interactions are taking place on our mocked scheduleTaskRepository
    verifyNoMoreInteractions(scheduleTaskRepository);
  }
}