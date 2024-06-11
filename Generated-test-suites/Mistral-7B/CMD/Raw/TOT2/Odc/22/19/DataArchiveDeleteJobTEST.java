import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskEntity scheduleTaskEntity;

 @BeforeEach
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 scheduleTaskEntity = new ScheduleTaskEntity();
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("-1");
 scheduleTaskEntity.setId("1");
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 dataArchiveDeleteJob.execute(null);

 // Check if the task status has been updated to FAILED.
 assertTrue(dataArchiveDeleteJob.getTaskStatus(new ArrayList<>()).isFailed());
 }

 @Test
 public void testExecute_DataArchiveTaskAlreadyFinished() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("1");
 scheduleTaskEntity.setId("1");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 // Prepare mocked data archive tasks.
 List<ScheduleTaskEntity> dataArchiveTasks = new ArrayList<>();
 dataArchiveTasks.add(scheduleTaskEntity);

 // Execute the job.
 dataArchiveDeleteJob.execute(new JobExecutionContext() {});

 // Check if the task status has been updated correctly.
 TaskStatus expectedTaskStatus = TaskStatus.COMPLETED;
 assertEquals(expectedTaskStatus, dataArchiveDeleteJob.getTaskStatus(dataArchiveTasks).get());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("1");
 scheduleTaskEntity.setId("1");
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJsonString(dataArchiveClearParameters));

 // Prepare mocked data archive tasks.
 List<ScheduleTaskEntity> dataArchiveTasks = new ArrayList<>();
 dataArchiveTasks.add(scheduleTaskEntity);

 // Execute the job.
 dataArchiveDeleteJob.execute(new JobExecutionContext() {});

 // Check if the task status has been updated correctly.
 TaskStatus expectedTaskStatus = TaskStatus.FAILED;
 assertEquals(expectedTaskStatus, dataArchiveDeleteJob.getTaskStatus(dataArchiveTasks).get());
 }

 @AfterEach
 public void tearDown() {
 dataArchiveDeleteJob = null;
 }
}