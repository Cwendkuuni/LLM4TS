import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoJobExecutionContext;
import org.quartz.MockitoScheduler;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ObjectMapper objectMapper;
 private ScheduleTaskEntity scheduleTaskEntity;
 private Scheduler scheduler;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 objectMapper = new ObjectMapper();
 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduler = new MockitoScheduler();
 StdSchedulerFactory factory = new StdSchedulerFactory();
 factory.setScheduler(scheduler);
 }

 @Test
 public void testExecute_ValidInput_Success() {
 // Given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 JobExecutionContext context = new MockitoJobExecutionContext();
 context.setResult(taskEntity);

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 verify(scheduler).startJob(eq(DataArchiveRollbackJob.class), eq(taskEntity.getId()), anyMap());
 }

 @Test
 public void testExecute_InvalidInput_Failed() {
 // Given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(-1L);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 JobExecutionContext context = new MockitoJobExecutionContext();
 context.setResult(taskEntity);

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 verify(scheduleTaskRepository, never()).updateStatusById(taskEntity.getId(), eq(TaskStatus.DONE));
 }

 @Test
 public void testExecute_PartiallyFailed() {
 // Given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setStatus(TaskStatus.FAILED);
 taskUnits.add(dlmTask);
 when(dataArchiveTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));
 when(dataArchiveTaskRepository.getTaskUnits(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(taskUnits);

 JobExecutionContext context = new MockitoJobExecutionContext();
 context.setResult(taskEntity);

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_PartiallySucceeded() {
 // Given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(rollbackParameters));
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setStatus(TaskStatus.DONE);
 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setStatus(TaskStatus.FAILED);
 taskUnits.add(dlmTask1);
 taskUnits.add(dlmTask2);
 when(dataArchiveTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));
 when(dataArchiveTaskRepository.getTaskUnits(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(taskUnits);

 JobExecutionContext context = new MockitoJobExecutionContext();
 context.setResult(taskEntity);

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(),
 eq(TaskStatus.getStatusByCode(TaskStatus.getPartialSuccess().getCode())));
 }
}