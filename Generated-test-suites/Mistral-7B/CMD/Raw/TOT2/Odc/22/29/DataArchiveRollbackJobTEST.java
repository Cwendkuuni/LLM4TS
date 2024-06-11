import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private JobRepository jobRepository;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 jobRepository = mock(JobRepository.class);
 }

 @Test
 public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = createScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = createRollbackParameters();
 when(jobRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));

 // When
 dataArchiveRollbackJob.execute(createJobExecutionContext(scheduleTaskEntity));

 // Then
 assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = createScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = createRollbackParameters();
 when(jobRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

 // When & Then
 dataArchiveRollbackJob.execute(createJobExecutionContext(scheduleTaskEntity));
 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_TaskUnitsPrepared() throws JobExecutionException {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = createScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = createRollbackParameters();
 List<DlmTask> taskUnits = createTaskUnits();
 when(jobRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));

 // When
 dataArchiveRollbackJob.execute(createJobExecutionContext(scheduleTaskEntity));

 // Then
 assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
 for (DlmTask taskUnit : taskUnits) {
 assertEquals(TaskStatus.DONE, taskUnit.getStatus());
 }
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_TaskUnitError() throws JobExecutionException {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = createScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = createRollbackParameters();
 List<DlmTask> taskUnits = createTaskUnits();
 taskUnits.get(0).setStatus(TaskStatus.FAILED);
 when(jobRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(scheduleTaskEntity));

 // When
 dataArchiveRollbackJob.execute(createJobExecutionContext(scheduleTaskEntity));

 // Then
 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 // Utils
 private ScheduleTaskEntity createScheduleTaskEntity() {
 return new ScheduleTaskEntity();
 }

 private DataArchiveRollbackParameters createRollbackParameters() {
 ObjectMapper objectMapper = new ObjectMapper();
 return new DataArchiveRollbackParameters("testDataArchiveTaskId", "testJson");
 }

 private List<DlmTask> createTaskUnits() {
 return List.of(new DlmTask(), new DlmTask());
 }

 private JobExecutionContext createJobExecutionContext(ScheduleTaskEntity scheduleTaskEntity) {
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 when(context.getMergedJobDataMap().getString("taskEntity")).thenReturn(JsonUtils.toJsonString(scheduleTaskEntity));
 return context;
 }
}