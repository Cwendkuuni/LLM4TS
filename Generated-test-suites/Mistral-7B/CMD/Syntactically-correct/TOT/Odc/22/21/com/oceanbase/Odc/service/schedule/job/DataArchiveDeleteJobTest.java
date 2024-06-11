import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobResult;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.jobstore.MemoryJobStore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.utils.DlmJobIdUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ObjectMapper objectMapper;
 private ScheduleTaskEntity scheduleTaskEntity;
 private StdSchedulerFactory schedulerFactory;
 private MemoryJobStore jobStore;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 objectMapper = new ObjectMapper();
 schedulerFactory = new StdSchedulerFactory();
 jobStore = new MemoryJobStore();
 schedulerFactory.setJobStore(jobStore);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("nonexistent-task-id");
 taskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class), taskEntity);

 // then
 TaskStatus expectedTaskStatus = TaskStatus.FAILED;
 String expectedMessage = String.format("Data archive task not found,rollback task fast failed.dataArchiveTaskId=%s",
         dataArchiveClearParameters.getDataArchiveTaskId());
 verify(log).warn(expectedMessage);
 assertFalse(taskEntity.isDone());
 assertTrue(taskEntity.getStatus().equals(expectedTaskStatus));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() throws JobExecutionException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-task-id");
 taskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 taskEntity.setStatus(TaskStatus.RUNNING);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class), taskEntity);

 // then
 TaskStatus expectedTaskStatus = TaskStatus.FAILED;
 String expectedMessage = String.format("Data archive task do not finish,data archive task id = %s", taskEntity.getId());
 verify(log).warn(expectedMessage);
 assertFalse(taskEntity.isDone());
 assertTrue(taskEntity.getStatus().equals(expectedTaskStatus));
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() throws JobExecutionException, JsonProcessingException {
 // given
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing-task-id");
 dataArchiveClearParameters.setDataArchiveTaskId("existing-task-id");
 taskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setResultJson("[]");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));

 DlmTask dlmTask1 = new DlmTask();
 DlmTask dlmTask2 = new DlmTask();

 List<DlmTask> taskUnits = asList(dlmTask1, dlmTask2);

 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity))
 .thenReturn(Optional.empty());

 // when
 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class), taskEntity);

 // then
 TaskStatus expectedTaskStatus = TaskStatus.PREPARING;
 for (DlmTask dlmTask : taskUnits) {
 dlmTask.setId(DlmJobIdUtil.generateHistoryJobId(taskEntity.getJobName(), taskEntity.getJobGroup(),
         taskEntity.getId(),
         taskUnits.size()));
 dlmTask.setJobType(JobType.DELETE);
 dlmTask.setStatus(expectedTaskStatus);
 }

 JobResult expectedJobResult = new JobResult();
 expectedJobResult.setJobId(taskEntity.getId());
 expectedJobResult.setResult(null);
 expectedJobResult.setStatus(expectedTaskStatus.toString());

 verify(schedulerFactory, times(2)).scheduleJob(eq(dataArchiveDeleteJob), eq(taskEntity.getId()), anyList());
 verify(schedulerFactory).getScheduler().start();
 verify(schedulerFactory).getScheduler().shutdown();
 verify(schedulerFactory, times(1)).getScheduler().getJobDetails(taskEntity.getId());
 assertEquals(expectedTaskStatus, taskEntity.getStatus());
 assertEquals(expectedJobResult.getStatus(), taskEntity.getLastStatus());
 }
}