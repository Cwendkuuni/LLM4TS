package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockObject;
import org.quartz.impl.context.job.DefaultJobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveDeleteJobTestData;
import com.oceanbase.tools.migrator.common.enums.JobType;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
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
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 String taskEntityJson = objectMapper.writeValueAsString(taskEntity);
 taskEntity.setParametersJson(taskEntityJson);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when
 dataArchiveDeleteJob.execute(new DefaultJobExecutionContext());

 // then
 verifyTaskStatusUpdated(taskEntity, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 DataArchiveDeleteJobTestData dataArchiveDeleteJobTestData = new DataArchiveDeleteJobTestData();
 dataArchiveDeleteJobTestData.setTaskStatus(TaskStatus.FAILED);
 DataArchiveDeleteJobTestData dataArchiveTask = dataArchiveDeleteJobTestData.getDataArchiveTask();
 dataArchiveTask.setId(1L);
 String taskEntityJson = objectMapper.writeValueAsString(taskEntity);
 taskEntity.setParametersJson(taskEntityJson);
 String taskUnitsJson = objectMapper.writeValueAsString(dataArchiveDeleteJobTestData.getTaskUnits());
 dataArchiveTask.setResultJson(taskUnitsJson);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(new DefaultJobExecutionContext());

 // then
 verifyTaskStatusUpdated(taskEntity, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 DataArchiveDeleteJobTestData dataArchiveDeleteJobTestData = new DataArchiveDeleteJobTestData();
 DataArchiveDeleteJobTestData dataArchiveTask = dataArchiveDeleteJobTestData.getDataArchiveTask();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 String taskEntityJson = objectMapper.writeValueAsString(taskEntity);
 taskEntity.setParametersJson(taskEntityJson);
 String taskUnitsJson = objectMapper.writeValueAsString(dataArchiveDeleteJobTestData.getTaskUnits());
 dataArchiveTask.setResultJson(taskUnitsJson);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(new DefaultJobExecutionContext());

 // then
 DataArchiveDeleteJobTestData expectedDataArchiveTask = new DataArchiveDeleteJobTestData();
 expectedDataArchiveTask.setTaskStatus(TaskStatus.RUNNING);
 verify(scheduleTaskRepository).updateStatusById(dataArchiveClearParameters.getDataArchiveTaskId(), eq(expectedDataArchiveTask.getTaskStatus()));
 }

 private void verifyTaskStatusUpdated(ScheduleTaskEntity taskEntity, TaskStatus expectedTaskStatus) {
 // then
 TaskStatus actualTaskStatus = taskEntity.getStatus();
 assertEquals(expectedTaskStatus, actualTaskStatus);
 }
}