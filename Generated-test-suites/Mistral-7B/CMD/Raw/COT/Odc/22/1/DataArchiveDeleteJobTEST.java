package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.JobType;
import com.oceanbase.odc.service.schedule.model.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobExecStatus;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {

 private ScheduleTaskRepository scheduleTaskRepository;
 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId("123456");
 JsonNode jsonNode = objectMapper.valueToTree(parameters);
 taskEntity.setParametersJson(jsonNode.toString());
 when(scheduleTaskRepository.findById("123456")).thenReturn(Optional.empty());

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // then
 assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getJobThread().getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId("123456");
 JsonNode jsonNode = objectMapper.valueToTree(parameters);
 taskEntity.setParametersJson(jsonNode.toString());
 taskEntity.setStatus(TaskStatus.RUNNING);
 when(scheduleTaskRepository.findById("123456")).thenReturn(Optional.of(taskEntity));

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // then
 assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getJobThread().getStatus());
 }

 @Test
 public void testExecute_ClearTasks() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId("123456");
 JsonNode jsonNode = objectMapper.valueToTree(parameters);
 taskEntity.setParametersJson(jsonNode.toString());
 taskEntity.setStatus(TaskStatus.DONE);
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById("123456")).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED)).thenReturn(true);
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING)).thenReturn(true);
 when(scheduleTaskRepository.saveAll(dlmTasks)).thenReturn(dlmTasks);

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // then
 assertEquals(TaskStatus.PREPARING, dataArchiveDeleteJob.getJobThread().getStatus());
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_ClearTasks_JobExecutionException() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId("123456");
 JsonNode jsonNode = objectMapper.valueToTree(parameters);
 taskEntity.setParametersJson(jsonNode.toString());
 taskEntity.setStatus(TaskStatus.DONE);
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById("123456")).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED)).thenReturn(true);
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING)).thenReturn(true);
 when(scheduleTaskRepository.saveAll(dlmTasks)).thenThrow(new JobExecutionException("Test error."));

 // when & then
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 }

 @Test
 public void testGetTaskStatus() {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 dlmTasks.get(0).setJobStatus(JobExecStatus.SUCCEED);
 dlmTasks.get(1).setJobStatus(JobExecStatus.FAILED);

 // when
 List<DlmTask> dlmTasksResult = Arrays.asList(new DlmTask(), new DlmTask());
 JobExecStatus taskStatus = DataArchiveDeleteJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(JobExecStatus.FAILED, taskStatus);
 }
}