import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for DataArchiveDeleteJob
 */
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 private ObjectMapper objectMapper = new ObjectMapper();

 @Before
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 scheduleTaskEntity = new ScheduleTaskEntity();
 }

 /**
 * Test method for execute method with valid parameters
 */
 @Test
 public void testExecuteWithValidParameters() throws JobExecutionException {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 // When
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 // Then
 assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
 }

 /**
 * Test method for execute method with non-existing data archive task
 */
 @Test
 public void testExecuteWithNonExistingDataArchiveTask() throws JobExecutionException {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // When and Then
 dataArchiveDeleteJob.execute(new JobExecutionContext());
 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 /**
 * Test method for execute method with already finished data archive task
 */
 @Test
 public void testExecuteWithFinishedDataArchiveTask() throws JobExecutionException {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity finishedDataArchiveTask = new ScheduleTaskEntity();
 finishedDataArchiveTask.setId(1L);
 finishedDataArchiveTask.setJobName("testJobName");
 finishedDataArchiveTask.setJobGroup("testJobGroup");
 finishedDataArchiveTask.setStatus(TaskStatus.DONE);
 finishedDataArchiveTask.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(finishedDataArchiveTask));

 // When and Then
 dataArchiveDeleteJob.execute(new JobExecutionContext());
 assertEquals(TaskStatus.PREPARING, finishedDataArchiveTask.getStatus());
 }

 /**
 * Test method for execute method with error during task execution
 */
 @Test(expected = JobExecutionException.class)
 public void testExecuteWithErrorDuringTaskExecution() throws JobExecutionException {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 List<DlmTask> dlmTasks = new ArrayList<>();
 dlmTasks.add(new DlmTask());
 when(scheduleTaskRepository.updateStatusById(scheduleTaskEntity.getId(), Mockito.any(TaskStatus.class)))
 .thenThrow(new RuntimeException("Test error during task execution"));

 // When and Then
 dataArchiveDeleteJob.execute(new JobExecutionContext());
 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 /**
 * Test method for getTaskStatus method with success status
 */
 @Test
 public void testGetTaskStatusWithSuccessStatus() {
 List<DlmTask> dlmTasks = new ArrayList<>();
 dlmTasks.add(new DlmTask());
 dlmTasks.add(new DlmTask());
 dlmTasks.set(0, new DlmTask().setStatus(TaskStatus.DONE));
 dlmTasks.set(1, new DlmTask().setStatus(TaskStatus.FAILED));

 assertEquals(TaskStatus.DONE, DataArchiveDeleteJob.getTaskStatus(dlmTasks));
 }

 /**
 * Test method for getTaskStatus method with failed status
 */
 @Test
 public void testGetTaskStatusWithFailedStatus() {
 List<DlmTask> dlmTasks = new ArrayList<>();
 dlmTasks.add(new DlmTask().setStatus(TaskStatus.FAILED));
 dlmTasks.add(new DlmTask().setStatus(TaskStatus.DONE));

 assertEquals(TaskStatus.FAILED, DataArchiveDeleteJob.getTaskStatus(dlmTasks));
 }
}