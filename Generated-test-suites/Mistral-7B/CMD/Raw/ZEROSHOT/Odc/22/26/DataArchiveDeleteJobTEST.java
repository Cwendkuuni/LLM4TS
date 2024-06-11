import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.JobExecutionContext;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.AbstractDlmJob.ThreadLocalJobThread;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;
 private ThreadLocalJobThread threadLocalJobThread;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 objectMapper = new ObjectMapper();
 threadLocalJobThread = new ThreadLocalJobThread();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("123456");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(new JobExecutionContext() {});
 verify(scheduleTaskRepository, times(1)).updateStatusById(ArgumentMatchers.anyLong(),
 Equals.equalTo(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyDone() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("123456");
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setId(1L);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 dataArchiveDeleteJob.execute(new JobExecutionContext() {});
 verify(scheduleTaskRepository, times(1)).updateStatusById(ArgumentMatchers.anyLong(),
 Equals.equalTo(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskPreparing() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("123456");
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.PREPARING);
 dataArchiveTask.setId(1L);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 dataArchiveDeleteJob.execute(new JobExecutionContext() {});
 verify(scheduleTaskRepository, times(1)).updateStatusById(ArgumentMatchers.anyLong(),
 Equals.not(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskSuccess() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("123456");
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setId(1L);
 dataArchiveTask.setResultJson("[]");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(dataArchiveDeleteJob.executeTask(ArgumentMatchers.anyLong(), ArgumentMatchers.isA(List.class)))
 .thenReturn(dlmTasks);

 dataArchiveDeleteJob.execute(new JobExecutionContext() {});
 verify(scheduleTaskRepository, times(1)).updateStatusById(ArgumentMatchers.anyLong(), Equals.not(TaskStatus.FAILED));
 }
}