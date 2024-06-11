import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.MockitoJobDetailFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private Scheduler scheduler;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 ObjectMapper objectMapper = new ObjectMapper();
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJob");
 scheduleTaskEntity.setJobGroup("testGroup");
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));
 scheduler = new StdSchedulerFactory().getScheduler();
 }

 @Test
 public void testExecute_NoDataArchiveTaskFound_RollbackTaskFailed() throws JobExecutionException, Exception {
 // Given
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(scheduleTaskEntity);

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 verify(context, times(1)).updateStatusById(eq(scheduleTaskEntity.getId()), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskFound_RollbackTasksExecuted() throws JobExecutionException, Exception {
 // Given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJob");
 scheduleTaskEntity.setJobGroup("testGroup");
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setParametersJson(new ObjectMapper().writeValueAsString(rollbackParameters));
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

 when(context.getResult()).thenReturn(dataArchiveTask);

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 verify(context, times(1)).updateStatusById(eq(dataArchiveTask.getId()), any(TaskStatus.class));
 // Check if tasks were executed
 for (DlmTask task : dlmTasks) {
 assertEquals(TaskStatus.DONE, task.getStatus());
 }
 }

 @Test
 public void testExecute_DataArchiveTaskFound_RollbackTasksPartiallyExecuted() throws JobExecutionException, Exception {
 // Given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJob");
 scheduleTaskEntity.setJobGroup("testJob");
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setParametersJson(new ObjectMapper().writeValueAsString(rollbackParameters));
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 DlmTask failedTask = dlmTasks.get(0);
 failedTask.setStatus(TaskStatus.FAILED);

 when(context.getResult()).thenReturn(dataArchiveTask);

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 verify(context, times(1)).updateStatusById(eq(dataArchiveTask.getId()), any(TaskStatus.class));
 // Check if all but one task were executed
 for (int i = 0; i < dlmTasks.size() - 1; i++) {
 assertEquals(TaskStatus.DONE, dlmTasks.get(i).getStatus());
 }
 // Check if the failed task remained the same
 assertEquals(TaskStatus.FAILED, failedTask.getStatus());
 }

 @Test
 public void testExecute_NullContext_ShouldThrowJobExecutionException() {
 // Given, When, Then
 assertThrows(JobExecutionException.class, () -> dataArchiveRollbackJob.execute(null));
 }
}