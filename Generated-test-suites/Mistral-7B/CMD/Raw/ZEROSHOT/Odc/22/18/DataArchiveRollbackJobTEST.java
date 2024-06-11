import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.JobTestHelper;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class DataArchiveRollbackJobTest {

 private ObjectMapper jsonMapper;
 private Scheduler scheduler;

 @Before
 public void setUp() throws Exception {
 jsonMapper = new ObjectMapper();
 scheduler = new StdSchedulerFactory().getScheduler();
 scheduler.start();
 }

 @After
 public void tearDown() throws Exception {
 scheduler.shutdown();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("non-existent-id");

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId("task-id");
 scheduleTaskEntity.setJobName("job-name");
 scheduleTaskEntity.setJobGroup("job-group");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 // when
 DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
 dataArchiveRollbackJob.execute(new JobExecutionContext() {
 @Override
 public JobDetail getJobDetail() {
 return JobBuilder.newJob(DataArchiveRollbackJob.class).withIdentity("job-key", "group-key").build();
 }

 @Override
 public Trigger getTrigger() {
 return Trigger.none();
 }

 @Override
 public JobDataMap getJobDataMap() {
 return new JobDataMap();
 }

 @Override
 public ScheduleTaskEntity getMergedJobData() {
 return scheduleTaskEntity;
 }
 });

 // then
 Assert.assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskExists() throws Exception {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId("existing-id");

 ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
 dataArchiveTaskEntity.setId("task-id");
 dataArchiveTaskEntity.setJobName("job-name");
 dataArchiveTaskEntity.setJobGroup("job-group");
 dataArchiveTaskEntity.setStatus(TaskStatus.SUCCESS);
 dataArchiveTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));

 ScheduleTaskEntity dlmTaskEntity = new ScheduleTaskEntity();
 dlmTaskEntity.setId("task-unit-id");
 dlmTaskEntity.setSourceDatabaseId(1L);
 dlmTaskEntity.setResultJson(JsonUtils.toJsonString(new ArrayList<>()));
 dlmTaskEntity.setStatus(TaskStatus.PREPARING);
 dlmTaskEntity.setJobName("dlm-job-name");
 dlmTaskEntity.setJobGroup("dlm-job-group");
 dlmTaskEntity.setCreateTime(System.currentTimeMillis());

 List<ScheduleTaskEntity> scheduleTasks = new ArrayList<>();
 scheduleTasks.add(dataArchiveTaskEntity);
 scheduleTasks.add(dlmTaskEntity);

 when(scheduleTaskRepository.findById(eq("existing-id"))).thenReturn(Optional.of(dataArchiveTaskEntity));
 when(scheduleTaskRepository.save(any())).then(invocation -> invocation.getArgument(0));

 // when
 DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
 dataArchiveRollbackJob.execute(new JobExecutionContext() {
 @Override
 public JobDetail getJobDetail() {
 return JobBuilder.newJob(DataArchiveRollbackJob.class).withIdentity("job-key", "group-key").build();
 }

 @Override
 public Trigger getTrigger() {
 return Trigger.none();
 }

 @Override
 public JobDataMap getJobDataMap() {
 return new JobDataMap();
 }

 @Override
 public ScheduleTaskEntity getMergedJobData() {
 return scheduleTaskEntity;
 }
 });

 // then
 verify(scheduleTaskRepository, times(2)).save(any());
 DataArchiveRollbackParameters actualRollbackParameters = jsonMapper.readValue(dataArchiveTaskEntity.getParametersJson(), DataArchiveRollbackParameters.class);
 Assert.assertEquals("existing-id", actualRollbackParameters.getDataArchiveTaskId());

 TaskStatus taskStatus = getTaskStatus(dataArchiveTaskEntity, dlmTaskEntity);
 Assert.assertEquals(TaskStatus.DONE, taskStatus);
 }

 private TaskStatus getTaskStatus(ScheduleTaskEntity dataArchiveTaskEntity, ScheduleTaskEntity dlmTaskEntity) {
 List<DlmTask> dlmTasks = jsonMapper.readValue(dataArchiveTaskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {});
 TaskStatus taskStatus = dlmTasks.isEmpty() ? dataArchiveTaskEntity.getStatus() : dlmTasks.get(0).getStatus();
 return taskStatus;
 }
}