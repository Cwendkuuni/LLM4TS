import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;

 @Before
 public void setUp() {
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("non-existing-id");

 // when
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 Mockito.when(context.getResult()).thenReturn(dataArchiveClearParameters);

 // then
 try {
 dataArchiveDeleteJob.execute(context);
 fail("Expected JobExecutionException");
 } catch (JobExecutionException e) {
 // expected
 }

 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyDone() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setResultJson(JsonUtils.toJsonString(new ArrayList<>()));

 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(scheduleTaskEntity);

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(dataArchiveTaskOption);

 // when
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 Mockito.when(context.getResult()).thenReturn(dataArchiveClearParameters);

 // then
 assertNull(dataArchiveDeleteJob.execute(context));

 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(ArgumentMatchings.eq(dataArchiveClearParameters.getDataArchiveTaskId()),
 ArgumentMatchers.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFoundAndRollback() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("non-existing-id");

 // when
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 Mockito.when(context.getResult()).thenReturn(dataArchiveClearParameters);

 // then
 try {
 dataArchiveDeleteJob.execute(context);
 fail("Expected JobExecutionException");
 } catch (JobExecutionException e) {
 // expected
 }

 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.FAILED));
 }

 @Test(expected = RuntimeException.class)
 public void testExecute_DataArchiveTaskAlreadyPreparing() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-id");

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskEntity.setResultJson(JsonUtils.toJsonString(new ArrayList<>()));

 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(scheduleTaskEntity);

 Mockito.when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(dataArchiveTaskOption);

 // when
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 Mockito.when(context.getResult()).thenReturn(dataArchiveClearParameters);

 // then
 assertSame(dataArchiveDeleteJob, dataArchiveDeleteJob.execute(context));

 Mockito.verify(scheduleTaskRepository, Mockito.times(1))
 .updateStatusById(ArgumentMatchings.eq(dataArchiveClearParameters.getDataArchiveTaskId()),
 ArgumentMatchers.eq(TaskStatus.FAILED));
 }

 // Add more test cases for other methods and edge cases as needed
}