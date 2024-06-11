package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 objectMapper = new ObjectMapper();
 }

 // Test method for execute(JobExecutionContext)
 @Test
 public void testExecute_TaskEntityIsNull_ShouldRollbackTask() {
 ScheduleTaskEntity taskEntity = null;
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 JobExecutionContext context = new JobExecutionContext();
 context.setResult(taskEntity);

 try {
 dataArchiveDeleteJob.execute(context);
 fail("Expected JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // Expected
 }

 // Check if task status is updated to FAILED
 ScheduleTaskEntity savedTaskEntity = scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())
 .orElseThrow(() -> new RuntimeException("Task entity not found."));
 assertEquals(TaskStatus.FAILED, savedTaskEntity.getStatus());
 }

 // Test method for execute(JobExecutionContext)
 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 JobExecutionContext context = new JobExecutionContext();
 context.setResult(taskEntity);

 try {
 dataArchiveDeleteJob.execute(context);
 fail("Expected JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // Expected
 }

 // Check if task status is updated to FAILED
 ScheduleTaskEntity savedTaskEntity = scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())
 .orElseThrow(() -> new RuntimeException("Task entity not found."));
 assertEquals(TaskStatus.FAILED, savedTaskEntity.getStatus());
 }

 // Test method for execute(JobExecutionContext)
 @Test
 public void testExecute_DataArchiveTaskStatusIsNotDone() {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setStatus(TaskStatus.RUNNING);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 JobExecutionContext context = new JobExecutionContext();
 context.setResult(taskEntity);

 try {
 dataArchiveDeleteJob.execute(context);
 fail("Expected JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // Expected
 }

 // Check if task status is updated to FAILED
 ScheduleTaskEntity savedTaskEntity = scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())
 .orElseThrow(() -> new RuntimeException("Task entity not found."));
 assertEquals(TaskStatus.FAILED, savedTaskEntity.getStatus());
 }

 // Test method for execute(JobExecutionContext) with valid inputs
 @Test
 public void testExecute_ValidInputs() {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 JsonNode parametersJson = objectMapper.createObjectNode();
 dataArchiveClearParameters.setParametersJson(parametersJson);
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 JsonNode resultJson = objectMapper.createObjectNode();
 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask taskUnit1 = new DlmTask();
 DlmTask taskUnit2 = new DlmTask();
 taskUnits.add(taskUnit1);
 taskUnits.add(taskUnit2);
 taskEntity.setResultJson(JsonUtils.toJson(taskUnits));

 JobExecutionContext context = new JobExecutionContext();
 context.setResult(taskEntity);

 dataArchiveDeleteJob.execute(context);

 // Check if tasks are executed
 List<DlmTask> executedTasks = executeTask(1L, taskUnits);
 assertFalse(executedTasks.isEmpty());

 // Check if task status is updated to SUCCESS
 ScheduleTaskEntity savedTaskEntity = scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())
 .orElseThrow(() -> new RuntimeException("Task entity not found."));
 assertEquals(TaskStatus.SUCCESS, savedTaskEntity.getStatus());
 }

 // Test method for execute(JobExecutionContext) with invalid input (null dataArchiveClearParameters)
 @Test(expected = IllegalArgumentException.class)
 public void testExecute_InvalidInput_NullDataArchiveClearParameters() {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = null;
 JobExecutionContext context = new JobExecutionContext();
 context.setResult(taskEntity);
 dataArchiveDeleteJob.execute(context);
 }

 // Test method for execute(JobExecutionContext) with invalid input (empty parametersJson)
 @Test(expected = IllegalArgumentException.class)
 public void testExecute_InvalidInput_EmptyParametersJson() {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 JsonNode parametersJson = objectMapper.createObjectNode();
 dataArchiveClearParameters.setParametersJson(parametersJson);
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 JsonNode resultJson = objectMapper.createObjectNode();
 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask taskUnit1 = new DlmTask();
 DlmTask taskUnit2 = new DlmTask();
 taskUnits.add(taskUnit1);
 taskUnits.add(taskUnit2);
 taskEntity.setResultJson(JsonUtils.toJson(taskUnits));

 JobExecutionContext context = new JobExecutionContext();
 context.setResult(taskEntity);

 dataArchiveDeleteJob.execute(context);
 }

 // Test method for execute(JobExecutionContext) with invalid input (empty resultJson)
 @Test(expected = IllegalArgumentException.class)
 public void testExecute_InvalidInput_EmptyResultJson() {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 JsonNode parametersJson = objectMapper.createObjectNode();
 dataArchiveClearParameters.setParametersJson(parametersJson);
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 JsonNode resultJson = objectMapper.createObjectNode();
 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask taskUnit1 = new DlmTask();
 DlmTask taskUnit2 = new DlmTask();
 taskUnits.add(taskUnit1);
 taskUnits.add(taskUnit2);
 taskEntity.setResultJson(JsonUtils.toJson(taskUnits));

 JobExecutionContext context = new JobExecutionContext();
 context.setResult(taskEntity);

 dataArchiveDeleteJob.execute(context);
 }
}