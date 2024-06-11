import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.quartz.JobExecutionContext;
import org.quartz.MockJobExecutionContext;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveDeleteJobTest {

 private final ObjectMapper objectMapper = new ObjectMapper();
 private final ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 private final DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

 @Before
 public void setUp() {
 // Initialize mocks
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(createScheduleTaskEntity()));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound_TaskStatusUpdatedToFailed() {
 // Arrange
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 MockJobExecutionContext context = new MockJobExecutionContext();
 context.setMergedJobDataMap(createJobDataMap(dataArchiveClearParameters));

 // Act
 dataArchiveDeleteJob.execute(context);

 // Assert
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished_TaskStatusUpdatedToFailed() {
 // Arrange
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = createScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
 MockJobExecutionContext context = new MockJobExecutionContext();
 context.setMergedJobDataMap(createJobDataMap(dataArchiveClearParameters));

 // Act
 dataArchiveDeleteJob.execute(context);

 // Assert
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_PrepareTasksForClear_TaskStatusUpdatedToPreparing() {
 // Arrange
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = createScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.DONE);
 DlmTask dlmTask = new DlmTask();
 List<DlmTask> dlmTasks = Arrays.asList(dlmTask);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
 MockJobExecutionContext context = new MockJobExecutionContext();
 context.setMergedJobDataMap(createJobDataMap(dataArchiveClearParameters));

 // Act
 dataArchiveDeleteJob.execute(context);

 // Assert
 verify(dlmTask, times(1)).setId(anyLong());
 verify(dlmTask, times(1)).setJobType(eq(JobType.DELETE));
 verify(dlmTask, times(1)).setStatus(eq(TaskStatus.PREPARING));
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(dataArchiveTask.getId()), any(TaskStatus.class));
 }

 @Test
 public void testExecute_UpdateTaskStatus_TaskStatusUpdatedToCompleted() {
 // Arrange
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = createScheduleTaskEntity();
 dataArchiveTask.setStatus(TaskStatus.DONE);
 DlmTask dlmTask = new DlmTask();
 List<DlmTask> dlmTasks = Arrays.asList(dlmTask);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
 MockJobExecutionContext context = new MockJobExecutionContext();
 context.setMergedJobDataMap(createJobDataMap(dataArchiveClearParameters));

 // Act
 dataArchiveDeleteJob.execute(context);

 // Assert
 verify(dlmTask, times(1)).setId(anyLong());
 verify(dlmTask, times(1)).setJobType(eq(JobType.DELETE));
 verify(dlmTask, times(1)).setStatus(eq(TaskStatus.PREPARING));
 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(dataArchiveTask.getId()), eq(TaskStatus.COMPLETED));
 }

 private ScheduleTaskEntity createScheduleTaskEntity() {
 return new ScheduleTaskEntity()
 .setId(1L)
 .setJobName("DataArchiveDeleteJob")
 .setJobGroup("test_group")
 .setParametersJson(objectMapper.writeValueAsString(new DataArchiveClearParameters()))
 .setResultJson(objectMapper.writeValueAsString(Arrays.asList(new DlmTask())))
 .setStatus(TaskStatus.DONE)
 .setCreatedAt(LocalDateTime.now())
 .setUpdatedAt(LocalDateTime.now());
 }

 private JsonNode createJobDataMap(DataArchiveClearParameters dataArchiveClearParameters) {
 return objectMapper.createObjectNode().put("dataArchiveClearParameters", dataArchiveClearParameters);
 }
}