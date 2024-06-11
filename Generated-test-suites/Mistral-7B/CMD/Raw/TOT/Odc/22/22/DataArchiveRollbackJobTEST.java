import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.jobstore.MemoryJobStore;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private MemoryJobStore jobStore;
 private StdSchedulerFactory schedulerFactory;
 private ScheduleTaskRepository scheduleTaskRepository;

 @BeforeEach
 public void setUp() {
 jobStore = new MemoryJobStore();
 schedulerFactory = new StdSchedulerFactory(jobStore);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dataArchiveRollbackJob = new DataArchiveRollbackJob() {
 {
 this.scheduleTaskRepository = scheduleTaskRepository;
 }
 };
 }

 @Test
 public void testExecute_DataArchiveTaskFound_AllTasksRollbackSuccess() throws Exception {
 // given
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(new ScheduleTaskEntity());
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
 List<DlmTask> dlmTasks = List.of(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 // when
 dataArchiveRollbackJob.execute(context);
 verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);

 // then
 // assert all tasks are executed with status Done
 List<DlmTask> executedTasks = dlmTasks;
 for (DlmTask dlmTask : executedTasks) {
 verify(dlmTask).setStatus(TaskStatus.DONE);
 }
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound_RollbackTaskFailed() throws Exception {
 // given
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(new ScheduleTaskEntity());
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

 // when
 dataArchiveRollbackJob.execute(context);
 verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);

 // then
 // assert no tasks are executed
 List<DlmTask> executedTasks = List.of();
 verifyNoInteractions(dlmTasks);
 }

 @Test
 public void testExecute_ErrorDuringRollback_RollbackTaskFailed() throws Exception {
 // given
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(new ScheduleTaskEntity());
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
 List<DlmTask> dlmTasks = List.of(new DlmTask(), new DlmTask());
 doThrow(new JobExecutionException("Test error during rollback"))
 .when(dataArchiveRollbackJob)
 .executeTask(scheduleTaskEntity.getId(), dlmTasks);

 // when
 dataArchiveRollbackJob.execute(context);
 verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);

 // then
 // assert no tasks are executed
 verifyNoInteractions(dlmTasks);
 }
}