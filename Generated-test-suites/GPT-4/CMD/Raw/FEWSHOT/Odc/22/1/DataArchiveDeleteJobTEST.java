
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

  @InjectMocks
  private DataArchiveDeleteJob dataArchiveDeleteJob;

  @Mock
  private JobExecutionContext context;

  @Mock
  private ScheduleTaskEntity taskEntity, dataArchiveTask;

  @Mock
  private DataArchiveClearParameters dataArchiveClearParameters;

  @Mock
  private DlmTask taskUnit;

  @Test
  public void testExecute_NoDataArchiveTask() {
    when(context.getResult()).thenReturn(taskEntity);
    when(taskEntity.getParametersJson()).thenReturn(
        dataArchiveClearParameters.getDataArchiveTaskId());
    when(scheduleTaskRepository.findById(
        dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

    dataArchiveDeleteJob.execute(context);
    verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
  }


  @Test
  public void testExecute_DataArchiveTaskNotDone() {
    when(context.getResult()).thenReturn(taskEntity);
    when(taskEntity.getParametersJson()).thenReturn(
        dataArchiveClearParameters.getDataArchiveTaskId());
    when(scheduleTaskRepository.findById(
        dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
    when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);

    dataArchiveDeleteJob.execute(context);
    verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
  }


  @Test
  public void testExecute_Success() {
    when(context.getResult()).thenReturn(taskEntity);
    when(taskEntity.getParametersJson()).thenReturn(
        dataArchiveClearParameters.getDataArchiveTaskId());
    when(scheduleTaskRepository.findById(
        dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
    when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
    when(dataArchiveTask.getResultJson()).thenReturn(
        Arrays.asList(taskUnit));

    dataArchiveDeleteJob.execute(context);
    verify(taskUnit).setId(anyString());  // Assumed method setId exist
    verify(taskUnit).setJobType(JobType.DELETE);
    verify(taskUnits).setStatus(TaskStatus.PREPARING);
  }
}
