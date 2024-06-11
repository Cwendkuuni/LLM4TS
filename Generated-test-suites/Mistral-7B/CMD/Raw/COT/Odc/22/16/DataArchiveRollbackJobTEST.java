package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ObjectMapper objectMapper;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskRepository scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 objectMapper = new ObjectMapper();

 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("jobName");
 scheduleTaskEntity.setJobGroup("jobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters(1L, Arrays.asList(new DlmTask(1L, 1L, "taskUnit1")))));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

 verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskFound() {
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(Arrays.asList(new DlmTask(1L, 1L, "taskUnit1"))));

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));

 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

 verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.DONE);
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_ExceptionInExecuteTask() {
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(scheduleTaskEntity));

 doThrow(new RuntimeException("Exception in executeTask")).when(dataArchiveRollbackJob).executeTask(1L, Arrays.asList(new DlmTask(1L, 1L, "taskUnit1")));

 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));
 }

 @Test
 public void testGetTaskStatus() {
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(1L, 1L, "taskUnit1"), new DlmTask(1L, 1L, "taskUnit2"));

 List<DlmTask> doneTasks = Arrays.asList(new DlmTask(1L, 1L, "taskUnit1"), new DlmTask(1L, 1L, "taskUnit2"));
 dlmTasks.forEach(task -> task.setStatus(TaskStatus.DONE));

 assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(doneTasks));

 List<DlmTask> preparingTasks = Arrays.asList(new DlmTask(1L, 1L, "taskUnit1"), new DlmTask(1L, 1L, "taskUnit2"));
 dlmTasks.forEach(task -> task.setStatus(TaskStatus.PREPARING));

 assertEquals(TaskStatus.PREPARING, dataArchiveRollbackJob.getTaskStatus(preparingTasks));
}
}