package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveDeleteJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setup() {
 ObjectMapper objectMapper = new ObjectMapper();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 String jsonString = JsonUtils.toJsonString(dataArchiveClearParameters);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(jsonString);
 scheduleTaskEntity.setJobName("TestJob");
 scheduleTaskEntity.setJobGroup("TestGroup");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));
 }

 @Test
 public void testExecute_DataArchiveTaskFoundAndFinished_TasksExecutedSuccessfully() {
 JobExecutionContext context = mock(JobExecutionContext.class);
 List<DlmTask> dlmTasks = new ArrayList<>();
 dlmTasks.add(new DlmTask());

 when(scheduleTaskRepository.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED)).thenReturn(true);
 dataArchiveDeleteJob.execute(context);
 assertEquals(TaskStatus.FAILED, scheduleTaskRepository.findById(1L).get().getStatus());

 when(scheduleTaskRepository.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED))
 .thenReturn(false);

 dataArchiveDeleteJob.execute(context);
 List<DlmTask> taskUnits = JsonUtils.fromJson(scheduleTaskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {});
 assertEquals(1, taskUnits.size());
 TaskStatus taskStatus = getTaskStatus(taskUnits);
 assertEquals(TaskStatus.SUCCESS, taskStatus);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound_TaskStatusNotUpdated() {
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 assertEquals(TaskStatus.DONE, scheduleTaskRepository.findById(1L).get().getStatus());
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_DataArchiveTaskNotFinished_ExceptionThrown() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setJobName("TestJob");
 scheduleTaskEntity.setJobGroup("TestGroup");
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound_ExceptionThrown() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 }
}