package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private JobRepository jobRepository = mock(JobRepository.class);

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setJobRepository(jobRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId1");

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // then
 verify(jobRepository).updateStatusById("taskId1", TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("taskId2");
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId2");

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // then
 verify(jobRepository, never()).updateStatusById("taskId2", TaskStatus.DONE);
 verify(jobRepository).updateStatusById("taskId2", TaskStatus.FAILED);
 }

 @Test
 public void testExecute_Success() {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("taskId3");
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId3");
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId("taskId4");
 dlmTasks.add(dlmTask);

 when(jobRepository.findById("taskId3"))
 .thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // then
 ArgumentCaptor<List<DlmTask>> dlmTasksCaptor = ArgumentCaptor.forClass(List.class);
 verify(dataArchiveDeleteJob).executeTask("taskId3", dlmTasksCaptor.capture());
 TaskStatus taskStatus = TaskStatus.SUCCEEDED;
 verify(jobRepository).updateStatusById("taskId3", taskStatus);
 assertEquals(1, dlmTasksCaptor.getValue().size());
 }
}