package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.DlmJobIdUtil;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private JobRepository jobRepository;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 jobRepository = mock(JobRepository.class);
 dataArchiveRollbackJob.setJobRepository(jobRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(-1L);
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 JobExecutionContext context = new JobExecutionContext();
 context.setMergedJobDataMap(new JobDataMap());
 context.setJobDetail(mock(JobDetail.class));
 context.setJobInstance(mock(JobInstance.class));
 context.setResult(taskEntity);

 try {
 dataArchiveRollbackJob.execute(context);
 fail("Expected JobExecutionException but got none");
 } catch (JobExecutionException e) {
 // expected
 }

 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskExists() {
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(Arrays.asList(new DlmTask(), new DlmTask())));

 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);

 taskEntity.setId(2L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 ObjectMapper objectMapper = new ObjectMapper();
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 String jsonString = objectMapper.writeValueAsString(dlmTasks);
 dataArchiveTask.setResultJson(jsonString);

 context.setMergedJobDataMap(new JobDataMap());
 context.setJobDetail(mock(JobDetail.class));
 context.setJobInstance(mock(JobInstance.class));
 context.setResult(dataArchiveTask);

 try {
 dataArchiveRollbackJob.execute(context);
 fail("Expected JobExecutionException but got none");
 } catch (JobExecutionException e) {
 // expected
 }

 assertEquals(TaskStatus.DONE, dataArchiveTask.getStatus());
 }

 @Test(expected = IllegalStateException.class)
 public void testExecute_NullContext() {
 dataArchiveRollbackJob.execute(null);
 }

 @Test(expected = NullPointerException.class)
 public void testExecute_NullDataArchiveTask() {
 ScheduleTaskEntity taskEntity = null;
 dataArchiveRollbackJob.execute(new JobExecutionContext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecute_EmptyTaskList() {
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(Arrays.asList()));
 dataArchiveRollbackJob.execute(new JobExecutionContext());
 }

 @Test
 public void testExecute_TaskListNotEmpty() {
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(Arrays.asList(new DlmTask(), new DlmTask())));

 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);

 taskEntity.setId(2L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 ObjectMapper objectMapper = new ObjectMapper();
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 String jsonString = objectMapper.writeValueAsString(dlmTasks);
 dataArchiveTask.setResultJson(jsonString);

 context.setMergedJobDataMap(new JobDataMap());
 context.setJobDetail(mock(JobDetail.class));
 context.setJobInstance(mock(JobInstance.class));
 context.setResult(dataArchiveTask);

 dataArchiveRollbackJob.execute(context);

 assertEquals(TaskStatus.DONE, dataArchiveTask.getStatus());
 }
}