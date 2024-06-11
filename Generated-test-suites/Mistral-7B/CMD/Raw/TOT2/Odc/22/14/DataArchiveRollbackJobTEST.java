import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobExecutionContext;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_TaskNotFound() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("123");
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 when(scheduleTaskRepository.findById("123")).thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById("123", TaskStatus.FAILED);
 }

 @Test
 public void testExecute_TaskFound() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("123");
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 taskEntity.setId("123");

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById("123")).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById("123", anyString())).thenReturn(true);

 // when
 dataArchiveRollbackJob.execute(new JobExecutionContext() {});

 // then
 verify(scheduleTaskRepository, times(1)).findById("123");
 verify(scheduleTaskRepository, times(1)).updateStatusById("123", TaskStatus.DONE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExecute_NullContext() {
 // when
 dataArchiveRollbackJob.execute(null);

 // then
 // an exception is thrown
 }

 @Test
 public void testGetTaskStatus() {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 DlmTask dlmTask1 = new DlmTask();
 DlmTask dlmTask2 = new DlmTask();
 dlmTask1.setStatus(TaskStatus.FAILED);
 dlmTask2.setStatus(TaskStatus.DONE);

 // when
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(Arrays.asList(dlmTask1, dlmTask2));

 // then
 assertEquals(TaskStatus.FAILED, taskStatus);
 }

 @Test
 public void testGetTaskStatus_AllTasksFailed() {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask(), new DlmTask());
 dlmTasks.forEach(dlmTask -> dlmTask.setStatus(TaskStatus.FAILED));

 // when
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.FAILED, taskStatus);
 }

 @Test
 public void testGetTaskStatus_AllTasksDone() {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask(), new DlmTask());
 dlmTasks.forEach(dlmTask -> dlmTask.setStatus(TaskStatus.DONE));

 // when
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);
 // then
 assertEquals(TaskStatus.DONE, taskStatus);
 }

 @Test
 public void testGetTaskStatus_MixedStatus() {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask(), new DlmTask());
 DlmTask dlmTask1 = new DlmTask();
 DlmTask dlmTask2 = new DlmTask();
 DlmTask dlmTask3 = new DlmTask();
 dlmTask1.setStatus(TaskStatus.FAILED);
 dlmTask2.setStatus(TaskStatus.DONE);
 dlmTask3.setStatus(TaskStatus.PREPARING);

 // when
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);
 // then
 assertEquals(TaskStatus.PREPARING, taskStatus);
 }
}