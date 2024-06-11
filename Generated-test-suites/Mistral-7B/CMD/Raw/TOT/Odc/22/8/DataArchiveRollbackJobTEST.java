import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockJobExecutionContext;
import org.quartz.impl.JobDetailImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import static org.junit.Assert.*;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private JobRepository jobRepository;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 jobRepository = mock(JobRepository.class);
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testExecute_DataArchiveTaskExists_TasksRolledBackSuccessfully() {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("dataArchiveJob");
 scheduleTaskEntity.setJobGroup("dataArchiveGroup");
 scheduleTaskEntity.setParametersJson(getJsonString(new DataArchiveRollbackParameters(1L, List.of())));
 JobDetailImpl jobDetail = new JobDetailImpl("jobDetailKey", "jobGroup", DataArchiveRollbackJob.class);
 jobDetail.setJobDataMap(new JobDataMap());
 MockJobExecutionContext context = new MockJobExecutionContext(jobDetail);
 context.setMergedJobData(new JobDataMap());
 context.setResult(scheduleTaskEntity);

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
 // Verify that the tasks were rolled back successfully
 // (Implement a method to check the rolled back tasks' statuses and verify it here)
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound_TaskStatusUpdatedToFailed() {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("dataArchiveJob");
 scheduleTaskEntity.setJobGroup("dataArchiveGroup");
 scheduleTaskEntity.setParametersJson(getJsonString(new DataArchiveRollbackParameters(2L, List.of()))); // wrong task id
 JobDetailImpl jobDetail = new JobDetailImpl("jobDetailKey", "jobGroup", DataArchiveRollbackJob.class);
 jobDetail.setJobDataMap(new JobDataMap());
 MockJobExecutionContext context = new MockJobExecutionContext(jobDetail);
 context.setMergedJobData(new JobDataMap());
 context.setResult(scheduleTaskEntity);

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 // Add more test cases as needed, such as:
 // - Task units are null or empty
 // - Task units have invalid IDs
 // - Rollback fails for some tasks

 private String getJsonString(DataArchiveRollbackParameters parameters) {
 return objectMapper.writeValueAsString(parameters);
 }
}