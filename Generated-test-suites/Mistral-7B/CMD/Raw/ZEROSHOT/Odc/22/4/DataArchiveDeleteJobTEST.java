import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.JobThread;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 private JobThread jobThread;
 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ScheduleTaskEntity scheduleTaskEntity;
 private List<DlmTask> dlmTasks;

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 jobThread = mock(JobThread.class);
 doReturn(jobThread).when(dataArchiveDeleteJob).getJobThread();

 dlmTasks = new ArrayList<>();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setJobName("testJobName");
 scheduleTask.setJobGroup("testJobGroup");
 scheduleTask.setParametersJson("{\"dataArchiveTaskId\":1}");
 scheduleTask.setResultJson(JsonUtils.toJson(dlmTasks));
 scheduleTaskEntity = scheduleTask;
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 doReturn(Optional.empty()).when(scheduleTaskRepository).findById(1L);

 try {
 dataArchiveDeleteJob.execute(new JobExecutionContext());
 } catch (JobExecutionException e) {
 assert e.getMessage().contains("Data archive task not found,rollback task fast failed.");
 }

 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 dataArchiveTask.setJobName("testJobName");
 dataArchiveTask.setJobGroup("testJobGroup");
 dataArchiveTask.setParametersJson("{\"dataArchiveTaskId\":1}");
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(1L);

 try {
 dataArchiveDeleteJob.execute(new JobExecutionContext());
 } catch (JobExecutionException e) {
 assert e.getMessage().contains("Data archive task do not finish,data archive task id = ");
 }

 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_Success() {
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setJobName("testJobName");
 dataArchiveTask.setJobGroup("testJobGroup");
 dataArchiveTask.setParametersJson("{\"dataArchiveTaskId\":1}");
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(1L);

 dataArchiveDeleteJob.execute(new JobExecutionContext());

 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
 }

 @Test
 public void testExecute_TaskUnitsNotClear() {
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setJobName("testJobName");
 dataArchiveTask.setJobGroup("testJobGroup");
 dataArchiveTask.setParametersJson("{\"dataArchiveTaskId\":1}");
 dataArchiveTask.setResultJson(JsonUtils.toJson(dlmTasks));
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(1L);

 dlmTasks.get(0).setId(1L);
 dlmTasks.get(0).setStatus(TaskStatus.FAILED);

 try {
 dataArchiveDeleteJob.execute(new JobExecutionContext());
 } catch (JobExecutionException e) {
 assert e.getMessage().contains("executeTask failed.");
 }

 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_TaskUnitsClear() {
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setJobName("testJobName");
 dataArchiveTask.setJobGroup("testJobGroup");
 dataArchiveTask.setParametersJson("{\"dataArchiveTaskId\":1}");
 dataArchiveTask.setResultJson(JsonUtils.toJson(dlmTasks));
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(1L);

 dlmTasks.get(0).setId(1L);
 dlmTasks.get(0).setStatus(TaskStatus.DONE);

 dataArchiveDeleteJob.execute(new JobExecutionContext());

 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
 }
}