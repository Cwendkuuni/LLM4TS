package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.quartz.JobExecutionContext;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

  @Mock
  private JobExecutionContext context;

  @Mock
  private ScheduleTaskEntity taskEntity;

  @Mock
  private ScheduleTaskEntity dataArchiveTask;

  @InjectMocks
  private DataArchiveRollbackJob dataArchiveRollbackJob;

  @Test
  public void testExecute_dataArchiveTaskPresent() {

    when(context.getResult()).thenReturn(taskEntity);
    when(taskEntity.getParametersJson()).thenReturn("{}");
    when(taskEntity.getId()).thenReturn(1L);
    when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

    dataArchiveRollbackJob.execute(context);

    //verify methods that should have been called
    verify(scheduleTaskRepository).findById(anyLong());
    verify(scheduleTaskRepository).updateStatusById(anyLong(), any());
  }

  @Test
  public void testExecute_dataArchiveTaskNotPresent() {

    when(context.getResult()).thenReturn(taskEntity);
    when(taskEntity.getParametersJson()).thenReturn("{}");
    when(taskEntity.getId()).thenReturn(1L);
    when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

    dataArchiveRollbackJob.execute(context);

    verify(scheduleTaskRepository).findById(anyLong());
    verify(scheduleTaskRepository).updateStatusById(anyLong(), any());
  }
}