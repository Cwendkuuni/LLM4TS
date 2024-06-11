package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobThread;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.google.common.collect.Lists;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private JobThread jobThread;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 jobThread = mock(JobThread.class);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("123");
 when(scheduleTaskRepository.findById("123")).thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

 // then
 dataArchiveRollbackJob.jobThread.join();
 assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(Lists.newArrayList()));
 }

 @Test
 public void testExecute_DataArchiveTaskFound() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("123");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId("123");
 scheduleTaskEntity.setParametersJson(new ObjectMapper().writeValueAsString(rollbackParameters));
 when(scheduleTaskRepository.findById("123")).thenReturn(Optional.of(scheduleTaskEntity));

 DlmTask dlmTask = new DlmTask();
 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(dlmTask);
 when(scheduleTaskRepository.findById("123")).thenReturn(Optional.of(scheduleTaskEntity)).thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

 // then
 dataArchiveRollbackJob.jobThread.join();
 ArgumentCaptor<List<DlmTask>> taskUnitsCaptor = ArgumentCaptor.forClass(List.class);
 verify(scheduleTaskRepository).updateStatusById("123", eq(TaskStatus.FAILED));
 verify(dataArchiveRollbackJob).executeTask("123", taskUnitsCaptor.capture());
 assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(taskUnits));
 }

 @Test(expected = NullPointerException.class)
 public void testExecute_NullScheduleTaskEntity() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("123");
 when(scheduleTaskRepository.findById("123")).thenReturn(Optional.empty());
 when(scheduleTaskRepository.findById("123")).thenReturn(Optional.of(null));

 // when & then
 dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecute_InvalidDataArchiveTaskJson() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("123");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId("123");
 scheduleTaskEntity.setParametersJson("invalid json");
 when(scheduleTaskRepository.findById("123")).thenReturn(Optional.of(scheduleTaskEntity));

 // when & then
 dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));
 }

 @Test
 public void testExecute_TaskUnitsPreparing() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("123");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId("123");
 scheduleTaskEntity.setResultJson(new ObjectMapper().writeValueAsString(Lists.newArrayList(new DlmTask())));
 when(scheduleTaskRepository.findById("123")).thenReturn(Optional.of(scheduleTaskEntity));

 // when
 dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

 // then
 dataArchiveRollbackJob.jobThread.join();
 ArgumentCaptor<List<DlmTask>> taskUnitsCaptor = ArgumentCaptor.forClass(List.class);
 verify(dataArchiveRollbackJob).executeTask("123", taskUnitsCaptor.capture());
 List<DlmTask> taskUnits = taskUnitsCaptor.getValue();
 assertEquals(TaskStatus.DONE, taskUnits.get(0).getStatus());
 }

 @Test
 public void testExecute_TaskUnitsDone() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("123");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId("123");
 scheduleTaskEntity.setResultJson(new ObjectMapper().writeValueAsString(Lists.newArrayList(new DlmTask().setStatus(TaskStatus.DONE))));
 when(scheduleTaskRepository.findById("123")).thenReturn(Optional.of(scheduleTaskEntity));

 // when
 dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

 // then
 dataArchiveRollbackJob.jobThread.join();
 ArgumentCaptor<List<DlmTask>> taskUnitsCaptor = ArgumentCaptor.forClass(List.class);
 verify(dataArchiveRollbackJob).executeTask("123", taskUnitsCaptor.capture());
 List<DlmTask> taskUnits = taskUnitsCaptor.getValue();
 assertEquals(TaskStatus.DONE, taskUnits.get(0).getStatus());
 }
}