import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.google.common.collect.Lists.newArrayList;
import static com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve.*;
import static com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult.*;
import static com.oceanbase.odc.service.onlineschemachange.model.SwapTableType.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.pipeline.BaseValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class ScheduleCheckOmsProjectValveTest {

 @Autowired
 private ProjectOpenApiService projectOpenApiService;
 @Autowired
 private OscTaskCompleteHandler completeHandler;
 @Autowired
 private ScheduleTaskRepository scheduleTaskRepository;

 @Autowired
 private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

 @BeforeAll
 static void setup() {
 // initialize mocks or fixtures here
 }

 @AfterAll
 static void teardown() {
 // clean up mocks or fixtures here
 }

 @Test
 void testInvoke_Success() throws OscException, InterruptedException {
 // Arrange
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 OscValveContext context = new OscValveContext();
 context.setScheduleTask(scheduleTask);
 context.setTaskParameter(taskParameter);

 // Act
 ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
 valve.invoke(context);

 // Assert
 assertEquals(TaskStatus.DONE, context.getScheduleTask().getStatus());
 }

 @Test
 void testInvoke_PreCheckFailed() throws OscException, InterruptedException {
 // Arrange
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 ProjectStepResult projectStepResult = new ProjectStepResult();
 projectStepResult.setCurrentStep("PRE_CHECK");
 projectStepResult.setCurrentStepStatus(TaskStatus.FAILED.name());
 projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
 projectStepResult.setErrorMsg("Precheck failed: invalid schema version");

 OscValveContext context = new OscValveContext();
 context.setScheduleTask(scheduleTask);
 context.setTaskParameter(taskParameter);
 context.setProjectStepResult(projectStepResult);

 // Act and Assert
 assertThrows(OscException.class, () -> new ScheduleCheckOmsProjectValve().invoke(context));
 }

 @Test
 void testInvoke_FullVerificationInconsistent() throws OscException, InterruptedException {
 // Arrange
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 ProjectStepResult projectStepResult = new ProjectStepResult();
 projectStepResult.setCurrentStep("FULL_VERIFIER");
 projectStepResult.setCurrentStepStatus(TaskStatus.RUNNING.name());
 projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
 projectStepResult.setFullVerificationResultDescription("Table xyz has inconsistent data");

 OscValveContext context = new OscValveContext();
 context.setScheduleTask(scheduleTask);
 context.setTaskParameter(taskParameter);
 context.setProjectStepResult(projectStepResult);

 // Act and Assert
 assertThrows(OscException.class, () -> new ScheduleCheckOmsProjectValve().invoke(context));
 }

 @Test
 void testInvoke_FullTransferFailed() throws OscException, InterruptedException {
 // Arrange
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 ProjectStepResult projectStepResult = new ProjectStepResult();
 projectStepResult.setCurrentStep("FULL_TRANSFER");
 projectStepResult.setCurrentStepStatus(TaskStatus.FAILED.name());
 projectStepResult.setFullTransferProgressPercentage(50);
 projectStepResult.setFullTransferFinishedCount(10);
 projectStepResult.setFullTransferEstimatedCount(20);

 OscValveContext context = new OscValveContext();
 context.setScheduleTask(scheduleTask);
 context.setTaskParameter(taskParameter);
 context.setProjectStepResult(projectStepResult);

 // Act and Assert
 assertThrows(OscException.class, () -> new ScheduleCheckOmsProjectValve().invoke(context));
 }

 @Test
 void testInvoke_FullVerificationSkipped() throws OscException, InterruptedException {
 // Arrange
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 ProjectStepResult projectStepResult = new ProjectStepResult();
 projectStepResult.setCurrentStep("FULL_VERIFIER");
 projectStepResult.setCurrentStepStatus(TaskStatus.SKIPPED.name());
 projectStepResult.setFullVerificationResult(FullVerificationResult.UNCHECK);
 projectStepResult.setFullVerificationResultDescription("");

 OscValveContext context = new OscValveContext();
 context.setScheduleTask(scheduleTask);
 context.setTaskParameter(taskParameter);
 context.setProjectStepResult(projectStepResult);

 // Act
 ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
 valve.invoke(context);

 // Assert
 assertEquals(TaskStatus.SKIPPED, context.getScheduleTask().getStatus());
 }

 @Test
 void testInvoke_FullVerificationConsistent() throws OscException, InterruptedException {
 // Arrange
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 ProjectStepResult projectStepResult = new ProjectStepResult();
 projectStepResult.setCurrentStep("FULL_VERIFIER");
 projectStepResult.setCurrentStepStatus(TaskStatus.DONE.name());
 projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
 projectStepResult.setFullVerificationResultDescription("");

 OscValveContext context = new OscValveContext();
 context.setScheduleTask(scheduleTask);
 context.setTaskParameter(taskParameter);
 context.setProjectStepResult(projectStepResult);

 // Act
 ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
 valve.invoke(context);

 // Assert
 assertEquals(TaskStatus.DONE, context.getScheduleTask().getStatus());
 }

 @Test
 void testInvoke_FullVerificationSkippedWithError() throws OscException, InterruptedException {
 // Arrange
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 ProjectStepResult projectStepResult = new ProjectStepResult();
 projectStepResult.setCurrentStep("FULL_VERIFIER");
 projectStepResult.setCurrentStepStatus(TaskStatus.FAILED.name());
 projectStepResult.setFullVerificationResult(FullVerificationResult.SKIPPED_WITH_ERROR);
 projectStepResult.setFullVerificationResultDescription("Verification skipped due to error");

 OscValveContext context = new OscValveContext();
 context.setScheduleTask(scheduleTask);
 context.setTaskParameter(taskParameter);
 context.setProjectStepResult(projectStepResult);

 // Act and Assert
 assertThrows(OscException.class, () -> new ScheduleCheckOmsProjectValve().invoke(context));
 }

 @Test
 void testInvoke_SwapTableEnabled() throws OscException, InterruptedException {
 // Arrange
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 ProjectStepResult projectStepResult = new ProjectStepResult();
 projectStepResult.setCurrentStep("FULL_VERIFIER");
 projectStepResult.setCurrentStepStatus(TaskStatus.DONE.name());
 projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
 projectStepResult.setFullVerificationProgressPercentage(100);
 projectStepResult.setFullVerificationResultDescription("");
 projectStepResult.setFullTransferProgressPercentage(100);
 projectStepResult.setFullTransferFinishedCount(20);
 projectStepResult.setFullTransferEstimatedCount(20);
 projectStepResult.setSwapTableType(MANUAL_SWAP_TABLE);

 OscValveContext context = new OscValveContext();
 context.setScheduleTask(scheduleTask);
 context.setTaskParameter(taskParameter);
 context.setProjectStepResult(projectStepResult);

 // Act
 ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
 valve.invoke(context);

 // Assert
 assertTrue(context.getScheduleTask().isManualSwapTableEnabled());
 assertTrue(context.getScheduleTask().isManualSwapTableStarted());
 }

 @Test
 void testInvoke_SwapTableDisabled() throws OscException, InterruptedException {
 // Arrange
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

 ProjectStepResult projectStepResult = new ProjectStepResult();
 projectStepResult.setCurrentStep("FULL_VERIFIER");
 projectStepResult.setCurrentStepStatus(TaskStatus.DONE.name());
 projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
 projectStepResult.setFullVerificationProgressPercentage(100);
 projectStepResult.setFullVerificationResultDescription("");
 projectStepResult.setFullTransferProgressPercentage(100);
 projectStepResult.setFullTransferFinishedCount(20);
 projectStepResult.setFullTransferEstimatedCount(20);
 projectStepResult.setSwapTableType(AUTO_SWAP_TABLE);

 OscValveContext context = new OscValveContext();
 context.setScheduleTask(scheduleTask);
 context.setTaskParameter(taskParameter);
 context.setProjectStepResult(projectStepResult);

 // Act
 ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
 valve.invoke(context);

 // Assert
 assertFalse(context.getScheduleTask().isManualSwapTableEnabled());
 assertFalse(context.getScheduleTask().isManualSwapTableStarted());
 }
}