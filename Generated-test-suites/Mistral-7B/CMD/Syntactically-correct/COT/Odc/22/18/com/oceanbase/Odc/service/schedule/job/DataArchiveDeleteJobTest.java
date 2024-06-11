import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.impl.StdSchedulerFactory;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.JobType;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobStatus;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskRepository scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1);

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 jobThread = Thread.currentThread();
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getLastStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1);

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 jobThread = Thread.currentThread();
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getLastStatus());
 }

 @Test
 public void testExecute_Success() throws Exception {
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.DONE);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1);

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 List<DlmTask> dlmTasks = Mockito.mock(List.class);
 DlmTask dlmTask = new DlmTask();
 dlmTask.setStatus(TaskStatus.PREPARING);
 dlmTasks.add(dlmTask);

 Mockito.when(scheduleTaskRepository.getLastStatus()).thenReturn(TaskStatus.PREPARING);

 jobThread = Thread.currentThread();
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 assertEquals(TaskStatus.DONE, scheduleTaskRepository.getLastStatus());
 }

 @Test(expected = Exception.class)
 public void testExecute_Exception() throws Exception {
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.DONE);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1);

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 List<DlmTask> dlmTasks = Mockito.mock(List.class);
 DlmTask dlmTask = Mockito.mock(DlmTask.class);
 Mockito.when(dlmTask.getStatus()).thenReturn(TaskStatus.PREPARING);
 dlmTasks.add(dlmTask);

 StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
 schedulerFactory.start();

 jobThread = Thread.currentThread();
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getLastStatus());
 }
}