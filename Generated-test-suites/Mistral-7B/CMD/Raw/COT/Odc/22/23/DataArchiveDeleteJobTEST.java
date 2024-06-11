package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

/**
 * Test class for DataArchiveDeleteJob
 */
@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("non-existent-id");

 // When & Then
 try {
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 fail("Expected JobExecutionException to be thrown");
 } catch (JobExecutionException e) {
 // Expected
 log.warn("Expected log message for DataArchiveTaskNotFound");
 assertTrue(e.getMessage().contains("Data archive task not found,rollback task fast failed."));
 }

 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED);

 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("existing-id");
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 dataArchiveTask.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId("existing-id");
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setResultJson(JsonUtils.toJson(new ArrayList<>()));

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // When
 try {
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 fail("Expected JobExecutionException to be thrown");
 } catch (JobExecutionException e) {
 // Expected
 log.warn("Expected log message for DataArchiveTaskNotFinished");
 assertTrue(e.getMessage().contains("Data archive task do not finish,data archive task id = "));
 }

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_Success() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("existing-id");
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setResultJson(JsonUtils.toJson( Arrays.asList(new DlmTask()) ));

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // When
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 DataArchiveClearParameters actualDataArchiveClearParameters = JsonUtils.fromJson(dataArchiveTask.getParametersJson(), DataArchiveClearParameters.class);
 List<DlmTask> actualDlmTasks = JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {});

 assertEquals(dataArchiveClearParameters, actualDataArchiveClearParameters);
 assertEquals(dlmTasks, actualDlmTasks);

 TaskStatus taskStatus = getTaskStatus(dlmTasks);
 assertEquals(TaskStatus.SUCCESS, taskStatus);

 verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveTask.getId(), taskStatus);
 }

 private TaskStatus getTaskStatus(List<DlmTask> dlmTasks) {
 ObjectMapper objectMapper = new ObjectMapper();
 String jsonString = objectMapper.writeValueAsString(dlmTasks);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setResultJson(jsonString);
 return scheduleTaskEntity.getStatus();
 }
}