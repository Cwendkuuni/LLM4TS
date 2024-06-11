import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobThread;
import com.oceanbase.odc.service.schedule.Repository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private Repository scheduleTaskRepository;
 private ObjectMapper objectMapper;
 private JobThread jobThread;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = Mockito.mock(Repository.class);
 objectMapper = new ObjectMapper();
 jobThread = Mockito.mock(JobThread.class);
 }

 @Test
 public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = createScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("123");
 String json = objectMapper.writeValueAsString(rollbackParameters);
 taskEntity.setParametersJson(json);

 when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong()))
 .thenReturn(Optional.of(taskEntity));

 // when
 dataArchiveRollbackJob.execute(createJobExecutionContext());

 // then
 verifyTasksExecuted(taskEntity.getId());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 ScheduleTaskEntity taskEntity = createScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("123");
 String json = objectMapper.writeValueAsString(rollbackParameters);
 taskEntity.setParametersJson(json);

 when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong()))
 .thenReturn(Optional.empty());

 // when
 try {
 dataArchiveRollbackJob.execute(createJobExecutionContext());
 fail("Expected JobExecutionException to be thrown");
 } catch (JobExecutionException e) {
 // then
 verifyNoTasksExecuted();
 }
 }

 @Test
 public void testExecute_TaskUnitsPrepared() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = createScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("123");
 String json = objectMapper.writeValueAsString(rollbackParameters);
 taskEntity.setParametersJson(json);

 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask taskUnit1 = new DlmTask();
 DlmTask taskUnit2 = new DlmTask();
 taskUnits.add(taskUnit1);
 taskUnits.add(taskUnit2);

 when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong()))
 .thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.FAILED)))
 .thenReturn(1);
 when(scheduleTaskRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.DONE)))
 .thenReturn(1);

 // when
 dataArchiveRollbackJob.execute(createJobExecutionContext());

 // then
 verifyTasksExecuted(taskEntity.getId());
 verifyTasksExecuted(taskEntity.getId(), taskUnits);
 }

 private void verifyTasksExecuted(long taskId, List<DlmTask> expectedTasks) {
 // then
 List<DlmTask> actualTasks = dataArchiveRollbackJob.getTasks(taskId);
 assertEquals(expectedTasks.size(), actualTasks.size());
 for (int i = 0; i < expectedTasks.size(); i++) {
 DlmTask expectedTask = expectedTasks.get(i);
 DlmTask actualTask = actualTasks.get(i);
 assertEquals(expectedTask.getId(), actualTask.getId());
 assertEquals(expectedTask.getSourceDatabaseId(), actualTask.getSourceDatabaseId());
 assertEquals(expectedTask.getTargetDatabaseId(), actualTask.getTargetDatabaseId());
 assertEquals(expectedTask.getJobType(), actualTask.getJobType());
 assertEquals(expectedTask.getStatus(), actualTask.getStatus());
 }
}

 private void verifyNoTasksExecuted() {
 // then
 assertNull(dataArchiveRollbackJob.getTasks(1L));
 }

 private JobExecutionContext createJobExecutionContext() {
 return Mockito.mock(JobExecutionContext.class);
 }

 private ScheduleTaskEntity createScheduleTaskEntity() {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 return taskEntity;
 }
}