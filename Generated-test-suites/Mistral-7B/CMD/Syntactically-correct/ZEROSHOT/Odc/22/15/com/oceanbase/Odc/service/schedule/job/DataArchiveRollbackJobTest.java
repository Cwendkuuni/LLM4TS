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

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.model.DlmTaskStatus;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private JobExecutionContext jobExecutionContext;
 private ScheduleTaskEntity scheduleTaskEntity;
 private DlmTask dlmTask;

 @Before
 public void setUp() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 jobExecutionContext = mock(JobExecutionContext.class);
 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
 dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setStatus(DlmTaskStatus.DONE);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

 try {
 dataArchiveRollbackJob.execute(jobExecutionContext);
 } catch (JobExecutionException e) {
 // expected
 }

 verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskFound() {
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

 try {
 dataArchiveRollbackJob.execute(jobExecutionContext);
 } catch (JobExecutionException e) {
 // not expected
 }

 assertEquals(TaskStatus.DONE, getTaskStatus(dataArchiveRollbackJob.executeTasks(new ArrayList<>(), new ArrayList<>())));
 verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.DONE);
 }

 @Test
 public void testExecuteTasks_TasksRollbackSuccess() {
 DlmTask taskUnit = new DlmTask();
 taskUnit.setId(1L);
 taskUnit.setStatus(DlmTaskStatus.PREPARING);
 List<DlmTask> tasks = new ArrayList<>();
 tasks.add(taskUnit);

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> tasksRollback = dataArchiveRollbackJob.executeTasks(tasks, new ArrayList<>());

 assertEquals(1, tasksRollback.size());
 assertEquals(DlmJobIdUtil.generateHistoryJobId(scheduleTaskEntity.getJobName(), scheduleTaskEntity.getJobGroup(),
 scheduleTaskEntity.getId(), 1), tasksRollback.get(0).getId());
 assertEquals(DlmTaskStatus.DONE, tasksRollback.get(0).getStatus());
 }

 @Test
 public void testExecuteTasks_TasksRollbackFailed() {
 DlmTask taskUnit = new DlmTask();
 taskUnit.setId(1L);
 taskUnit.setStatus(DlmTaskStatus.FAILED);
 List<DlmTask> tasks = new ArrayList<>();
 tasks.add(taskUnit);

 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> tasksRollback = dataArchiveRollbackJob.executeTasks(tasks, new ArrayList<>());

 assertEquals(1, tasksRollback.size());
 assertEquals(DlmTaskStatus.FAILED, tasksRollback.get(0).getStatus());
 }

 @Test
 public void testGetTaskStatus_AllTasksDone() {
 List<DlmTask> tasks = new ArrayList<>();
 tasks.add(new DlmTask());
 tasks.add(new DlmTask());
 tasks.add(new DlmTask());
 tasks.get(0).setStatus(DlmTaskStatus.DONE);
 tasks.get(1).setStatus(DlmTaskStatus.DONE);
 tasks.get(2).setStatus(DlmTaskStatus.DONE);

 assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(tasks));
 }

 @Test
 public void testGetTaskStatus_TasksNotAllDone() {
 List<DlmTask> tasks = new ArrayList<>();
 tasks.add(new DlmTask());
 tasks.add(new DlmTask());
 tasks.add(new DlmTask());
 tasks.get(0).setStatus(DlmTaskStatus.DONE);
 tasks.get(1).setStatus(DlmTaskStatus.FAILED);
 tasks.get(2).setStatus(DlmTaskStatus.PREPARING);

 assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(tasks));
 }
}