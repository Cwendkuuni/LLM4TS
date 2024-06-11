import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.JobScheduler;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.enums.JobType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private JobScheduler jobScheduler;

 @BeforeEach
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 jobScheduler = mock(JobScheduler.class);
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
 dataArchiveRollbackJob.setJobScheduler(jobScheduler);
 }

 @Test
 public void testExecute_ValidDataArchiveTask_DataArchiveRollback() throws Exception {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");

 ObjectMapper objectMapper = new ObjectMapper();
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.updateStatusById(any(Long.class), any(String.class))).thenReturn(1);
 when(jobScheduler.executeTasks(any(List.class))).thenReturn(true);

 // When
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 // Add more assertions here to test the expected behavior of the execute method.
 }

 @Test
 public void testExecute_InvalidDataArchiveTask_TaskStatusUpdate() throws Exception {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");

 ObjectMapper objectMapper = new ObjectMapper();
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.updateStatusById(any(Long.class), any(String.class))).thenReturn(1);
 when(jobScheduler.executeTasks(any(List.class))).thenThrow(new JobExecutionException("testException"));

 // When
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 // Add more assertions here to test the expected behavior of the execute method when an error occurs.
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound_TaskStatusUpdate() throws Exception {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(rollbackParameters));
 scheduleTaskEntity.setJobName("testJobName");
 scheduleTaskEntity.setJobGroup("testJobGroup");

 ObjectMapper objectMapper = new ObjectMapper();
 List<DlmTask> dlmTasks = Arrays.asList(new DlmTask(), new DlmTask());
 when(scheduleTaskRepository.findById(any(Long.class))).thenReturn(Optional.empty());
 when(scheduleTaskRepository.updateStatusById(any(Long.class), any(String.class))).thenReturn(1);

 // When
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 // Add more assertions here to test the expected behavior of the execute method when a data archive task is not found.
 }
}