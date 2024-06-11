import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.MockitoAnnotations;
import org.quartz.impl.StdSchedulerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.JobScheduler;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskEntity taskEntity;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;
 private JobScheduler jobScheduler;

 @BeforeEach
 public void init() {
 MockitoAnnotations.initMocks(this);
 objectMapper = new ObjectMapper();
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 taskEntity = mock(ScheduleTaskEntity.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 jobScheduler = mock(JobScheduler.class);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when
 doCallRealMethod().when(dataArchiveDeleteJob).execute(any(JobExecutionContext.class));

 // then
 Assertions.assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyDone() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

 // when
 doCallRealMethod().when(dataArchiveDeleteJob).execute(any(JobExecutionContext.class));

 // then
 Assertions.assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotDone() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

 // when
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 dataArchiveDeleteJob.execute(context);

 // then
 Assertions.assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskPreparing() throws JobExecutionException {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.PREPARING);
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTasks.add(dlmTask);
 when(jobScheduler.executeTask(anyLong(), any(List.class))).thenReturn(dlmTasks);

 // when
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 dataArchiveDeleteJob.execute(context);

 // then
 TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus();
 Assertions.assertEquals(TaskStatus.SUCCESS, taskStatus);
 }
}