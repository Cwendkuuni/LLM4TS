import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockObject;
import org.quartz.impl.JobDetail;
import org.quartz.impl.StdSchedulerFactory;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private JobExecutionContext context;
 private ScheduleTaskEntity taskEntity;
 private StdSchedulerFactory schedulerFactory;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 context = mock(JobExecutionContext.class);
 schedulerFactory = new StdSchedulerFactory();
 }

 @Test
 public void testExecute_success() {
 // Given
 taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 JobDetail jobDetail = new JobDetail("testJobName", "testJobGroup", DataArchiveRollbackJob.class);
 when(context.getResult()).thenReturn(taskEntity);

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 assertEquals(TaskStatus.DONE, taskEntity.getStatus());
 }

 @Test
 public void testExecute_dataArchiveTaskNotFound() {
 // Given
 taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 when(context.getResult()).thenReturn(taskEntity);
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_taskUnitsPreparing() {
 // Given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 List<DlmTask> taskUnits = new java.util.ArrayList<>();
 DlmTask taskUnit = new DlmTask();
 taskUnit.setStatus(TaskStatus.PREPARING);
 taskUnits.add(taskUnit);
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(taskUnits));
 when(context.getResult()).thenReturn(dataArchiveTask);

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 assertEquals(TaskStatus.DONE, dataArchiveTask.getStatus());
 }

 @Test(expected = IllegalStateException.class)
 public void testExecute_taskUnitsAlreadyDone() {
 // Given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 List<DlmTask> taskUnits = new java.util.ArrayList<>();
 DlmTask taskUnit = new DlmTask();
 taskUnit.setStatus(TaskStatus.DONE);
 taskUnits.add(taskUnit);
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(taskUnits));
 when(context.getResult()).thenReturn(dataArchiveTask);

 // When & Then
 dataArchiveRollbackJob.execute(context);
 }
}