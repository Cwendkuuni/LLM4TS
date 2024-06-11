import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveRollbackJobTest {

 private ScheduleTaskRepository scheduleTaskRepository;
 private DataArchiveRollbackJob dataArchiveRollbackJob;

 @Before
 public void setup() {
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dataArchiveRollbackJob = new DataArchiveRollbackJob() {
 @Override
 ScheduleTaskRepository getScheduleTaskRepository() {
 return scheduleTaskRepository;
 }
 };
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFound_shouldUpdateTaskStatusToFailed() throws JobExecutionException {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("non-existing-task-id");
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(scheduleTaskRepository.findById("non-existing-task-id")).thenReturn(Optional.empty());

 // when
 dataArchiveRollbackJob.execute(context);

 // then
 assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus());
 }

 @Test
 public void testExecute_whenDataArchiveTaskFound_shouldUpdateTaskStatusToDone() throws JobExecutionException {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("existing-task-id");
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("existing-task-id");
 dataArchiveTask.setJobName("test-job-name");
 dataArchiveTask.setJobGroup("test-job-group");
 dataArchiveTask.setParametersJson(new ObjectMapper().writeValueAsString(rollbackParameters));
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(scheduleTaskRepository.findById("existing-task-id")).thenReturn(Optional.of(dataArchiveTask));

 List<DlmTask> tasks = new ArrayList<>();
 tasks.add(new DlmTask());

 when(dataArchiveRollbackJob.executeTask(dataArchiveTask.getId(), tasks)).thenReturn(true);
 when(dataArchiveRollbackJob.getTaskStatus(tasks)).thenReturn(TaskStatus.DONE);

 // when
 dataArchiveRollbackJob.execute(context);

 // then
 assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus());
 }

 @Test
 public void testExecute_whenTaskExecutionFailed_shouldUpdateTaskStatusToFailed() throws JobExecutionException {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("existing-task-id");
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("existing-task-id");
 dataArchiveTask.setJobName("test-job-name");
 dataArchiveTask.setJobGroup("test-job-group");
 dataArchiveTask.setParametersJson(new ObjectMapper().writeValueAsString(rollbackParameters));
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(scheduleTaskRepository.findById("existing-task-id")).thenReturn(Optional.of(dataArchiveTask));

 List<DlmTask> tasks = new ArrayList<>();
 tasks.add(new DlmTask());

 when(dataArchiveRollbackJob.executeTask(dataArchiveTask.getId(), tasks)).thenReturn(false);
 when(dataArchiveRollbackJob.getTaskStatus(tasks)).thenReturn(TaskStatus.FAILED);

 // when
 dataArchiveRollbackJob.execute(context);

 // then
 assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus());
 }

 @Test
 public void testGetTaskStatus_whenTasksAreNotPrepared_shouldReturnPreparing() throws Exception {
 // given
 List<DlmTask> tasks = new ArrayList<>();
 tasks.add(new DlmTask().setStatus(TaskStatus.PREPARING));

 // when
 TaskStatus actual = dataArchiveRollbackJob.getTaskStatus(tasks);

 // then
 assertEquals(TaskStatus.PREPARING, actual);
 }

 @Test
 public void testGetTaskStatus_whenTasksAreDone_shouldReturnDone() throws Exception {
 // given
 List<DlmTask> tasks = new ArrayList<>();
 tasks.add(new DlmTask().setStatus(TaskStatus.DONE));

 // when
 TaskStatus actual = dataArchiveRollbackJob.getTaskStatus(tasks);

 // then
 assertEquals(TaskStatus.DONE, actual);
 }

 @Test
 public void testGetTaskStatus_whenTasksAreFailed_shouldReturnFailed() throws Exception {
 // given
 List<DlmTask> tasks = new ArrayList<>();
 tasks.add(new DlmTask().setStatus(TaskStatus.FAILED));

 // when
 TaskStatus actual = dataArchiveRollbackJob.getTaskStatus(tasks);

 // then
 assertEquals(TaskStatus.FAILED, actual);
 }

 @Test
 public void testGetTaskStatus_whenTasksAreRunning_shouldReturnRunning() throws Exception {
 // given
 List<DlmTask> tasks = new ArrayList<>();
 tasks.add(new DlmTask().setStatus(TaskStatus.RUNNING));

 // when
 TaskStatus actual = dataArchiveRollbackJob.getTaskStatus(tasks);

 // then
 assertEquals(TaskStatus.RUNNING, actual);
 }
}