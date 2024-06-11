package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.core.type.TypeReference;
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
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

 @Before
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // then
 assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setId(1L);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTasks.add(dlmTask);

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // then
 assertEquals(TaskStatus.COMPLETED, dataArchiveDeleteJob.getTaskStatus());
 }

 @Test(expected = IllegalStateException.class)
 public void testExecute_InvalidDataArchiveTaskStatus() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.FAILED);
 dataArchiveTask.setId(1L);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTasks.add(dlmTask);

 // when & then
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 }

 @Test
 public void testExecute_ClearTasks() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setId(1L);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTasks.add(dlmTask);

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // then
 assertEquals(TaskStatus.COMPLETED, dataArchiveDeleteJob.getTaskStatus());
 }
}