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

 private ScheduleTaskEntity taskEntity;
 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 taskEntity = Mockito.spy(new ScheduleTaskEntity());
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_DataArchiveTaskExists_Success() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 taskEntity.setStatus(TaskStatus.READY);
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setStatus(TaskStatus.DONE);
 dlmTasks.add(dlmTask);

 // When
 dataArchiveRollbackJob.execute(createJobExecutionContext(taskEntity));

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound_Failed() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 taskEntity.setStatus(TaskStatus.READY);

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

 // When
 dataArchiveRollbackJob.execute(createJobExecutionContext(taskEntity));

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_TasksExecutionFailed_Failed() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 taskEntity.setStatus(TaskStatus.READY);

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setStatus(TaskStatus.FAILED);
 dlmTasks.add(dlmTask);

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.executeTask(taskEntity.getId(), dlmTasks)).thenThrow(RuntimeException.class);

 // When
 dataArchiveRollbackJob.execute(createJobExecutionContext(taskEntity));

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 // Add test cases for other methods and edge cases as needed

 private JobExecutionContext createJobExecutionContext(ScheduleTaskEntity taskEntity) {
 return Mockito.mock(JobExecutionContext.class);
 }
}