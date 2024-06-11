import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.MockObject;
import org.quartz.impl.jobstore.JobKey;
import org.quartz.impl.jobstore.mock.MockJobExecutionContext;
import org.quartz.impl.jobstore.mock.MockQuartzScheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private MockQuartzScheduler quartzScheduler;
 private JobExecutionContext jobExecutionContext;
 private ObjectMapper objectMapper;

 @BeforeEach
 public void setUp() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 quartzScheduler = new MockQuartzScheduler();
 jobExecutionContext = new MockJobExecutionContext();
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testExecute_DataArchiveTaskFound() {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = createScheduleTaskEntity();
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of( scheduleTaskEntity ));
 List<DlmTask> dlmTasks = Arrays.asList(createDlmTask(), createDlmTask());
 DlmTask dlmTask1 = dlmTasks.get(0);
 DlmTask dlmTask2 = dlmTasks.get(1);
 when(scheduleTaskRepository.updateStatusById(anyLong(), eq(TaskStatus.FAILED))).thenReturn(true);
 doCallRealMethod().when(dataArchiveRollbackJob).execute(jobExecutionContext);

 // When
 dataArchiveRollbackJob.execute(jobExecutionContext);

 // Then
 verify(scheduleTaskRepository, times(1)).findById(rollbackParameters.getDataArchiveTaskId());
 verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), eq(TaskStatus.FAILED));
 verify(quartzScheduler, times(1)).pauseJob(eq(new JobKey(scheduleTaskEntity.getJobName(), scheduleTaskEntity.getJobGroup())), false);

 // Data archive rollback job should process all tasks
 verify(dataArchiveRollbackJob, times(1)).executeTask(anyLong(), eq(dlmTasks));

 // Data archive rollback job should update task statuses
 verify(dataArchiveRollbackJob, times(2)).getTaskStatus(eq(dlmTasks));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // Given
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
 doCallRealMethod().when(dataArchiveRollbackJob).execute(jobExecutionContext);

 // When
 dataArchiveRollbackJob.execute(jobExecutionContext);

 // Then
 verify(scheduleTaskRepository, times(1)).findById(anyLong());
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 verify(quartzScheduler, never()).pauseJob(any(), anyBoolean());
 verify(dataArchiveRollbackJob, never()).executeTask(anyLong(), any());
 }

 private ScheduleTaskEntity createScheduleTaskEntity() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(new DataArchiveRollbackParameters("1", null)));
 return scheduleTaskEntity;
 }

 private DlmTask createDlmTask() {
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setSourceDatabaseId(1L);
 dlmTask.setTargetDatabaseId(2L);
 dlmTask.setJobType(JobType.ARCHIVE);
 dlmTask.setStatus(TaskStatus.PREPARING);
 return dlmTask;
 }

}