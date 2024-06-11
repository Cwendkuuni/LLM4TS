package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.JobExecutionContext;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("non-existent-task-id");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskFound() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("existing-task-id");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId("existing-task-id");
 taskEntity.setResultJson("[]");
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("dataArchiveTaskId");
 dataArchiveTask.setJobName("jobName");
 dataArchiveTask.setJobGroup("jobGroup");
 dataArchiveTask.setParametersJson(JsonUtils.writeValueAsString(rollbackParameters));

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_NullDataArchiveTask() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("existing-task-id");
 ScheduleTaskEntity taskEntity = null;
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 // when & then
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});
 }

 @Test
 public void testExecute_NullTaskUnits() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("existing-task-id");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId("existing-task-id");
 taskEntity.setResultJson("[]");
 List<DlmTask> taskUnits = null;

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testGetTaskStatus() {
 // given
 List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

 // when
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

 // then
 assertEquals(TaskStatus.DONE, taskStatus);
 }
}