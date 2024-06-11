import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.MockitoJobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.StdSchedulerFactory;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.matchers.GroupMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 private DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 doReturn(Optional.empty())
 .when(scheduleTaskRepository)
 .findById(dataArchiveClearParameters.getDataArchiveTaskId());

 Scheduler scheduler = new StdSchedulerFactory().getScheduler();
 try {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("DataArchiveDeleteJob");
 scheduleTaskEntity.setJobGroup("DefaultGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

 JobExecutionContext context = new MockitoJobExecutionContext(scheduleTaskEntity);
 dataArchiveDeleteJob.execute(context);

 verify(scheduleTaskRepository, times(1))
 .updateStatusById(eq(scheduleTaskEntity.getId()), eq(TaskStatus.FAILED));
 } catch (SchedulerException e) {
 throw new JobExecutionException("Test exception", e);
 } finally {
 try {
 scheduler.shutdown();
 } catch (SchedulerException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyDone() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setJobName("DataArchiveDeleteJob");
 dataArchiveTask.setJobGroup("DefaultGroup");
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setResultJson(JsonUtils.toJsonString( Arrays.asList(new DlmTask()) ));

 doReturn(Optional.of(dataArchiveTask))
 .when(scheduleTaskRepository)
 .findById(dataArchiveClearParameters.getDataArchiveTaskId());

 Scheduler scheduler = new StdSchedulerFactory().getScheduler();
 try {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("DataArchiveDeleteJob");
 scheduleTaskEntity.setJobGroup("DefaultGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

 JobExecutionContext context = new MockitoJobExecutionContext(scheduleTaskEntity);
 dataArchiveDeleteJob.execute(context);

 // do nothing as the test is passing if no exception is thrown

 } catch (SchedulerException e) {
 throw new JobExecutionException("Test exception", e);
 } finally {
 try {
 scheduler.shutdown();
 } catch (SchedulerException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testExecute_DataArchiveTaskPreparing() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setJobName("DataArchiveDeleteJob");
 dataArchiveTask.setJobGroup("DefaultGroup");
 dataArchiveTask.setStatus(TaskStatus.PREPARING);
 dataArchiveTask.setResultJson(JsonUtils.toJsonString( Arrays.asList(new DlmTask()) ));

 doReturn(Optional.of(dataArchiveTask))
 .when(scheduleTaskRepository)
 .findById(dataArchiveClearParameters.getDataArchiveTaskId());

 Scheduler scheduler = new StdSchedulerFactory().getScheduler();
 try {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("DataArchiveDeleteJob");
 scheduleTaskEntity.setJobGroup("DefaultGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

 JobExecutionContext context = new MockitoJobExecutionContext(scheduleTaskEntity);
 dataArchiveDeleteJob.execute(context);

 verify(scheduleTaskRepository, never()).updateStatusById(eq(scheduleTaskEntity.getId()), eq(TaskStatus.FAILED));

 } catch (SchedulerException e) {
 throw new JobExecutionException("Test exception", e);
 } finally {
 try {
 scheduler.shutdown();
 } catch (SchedulerException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound_WithMockedDlmTaskRepository() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 doReturn(Optional.empty())
 .when(scheduleTaskRepository)
 .findById(dataArchiveClearParameters.getDataArchiveTaskId());

 DataArchiveDeleteJob dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());
 doThrow(new RuntimeException("Test exception"))
 .when(dataArchiveDeleteJob)
 .executeTask(eq(1L), eq(Arrays.asList(new DlmTask())));

 Scheduler scheduler = new StdSchedulerFactory().getScheduler();
 try {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("DataArchiveDeleteJob");
 scheduleTaskEntity.setJobGroup("DefaultGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

 JobExecutionContext context = new MockitoJobExecutionContext(scheduleTaskEntity);
 dataArchiveDeleteJob.execute(context);

 verify(scheduleTaskRepository, times(1))
 .updateStatusById(eq(scheduleTaskEntity.getId()), eq(TaskStatus.FAILED));

 } catch (SchedulerException e) {
 throw new JobExecutionException("Test exception", e);
 } finally {
 try {
 scheduler.shutdown();
 } catch (SchedulerException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testGetTaskStatus_NoTasksExecuted() {
 List<DlmTask> dlmTasks = Arrays.asList();

 String jsonString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(dlmTasks);

 when(dataArchiveDeleteJob.getTaskUnits(anyLong()))
 .thenReturn(jsonString);

 Assertions.assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(1L));
 }

 @Test
 public void testGetTaskStatus_TasksExecutedButFailed() {
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 dlmTasks.get(0).setStatus(TaskStatus.FAILED);

 String jsonString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(dlmTasks);

 when(dataArchiveDeleteJob.getTaskUnits(anyLong()))
 .thenReturn(jsonString);

 Assertions.assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(1L));
 }

 @Test
 public void testGetTaskStatus_TasksExecutedSuccessfully() {
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 dlmTasks.get(0).setStatus(TaskStatus.SUCCESS);
 dlmTasks.get(1).setStatus(TaskStatus.SUCCESS);

 String jsonString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(dlmTasks);

 when(dataArchiveDeleteJob.getTaskUnits(anyLong()))
 .thenReturn(jsonString);

 Assertions.assertEquals(TaskStatus.SUCCEEDED, dataArchiveDeleteJob.getTaskStatus(1L));
 }

 @Test
 public void testGetTaskStatus_TasksPartiallyFailed() {
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask(), new DlmTask());
 dlmTasks.get(0).setStatus(TaskStatus.FAILED);
 dlmTasks.get(1).setStatus(TaskStatus.SUCCESS);
 dlmTasks.get(2).setStatus(TaskStatus.FAILED);

 String jsonString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(dlmTasks);

 when(dataArchiveDeleteJob.getTaskUnits(anyLong()))
 .thenReturn(jsonString);

 Assertions.assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(1L));
 }
}