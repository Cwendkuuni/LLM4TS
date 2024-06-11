package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

/**
 * Test class for DataArchiveDeleteJob
 */
@Slf4j
public class DataArchiveDeleteJobTest {

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 Mockito.when(scheduleTaskRepository.findById(Mockito.eq(dataArchiveClearParameters.getDataArchiveTaskId())))
 .thenReturn(Optional.of(dataArchiveTask));

 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(Mockito.eq(dataArchiveClearParameters.getDataArchiveTaskId()),
 Mockito.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_Success() throws Exception {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 Mockito.when(scheduleTaskRepository.findById(Mockito.eq(dataArchiveClearParameters.getDataArchiveTaskId())))
 .thenReturn(Optional.of(dataArchiveTask));

 // prepare mocks for executeTask
 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setId(1L);
 dlmTask1.setStatus(TaskStatus.PREPARING);
 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setId(2L);
 dlmTask2.setStatus(TaskStatus.PREPARING);
 List<DlmTask> taskUnits = Arrays.asList(dlmTask1, dlmTask2);

 Mockito.when(dataArchiveDeleteJob.executeTask(Mockito.anyLong(), Mockito.eq(taskUnits)))
 .thenReturn(true);

 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(Mockito.eq(dataArchiveClearParameters.getDataArchiveTaskId()),
 Mockito.any(TaskStatus.class));
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_TaskExecutionFailed() throws Exception {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 Mockito.when(scheduleTaskRepository.findById(Mockito.eq(dataArchiveClearParameters.getDataArchiveTaskId())))
 .thenReturn(Optional.of(dataArchiveTask));

 // prepare mocks for executeTask
 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setId(1L);
 dlmTask1.setStatus(TaskStatus.PREPARING);
 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setId(2L);
 dlmTask2.setStatus(TaskStatus.PREPARING);
 List<DlmTask> taskUnits = Arrays.asList(dlmTask1, dlmTask2);

 Mockito.when(dataArchiveDeleteJob.executeTask(Mockito.anyLong(), Mockito.eq(taskUnits)))
 .thenThrow(new JobExecutionException("Test error"));

 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);

 // when, then
 dataArchiveDeleteJob.execute(context);

 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(Mockito.eq(dataArchiveClearParameters.getDataArchiveTaskId()),
 Mockito.eq(TaskStatus.FAILED));
 }
}