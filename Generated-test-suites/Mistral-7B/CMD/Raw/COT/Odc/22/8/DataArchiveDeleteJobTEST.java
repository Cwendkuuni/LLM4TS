package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.JobExecutionContext;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

 @Before
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob() {
 @Override
 ScheduleTaskRepository getScheduleTaskRepository() {
 return scheduleTaskRepository;
 }
 };
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // arrange
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // act
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // assert
 Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() {
 // arrange
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // act
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // assert
 List<DlmTask> dlmTasks = new ArrayList<>();
 dlmTasks.add(new DlmTask());
 Mockito.verify(dataArchiveDeleteJob).executeTask(taskEntity.getId(), dlmTasks);
 TaskStatus taskStatus = TaskStatus.SUCCEED;
 Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), taskStatus);
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_DataArchiveTaskNotFinished() {
 // arrange
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // act and assert
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 }
}