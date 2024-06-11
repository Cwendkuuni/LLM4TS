package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.DataArchiveDeleteJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 private DataArchiveClearParameters dataArchiveClearParameters;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskEntity dataArchiveTask;
 private List<DlmTask> taskUnits;
 private ScheduleTaskRepository scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setup() {
 dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("taskId1");

 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId("taskId2");
 scheduleTaskEntity.setJobName("jobName");
 scheduleTaskEntity.setJobGroup("jobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("dataArchiveTaskId");
 dataArchiveTask.setJobName("jobName");
 dataArchiveTask.setJobGroup("jobGroup");
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(new ArrayList<>()));
 dataArchiveTask.setStatus(TaskStatus.DONE);

 taskUnits = new ArrayList<>();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when
 try {
 dataArchiveDeleteJob.execute(new JobExecutionContext());
 fail("Expected exception not thrown");
 } catch (JobExecutionException e) {
 // then
 assertEquals("Data archive task not found", e.getCause().getMessage());
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // given
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // when
 try {
 dataArchiveDeleteJob.execute(new JobExecutionContext());
 fail("Expected exception not thrown");
 } catch (JobExecutionException e) {
 // then
 assertEquals("Data archive task do not finish", e.getCause().getMessage());
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() {
 // given
 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.COMPLETED);
 }
}