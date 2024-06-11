import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoJobExecutionContext;
import org.quartz.impl.JobDetail;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobType;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobStatus;
import com.oceanbase.tools.migrator.common.utils.TestUtils;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private StdSchedulerFactory schedulerFactory;

 @Before
 public void setUp() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskEntity = new ScheduleTaskEntity();
 schedulerFactory = new StdSchedulerFactory();
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFound_thenStatusFailed() {
 // given
 String taskId = "taskId";
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("dataArchiveTaskId");
 scheduleTaskEntity.setId(taskId);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 JobExecutionContext context = new MockitoJobExecutionContext();
 context.setJobDetail(new JobDetail());
 context.setMergedJobDataMap(TestUtils.createJobDataMap());

 // when
 doCallRealMethod().when(dataArchiveRollbackJob).execute(context);
 dataArchiveRollbackJob.execute(context);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_whenDataArchiveTaskFound_thenStatusUpdated() {
 // given
 String taskId = "taskId";
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(taskId);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(taskId);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(dataArchiveTask.getId());
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 JobExecutionContext context = new MockitoJobExecutionContext();
 context.setJobDetail(new JobDetail());
 context.setMergedJobDataMap(TestUtils.createJobDataMap());
 when(scheduleTaskRepository.findById(dataArchiveTask.getId())).thenReturn(Optional.of(dataArchiveTask));

 // when
 doCallRealMethod().when(dataArchiveRollbackJob).execute(context);
 dataArchiveRollbackJob.execute(context);

 // then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.DONE);
 }

 @Test
 public void testGetTaskStatus_whenTasksDone_thenStatusDone() {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 dlmTasks.get(0).setStatus(JobStatus.DONE);
 dlmTasks.get(1).setStatus(JobStatus.DONE);

 // when
 TaskStatus actual = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.DONE, actual);
 }

 @Test
 public void testGetTaskStatus_whenTasksPreparing_thenStatusPreparing() {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 dlmTasks.get(0).setStatus(JobStatus.PREPARING);
 dlmTasks.get(1).setStatus(JobStatus.PREPARING);

 // when
 TaskStatus actual = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.PREPARING, actual);
 }

 @Test
 public void testGetTaskStatus_whenTasksFailed_thenStatusFailed() {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 dlmTasks.get(0).setStatus(JobStatus.FAILED);
 dlmTasks.get(1).setStatus(JobStatus.FAILED);

 // when
 TaskStatus actual = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

 // then
 assertEquals(TaskStatus.FAILED, actual);
 }

 @Test
 public void testExecute_whenDataArchiveTaskFound_thenExecuteTaskCalled() {
 // given
 String taskId = "taskId";
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(taskId);
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(dataArchiveTask.getId());
 scheduleTaskEntity.setId(taskId);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 JobExecutionContext context = new MockitoJobExecutionContext();
 context.setJobDetail(new JobDetail());
 context.setMergedJobDataMap(TestUtils.createJobDataMap());
 when(scheduleTaskRepository.findById(dataArchiveTask.getId())).thenReturn(Optional.of(dataArchiveTask));

 // when
 doCallRealMethod().when(dataArchiveRollbackJob).execute(context);
 dataArchiveRollbackJob.execute(context);
 verify(dataArchiveRollbackJob, times(1)).executeTask(taskId, dlmTasks);
 }
}