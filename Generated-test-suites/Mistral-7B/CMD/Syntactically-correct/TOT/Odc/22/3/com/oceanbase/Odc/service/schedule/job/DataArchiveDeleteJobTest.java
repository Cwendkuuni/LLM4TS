package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveDeleteJobTest {

 private ObjectMapper objectMapper = new ObjectMapper();
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

 @Test
 public void testDataArchiveDeleteJobSuccess() throws JobExecutionException {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("dataArchiveDeleteJob");
 taskEntity.setJobGroup("default");
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 JsonNode parametersJson = objectMapper.valueToTree(dataArchiveClearParameters);
 taskEntity.setParametersJson(JsonUtils.toJsonString(parametersJson));

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(1);
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.PREPARING)).thenReturn(1);
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.RUNNING)).thenReturn(1);
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.COMPLETED)).thenReturn(1);

 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 assertEquals(TaskStatus.COMPLETED, dataArchiveDeleteJob.getTaskStatus(Arrays.asList(new DlmTask())));
 }

 @Test
 public void testDataArchiveDeleteJobDataArchiveTaskNotFound() {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("dataArchiveDeleteJob");
 taskEntity.setJobGroup("default");
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(100L);
 JsonNode parametersJson = objectMapper.valueToTree(dataArchiveClearParameters);
 taskEntity.setParametersJson(JsonUtils.toJsonString(parametersJson));

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(1);

 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(Arrays.asList(new DlmTask())));
 }

 @Test
 public void testDataArchiveDeleteJobDataArchiveTaskNotDone() {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("dataArchiveDeleteJob");
 taskEntity.setJobGroup("default");
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 JsonNode parametersJson = objectMapper.valueToTree(dataArchiveClearParameters);
 taskEntity.setParametersJson(JsonUtils.toJsonString(parametersJson));
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 dataArchiveTask.setJobName("dataArchiveDeleteJob");
 dataArchiveTask.setJobGroup("default");
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(Arrays.asList(new DlmTask())));

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(1);

 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(Arrays.asList(new DlmTask())));
 }
}