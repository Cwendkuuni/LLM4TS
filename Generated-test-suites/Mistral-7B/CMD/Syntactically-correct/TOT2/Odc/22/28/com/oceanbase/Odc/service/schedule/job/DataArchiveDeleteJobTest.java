import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ScheduleTaskEntity scheduleTaskEntity;

 @BeforeAll
 public static void setupClass() {
 // Initialize mocks or setup static data if needed
 }

 @BeforeEach
 public void setup() {
 // Initialize test variables or setup test data here
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_TaskNotFound() {
 // Given: A task entity with invalid dataArchiveTaskId
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("invalid-task-id");

 // When: Executing the job
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

 try {
 dataArchiveDeleteJob.execute(context);
 fail("Expected a JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // Then: The job should handle the task not found scenario correctly
 verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }
 }

 @Test
 public void testExecute_TaskNotFinished() {
 // Given: A task entity with a task that is not finished
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.RUNNING);
 dataArchiveTask.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 // When: Executing the job
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(2L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskRepository = new ScheduleTaskRepository() {
 @Override
 public Optional<ScheduleTaskEntity> findById(Long id) {
 return Optional.ofNullable(dataArchiveTask);
 }
 };
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

 try {
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 fail("Expected a JobExecutionException to be thrown.");
 } catch (JobExecutionException e) {
 // Then: The job should handle the task not finished scenario correctly
 verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }
 }

 @Test
 public void testExecute_Success() {
 // Given: A valid task entity
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setResultJson(JsonUtils.toJsonString(new ArrayList<>()));

 // When: Executing the job
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(2L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));
 scheduleTaskRepository = new ScheduleTaskRepository() {
 @Override
 public Optional<ScheduleTaskEntity> findById(Long id) {
 return Optional.ofNullable(dataArchiveTask);
 }
 };
 DlmTask dlmTask = new DlmTask();
 List<DlmTask> taskUnits = Arrays.asList(dlmTask);
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.ofNullable(dataArchiveTask));

 try {
 dataArchiveDeleteJob.execute(Mockito.mock(JobExecutionContext.class));
 // Then: The job should clear the data archives successfully
 verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.SUCCESS);
 } catch (JobExecutionException e) {
 fail("Unexpected exception thrown.");
 }
 }

 @AfterEach
 public void teardown() {
 // Clean up test data or reset test variables here
 }

 @AfterAll
 public static void teardownClass() {
 // Clean up static data or mocks here
 }
}