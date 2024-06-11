package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJobTest;
import com.oceanbase.odc.service.schedule.job.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest extends AbstractDlmJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = mockScheduleTaskRepository();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId1");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId("taskId1");
 taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 taskEntity.setStatus(TaskStatus.CREATED);
 when(scheduleTaskRepository.findById("taskId1")).thenReturn(Optional.empty());

 // when / then
 try {
 dataArchiveDeleteJob.execute(mockJobExecutionContext());
 fail("Expected JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // expected
 log.warn("Data archive task not found,rollback task fast failed.");
 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId2");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId("taskId2");
 taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 taskEntity.setStatus(TaskStatus.CREATED);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("dataArchiveTaskId1");
 dataArchiveTask.setStatus(TaskStatus.PROCESSING);
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(new ArrayList<>()));
 when(scheduleTaskRepository.findById("dataArchiveTaskId1")).thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.findById("taskId2")).thenReturn(Optional.of(taskEntity));

 // when / then
 try {
 dataArchiveDeleteJob.execute(mockJobExecutionContext());
 fail("Expected JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // expected
 log.warn("Data archive task do not finish.");
 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId3");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId("taskId3");
 taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 taskEntity.setStatus(TaskStatus.DONE);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("dataArchiveTaskId2");
 dataArchiveTask.setStatus(TaskStatus.DONE);
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId("dlmTaskId1");
 dlmTask.setStatus(TaskStatus.SUCCESS);
 dlmTask.setJobType(JobType.DELETE);
 dlmTasks.add(dlmTask);
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(dlmTasks));
 when(scheduleTaskRepository.findById("dataArchiveTaskId2")).thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.findById("taskId3")).thenReturn(Optional.of(taskEntity));

 ObjectMapper mapper = new ObjectMapper();
 String jsonString = "{\"dlmTasks\":[" +
         "{\"id\":\"dlmTaskId2\",\"status\":\"PROCESSING\",\"jobType\":\"DELETE\"}," +
         "{\"id\":\"dlmTaskId3\",\"status\":\"PROCESSING\",\"jobType\":\"DELETE\"}]}";
 when(scheduleTaskRepository.findAllByJobNameAndStatus(eq("DeleteDataArchiveJob"), eq(TaskStatus.PROCESSING)))
         .thenReturn(mapper.readValue(jsonString, new TypeReference<List<ScheduleTaskEntity>>(){}) );

 // when
 dataArchiveDeleteJob.execute(mockJobExecutionContext());

 // then
 assertEquals(TaskStatus.SUCCESS, taskEntity.getStatus());
 }

 private JobExecutionContext mockJobExecutionContext() {
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(taskEntity);
 return context;
 }

 private ScheduleTaskRepository mockScheduleTaskRepository() {
 ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 return scheduleTaskRepository;
 }
}