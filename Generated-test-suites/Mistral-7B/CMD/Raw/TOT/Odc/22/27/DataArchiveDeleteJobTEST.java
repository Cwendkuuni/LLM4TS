import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 objectMapper = new ObjectMapper();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecuteWithSuccessfulDataArchiveTask() throws Exception {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId("taskId");
 scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById("taskId")).thenReturn(Optional.of(scheduleTaskEntity));
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId("taskUnitId");
 dlmTask.setStatus(TaskStatus.DONE);
 dlmTasks.add(dlmTask);
 when(scheduleTaskRepository.executeTask(any(String.class), any(List.class))).thenReturn(dlmTasks);

 // When
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 assertEquals(TaskStatus.SUCCESS, scheduleTaskRepository.getLastStatusById("taskId"));
 }

 @Test
 public void testExecuteWithNotFoundDataArchiveTask() throws Exception {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
 when(scheduleTaskRepository.findById("dataArchiveTaskId")).thenReturn(Optional.empty());

 // When & Then
 try {
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 } catch (JobExecutionException e) {
 assertEquals(TaskStatus.FAILED, e.getJobResult().getJobStatus());
 }
 }

 @Test
 public void testExecuteWithRunningDataArchiveTask() throws Exception {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId("taskId");
 scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
 when(scheduleTaskRepository.findById("taskId")).thenReturn(Optional.of(scheduleTaskEntity));

 // When & Then
 try {
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 } catch (JobExecutionException e) {
 assertEquals(TaskStatus.FAILED, e.getJobResult().getJobStatus());
 }
 }

 @Test
 public void testExecuteWithErrorInExecuteTask() throws Exception {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId("taskId");
 scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById("taskId")).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.executeTask(any(String.class), any(List.class)))
 .thenThrow(new RuntimeException("Error in executeTask"));

 // When & Then
 try {
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 } catch (JobExecutionException e) {
 assertEquals(TaskStatus.FAILED, e.getJobResult().getJobStatus());
 }
 }
}