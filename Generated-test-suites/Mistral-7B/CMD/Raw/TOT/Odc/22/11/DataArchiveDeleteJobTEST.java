import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockJobExecutionContext;
import org.quartz.impl.StdSchedulerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveDeleteJobTest {

 private final ObjectMapper objectMapper = new ObjectMapper();
 private final ScheduleTaskRepository scheduleTaskRepository = new ScheduleTaskRepository();
 private final DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

 @Test
 public void testDataArchiveDeleteJobSuccess() throws SchedulerException {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("TestJobName");
 scheduleTaskEntity.setJobGroup("TestJobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(getTestDataArchiveClearParameters()));
 scheduleTaskEntity.setStatus(TaskStatus.SCHEDULED);

 MockJobExecutionContext context = new MockJobExecutionContext();
 context.setJobDetail(JobBuilder.newJob(DataArchiveDeleteJob.class)
 .withIdentity("TestJobName", "TestJobGroup")
 .build());
 context.setMergedJobDataMap(new JobDataMap());
 context.setResult(scheduleTaskEntity);

 // When
 dataArchiveDeleteJob.execute(context);

 // Then
 ScheduleTaskEntity actualScheduleTaskEntity = scheduleTaskRepository.findById(scheduleTaskEntity.getId()).get();
 Assertions.assertEquals(TaskStatus.DONE, actualScheduleTaskEntity.getStatus());
 }

 @Test
 public void testDataArchiveDeleteJobFailed() throws SchedulerException {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("TestJobName");
 scheduleTaskEntity.setJobGroup("TestJobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(getTestDataArchiveClearParameters()));
 scheduleTaskEntity.setStatus(TaskStatus.SCHEDULED);

 MockJobExecutionContext context = new MockJobExecutionContext();
 context.setJobDetail(JobBuilder.newJob(DataArchiveDeleteJob.class)
 .withIdentity("TestJobName", "TestJobGroup")
 .build());
 context.setMergedJobDataMap(new JobDataMap());
 context.setResult(scheduleTaskEntity);

 // When
 dataArchiveDeleteJob.execute(context);

 // Then
 ScheduleTaskEntity actualScheduleTaskEntity = scheduleTaskRepository.findById(scheduleTaskEntity.getId()).get();
 Assertions.assertEquals(TaskStatus.FAILED, actualScheduleTaskEntity.getStatus());
 }

 @Test
 public void testDataArchiveDeleteJobNotFound() throws SchedulerException {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("TestJobName");
 scheduleTaskEntity.setJobGroup("TestJobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(getTestDataArchiveClearParameters()));
 scheduleTaskEntity.setStatus(TaskStatus.SCHEDULED);
 scheduleTaskRepository.save(scheduleTaskEntity);

 MockJobExecutionContext context = new MockJobExecutionContext();
 context.setJobDetail(JobBuilder.newJob(DataArchiveDeleteJob.class)
 .withIdentity("TestJobName", "TestJobGroup")
 .build());
 context.setMergedJobDataMap(new JobDataMap());
 context.setResult(scheduleTaskEntity);

 // When
 dataArchiveDeleteJob.execute(context);

 // Then
 Assertions.assertFalse(scheduleTaskRepository.findById(scheduleTaskEntity.getId()).isPresent());
 }

 @Test
 public void testDataArchiveDeleteJobRunning() throws SchedulerException {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("TestJobName");
 scheduleTaskEntity.setJobGroup("TestJobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(getTestDataArchiveClearParameters()));
 scheduleTaskEntity.setStatus(TaskStatus.SCHEDULED);
 scheduleTaskRepository.save(scheduleTaskEntity);

 MockJobExecutionContext context = new MockJobExecutionContext();
 context.setJobDetail(JobBuilder.newJob(DataArchiveDeleteJob.class)
 .withIdentity("TestJobName", "TestJobGroup")
 .build());
 context.setMergedJobDataMap(new JobDataMap());
 context.setResult(scheduleTaskEntity);

 // When
 dataArchiveDeleteJob.execute(context);

 // Then
 ScheduleTaskEntity actualScheduleTaskEntity = scheduleTaskRepository.findById(scheduleTaskEntity.getId()).get();
 Assertions.assertEquals(TaskStatus.RUNNING, actualScheduleTaskEntity.getStatus());
 }

 private DataArchiveClearParameters getTestDataArchiveClearParameters() {
 return new DataArchiveClearParameters() {{
 setDataArchiveTaskId(1L);
 }};
 }

 @Test
 public void testExecuteTask() throws SchedulerException {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("TestJobName");
 scheduleTaskEntity.setJobGroup("TestJobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(getTestDataArchiveClearParameters()));
 scheduleTaskEntity.setStatus(TaskStatus.SCHEDULED);
 scheduleTaskRepository.save(scheduleTaskEntity);

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask() {{
 setId(1L);
 setStatus(TaskStatus.SCHEDULED);
 }},
 new DlmTask() {{
 setId(2L);
 setStatus(TaskStatus.SCHEDULED);
 }});

 // When
 dataArchiveDeleteJob.executeTask(scheduleTaskEntity.getId(), dlmTasks);

 // Then
 for (DlmTask dlmTask : dlmTasks) {
 ScheduleTaskEntity actualDlmTaskEntity = scheduleTaskRepository.findById(dlmTask.getId()).get();
 Assertions.assertEquals(TaskStatus.DONE, actualDlmTaskEntity.getStatus());
 }
 }

 @Test
 public void testGetTaskStatus() throws SchedulerException {
 // Given
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask() {{
 setId(1L);
 setStatus(TaskStatus.SCHEDULED);
 }},
 new DlmTask() {{
 setId(2L);
 setStatus(TaskStatus.FAILED);
 }},
 new DlmTask() {{
 setId(3L);
 setStatus(TaskStatus.RUNNING);
 }});

 // When
 TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTasks);

 // Then
 Assertions.assertEquals(TaskStatus.RUNNING, taskStatus);
 }

 @Test
 public void testExecute() throws SchedulerException {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("TestJobName");
 scheduleTaskEntity.setJobGroup("TestJobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(getTestDataArchiveClearParameters()));
 scheduleTaskEntity.setStatus(TaskStatus.SCHEDULED);
 scheduleTaskRepository.save(scheduleTaskEntity);

 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask() {{
 setId(1L);
 setStatus(TaskStatus.SCHEDULED);
 }},
 new DlmTask() {{
 setId(2L);
 setStatus(TaskStatus.SCHEDULED);
 }});

 Scheduler scheduler = new StdSchedulerFactory().getScheduler();
 scheduler.start();
 Trigger trigger = TriggerBuilder.newTrigger()
 .withIdentity("TestTriggerName", "TestTriggerGroup")
 .startAt(0, 0, 0)
 .build();

 JobExecutionContext context = JobBuilder.newJob(DataArchiveDeleteJob.class)
 .withIdentity("TestJobName", "TestJobGroup")
 .build()
 .executeInTransaction(true)
 .build();
 context.setJobDataMap(new JobDataMap());
 context.setMergedJobDataMap(new JobDataMap());
 context.setResult(scheduleTaskEntity);
 scheduler.scheduleJob(context, trigger);
 scheduler.run();
 scheduler.shutdown();

 // Then
 ScheduleTaskEntity actualScheduleTaskEntity = scheduleTaskRepository.findById(scheduleTaskEntity.getId()).get();
 Assertions.assertEquals(TaskStatus.DONE, actualScheduleTaskEntity.getStatus());
 }

 @AfterEach
 public void tearDown() {
 scheduleTaskRepository.deleteAll();
 }
}