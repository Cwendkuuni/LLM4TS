package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
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

 private ObjectMapper objectMapper = new ObjectMapper();

 @org.junit.Before
 public void setUp() {
 }

 @org.junit.Test
 public void testExecute_DataArchiveTaskNotFound() throws Exception {
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);

 when(taskEntity.getParametersJson()).thenReturn(objectMapper.writeValueAsString(new DataArchiveClearParameters()));
 when(taskEntity.getId()).thenReturn(1L);
 when(taskEntity.getJobName()).thenReturn("testJobName");
 when(taskEntity.getJobGroup()).thenReturn("testJobGroup");

 dataArchiveDeleteJob.execute(context);

 ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskEntity.getId()).thenReturn(1L);
 when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.FAILED);

 dataArchiveDeleteJob.scheduleTaskRepository = mock(ScheduleTaskRepository.class);

 dataArchiveDeleteJob.scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED);

 assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(Collections.emptyList()));
 }

 @org.junit.Test
 public void testExecute_DataArchiveTaskNotFinished() throws Exception {
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 when(taskEntity.getParametersJson()).thenReturn(objectMapper.writeValueAsString(dataArchiveClearParameters));
 when(taskEntity.getId()).thenReturn(1L);
 when(taskEntity.getJobName()).thenReturn("testJobName");
 when(taskEntity.getJobGroup()).thenReturn("testJobGroup");

 ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
 when(dataArchiveTask.getId()).thenReturn(1L);
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PROCESSING);
 when(dataArchiveTask.getResultJson()).thenReturn("[]");

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 dataArchiveDeleteJob.scheduleTaskRepository = mock(ScheduleTaskRepository.class);

 dataArchiveDeleteJob.execute(context);

 assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(Collections.emptyList()));
 }

 @org.junit.Test
 public void testExecute_Success() throws Exception {
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 when(taskEntity.getParametersJson()).thenReturn(objectMapper.writeValueAsString(dataArchiveClearParameters));
 when(taskEntity.getId()).thenReturn(1L);
 when(taskEntity.getJobName()).thenReturn("testJobName");
 when(taskEntity.getJobGroup()).thenReturn("testJobGroup");

 ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
 when(dataArchiveTask.getId()).thenReturn(1L);
 when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
 when(dataArchiveTask.getResultJson()).thenReturn(objectMapper.writeValueAsString(createDlmTasks(2)));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 dataArchiveDeleteJob.scheduleTaskRepository = mock(ScheduleTaskRepository.class);

 List<DlmTask> dlmTasks = dataArchiveDeleteJob.execute(context);

 assertEquals(TaskStatus.SUCCESS, dataArchiveDeleteJob.getTaskStatus(dlmTasks));
 }

 private List<DlmTask> createDlmTasks(int size) {
 List<DlmTask> dlmTasks = new ArrayList<>();
 for (int i = 0; i < size; i++) {
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L + i);
 dlmTask.setJobType(JobType.DELETE);
 dlmTask.setStatus(TaskStatus.SUCCESS);
 dlmTasks.add(dlmTask);
 }
 return dlmTasks;
 }
}