package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 // Arrange
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId1");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // Act & Assert
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 assertEquals(TaskStatus.FAILED, dataArchiveClearParameters.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() throws JobExecutionException {
 // Arrange
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId1");
 ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);

 // Act & Assert
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 assertEquals(TaskStatus.FAILED, dataArchiveClearParameters.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() throws JobExecutionException {
 // Arrange
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId1");
 ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setId("dlmTaskId1");
 dlmTasks.add(dlmTask1);
 when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJsonString(dlmTasks));

 // Act
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // Assert
 assertEquals(TaskStatus.DONE, dataArchiveClearParameters.getStatus());
 }
}