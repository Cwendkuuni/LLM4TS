package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.DataArchiveRollbackJob;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setUp() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 String dataArchiveTaskId = "123456";
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);
 taskEntity.setId("taskId");
 taskEntity.setParametersJson(JsonUtils.writeValueAsString(rollbackParameters));
 when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // then
 verify(scheduleTaskRepository).updateStatusById(any(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskFound() {
 // given
 String dataArchiveTaskId = "123456";
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("dataArchiveTaskId");
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId("taskId");
 taskEntity.setParametersJson(JsonUtils.writeValueAsString(new DataArchiveRollbackParameters()));
 taskEntity.setResultJson(JsonUtils.writeValueAsString(createDlmTasks()));
 when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq("dataArchiveTaskId"), any(TaskStatus.class));
 }

 @Test
 public void testGetTaskStatus() {
 // given
 List<DlmTask> dlmTasks = createDlmTasks();

 // when
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.DONE, taskStatus);
 }

 private List<DlmTask> createDlmTasks() {
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setStatus(TaskStatus.DONE);
 dlmTasks.add(dlmTask);
 return dlmTasks;
 }
}