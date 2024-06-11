package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

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
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

 // then
 assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus());
 }

 @Test
 public void testExecute_PrepareTasksForRollback() throws JobExecutionException {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setId(1L);
 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setId(2L);
 dlmTasks.add(dlmTask1);
 dlmTasks.add(dlmTask2);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.DONE)).thenReturn(true);

 // when
 dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));

 // then
 assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus());

 ObjectMapper objectMapper = new ObjectMapper();
 JsonNode resultJsonNode = objectMapper.valueToTree(dlmTasks);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setResultJson(resultJsonNode.toString());
 verify(scheduleTaskRepository).save(scheduleTaskEntity);
 }

 private TaskStatus getTaskStatus(List<DlmTask> dlmTasks) {
 List<DlmTask> doneTasks = new ArrayList<>();
 for (DlmTask dlmTask : dlmTasks) {
 if (TaskStatus.DONE.equals(dlmTask.getStatus())) {
 doneTasks.add(dlmTask);
 }
 }
 if (doneTasks.size() == dlmTasks.size()) {
 return TaskStatus.DONE;
 } else {
 return TaskStatus.PREPARING;
 }
 }
}