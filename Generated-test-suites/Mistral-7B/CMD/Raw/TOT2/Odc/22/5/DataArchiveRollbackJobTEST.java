import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DataArchiveRollbackJobTest {

 @InjectMocks
 private DataArchiveRollbackJob dataArchiveRollbackJob;

 @Test
 public void testExecute_ValidParameters_Success() {
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("validTaskId");
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 dataArchiveRollbackJob.execute(null);

 Assertions.assertNull(taskEntity.getStatus());
 }

 @Test
 public void testExecute_InvalidTaskEntity_Failed() {
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("invalidTaskId");
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 dataArchiveRollbackJob.execute(null);

 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_InvalidDataArchiveTask_Failed() {
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("validTaskId");
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 DataArchiveRollbackParameters invalidRollbackParameters = new DataArchiveRollbackParameters();
 invalidRollbackParameters.setDataArchiveTaskId("invalidTaskId");

 Mockito.when(taskEntity.getParametersJson())
 .thenReturn(JsonUtils.toJsonString(invalidRollbackParameters));

 dataArchiveRollbackJob.execute(null);

 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_EmptyDlmTasks_Failed() {
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("validTaskId");
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 DataArchiveRollbackParameters rollbackParametersWithEmptyTasks = new DataArchiveRollbackParameters();
 rollbackParametersWithEmptyTasks.setDataArchiveTaskId("validTaskId");
 rollbackParametersWithEmptyTasks.setResultJson(JsonUtils.toJsonString(new TypeReference<List<DlmTask>>() {}.getType()));

 Mockito.when(taskEntity.getResultJson())
 .thenReturn(rollbackParametersWithEmptyTasks.getResultJson());

 dataArchiveRollbackJob.execute(null);

 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_ValidDlmTasks_Success() {
 ScheduleTaskEntity taskEntity = Mockito.mock(ScheduleTaskEntity.class);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("validTaskId");
 rollbackParameters.setResultJson("[{'sourceDatabaseId':1},{'sourceDatabaseId':2}]");
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 Mockito.when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 Mockito.when(jsonUtils.fromJson(taskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {}.getType()))
 .thenReturn(dlmTasks);

 dataArchiveRollbackJob.execute(null);

 for (DlmTask dlmTask : dlmTasks) {
 Mockito.verify(dlmTask).setId(Mockito.anyLong());
 Mockito.verify(dlmTask).setSourceDatabaseId(Mockito.notNull());
 Mockito.verify(dlmTask).setTargetDatabaseId(Mockito.notNull());
 Mockito.verify(dlmTask).setJobType(JobType.ROLLBACK);
 Mockito.verify(dlmTask).setStatus(Mockito.notNull());
 }

 Assertions.assertNull(taskEntity.getStatus());
 }
}