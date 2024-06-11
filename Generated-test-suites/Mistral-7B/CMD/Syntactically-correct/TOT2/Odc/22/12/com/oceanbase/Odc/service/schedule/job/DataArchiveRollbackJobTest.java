import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import static org.junit.jupiter.api.Assertions.*;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.fasterxml.jackson.core.type.TypeReference;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private AbstractDlmJob abstractDlmJobMock;
 private ScheduleTaskRepository scheduleTaskRepositoryMock;

 @BeforeEach
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 abstractDlmJobMock = Mockito.mock(AbstractDlmJob.class);
 scheduleTaskRepositoryMock = Mockito.mock(ScheduleTaskRepository.class);
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepositoryMock);
 dataArchiveRollbackJob.setAbstractDlmJob(abstractDlmJobMock);
 }

 @Test
 public void testExecute_ValidDataArchiveTask_ShouldRollbackTasks() {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 String jsonString = JsonUtils.toJsonString(List.of(new DlmTask()));
 scheduleTaskEntity.setParametersJson(jsonString);
 scheduleTaskEntity.setResultJson(jsonString);

 Mockito.when(scheduleTaskRepositoryMock.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 // When
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // Then
 Mockito.verify(abstractDlmJobMock, Mockito.times(1)).executeTask(eq(1L), eq(List.of(new DlmTask())));
 }

 @Test
 public void testExecute_InvalidDataArchiveTask_ShouldUpdateStatusAndLogWarning() {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);

 Mockito.when(scheduleTaskRepositoryMock.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // When
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // Then
 Mockito.verify(scheduleTaskRepositoryMock, Mockito.times(1))
 .updateStatusById(eq(1L), eq(TaskStatus.FAILED));
 log.warn("Data archive task not found,rollback task fast failed.");
 }

 @Test
 public void testExecute_NullDataArchiveTask_ShouldUpdateStatusAndLogWarning() {
 // Given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(null);

 // When
 dataArchiveRollbackJob.execute(new JobExecutionContext());

 // Then
 Mockito.verify(scheduleTaskRepositoryMock, Mockito.times(1))
 .updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 log.warn("Data archive task not found,rollback task fast failed.");
 }

 @Test
 public void testExecute_EmptyDataArchiveTaskList_ShouldUpdateStatusAndLogWarning() {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 String jsonString = "";
 scheduleTaskEntity.setParametersJson(jsonString);
 scheduleTaskEntity.setResultJson(jsonString);

 Mockito.when(scheduleTaskRepositoryMock.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 // When
 dataArchiveRollbackJob.execute(new JobExecutionContext());
 // Then
 Mockito.verify(scheduleTaskRepositoryMock, Mockito.times(1))
 .updateStatusById(eq(1L), eq(TaskStatus.FAILED));
 log.warn("Data archive task list is empty.");
 }
}