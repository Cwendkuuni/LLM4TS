import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.MockitoJobDetail;
import org.quartz.MockitoScheduler;
import org.quartz.Trigger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters.DataArchiveRollbackParametersBuilder;
import com.oceanbase.odc.service.schedule.service.DataArchiveRollbackJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.google.common.collect.Lists;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private JobRepository jobRepository;
 private MockitoScheduler scheduler;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 jobRepository = mock(JobRepository.class);
 scheduler = new MockitoScheduler();
 dataArchiveRollbackJob.setJobRepository(jobRepository);
 dataArchiveRollbackJob.setScheduler(scheduler);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParametersBuilder()
 .dataArchiveTaskId(1L)
 .build();
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
 ScheduleTaskEntity dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
 when(jobRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(dataArchiveTaskOption);

 // when
 dataArchiveRollbackJob.execute(new MockitoJobExecutionContext(new MockitoJobDetail("job-name", "job-group", "job-key"),
 scheduler.getContext(), new Trigger()));

 // then
 verify(jobRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_Success() {
 // given
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParametersBuilder()
 .dataArchiveTaskId(1L)
 .build();
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setResultJson(JsonUtils.toJson(Lists.newArrayList(new DlmTask(1L, 1L, "task-name", "task-desc",
 JobType.ROLLBACK, TaskStatus.DONE, "source-db-url", "target-db-url"))));
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
 ScheduleTaskEntity dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
 when(jobRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(dataArchiveTaskOption);

 ObjectMapper objectMapper = new ObjectMapper();
 JsonNode dlmTasksJson = objectMapper.valueToTree(Lists.newArrayList(
 new DlmTask(1L, 1L, "task-name", "task-desc", JobType.ROLLBACK, TaskStatus.DONE, "source-db-url", "target-db-url"),
 new DlmTask(2L, 2L, "task-name", "task-desc", JobType.ROLLBACK, TaskStatus.DONE, "source-db-url", "target-db-url")));

 // when
 dataArchiveRollbackJob.execute(new MockitoJobExecutionContext(new MockitoJobDetail("job-name", "job-group", "job-key"),
 scheduler.getContext(), new Trigger()));

 // then
 verify(jobRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.DONE));
 }
}