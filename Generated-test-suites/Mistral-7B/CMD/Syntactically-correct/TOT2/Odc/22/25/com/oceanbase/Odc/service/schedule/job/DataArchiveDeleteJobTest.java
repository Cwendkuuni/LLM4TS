import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoJobDetail;
import org.quartz.Trigger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = Mockito.spy(ScheduleTaskRepository.class);
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
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

 // when
 dataArchiveDeleteJob.execute(new MockitoJobExecutionContext());

 // then
 assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(null));
 }

 @Test
 public void testExecute_TaskNotFinished() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 taskEntity.setId(1L);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));

 // when
 dataArchiveDeleteJob.execute(new MockitoJobExecutionContext());

 // then
 assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(null));
 }

 @Test
 public void testExecute_TasksPreparing() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 taskEntity.setId(1L);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 List<DlmTask> taskUnits = Mockito.mockList(DlmTask.class);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.saveAll(anyList())).thenReturn(taskUnits);

 // when
 JobExecutionContext context = new MockitoJobExecutionContext();
 context.setJobDetail(new MockitoJobDetail("jobName", "jobGroup", DataArchiveDeleteJob.class));
 context.setMergedJobDataMap(new JobExecutionContext.JobContextDataMap());
 context.setTrigger(Trigger.triggerInstance(null));
 dataArchiveDeleteJob.execute(context);

 // then
 assertEquals(TaskStatus.PREPARING, dataArchiveDeleteJob.getTaskStatus(taskUnits));
 }

 @Test
 public void testExecute_TasksSuccess() {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 taskEntity.setId(1L);
 taskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 List<DlmTask> taskUnits = Mockito.mockList(DlmTask.class);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.saveAll(anyList())).thenReturn(taskUnits);

 // when
 JobExecutionContext context = new MockitoJobExecutionContext();
 context.setJobDetail(new MockitoJobDetail("jobName", "jobGroup", DataArchiveDeleteJob.class));
 context.setMergedJobDataMap(new JobExecutionContext.JobContextDataMap());
 context.setTrigger(Trigger.triggerInstance(null));
 dataArchiveDeleteJob.execute(context);

 // then
 assertEquals(TaskStatus.SUCCESS, dataArchiveDeleteJob.getTaskStatus(taskUnits));
 }
}