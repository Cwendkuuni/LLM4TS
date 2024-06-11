import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;import static org.mockito.Mockito.mock;import static org.mockito.Mockito.when;import static org.junit.Assert.*;import com.oceanbase.odc.common.json.JsonUtils;import com.oceanbase.odc.core.shared.constant.TaskStatus;import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;import com.oceanbase.odc.service.dlm.model.DlmTask;import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;import org.quartz.JobExecutionContext;import java.util.ArrayList;import java.util.Optional;import java.util.List;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

 @InjectMocks
 private DataArchiveRollbackJob dataArchiveRollbackJob;

 private AbstractDlmJob abstractDlmJob = mock(AbstractDlmJob.class);
 private ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

 @Test
 public void testExecute_WhenDataArchiveTaskNotFound_ThenStatusUpdatedToFailed() {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("123");
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());
 dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));
 verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_WhenDataArchiveTaskFound_ThenStatusUpdatedBasedOnTasks() {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("123");
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("1");
 dataArchiveTask.setJobName("testJob");
 dataArchiveTask.setJobGroup("testGroup");
 dataArchiveTask.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 List<DlmTask> tasks = new ArrayList<>();
 DlmTask task1 = new DlmTask();
 DlmTask task2 = new DlmTask();
 tasks.add(task1);
 tasks.add(task2);
 when(scheduleTaskRepository.getTaskStatus(tasks))
 .thenReturn(TaskStatus.FAILED);

 when(abstractDlmJob.executeTask(dataArchiveTask.getId(), tasks))
 .thenReturn(true);
 dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));
 TaskStatus expectedStatus = getTaskStatus(tasks);
 verify(scheduleTaskRepository).updateStatusById(dataArchiveTask.getId(), expectedStatus);
 }

 @Test(expected = Exception.class)
 public void testExecute_WhenTasksPreparing_ThenThrowException() {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("123");
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("1");
 dataArchiveTask.setJobName("testJob");
 dataArchiveTask.setJobGroup("testGroup");
 dataArchiveTask.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 DlmTask task1 = new DlmTask();
 DlmTask task2 = new DlmTask();
 task1.setStatus(TaskStatus.PREPARING);
 task2.setStatus(TaskStatus.PREPARING);
 List<DlmTask> tasks = new ArrayList<>();
 tasks.add(task1);
 tasks.add(task2);
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.getTaskStatus(tasks))
 .thenReturn(TaskStatus.PREPARING);
 dataArchiveRollbackJob.execute(mock(JobExecutionContext.class));
 }

 private TaskStatus getTaskStatus(List<DlmTask> tasks) {
 if (tasks.isEmpty()) {
 return TaskStatus.DONE;
 }
 return tasks.get(tasks.size() - 1).getStatus();
 }
}