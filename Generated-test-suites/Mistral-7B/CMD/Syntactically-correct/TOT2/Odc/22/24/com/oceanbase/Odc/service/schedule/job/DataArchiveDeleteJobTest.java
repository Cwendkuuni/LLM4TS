package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
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

 private ScheduleTaskRepository scheduleTaskRepository;
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setup() {
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob = new DataArchiveDeleteJob() {
 @Override
 ScheduleTaskRepository getScheduleTaskRepository() {
 return scheduleTaskRepository;
 }
 };
 }

 @After
 public void teardown() {
 reset(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("nonexistentID");

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId("123");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.CREATED);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(null);

 verify(scheduleTaskRepository).updateStatusById("123", TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("123");

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId("123");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setResultJson(JsonUtils.toJsonString(new ArrayList<>()));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 dataArchiveDeleteJob.execute(null);

 ArgumentCaptor<List<DlmTask>> dlmTasksArgumentCaptor = ArgumentCaptor.forClass(List.class);
 verify(dataArchiveDeleteJob).executeTask("123", dlmTasksArgumentCaptor.capture());

 List<DlmTask> dlmTasks = dlmTasksArgumentCaptor.getValue();
 assertEquals(dlmTasks.size(), 0);
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_DataArchiveTaskStatusOtherThanDone() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("123");

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId("123");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.FAILED);
 scheduleTaskEntity.setResultJson(JsonUtils.toJsonString(new ArrayList<>()));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 dataArchiveDeleteJob.execute(null);
 }

 @Test
 public void testExecute_Success() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("123");

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId("123");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setResultJson(JsonUtils.toJsonString(new ArrayList<>()));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 dataArchiveDeleteJob.execute(null);

 ArgumentCaptor<List<DlmTask>> dlmTasksArgumentCaptor = ArgumentCaptor.forClass(List.class);
 verify(dataArchiveDeleteJob).executeTask("123", dlmTasksArgumentCaptor.capture());

 List<DlmTask> dlmTasks = dlmTasksArgumentCaptor.getValue();
 assertEquals(dlmTasks.size(), 1);
 DlmTask dlmTask = dlmTasks.get(0);
 assertEquals(dlmTask.getId(), DlmJobIdUtil.generateHistoryJobId(dataArchiveClearParameters.getJobName(),
 dataArchiveClearParameters.getJobGroup(), "123", 1));
 assertEquals(dlmTask.getJobType(), JobType.DELETE);
 assertEquals(dlmTask.getStatus(), TaskStatus.PREPARING);
 }
}