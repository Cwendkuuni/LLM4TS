import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 private ObjectMapper objectMapper = new ObjectMapper();

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{}");
 taskEntity.setJobName("testJobName");
 taskEntity.setJobGroup("testJobGroup");
 taskEntity.setStatus(TaskStatus.SCHEDULED);

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

 // when
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 // then
 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
 taskEntity.setJobName("testJobName");
 taskEntity.setJobGroup("testJobGroup");
 taskEntity.setStatus(TaskStatus.DONE);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setParametersJson("{}");
 dataArchiveTask.setResultJson("[]");
 dataArchiveTask.setJobName("testJobName");
 dataArchiveTask.setJobGroup("testJobGroup");
 dataArchiveTask.setStatus(TaskStatus.DONE);

 when(scheduleTaskRepository.findById(eq(1L))).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.findById(eq(1L))).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 // then
 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
 taskEntity.setJobName("testJobName");
 taskEntity.setJobGroup("testJobGroup");
 taskEntity.setStatus(TaskStatus.SCHEDULED);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setParametersJson("{}");
 dataArchiveTask.setResultJson("[]");
 dataArchiveTask.setJobName("testJobName");
 dataArchiveTask.setJobGroup("testJobGroup");
 dataArchiveTask.setStatus(TaskStatus.RUNNING);

 when(scheduleTaskRepository.findById(eq(1L))).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.findById(eq(1L))).thenReturn(Optional.of(dataArchiveTask));

 // when
 dataArchiveDeleteJob.execute(new JobContext());
 // then
 // we cannot check task status directly as it is an asynchronous operation
 // we can only check if the method did not throw an exception
 }

 @Test
 public void testExecute_DataArchiveTaskFoundAndPrepareTasks() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
 taskEntity.setJobName("testJobName");
 taskEntity.setJobGroup("testJobGroup");
 taskEntity.setStatus(TaskStatus.SCHEDULED);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setParametersJson("{}");
 dataArchiveTask.setResultJson("[]");
 dataArchiveTask.setJobName("testJobName");
 dataArchiveTask.setJobGroup("testJobGroup");
 dataArchiveTask.setStatus(TaskStatus.DONE);

 List<DlmTask> dlmTasks = Arrays.asList(
 new DlmTask(), new DlmTask());

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.updateStatusById(anyLong(), anyString())).thenReturn(true);

 ArgumentCaptor<List<DlmTask>> dlmTasksCaptor = ArgumentCaptor.forClass(List.class);

 // when
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 // then
 verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.PREPARING.toString()));
 verify(scheduleTaskRepository).saveAll(dlmTasksCaptor.capture());
 List<DlmTask> savedDlmTasks = dlmTasksCaptor.getValue();

 assertEquals(2, savedDlmTasks.size());
 for (DlmTask dlmTask : savedDlmTasks) {
 assertEquals(TaskStatus.PREPARING, dlmTask.getStatus());
 }
 }

 @Test
 public void testExecute_DataArchiveTaskFoundAndUpdateStatus() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
 taskEntity.setJobName("testJobName");
 taskEntity.setJobGroup("testJobGroup");
 taskEntity.setStatus(TaskStatus.SCHEDULED);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setParametersJson("{}");
 dataArchiveTask.setResultJson("[]");
 dataArchiveTask.setJobName("testJobName");
 dataArchiveTask.setJobGroup("testJobGroup");
 dataArchiveTask.setStatus(TaskStatus.DONE);

 List<DlmTask> dlmTasks = Arrays.asList(
 new DlmTask(), new DlmTask());

 when(scheduleTaskRepository.findById(eq(1L))).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.findById(eq(1L))).thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.updateStatusById(anyLong(), anyString())).thenReturn(true);

 ArgumentCaptor<List<DlmTask>> dlmTasksCaptor = ArgumentCaptor.forClass(List.class);
 ArgumentCaptor<TaskStatus> taskStatusCaptor = ArgumentCaptor.forClass(TaskStatus.class);

 // when
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 // then
 verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.PREPARING.toString()));
 verify(scheduleTaskRepository).saveAll(dlmTasksCaptor.capture());
 List<DlmTask> savedDlmTasks = dlmTasksCaptor.getValue();
 verify(scheduleTaskRepository).updateStatusById(eq(1L), taskStatusCaptor.capture());

 assertEquals(2, savedDlmTasks.size());
 for (DlmTask dlmTask : savedDlmTasks) {
 assertEquals(TaskStatus.PREPARING, dlmTask.getStatus());
 }
 assertEquals(TaskStatus.SUCCESS, taskStatusCaptor.getValue());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFoundAndUpdateStatus() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
 taskEntity.setJobName("testJobName");
 taskEntity.setJobGroup("testJobGroup");
 taskEntity.setStatus(TaskStatus.SCHEDULED);

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
 when(scheduleTaskRepository.updateStatusById(anyLong(), anyString())).thenReturn(true);

 // when
 dataArchiveDeleteJob.execute(new JobExecutionContext());

 // then
 verify(scheduleTaskRepository, never()).saveAll(anyList());
 verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.FAILED.toString()));
 }
}