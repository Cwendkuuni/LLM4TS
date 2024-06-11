package com.oceanbase.odc.service.schedule.job;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest{

  @Test
  public void testExecute(){
    DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

    // Mock objects/returns
    JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
    ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
    DlmTask task1 = Mockito.mock(DlmTask.class);
    DlmTask task2 = Mockito.mock(DlmTask.class);
    DataArchiveClearParameters dataArchiveClearParameters = Mockito.mock(DataArchiveClearParameters.class);
    Optional<ScheduleTaskEntity> scheduleTaskEntityOptional = Optional.of(Mockito.mock(ScheduleTaskEntity.class));

    // Mocking Json utils methods
    when(JsonUtils.fromJson(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(dataArchiveClearParameters);

    when(JsonUtils.fromJson(Mockito.anyString(), Mockito.any(TypeReference.class)))
.send(Arrays.asList(task1, task2));

    // Mocking methods
    when(context.getResult()).thenReturn(taskEntity);

    doNothing().when(task1).setId(Mockito.any(String.class));
    when(task1.getId()).thenReturn("id1");

    doNothing().when(task2).setId(Mockito.any(String.class));
    when(task2.getId()).thenReturn("id2");

    when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("taskId");

    when(taskEntity.getId()).thenReturn("id");
    when(taskEntity.getJobName()).thenReturn("name");
    when(taskEntity.getJobGroup()).thenReturn("group");
    when(taskEntity.getParametersJson()).thenReturn("jsonParams");
    when(taskEntity.getResultJson()).thenReturn("jsonResult");

    when(scheduleTaskEntityOptional.get()).thenReturn(taskEntity);
    doNothing().when(taskEntity).setStatus(Mockito.any(TaskStatus.class));

    // call test method
    dataArchiveDeleteJob.execute(context);

    // assertions
    assert(task1.getStatus() == TaskStatus.PREPARING);
    assert(task2.getStatus() == TaskStatus.PREPARING);
  }
}