import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveDeleteJobTest {

 private JobRepository scheduleTaskRepository = mock(JobRepository.class);
 private ObjectMapper objectMapper = new ObjectMapper();

 @BeforeEach
 public void setup() {
 // Initialize the mocks and any other setup code here
 }

 @Test
 public void testExecute_TaskNotFound() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // When
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 try {
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 fail("Expected JobExecutionException");
 } catch (JobExecutionException e) {
 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
 }
 }

 @Test
 public void testExecute_TaskAlreadyFinished() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 // When
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 try {
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 fail("Expected JobExecutionException");
 } catch (JobExecutionException e) {
 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
 }
 }

 @Test
 public void testExecute_TaskNotFinished() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.FAILED);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 // When
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 try {
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));
 fail("Expected JobExecutionException");
 } catch (JobExecutionException e) {
 // Then
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
 }
 }

 @Test
 public void testExecute_Success() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setStatus(TaskStatus.DONE);
 dlmTasks.add(dlmTask);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.updateStatusById(eq(1L), any()))
 .thenAnswer(invocation -> invocation.getArgument(1));

 // When
 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 // Then
 assertTrue(true);
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.SUCCESS));
 }
}