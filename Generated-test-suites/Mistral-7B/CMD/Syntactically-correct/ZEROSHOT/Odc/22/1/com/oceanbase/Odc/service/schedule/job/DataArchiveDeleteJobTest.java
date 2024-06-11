package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.DataArchiveDeleteJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 // when & then
 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 try {
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 fail("Expected JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // expected
 }

 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyDone() {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setParametersJson("{}");

 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 // when
 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.never())
 .updateStatusById(Mockito.anyLong(), Mockito.not(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskNotDone() {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.FAILED);
 dataArchiveTask.setParametersJson("{}");

 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 // when
 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 try {
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 fail("Expected JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // expected
 }

 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(Mockito.anyLong(), Mockito.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskDeleted() {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setParametersJson("{\"dataArchiveTaskId\":1,\"taskUnits\":[" +
 "{\"taskUnitId\":1,\"taskUnitName\":\"taskUnit1\",\"status\":\"DONE\"}," +
 "{\"taskUnitId\":2,\"taskUnitName\":\"taskUnit2\",\"status\":\"DONE\"}]" +
 "}");

 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 // when
 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 DlmTask taskUnit1 = new DlmTask();
 taskUnit1.setId(1L);
 taskUnit1.setTaskUnitName("taskUnit1");
 taskUnit1.setStatus(TaskStatus.DONE);
 taskUnit1.setJobType(JobType.DELETE);

 DlmTask taskUnit2 = new DlmTask();
 taskUnit2.setId(2L);
 taskUnit2.setTaskUnitName("taskUnit2");
 taskUnit2.setStatus(TaskStatus.DONE);
 taskUnit2.setJobType(JobType.DELETE);

 List<DlmTask> taskUnits = Arrays.asList(taskUnit1, taskUnit2);

 try {
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 } catch (JobExecutionException e) {
 fail("Unexpected JobExecutionException to be thrown.");
 }

 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(Mockito.anyLong(), Mockito.not(TaskStatus.FAILED));

 // then
 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .save(Mockito.any(ScheduleTaskEntity.class));
 }
}