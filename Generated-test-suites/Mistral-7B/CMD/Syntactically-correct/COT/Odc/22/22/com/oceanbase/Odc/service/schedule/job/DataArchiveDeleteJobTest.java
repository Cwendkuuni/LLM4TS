import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.JobExecutionContext;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ObjectMapper objectMapper;

 @BeforeAll
 public static void setup() {
 // Initialize the DataArchiveDeleteJob instance
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testExecute_NoTaskFound() {
 // Test case for when data archive task is not found
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId(-1L);

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(parameters));
 taskEntity.setStatus(TaskStatus.SCHEDULED);
 taskEntity.setJobName("testJobName");
 taskEntity.setJobGroup("testJobGroup");

 JobExecutionContext context = new JobExecutionContext();
 context.setMergedJobDataMap(null);
 context.setJobDetail(null);
 context.setTrigger(null);
 context.setResult(taskEntity);

 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.execute(context);

 // Verify that the task status is updated to FAILED
 ScheduleTaskEntity actualTaskEntity = scheduleTaskRepository.findById(1L).orElseGet(ScheduleTaskEntity::new);
 assertEquals(TaskStatus.FAILED, actualTaskEntity.getStatus());
 }

 @Test
 public void testExecute_TaskNotFinished() {
 // Test case for when data archive task is not finished
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(parameters));
 taskEntity.setStatus(TaskStatus.RUNNING);
 taskEntity.setJobName("testJobName");
 taskEntity.setJobGroup("testJobGroup");

 JobExecutionContext context = new JobExecutionContext();
 context.setMergedJobDataMap(null);
 context.setJobDetail(null);
 context.setTrigger(null);
 context.setResult(taskEntity);

 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.execute(context);

 // Verify that the task status is updated to FAILED
 ScheduleTaskEntity actualTaskEntity = scheduleTaskRepository.findById(1L).orElseGet(ScheduleTaskEntity::new);
 assertEquals(TaskStatus.FAILED, actualTaskEntity.getStatus());
 }

 @Test
 public void testExecute_TaskFinished_NoTasksToClear() {
 // Test case for when data archive task is finished but there are no tasks to clear
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(parameters));
 taskEntity.setResultJson(JsonUtils.toJsonString(Arrays.asList()));
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setJobName("testJobName");
 taskEntity.setJobGroup("testJobGroup");

 JobExecutionContext context = new JobExecutionContext();
 context.setMergedJobDataMap(null);
 context.setJobDetail(null);
 context.setTrigger(null);
 context.setResult(taskEntity);

 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.execute(context);

 // Verify that no tasks were executed
 List<DlmTask> actualTaskUnits = dlmTaskRepository.findAll();
 assertTrue(actualTaskUnits.isEmpty());
 }

 @Test
 public void testExecute_TaskFinished_OneTaskToClear() {
 // Test case for when data archive task is finished and there is one task to clear
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId(1L);

 JsonNode taskEntitiesJson = JsonUtils.fromJson("[\n" +
 "  {\n" +
 "    \"id\": 1,\n" +
 "    \"parametersJson\": \"" + JsonUtils.toJsonString(parameters) + "\",\n" +
 "    \"resultJson\": \"" + JsonUtils.toJsonString(Arrays.asList(new DlmTask("taskUnit1"))) + "\",\n" +
 "    \"status\": \"DONE\"\n" +
 "  }\n" +
 "]", new TypeReference<JsonNode>() {});

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(parameters));
 taskEntity.setResultJson(JsonUtils.toJsonString(taskEntitiesJson));
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setJobName("testJobName");
 taskEntity.setJobGroup("testJobGroup");

 JobExecutionContext context = new JobExecutionContext();
 context.setMergedJobDataMap(null);
 context.setJobDetail(null);
 context.setTrigger(null);
 context.setResult(taskEntity);

 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.execute(context);

 // Verify that one task was executed
 List<DlmTask> actualTaskUnits = dlmTaskRepository.findAll();
 assertEquals(1, actualTaskUnits.size());
 DlmTask actualTaskUnit = actualTaskUnits.get(0);
 assertEquals("taskUnit1", actualTaskUnit.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testExecute_TaskFinished_MultipleTasksToClear() {
 // Test case for when data archive task is finished but there are multiple tasks to clear, this should throw an exception
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId(1L);

 JsonNode taskEntitiesJson = JsonUtils.fromJson("[\n" +
 "  {\n" +
 "    \"id\": 1,\n" +
 "    \"parametersJson\": \"" + JsonUtils.toJsonString(parameters) + "\",\n" +
 "    \"resultJson\": \"" + JsonUtils.toJsonString(
 Arrays.asList(new DlmTask("taskUnit1"), new DlmTask("taskUnit2"))) + "\",\n" +
 "    \"status\": \"DONE\"\n" +
 "  }\n" +
 "]", new TypeReference<JsonNode>() {});

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJsonString(parameters));
 taskEntity.setResultJson(JsonUtils.toJsonString(taskEntitiesJson));
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setJobName("testJobName");
 taskEntity.setJobGroup("testJobGroup");

 JobExecutionContext context = new JobExecutionContext();
 context.setMergedJobDataMap(null);
 context.setJobDetail(null);
 context.setTrigger(null);
 context.setResult(taskEntity);

 DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.execute(context);
 }

 @AfterAll
 public static void teardown() {
 // Clean up the test data
 scheduleTaskRepository.deleteAll();
 dlmTaskRepository.deleteAll();
 }
}