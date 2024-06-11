package com.oceanbase.odc.service.schedule.job;
import org.quartz.JobExecutionException;
import org.quartz.JobExecutionContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;
    private ScheduleTaskRepository scheduleTaskRepository;
    
    @Before
    public void setUp() {
      dataArchiveDeleteJob = new DataArchiveDeleteJob();
      context = mock(JobExecutionContext.class);
      taskEntity = mock(ScheduleTaskEntity.class);
      dataArchiveClearParameters = mock(DataArchiveClearParameters.class);
      scheduleTaskRepository = mock(ScheduleTaskRepository.class);
      
      when(context.getResult()).thenReturn(taskEntity);
      when(taskEntity.getParametersJson()).thenReturn(dataArchiveClearParameters);
    }

    @Test
    public void testExecuteDataArchiveTaskNotFound() throws JobExecutionException {
       when(scheduleTaskRepository.findById(anyString()))
           .thenReturn(Optional.empty());
       dataArchiveDeleteJob.execute(context);
       verify(scheduleTaskRepository, times(1))
           .updateStatusById(anyString(), eq(TaskStatus.FAILED));  
    }

    @Test
    public void testExecuteDataArchiveTaskNotDone() throws JobExecutionException {
       scheduleTaskEntity task = mock(ScheduleTaskEntity.class);
       when(scheduleTaskRepository.findById(anyString()))
           .thenReturn(Optional.of(task));
       when(task.getStatus()).thenReturn(TaskStatus.RUNNING);
       dataArchiveDeleteJob.execute(context);
       verify(scheduleTaskRepository, times(1))
           .updateStatusById(anyString(), eq(TaskStatus.FAILED));  
    }
    // Add more tests for other scenarios of your DataArchiveDeleteJob class...  
}