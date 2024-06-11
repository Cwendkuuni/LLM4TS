import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob job;
 private ScheduleTaskEntity taskEntity;
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 private ObjectMapper objectMapper = mock(ObjectMapper.class);

 @Before
 public void setup() {
 job = new DataArchiveDeleteJob();
 job.setScheduleTaskRepository(scheduleTaskRepository);
 job.setJsonUtils(objectMapper);
 taskEntity = new ScheduleTaskEntity();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // Given
 when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong()))
 .thenReturn(Optional.empty());
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 // When & Then
 try {
 job.execute(new JobExecutionContext() {});
 } catch (JobExecutionException e) {
 // Expected
 }

 // Verify
 Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() {
 // Given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong()))
 .thenReturn(Optional.of(dataArchiveTask));

 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 // When
 job.execute(new JobExecutionContext() {});

 // Then
 // Verify task status is updated to the new status
 TaskStatus taskStatus = TaskStatus.FAILED;
 Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), taskStatus);
 }

 @Test
 public void testExecute_TasksExecutedSuccessfully() {
 // Given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 List<DlmTask> dlmTasks = new ArrayList<>();

 when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong()))
 .thenReturn(Optional.of(dataArchiveTask));

 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 // When
 job.execute(new JobExecutionContext() {});

 // Then
 TaskStatus expectedTaskStatus = TaskStatus.SUCCESS;
 Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), expectedTaskStatus);
 }

 @Test(expected = JobExecutionException.class)
 public void testExecute_IllegalArgumentException() {
 // Given
 when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong()))
 .thenThrow(new IllegalArgumentException());
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 // When & Then
 job.execute(new JobExecutionContext() {});
 }

 @Test
 public void testExecute_ExceptionThrownDuringTaskExecution() {
 // Given
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setStatus(TaskStatus.PREPARING);
 dlmTasks.add(dlmTask);
 when(scheduleTaskRepository.findById(ArgumentMatchers.anyLong()))
 .thenReturn(Optional.of(dataArchiveTask));

 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 // When & Then
 try {
 job.execute(new JobExecutionContext() {});
 } catch (JobExecutionException e) {
 // Expected
 }

 // Verify
 Mockito.verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }
}