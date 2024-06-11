package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.DataArchiveRollbackJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskRepository scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 private DataArchiveRollbackParameters dataArchiveRollbackParameters;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("jobName");
 scheduleTaskEntity.setJobGroup("jobGroup");
 scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");

 dataArchiveRollbackParameters = new DataArchiveRollbackParameters();
 dataArchiveRollbackParameters.setDataArchiveTaskId(1L);
 }

 @Test
 public void testExecute_DataArchiveTaskFound() {
 // given
 when(scheduleTaskRepository.findById(dataArchiveRollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 // when
 dataArchiveRollbackJob.execute(null);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 when(scheduleTaskRepository.findById(dataArchiveRollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(null);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_TaskExecutionSuccess() {
 // given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setResultJson(JsonUtils.toJson(Arrays.asList(createValidDlmTask()), new TypeReference<List<DlmTask>>(){}));

 when(scheduleTaskRepository.findById(dataArchiveRollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveRollbackJob.execute(null);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_TaskExecutionFailed() {
 // given
 when(scheduleTaskRepository.findById(dataArchiveRollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 // when & then
 assertThrows(RuntimeException.class, () -> dataArchiveRollbackJob.execute(null));
 }

 @Test
 public void testGetTaskStatus() {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(createValidDlmTask(), createValidDlmTask());

 // when
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.DONE, taskStatus);
 }

 private DlmTask createValidDlmTask() {
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setSourceDatabaseId(1L);
 dlmTask.setTargetDatabaseId(2L);
 dlmTask.setJobType(JobType.ROLLBACK);
 dlmTask.setStatus(TaskStatus.DONE);
 return dlmTask;
 }
}