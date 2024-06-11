package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
scheduleTaskEntity.setStatus(TaskStatus.PENDING);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
         .thenReturn(Optional.empty());

 // when
 try {
 dataArchiveDeleteJob.execute(new JobExecutionContext());
 fail("Expected JobExecutionException");
 } catch (JobExecutionException e) {
 // then
 verify(scheduleTaskRepository, times(1))
         .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setResultJson(JsonUtils.toJsonString( Arrays.asList(Mockito.mock(DlmTask.class))));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
         .thenReturn(Optional.of(scheduleTaskEntity));

 // when
 try {
 dataArchiveDeleteJob.execute(new JobExecutionContext());
 fail("Expected JobExecutionException");
 } catch (JobExecutionException e) {
 // then
 verify(scheduleTaskRepository, times(1))
         .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }
 }

 @Test
 public void testExecute_DataArchiveTaskSuccess() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setResultJson(JsonUtils.toJsonString( Arrays.asList(Mockito.mock(DlmTask.class))));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
         .thenReturn(Optional.of(scheduleTaskEntity));

 ObjectMapper mapper = new ObjectMapper();
 List<DlmTask> dlmTasks = Arrays.asList( Mockito.mock(DlmTask.class), Mockito.mock(DlmTask.class) );

 when(executeTask(scheduleTaskEntity.getId(), dlmTasks))
         .thenReturn(true);

 // when
 DataArchiveClearParameters actual = JsonUtils.fromJson(dataArchiveDeleteJob.execute(new JobExecutionContext())
         .getJobDetail().getJobDataMap().get("dataArchiveClearParameters"), DataArchiveClearParameters.class);

 // then
 assertEquals(dataArchiveClearParameters, actual);
 verify(scheduleTaskRepository, times(1))
         .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.COMPLETED);
 }
}