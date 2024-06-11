import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("non-existing-task-id");

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 JobExecutionContext context = new JobExecutionContext();
 context.setResult(taskEntity);
 dataArchiveRollbackJob.execute(context);

 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskFound() throws JobExecutionException {
 ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
 dataArchiveTaskEntity.setId("existing-task-id");

 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("existing-task-id");

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTaskEntity));
 when(scheduleTaskRepository.updateStatusById(any(String.class), any(TaskStatus.class)))
 .thenReturn(true);

 JobExecutionContext context = new JobExecutionContext();
 context.setResult(dataArchiveTaskEntity);
 dataArchiveRollbackJob.execute(context);

 assertEquals(TaskStatus.DONE, dataArchiveTaskEntity.getStatus());
 }

 @Test
 public void testExecute_TaskStatusIsPreparing() throws JobExecutionException {
 ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
 dataArchiveTaskEntity.setId("existing-task-id");

 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("existing-task-id");

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 dlmTasks.get(0).setStatus(TaskStatus.PREPARING);

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTaskEntity));
 when(scheduleTaskRepository.updateStatusById(any(String.class), any(TaskStatus.class)))
 .thenReturn(true);

 JobExecutionContext context = new JobExecutionContext();
 context.setResult(dataArchiveTaskEntity);
 dataArchiveRollbackJob.execute(context);

 assertEquals(TaskStatus.DONE, dataArchiveTaskEntity.getStatus());
 }

 @Test
 public void testExecute_TaskStatusIsDone() throws JobExecutionException {
 ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
 dataArchiveTaskEntity.setId("existing-task-id");

 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("existing-task-id");

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 dlmTasks.get(0).setStatus(TaskStatus.DONE);

 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTaskEntity));
 when(scheduleTaskRepository.updateStatusById(any(String.class), any(TaskStatus.class)))
 .thenReturn(true);

 JobExecutionContext context = new JobExecutionContext();
 context.setResult(dataArchiveTaskEntity);
 dataArchiveRollbackJob.execute(context);

 assertEquals(TaskStatus.DONE, dataArchiveTaskEntity.getStatus());
 }

 @Test
 public void testGetTaskStatus() {
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 dlmTasks.get(0).setStatus(TaskStatus.PREPARING);
 dlmTasks.get(1).setStatus(TaskStatus.DONE);

 assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(dlmTasks));
 }
}