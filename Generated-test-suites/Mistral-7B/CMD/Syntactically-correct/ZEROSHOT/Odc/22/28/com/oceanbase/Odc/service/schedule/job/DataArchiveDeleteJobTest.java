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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("nonexistent-task-id");

 // when
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 // then
 verify(scheduleTaskRepository).updateStatusById("nonexistent-task-id", TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() throws JobExecutionException {
 // given
 ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
 when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-task-id");
 when(scheduleTaskRepository.findById("existing-task-id")).thenReturn(Optional.of(scheduleTaskEntity));

 // when
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 // then
 verify(scheduleTaskRepository).updateStatusById("existing-task-id", TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() throws JobExecutionException {
 // given
 ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
 when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-task-id");
 when(scheduleTaskRepository.findById("existing-task-id")).thenReturn(Optional.of(scheduleTaskEntity));

 // when
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 // then
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setStatus(TaskStatus.PREPARING);
 dlmTasks.add(dlmTask);
 verify(dataArchiveDeleteJob).executeTask("existing-task-id", dlmTasks);

 // given mock return value for getTaskStatus
 when(dataArchiveDeleteJob.getTaskStatus(dlmTasks)).thenReturn(TaskStatus.COMPLETED);

 // when
 TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.COMPLETED, taskStatus);

 // then
 verify(scheduleTaskRepository).updateStatusById("existing-task-id", TaskStatus.COMPLETED);
 }
}