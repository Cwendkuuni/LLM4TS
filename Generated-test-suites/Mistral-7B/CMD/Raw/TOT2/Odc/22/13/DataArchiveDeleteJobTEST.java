package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.CREATED);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when
 dataArchiveDeleteJob.execute(null);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskRunning() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 // when
 dataArchiveDeleteJob.execute(null);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 scheduleTaskEntity.setResultJson(JsonUtils.toJsonString(dlmTasks));
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 // when
 dataArchiveDeleteJob.execute(null);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.RUNNING);
 // verify tasks are executed
 verify(dataArchiveDeleteJob, times(1)).executeTask(scheduleTaskEntity.getId(), dlmTasks.toArray(new DlmTask[0]));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecute_NullContext() {
 // when
 dataArchiveDeleteJob.execute(null);

 // then
 fail("should throw IllegalArgumentException");
 }

 @Test
 public void testGetTaskStatus_TasksRunning() {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(dataArchiveDeleteJob.executeTask(anyLong(), anyArray()))
 .thenReturn(dlmTasks);

 // when
 TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.RUNNING, taskStatus);
 }

 @Test
 public void testGetTaskStatus_TasksFailed() {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 Arrays.stream(dlmTasks).forEach(dlmTask -> dlmTask.setStatus(TaskStatus.FAILED));
 when(dataArchiveDeleteJob.executeTask(anyLong(), anyArray()))
 .thenReturn(dlmTasks);

 // when
 TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.FAILED, taskStatus);
 }

 @Test
 public void testGetTaskStatus_TasksSucceeded() {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 Arrays.stream(dlmTasks).forEach(dlmTask -> dlmTask.setStatus(TaskStatus.DONE));
 when(dataArchiveDeleteJob.executeTask(anyLong(), anyArray()))
 .thenReturn(dlmTasks);

 // when
 TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.DONE, taskStatus);
 }
}