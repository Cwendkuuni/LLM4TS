package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(UUID.randomUUID().toString());

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 JobExecutionContext context = mock(JobExecutionContext.class);
 dataArchiveDeleteJob.execute(context);

 assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getJobThread().getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() throws JobExecutionException {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(UUID.randomuuid().toString());
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 dataArchiveTask.setId("taskId");
 dataArchiveTask.setJobName("jobName");
 dataArchiveTask.setJobGroup("jobGroup");
 dataArchiveTask.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 JobExecutionContext context = mock(JobExecutionContext.class);
 dataArchiveDeleteJob.execute(context);

 assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getJobThread().getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() throws JobExecutionException {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(UUID.randomuuid().toString());
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setId("taskId");
 dataArchiveTask.setJobName("jobName");
 dataArchiveTask.setJobGroup("jobGroup");
 dataArchiveTask.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId("dlmTaskId");
 dlmTask.setStatus(TaskStatus.PREPARING);
 dlmTask.setJobType(JobType.DELETE);
 dlmTasks.add(dlmTask);
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(dlmTasks));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 JobExecutionContext context = mock(JobExecutionContext.class);
 dataArchiveDeleteJob.execute(context);

 assertEquals(TaskStatus.SUCCESS, dataArchiveDeleteJob.getJobThread().getStatus());
 }
}

class AbstractDlmJobTest {
 AbstractDlmJob abstractDlmJob = new AbstractDlmJob();

 @Test
 public void getJobThread_shouldReturnThread() {
 Thread thread = new Thread();
 abstractDlmJob.setJobThread(thread);
 assertEquals(thread, abstractDlmJob.getJobThread());
 }
}