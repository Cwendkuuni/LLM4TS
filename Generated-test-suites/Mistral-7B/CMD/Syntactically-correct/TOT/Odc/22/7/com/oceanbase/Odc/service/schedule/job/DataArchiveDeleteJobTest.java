import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.JobType;
import com.oceanbase.odc.service.schedule.JobUnitTestUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ObjectMapper objectMapper;

 @BeforeEach
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("nonexistent_task_id");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("test_job_name");
 scheduleTaskEntity.setJobGroup("test_job_group");
 scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.SCHEDULED);

 // when, then
 Assertions.assertThrows(JobExecutionException.class, () -> dataArchiveDeleteJob.execute(new JobExecutionContext()));
 }

 @Test
 public void testExecute_DataArchiveTaskExecuting() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing_task_id");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("test_job_name");
 scheduleTaskEntity.setJobGroup("test_job_group");
 scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

 // when, then
 Assertions.assertThrows(JobExecutionException.class, () -> dataArchiveDeleteJob.execute(new JobExecutionContext()));
 }

 @Test
 public void testExecute_DataArchiveTaskFinished() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing_task_id");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("test_job_name");
 scheduleTaskEntity.setJobGroup("test_job_group");
 scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setJobName("test_job_name");
 dataArchiveTask.setJobGroup("test_job_group");
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setResultJson("[{}]");

 List<ScheduleTaskEntity> scheduleTasks = new ArrayList<>();
 scheduleTasks.add(scheduleTaskEntity);
 scheduleTasks.add(dataArchiveTask);

 // when
 JobExecutionContext context = new MockContext(scheduleTasks);
 DataArchiveClearParameters input = new DataArchiveClearParameters();
 input.setDataArchiveTaskId("existing_task_id");

 // then
 dataArchiveDeleteJob.execute(context);

 JsonNode expectedResultJson = objectMapper.valueToTree(scheduleTasks.get(0).getResultJson());
 expectedResultJson.remove("parametersJson");
 expectedResultJson.set("status", JobStatus.SUCCEEDED.name());

 JsonNode actualResultJson = objectMapper.readTree(context.getResult());
 Assertions.assertEquals(expectedResultJson, actualResultJson);
 }

 @Test
 public void testExecute_DataArchiveTaskFailed() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing_task_id");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("test_job_name");
 scheduleTaskEntity.setJobGroup("test_job_group");
 scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setJobName("test_job_name");
 dataArchiveTask.setJobGroup("test_job_group");
 dataArchiveTask.setStatus(TaskStatus.FAILED);
 dataArchiveTask.setResultJson("[{}]");

 List<ScheduleTaskEntity> scheduleTasks = new ArrayList<>();
 scheduleTasks.add(scheduleTaskEntity);
 scheduleTasks.add(dataArchiveTask);

 // when
 DataArchiveClearParameters input = new DataArchiveClearParameters();
 input.setDataArchiveTaskId("existing_task_id");
 JobExecutionContext context = new MockContext(scheduleTasks);

 // then
 Assertions.assertThrows(JobExecutionException.class, () -> dataArchiveDeleteJob.execute(context));
 }

 @Test
 public void testExecute_DataArchiveTasksNotPrepared() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing_task_id");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("test_job_name");
 scheduleTaskEntity.setJobGroup("test_job_group");
 scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setJobName("test_job_name");
 dataArchiveTask.setJobGroup("test_job_group");
 dataArchiveTask.setStatus(TaskStatus.PREPARING);
 dataArchiveTask.setResultJson("[{}]");

 List<ScheduleTaskEntity> scheduleTasks = new ArrayList<>();
 scheduleTasks.add(scheduleTaskEntity);
 scheduleTasks.add(dataArchiveTask);

 // when
 DataArchiveClearParameters input = new DataArchiveClearParameters();
 input.setDataArchiveTaskId("existing_task_id");
 JobExecutionContext context = new MockContext(scheduleTasks);

 // then
 Assertions.assertThrows(JobExecutionException.class, () -> dataArchiveDeleteJob.execute(context));
 }

 @Test
 public void testExecute_DataArchiveTasksPrepared() {
 // given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("existing_task_id");
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("test_job_name");
 scheduleTaskEntity.setJobGroup("test_job_group");
 scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(dataArchiveClearParameters));
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setJobName("test_job_name");
 dataArchiveTask.setJobGroup("test_job_group");
 dataArchiveTask.setStatus(TaskStatus.PREPARING);
 dataArchiveTask.setResultJson("[{}]");
 List<ScheduleTaskEntity> scheduleTasks = new ArrayList<>();
 scheduleTasks.add(scheduleTaskEntity);
 scheduleTasks.add(dataArchiveTask);
 List<DlmTask> dlmTasks = new ArrayList<>();
 dlmTasks.add(new DlmTask());
 dlmTasks.add(new DlmTask());

 // when
 DataArchiveClearParameters input = new DataArchiveClearParameters();
 input.setDataArchiveTaskId("existing_task_id");
 JobExecutionContext context = new MockContext(scheduleTasks, dlmTasks);

 // then
 dataArchiveDeleteJob.execute(context);

 // verify
 JobUnitTestUtil.verifyTaskStatuses(scheduleTasks, TaskStatus.SUCCEEDED, TaskStatus.FAILED);
 JobUnitTestUtil.verifyTaskExecutions(dlmTasks, 2, JobType.DELETE);
 }
}