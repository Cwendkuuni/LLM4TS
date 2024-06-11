package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
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
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.DataArchiveRollbackJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_WhenDataArchiveTaskNotFound_ThenStatusUpdatedToFailed() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(-1L);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // When
 dataArchiveRollbackJob.execute(null);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_WhenDataArchiveTaskFound_ThenTasksExecutedAndStatusUpdated() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 taskEntity.setResultJson(JsonUtils.toJsonString(new ArrayList<>()));

 List<DlmTask> tasks = new ArrayList<>();
 tasks.add(new DlmTask());
 tasks.add(new DlmTask());
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 // When
 dataArchiveRollbackJob.execute(null);

 // Then
 verify(dataArchiveRollbackJob, times(1)).executeTask(taskEntity.getId(), tasks);
 TaskStatus taskStatus = getTaskStatus(tasks);
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), taskStatus);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecute_WhenNullContextProvided_ThenThrowException() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();

 // When & Then
 dataArchiveRollbackJob.execute(null);
 }

 private TaskStatus getTaskStatus(List<DlmTask> tasks) {
 if (tasks.stream().anyMatch(task -> task.getStatus() == TaskStatus.FAILED)) {
 return TaskStatus.FAILED;
 } else {
 return TaskStatus.DONE;
 }
 }
}