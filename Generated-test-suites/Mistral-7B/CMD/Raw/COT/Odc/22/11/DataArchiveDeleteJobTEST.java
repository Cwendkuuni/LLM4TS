package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());
 dataArchiveDeleteJob.execute(null);
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.PROCESSING);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
 dataArchiveDeleteJob.execute(null);
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_Success() throws Exception {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setStatus(TaskStatus.DONE);
 dlmTasks.add(dlmTask);

 when(dataArchiveDeleteJob.executeTask(eq(1L), anyList())).thenReturn(dlmTasks);

 DataArchiveDeleteJob dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());
 dataArchiveDeleteJob.scheduleTaskRepository = scheduleTaskRepository;
 dataArchiveDeleteJob.execute(null);

 TaskStatus taskStatus = getTaskStatus(dlmTasks);
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(taskStatus));
 }

 @Test(expected = Exception.class)
 public void testExecute_Exception() throws Exception {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
 doThrow(new Exception()).when(dataArchiveDeleteJob).executeTask(anyLong(), anyList());
 dataArchiveDeleteJob.execute(null);
 }

 @Test
 public void testGetTaskStatus_AllTasksFinished() {
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setId(1L);
 dlmTask1.setStatus(TaskStatus.DONE);
 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setId(2L);
 dlmTask2.setStatus(TaskStatus.DONE);
 dlmTasks.add(dlmTask1);
 dlmTasks.add(dlmTask2);
 TaskStatus expectedTaskStatus = TaskStatus.SUCCEEDED;
 assertEquals(expectedTaskStatus, dataArchiveDeleteJob.getTaskStatus(dlmTasks));
 }

 @Test
 public void testGetTaskStatus_SomeTasksFailed() {
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setId(1L);
 dlmTask1.setStatus(TaskStatus.DONE);
 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setId(2L);
 dlmTask2.setStatus(TaskStatus.FAILED);
 dlmTasks.add(dlmTask1);
 dlmTasks.add(dlmTask2);
 TaskStatus expectedTaskStatus = TaskStatus.FAILED;
 assertEquals(expectedTaskStatus, dataArchiveDeleteJob.getTaskStatus(dlmTasks));
 }

 @Test
 public void testGetTaskStatus_SomeTasksPending() {
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setId(1L);
 dlmTask1.setStatus(TaskStatus.PROCESSING);
 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setId(2L);
 dlmTask2.setStatus(TaskStatus.DONE);
 dlmTasks.add(dlmTask1);
 dlmTasks.add(dlmTask2);
 TaskStatus expectedTaskStatus = TaskStatus.PROCESSING;
 assertEquals(expectedTaskStatus, dataArchiveDeleteJob.getTaskStatus(dlmTasks));
 }

 private TaskStatus getTaskStatus(List<DlmTask> dlmTasks) {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonNode jsonNode = objectMapper.valueToTree(dlmTasks);
 return dataArchiveDeleteJob.getTaskStatus(jsonNode);
 }
}