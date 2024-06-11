import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ScheduleTaskEntity taskEntity;

 @BeforeEach
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 taskEntity = Mockito.spy(new ScheduleTaskEntity());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // Given
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

 // When & Then
 Assertions.assertThrows(JobExecutionException.class, () ->
 dataArchiveRollbackJob.execute(new DefaultJobExecutionContext()));

 verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskFound() {
 // Given
 ScheduleTaskEntity dataArchiveTask = Mockito.spy(new ScheduleTaskEntity());
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

 List<DlmTask> tasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 tasks.add(dlmTask);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
 when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJsonString(tasks));

 // When
 dataArchiveRollbackJob.execute(new DefaultJobExecutionContext());

 // Then
 verify(dataArchiveRollbackJob).executeTask(anyLong(), eq(tasks));
 verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.DONE));
 }
}