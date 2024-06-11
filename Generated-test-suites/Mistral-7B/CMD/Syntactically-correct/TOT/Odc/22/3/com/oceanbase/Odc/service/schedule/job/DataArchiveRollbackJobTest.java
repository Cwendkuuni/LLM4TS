package com.oceanbase.odc.service.schedule.job;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule. jobs.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;

 @BeforeEach
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound_ShouldUpdateStatusToFailed() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setParametersJson(objectMapper.valueToTree(rollbackParameters).toString());
 doReturn(Optional.empty()).when(scheduleTaskRepository).findById(rollbackParameters.getDataArchiveTaskId());

 // When
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskFound_ShouldUpdateStatusToDone() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setJobName("testJobName");
 dataArchiveTask.setJobGroup("testJobGroup");
 dataArchiveTask.setParametersJson(objectMapper.valueToTree(rollbackParameters).toString());
 dataArchiveTask.setResultJson(objectMapper.valueToTree(Arrays.asList(new DlmTask(), new DlmTask())).toString());
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(rollbackParameters.getDataArchiveTaskId());

 // When
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
 }

 @Test
 public void testExecute_ShouldThrowJobExecutionException() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setParametersJson(objectMapper.valueToTree(rollbackParameters).toString());
 doThrow(new JobExecutionException("Test Exception")).when(dataArchiveRollbackJob).executeTask(scheduleTaskEntity.getId(),
 Arrays.asList(new DlmTask(), new DlmTask()));

 // When & Then
 assertThrows(JobExecutionException.class, () -> dataArchiveRollbackJob.execute(new JobExecutionContext()));
 }

 @Test
 public void testGetTaskStatus_AllTasksDone_ShouldReturnTaskStatusDONE() {
 // Given
 List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
 DlmTask taskUnit1 = new DlmTask();
 DlmTask taskUnit2 = new DlmTask();
 taskUnit1.setStatus(TaskStatus.DONE);
 taskUnit2.setStatus(TaskStatus.DONE);

 // When
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

 // Then
 assertNotNull(taskStatus);
 assertFalse(taskStatus.isFailed());
 assertFalse(taskStatus.isPreparing());
 assertFalse(taskStatus.isRunning());
 assertTrue(taskStatus.isDone());
 }

 @Test
 public void testGetTaskStatus_OneTaskFailed_ShouldReturnTaskStatusFAILED() {
 // Given
 List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
 DlmTask taskUnit1 = new DlmTask();
 DlmTask taskUnit2 = new DlmTask();
 taskUnit1.setStatus(TaskStatus.DONE);
 taskUnit2.setStatus(TaskStatus.FAILED);

 // When
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

 // Then
 assertNotNull(taskStatus);
 assertFalse(taskStatus.isDone());
 assertTrue(taskStatus.isFailed());
 }

 @Test
 public void testGetTaskStatus_OneTaskRunning_ShouldReturnTaskStatusRUNNING() {
 // Given
 List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
 DlmTask taskUnit1 = new DlmTask();
 DlmTask taskUnit2 = new DlmTask();
 taskUnit1.setStatus(TaskStatus.DONE);
 taskUnit2.setStatus(TaskStatus.RUNNING);

 // When
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

 // Then
 assertNotNull(taskStatus);
 assertFalse(taskStatus.isFailed());
 assertFalse(taskStatus.isDone());
 assertTrue(taskStatus.isRunning());
 }
}