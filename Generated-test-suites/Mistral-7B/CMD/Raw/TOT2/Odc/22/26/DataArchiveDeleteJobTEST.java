import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@Slf4j
public class DataArchiveDeleteJobTest {

 private ScheduleTaskEntity taskEntity;
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 private DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

 @Test
 public void testExecute_DataArchiveTaskFoundAndFinished_ShouldClearDataArchiveTasks() {
 // Given
 taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("DataArchiveDeleteJob");
 taskEntity.setJobGroup("default");
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 JsonNode parametersJson = JsonUtils.toJsonNode(dataArchiveClearParameters);
 taskEntity.setParametersJson(parametersJson.toString());
 DataArchiveClearParameters expectedDataArchiveClearParameters = JsonUtils.fromJson(parametersJson.toString(), DataArchiveClearParameters.class);

 when(scheduleTaskRepository.findById(expectedDataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED)).thenReturn(true);

 // When
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 Assertions.assertEquals(TaskStatus.FAILED, taskEntity.getStatus());

 // Check if tasks were cleared
 List<DlmTask> expectedTaskUnits = new ArrayList<>();
 DlmTask expectedTaskUnit = new DlmTask();
 expectedTaskUnit.setId(1L);
 expectedTaskUnits.add(expectedTaskUnit);
 when(scheduleTaskRepository.findById(expectedDataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.findByResultJsonAndStatusAndJobNameAndJobGroup(null, TaskStatus.DONE, "DataArchiveDeleteJob", "default")).thenReturn(expectedTaskUnits);
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 Assertions.assertEquals(1, expectedTaskUnits.size());
 Assertions.assertEquals(TaskStatus.PREPARING, expectedTaskUnits.get(0).getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound_ShouldRollbackAndUpdateStatusToFailed() {
 // Given
 taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("DataArchiveDeleteJob");
 taskEntity.setJobGroup("default");
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 JsonNode parametersJson = JsonUtils.toJsonNode(dataArchiveClearParameters);
 taskEntity.setParametersJson(parametersJson.toString());

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

 // When
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 Assertions.assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished_ShouldRollbackAndUpdateStatusToFailed() {
 // Given
 taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("DataArchiveDeleteJob");
 taskEntity.setJobGroup("default");
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 JsonNode parametersJson = JsonUtils.toJsonNode(dataArchiveClearParameters);
 taskEntity.setParametersJson(parametersJson.toString());
 DataArchiveClearParameters expectedDataArchiveClearParameters = JsonUtils.fromJson(parametersJson.toString(), DataArchiveClearParameters.class);
 taskEntity.setStatus(TaskStatus.RUNNING);

 when(scheduleTaskRepository.findById(expectedDataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));

 // When
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 Assertions.assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_ExceptionThrown_ShouldRollbackAndUpdateStatusToFailed() {
 // Given
 taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("DataArchiveDeleteJob");
 taskEntity.setJobGroup("default");
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 JsonNode parametersJson = JsonUtils.toJsonNode(dataArchiveClearParameters);
 taskEntity.setParametersJson(parametersJson.toString());

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED)).thenAnswer(invocation -> false);
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.RUNNING)).thenAnswer(invocation -> true);

 // When & Then
 Assertions.assertThrows(JobExecutionException.class, () -> dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class)));
 Assertions.assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }
}