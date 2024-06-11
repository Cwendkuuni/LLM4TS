import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.exception.DataArchiveDeleteJobException;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import org.quartz.JobBuilder;
import org.quartz.Trigger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 private ObjectMapper objectMapper = new ObjectMapper();
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("task-id-not-found");
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class, Mockito.CALLS_REAL_METHODS));

 // then
 Assertions.assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("task-id-already-finished");
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 // when
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class, Mockito.CALLS_REAL_METHODS));

 // then
 Assertions.assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus());
 }

 @Test
 public void testExecute_ExecuteTaskSuccess() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("task-id-to-clear");
 dataArchiveClearParameters.setParameters(new Object());
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

 DlmTask dlmTask1 = new DlmTask();
 DlmTask dlmTask2 = new DlmTask();
 List<DlmTask> dlmTasks = Arrays.asList(dlmTask1, dlmTask2);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 // when
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class, Mockito.CALLS_REAL_METHODS));

 // then
 Assertions.assertEquals(TaskStatus.SUCCESS, dataArchiveDeleteJob.getTaskStatus());
 }

 @Test
 public void testExecute_ExecuteTaskFailure() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("task-id-to-clear");
 dataArchiveClearParameters.setParameters(new Object());
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

 DlmTask dlmTask1 = new DlmTask();
 DlmTask dlmTask2 = new DlmTask();
 dlmTask1.setId(1L);
 dlmTask1.setStatus(TaskStatus.FAILED);
 dlmTask2.setId(2L);
 dlmTask2.setStatus(TaskStatus.FAILED);
 List<DlmTask> dlmTasks = Arrays.asList(dlmTask1, dlmTask2);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(dataArchiveDeleteJob.executeTask(taskEntity.getId(), dlmTasks))
 .thenThrow(new DataArchiveDeleteJobException("Test error"));

 // when
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class, Mockito.CALLS_REAL_METHODS));

 // then
 Assertions.assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus());
 }

 @Test
 public void testGetTaskStatus() {
 // given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

 // when
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

 // then
 Assertions.assertEquals(TaskStatus.PREPARING, dataArchiveDeleteJob.getTaskStatus(dlmTasks));
 }
}