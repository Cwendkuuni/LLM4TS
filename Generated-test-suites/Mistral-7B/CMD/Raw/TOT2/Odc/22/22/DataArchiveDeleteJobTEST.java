package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveDeleteJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setup() {
 // Initialize mocks
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // Given
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId("non-existing-id");
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(new ScheduleTaskEntity());

 // When & Then
 try {
 dataArchiveDeleteJob.execute(context);
 fail("Expected an JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // Expected
 log.warn("Expected warning message: Data archive task not found.");
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
 }
 }

 @Test
 public void testExecute_DataArchiveTaskInProgress() {
 // Given
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId("existing-id");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setStatus(TaskStatus.RUNNING);
 when(scheduleTaskRepository.findById(parameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

 // When & Then
 try {
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 fail("Expected an JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // Expected
 log.warn("Expected warning message: Data archive task do not finish.");
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
 }
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() {
 // Given
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId("existing-id");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(parameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

 List<DlmTask> tasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));
 when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {}))).thenReturn(tasks);

 // When
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // Then
 TaskStatus taskStatus = TaskStatus.SUCCEEDED;
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(taskStatus));
 }

 @Test
 public void testExecute_DataArchiveTaskEmptyResultJson() {
 // Given
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId("existing-id");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setResultJson("");
 when(scheduleTaskRepository.findById(parameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

 // When & Then
 try {
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 fail("Expected an JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // Expected
 log.warn("Expected warning message: No task units found.");
 verify(scheduleTaskRepository, times(0)).updateStatusById(anyString(), any());
 }
 }

 @Test
 public void testExecute_DataArchiveTaskInvalidResultJson() {
 // Given
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId("existing-id");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setResultJson("{\"invalidJson\": true}");
 when(scheduleTaskRepository.findById(parameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

 // When & Then
 try {
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 fail("Expected an JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // Expected
 log.warn("Expected warning message: Failed to parse task units.");
 verify(scheduleTaskRepository, times(0)).updateStatusById(anyString(), any());
 }
 }

 @Test
 public void testExecute_DataArchiveTaskInvalidTaskUnits() {
 // Given
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId("existing-id");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setResultJson("{\"taskUnits\": []}");
 when(scheduleTaskRepository.findById(parameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

 // When & Then
 try {
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 fail("Expected an JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // Expected
 log.warn("Expected warning message: No valid task units found.");
 verify(scheduleTaskRepository, times(0)).updateStatusById(anyString(), any());
 }
 }

 @Test
 public void testExecute_DataArchiveTaskInvalidTaskUnitsId() {
 // Given
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId("existing-id");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask(), new DlmTask())));
 when(scheduleTaskRepository.findById(parameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

 List<DlmTask> tasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(JsonUtils.fromJson(taskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(tasks);
 when(tasks.get(0).getId()).thenReturn("");

 // When & Then
 try {
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 fail("Expected an JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // Expected
 log.warn("Expected warning message: Invalid task unit id.");
 verify(scheduleTaskRepository, times(0)).updateStatusById(anyString(), any());
 }
 }

 @Test
 public void testExecute_DataArchiveTaskInvalidTaskUnitsStatus() {
 // Given
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId("existing-id");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask(), new DlmTask())));
 when(scheduleTaskRepository.findById(parameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

 List<DlmTask> tasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(JsonUtils.fromJson(taskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(tasks);
 when(tasks.get(0).getStatus()).thenReturn("INVALID_STATUS");

 // When & Then
 try {
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 fail("Expected an JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // Expected
 log.warn("Expected warning message: Invalid task unit status.");
 verify(scheduleTaskRepository, times(0)).updateStatusById(anyString(), any());
 }
 }

 @Test
 public void testExecute_DataArchiveTaskInvalidTaskUnitsJobType() {
 // Given
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId("existing-id");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask(), new DlmTask())));
 when(scheduleTaskRepository.findById(parameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

 List<DlmTask> tasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(JsonUtils.fromJson(taskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(tasks);
 when(tasks.get(0).getJobType()).thenReturn("INVALID_TYPE");

 // When & Then
 try {
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 fail("Expected an JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // Expected
 log.warn("Expected warning message: Invalid task unit job type.");
 verify(scheduleTaskRepository, times(0)).updateStatusById(anyString(), any());
 }
 }

 @Test
 public void testExecute_DataArchiveTaskValidTaskUnits() {
 // Given
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId("existing-id");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask(), new DlmTask())));
 when(scheduleTaskRepository.findById(parameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

 List<DlmTask> tasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(JsonUtils.fromJson(taskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(tasks);

 // When
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.SUCCEEDED));
 }
}