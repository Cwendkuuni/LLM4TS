import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @BeforeEach
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when, then
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 Assertions.assertEquals(TaskStatus.FAILED, dataArchiveClearParameters.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyDone() throws JobExecutionException {
 // given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setResultJson("[]");

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 // when
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 Assertions.assertEquals(TaskStatus.FAILED, dataArchiveClearParameters.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotDone() throws JobExecutionException {
 // given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setResultJson("[]");

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 // when, then
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 Assertions.assertEquals(TaskStatus.FAILED, dataArchiveClearParameters.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskSuccess() throws JobExecutionException {
 // given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setResultJson("[]");

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setStatus(TaskStatus.DONE);
 dlmTasks.add(dlmTask);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));
 when(dataArchiveDeleteJob.executeTask(dataArchiveClearParameters.getId(), dlmTasks))
 .thenReturn(true);

 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 // when
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 Assertions.assertEquals(TaskStatus.SUCCESS, dataArchiveClearParameters.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskFailure() throws JobExecutionException {
 // given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setResultJson("[]");

 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setStatus(TaskStatus.FAILED);
 dlmTasks.add(dlmTask);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));
 when(dataArchiveDeleteJob.executeTask(dataArchiveClearParameters.getId(), dlmTasks))
 .thenReturn(false);

 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 // when, then
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // then
 Assertions.assertEquals(TaskStatus.FAILED, dataArchiveClearParameters.getStatus());
 }
}