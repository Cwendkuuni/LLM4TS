package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {
  
  @Mock
  private JobExecutionContext context;
  
  @Mock
  private ScheduleTaskEntity taskEntity;

  @Mock
  private ScheduleTaskEntity dataArchiveTask;
  
  private DataArchiveDeleteJob dataArchiveDeleteJob;
  
  @Before
  public void setUp() {
      dataArchiveDeleteJob = new DataArchiveDeleteJob();
  }

  @Test
  public void executeDataArchiveDeleteJobWhenDataArchiveTaskNotPresent() {
      when(context.getResult()).thenReturn(taskEntity);
      when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(new DataArchiveClearParameters()));
      when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());
      
      dataArchiveDeleteJob.execute(context);

      verify(scheduleTaskRepository).updateStatusById(any(), eq(TaskStatus.FAILED));
  }
    
  @Test
  public void executeDataArchiveDeleteJobWhenDataArchiveTaskNotDone() {
      when(context.getResult()).thenReturn(taskEntity);
      when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(new DataArchiveClearParameters()));
      when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));
      when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);

      dataArchiveDeleteJob.execute(context);
      
      verify(scheduleTaskRepository).updateStatusById(any(), eq(TaskStatus.FAILED));
  }

  @Test
  public void executeDataArchiveDeleteJobWhenDataArchiveTaskDone() {
      when(context.getResult()).thenReturn(taskEntity);
      when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(new DataArchiveClearParameters()));
      when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));
      when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
      
      dataArchiveDeleteJob.execute(context);
      
      verify(scheduleTaskRepository).updateStatusById(any(), any());
  }
}