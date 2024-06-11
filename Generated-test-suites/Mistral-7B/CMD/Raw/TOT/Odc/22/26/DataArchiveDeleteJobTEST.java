import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.MockitoJobExecutionMockito;
import org.quartz.MockitoSchedulerMockito;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private Scheduler scheduler;
 private StdSchedulerFactory schedulerFactory;
 private JobExecutionContext context;

 @BeforeEach
 public void setup() {
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 schedulerFactory = new StdSchedulerFactory();
 scheduler = MockitoSchedulerMockito.mockScheduler();
 context = MockitoJobExecutionMockito.mockJobExecutionContext();
 }

 @Test
 void testDataArchiveTaskNotFound() {
 // given
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 void testDataArchiveTaskNotFinished() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.RUNNING);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 void testDataArchiveTaskFinished() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());

 // when
 dataArchiveDeleteJob.execute(context);
 ScheduleTaskEntity updatedTaskEntity = new ScheduleTaskEntity();
 updatedTaskEntity.setId(1L);
 updatedTaskEntity.setStatus(TaskStatus.COMPLETED);
 verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(updatedTaskEntity.getStatus()));

 // then
 scheduler.start();
 scheduler.shutdown();
 }
}