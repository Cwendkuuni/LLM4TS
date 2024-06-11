package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParametersTestData;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.DataArchiveDeleteJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;
 @Mock
 private ObjectMapper objectMapper;

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws Exception {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = DataArchiveClearParametersTestData.createDataArchiveClearParameters();
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

 // when
 try {
 dataArchiveDeleteJob.execute(context);
 fail("expected JobExecutionException");
 } catch (JobExecutionException e) {
 // then
 assertEquals("Data archive task not found", e.getMessage());
 }

 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() throws Exception {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = DataArchiveClearParametersTestData.createDataArchiveClearParameters();
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setJobName("scheduleJobName");
 dataArchiveTask.setJobGroup("scheduleJobGroup");
 dataArchiveTask.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 dataArchiveTask.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask(), new DlmTask())));

 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 context.setMergedJobDataMap(Mockito.mock(JobExecutionContext.JobDataMap.class));
 context.setJobDetail(Mockito.mock(JobDetail.class));
 context.setExecutionContext(Mockito.mock(JobExecutionContext.ExecutionContext.class));
 context.setCause(null);
 context.setFireInstanceId(null);
 context.setPreviousFireInstanceId(null);
 context.setTrigger(null);
 context.setJobName("scheduleJobName");
 context.setJobGroup("scheduleJobGroup");
 context.setJobDataMap(Mockito.mock(JobExecutionContext.JobDataMap.class));
 context.setMergedJobDataMap(Mockito.mock(JobExecutionContext.JobDataMap.class));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveTask.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() throws Exception {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = DataArchiveClearParametersTestData.createDataArchiveClearParameters();
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 dataArchiveTask.setJobName("scheduleJobName");
 dataArchiveTask.setJobGroup("scheduleJobGroup");
 dataArchiveTask.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 dataArchiveTask.setResultJson(JsonUtils.toJson(Arrays.asList(new DlmTask(), new DlmTask())));

 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 context.setMergedJobDataMap(Mockito.mock(JobExecutionContext.JobDataMap.class));
 context.setJobDetail(Mockito.mock(JobDetail.class));
 context.setExecutionContext(Mockito.mock(JobExecutionContext.ExecutionContext.class));
 context.setCause(null);
 context.setFireInstanceId(null);
 context.setPreviousFireInstanceId(null);
 context.setTrigger(null);
 context.setJobName("scheduleJobName");
 context.setJobGroup("scheduleJobGroup");
 context.setJobDataMap(Mockito.mock(JobExecutionContext.JobDataMap.class));
 context.setMergedJobDataMap(Mockito.mock(JobExecutionContext.JobDataMap.class));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveTask.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() throws Exception {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = DataArchiveClearParametersTestData.createDataArchiveClearParameters();
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setJobName("scheduleJobName");
 dataArchiveTask.setJobGroup("scheduleJobGroup");
 dataArchiveTask.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

 JsonNode dataArchiveTaskResultJson = JsonUtils.fromJson(dataArchiveTask.getResultJson(), JsonNode.class);
 List<DlmTask> taskUnits = JsonUtils.fromJson(dataArchiveTaskResultJson, new TypeReference<List<DlmTask>>() {});

 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 context.setMergedJobDataMap(Mockito.mock(JobExecutionContext.JobDataMap.class));
 context.setJobDetail(Mockito.mock(JobDetail.class));
 context.setExecutionContext(Mockito.mock(JobExecutionContext.ExecutionContext.class));
 context.setCause(null);
 context.setFireInstanceId(null);
 context.setPreviousFireInstanceId(null);
 context.setTrigger(null);
 context.setJobName("scheduleJobName");
 context.setJobGroup("scheduleJobGroup");
 context.setJobDataMap(Mockito.mock(JobExecutionContext.JobDataMap.class));
 context.setMergedJobDataMap(Mockito.mock(JobExecutionContext.JobDataMap.class));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 TaskStatus taskStatus = getTaskStatus(taskUnits);
 verify(scheduleTaskRepository, times(1)).updateStatusById(dataArchiveTask.getId(), taskStatus);
 }

 private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
 JsonNode taskUnitsJson = JsonUtils.toJson(taskUnits);
 return JsonUtils.fromJson(taskUnitsJson.get(0).get("status").asText(), TaskStatus.class);
 }
}