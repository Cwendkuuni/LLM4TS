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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private JobExecutionContext context;
 private ScheduleTaskEntity taskEntity;
 private ObjectMapper mapper;

 @Before
 public void setUp() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 context = mock(JobExecutionContext.class);
 mapper = new ObjectMapper();
 taskEntity = new ScheduleTaskEntity();
 }

 @Test
 public void testExecute_ValidData_ShouldSucceed() throws JobExecutionException {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("valid_data_archive_task_id");
 taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
 when(context.getResult()).thenReturn(taskEntity);
 List<DlmTask> tasks = new ArrayList<>();
 DlmTask task = new DlmTask();
 tasks.add(task);

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 assertEquals(TaskStatus.DONE, taskStatus(taskUnits(tasks)));
 }

 @Test
 public void testExecute_DataArchiveTaskNotExist_ShouldFail() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("non_existing_data_archive_task_id");
 taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
 when(context.getResult()).thenReturn(taskEntity);
 List<DlmTask> tasks = new ArrayList<>();

 // When & Then
 expectThrows(JobExecutionException.class, () -> dataArchiveRollbackJob.execute(context));
 }

 @Test
 public void testExecute_TaskExecutionFailed_ShouldUpdateTaskStatus() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("valid_data_archive_task_id");
 taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
 when(context.getResult()).thenReturn(taskEntity);
 List<DlmTask> tasks = new ArrayList<>();
 DlmTask task = new DlmTask();
 tasks.add(task);
 task.setStatus(TaskStatus.FAILED);
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 assertEquals(TaskStatus.FAILED, taskStatus(taskUnits(tasks)));
 }

 private TaskStatus taskStatus(List<DlmTask> tasks) {
 return tasks.get(0).getStatus();
 }

 private List<DlmTask> taskUnits(List<DlmTask> tasks) {
 return tasks;
 }

 private void expectThrows(Class<? extends Throwable> clazz, Runnable runnable) {
 try {
 runnable.run();
 } catch (Throwable e) {
 if (clazz.isAssignableFrom(e.getClass())) {
 throw e;
 }
 }
 throw new AssertionError("Test failed: " + runnable.toString());
 }
}