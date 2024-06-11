import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.JobRepository;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(Mockito.MockitoJUnit.MockitoExtension.class)
public class DataArchiveRollbackJobTest {

 @InjectMocks
 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ObjectMapper objectMapper = new ObjectMapper();
 private JobRepository jobRepository = Mockito.mock(JobRepository.class);

 @Test
 public void testExecuteWithValidData() {
 // Given
 String jsonString = "{\"dataArchiveTaskId\":1,\"parametersJson\":{\"dataArchiveTaskId\":1,\"dataArchiveTaskName\":\"testDataArchiveTask\",\"rollbackParameters\":{}}}";
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJsonString(jsonString);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 List<DlmTask> dlmTasks = new ArrayList<>();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 DlmTask dlmTask = new DlmTask();
 dlmTask.setSourceDatabaseId(1L);
 dlmTask.setId(1L);
 dlmTasks.add(dlmTask);

 // When
 dataArchiveRollbackJob.jobRepository = jobRepository;
 Mockito.when(jobRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 Mockito.when(jobRepository.updateStatusById(Mockito.anyLong(), Mockito.any(TaskStatus.class))).thenReturn(true);
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 // Verify the expected methods call order and arguments
 Mockito.verify(jobRepository, Mockito.times(1)).findById(1L);
 Mockito.verify(jobRepository, Mockito.times(1)).updateStatusById(1L, TaskStatus.DONE);
 Assertions.assertThrows(JobExecutionException.class, () -> dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class)));
 }

 @Test
 public void testExecuteWithInvalidDataArchiveTaskId() {
 // Given
 String jsonString = "{\"dataArchiveTaskId\":-1,\"parametersJson\":{\"dataArchiveTaskId\":1,\"dataArchiveTaskName\":\"testDataArchiveTask\",\"rollbackParameters\":{}}}";
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJsonString(jsonString);

 // When
 dataArchiveRollbackJob.jobRepository = jobRepository;
 Mockito.when(jobRepository.findById(-1L)).thenReturn(Optional.empty());
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 // Verify the expected methods call order and arguments
 Mockito.verify(jobRepository, Mockito.times(1)).findById(-1L);
 Mockito.verify(jobRepository, Mockito.never()).updateStatusById(1L, TaskStatus.DONE);
 }

 @Test
 public void testExecuteWithNullTaskEntity() {
 // Given
 dataArchiveRollbackJob.jobRepository = jobRepository;
 Mockito.when(jobRepository.findById(1L)).thenReturn(Optional.empty());
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 // Verify the expected methods call order and arguments
 Mockito.verify(jobRepository, Mockito.times(1)).findById(1L);
 Mockito.verify(jobRepository, Mockito.never()).updateStatusById(1L, TaskStatus.DONE);
 }

 @Test
 public void testExecuteWithNullDlmTasks() {
 // Given
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJsonString("{\"dataArchiveTaskId\":1,\"parametersJson\":{\"dataArchiveTaskId\":1,\"dataArchiveTaskName\":\"testDataArchiveTask\",\"rollbackParameters\":{}}}");
 dataArchiveRollbackJob.jobRepository = jobRepository;
 Mockito.when(jobRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 List<DlmTask> dlmTasks = null;

 // When
 dataArchiveRollbackJob.execute(Mockito.mock(JobExecutionContext.class));

 // Then
 // Verify the expected methods call order and arguments
 Mockito.verify(jobRepository, Mockito.times(1)).findById(1L);
 Mockito.verify(jobRepository, Mockito.never()).updateStatusById(1L, TaskStatus.DONE);
 }
}