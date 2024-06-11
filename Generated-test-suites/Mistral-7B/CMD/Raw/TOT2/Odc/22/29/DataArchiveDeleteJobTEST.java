import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.ArgumentMatchers;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskEntity = mock(ScheduleTaskEntity.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("task-id");

 doReturn(Optional.empty()).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

 dataArchiveDeleteJob.execute(null);

 assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getLastStatusById(dataArchiveClearParameters.getDataArchiveTaskId()));
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() throws JobExecutionException {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("task-id");

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("task-id");
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setJobName("job-name");
 dataArchiveTask.setJobGroup("job-group");
 dataArchiveTask.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

 List<DlmTask> dlmTasks = mock(List.class);
 doReturn(dlmTasks).when(dataArchiveDeleteJob).executeTask(dataArchiveClearParameters.getDataArchiveTaskId(), ArgumentMatchers.eq(dlmTasks));

 dataArchiveDeleteJob.execute(null);

 assertEquals(TaskStatus.COMPLETED, scheduleTaskRepository.getLastStatusById(dataArchiveClearParameters.getDataArchiveTaskId()));
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_DataArchiveTaskAlreadyPreparing() throws JobExecutionException {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("task-id");

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("task-id");
 dataArchiveTask.setStatus(TaskStatus.PREPARING);
 dataArchiveTask.setJobName("job-name");
 dataArchiveTask.setJobGroup("job-group");
 dataArchiveTask.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

 List<DlmTask> dlmTasks = mock(List.class);
 doReturn(dlmTasks).when(dataArchiveDeleteJob).executeTask(dataArchiveClearParameters.getDataArchiveTaskId(), ArgumentMatchers.eq(dlmTasks));

 dataArchiveDeleteJob.execute(null);
 }

 @Test
 public void testExecute_DataArchiveTaskStatusNotDone() throws JobExecutionException {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("task-id");

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("task-id");
 dataArchiveTask.setStatus(TaskStatus.FAILED);
 dataArchiveTask.setJobName("job-name");
 dataArchiveTask.setJobGroup("job-group");
 dataArchiveTask.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

 List<DlmTask> dlmTasks = mock(List.class);
 doReturn(dlmTasks).when(dataArchiveDeleteJob).executeTask(dataArchiveClearParameters.getDataArchiveTaskId(), ArgumentMatchers.eq(dlmTasks));

 dataArchiveDeleteJob.execute(null);

 assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getLastStatusById(dataArchiveClearParameters.getDataArchiveTaskId()));
 }
}