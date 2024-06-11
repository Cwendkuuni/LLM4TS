import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoJobExecutionContext;
import org.quartz.impl.jobstore.JobKey;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // Given
 String dataArchiveTaskId = "123";
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId("1");
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(dataArchiveTaskId);
 taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 JobExecutionContext context = new MockitoJobExecutionContext(new JobKey("jobName", "jobGroup"), 1);

 // When
 doReturn(Optional.empty()).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());
 dataArchiveDeleteJob.execute(context);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyDone() {
 // Given
 String dataArchiveTaskId = "123";
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("1");
 dataArchiveTask.setStatus(TaskStatus.DONE);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(dataArchiveTaskId);
 taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 JobExecutionContext context = new MockitoJobExecutionContext(new JobKey("jobName", "jobGroup"), 1);

 // When
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());
 dataArchiveDeleteJob.execute(context);

 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_Success() {
 // Given
 String dataArchiveTaskId = "123";
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("1");
 dataArchiveTask.setStatus(TaskStatus.DONE);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(dataArchiveTaskId);
 taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
 DlmTask expectedDlmTask = new DlmTask();
 expectedDlmTask.setId("historyJobId");
 expectedDlmTask.setStatus(TaskStatus.PREPARING);
 doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING)).thenReturn(true);
 when(scheduleTaskRepository.saveAll(taskUnits)).thenReturn(taskUnits);

 // When
 dataArchiveDeleteJob.execute(context);

 // Then
 assertEquals(TaskStatus.PREPARING, dataArchiveTask.getStatus());
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.PREPARING);
 verify(scheduleTaskRepository, times(1)).saveAll(taskUnits);
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.COMPLETED);

 // Cleanup
 verifyNoMoreInteractions(scheduleTaskRepository);
 }

 @Test(expected = Exception.class)
 public void testExecute_Exception() {
 // Given
 String dataArchiveTaskId = "123";
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("1");
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(dataArchiveTaskId);
 taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 JobExecutionContext context = new MockitoJobExecutionContext(new JobKey("jobName", "jobGroup"), 1);

 // When & Then
 doThrow(new RuntimeException("Exception")).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());
 dataArchiveDeleteJob.execute(context);
 }
}