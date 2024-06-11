import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoJobDetail;
import org.quartz.MockitoScheduler;
import org.quartz.impl.JobDetailImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private MockitoScheduler scheduler;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduler = new MockitoScheduler();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testExecute_TaskNotFound() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 taskEntity.setId(1L);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById(ArgumentMatchers.eq(1L)))
 .thenReturn(Optional.empty());

 JobExecutionContext context = new JobExecutionContext();
 context.setResult(taskEntity);

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 Mockito.verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_TaskAlreadyFinished() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 taskEntity.setId(1L);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 taskEntity.setStatus(TaskStatus.DONE);

 when(scheduleTaskRepository.findById(ArgumentMatchers.eq(1L)))
 .thenReturn(Optional.of(taskEntity));

 JobExecutionContext context = new JobExecutionContext();
 context.setResult(taskEntity);

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 Mockito.verifyNoMoreInteractions(scheduleTaskRepository);
 }

 @Test(expected = IllegalStateException.class)
 public void testExecute_PrepareTasksException() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 taskEntity.setId(1L);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));

 List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById(ArgumentMatchers.eq(1L)))
 .thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.PREPARING)))
 .thenThrow(new RuntimeException());

 JobExecutionContext context = new JobExecutionContext();
 context.setResult(taskEntity);

 // when & then
 dataArchiveDeleteJob.execute(context);
 }

 @Test
 public void testExecute_Success() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 taskEntity.setId(1L);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));

 List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById(ArgumentMatchers.eq(1L)))
 .thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(TaskStatus.PREPARING)))
 .thenReturn(true);

 JobExecutionContext context = new JobExecutionContext();
 context.setResult(taskEntity);

 // when
 dataArchiveDeleteJob.execute(context);

 // then
 TaskStatus taskStatus = TaskStatus.COMPLETED;
 Mockito.verify(scheduleTaskRepository).updateStatusById(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(taskStatus));
 }
}