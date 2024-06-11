import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class ScheduleCheckOmsProjectValveTest {

 @MockBean
 private ProjectOpenApiService projectOpenApiService;
 @MockBean
 private ScheduleTaskRepository scheduleTaskRepository;
 @MockBean
 private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
 @MockBean
 private OscTaskCompleteHandler completeHandler;

 @Autowired
 private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

 private ProjectControlRequest projectRequest;
 private ScheduleTaskEntity scheduleTask;
 private OnlineSchemaChangeScheduleTaskParameters taskParameter;
 private ListProjectFullVerifyResultRequest listProjectFullVerifyResultRequest;
 private ProjectProgressResponse projectProgressResponse;
 private ProjectStepVO projectStepVO;
 private ProjectFullVerifyResultResponse projectFullVerifyResultResponse;
 private ProjectStepResult projectStepResult;
 private OnlineSchemaChangeScheduleTaskResult result;

 @Before
 public void setup() {
 projectRequest = new ProjectControlRequest();
 projectRequest.setUid("uid");
 projectRequest.setId("projectId");
 scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId("scheduleTaskId");
 scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
 taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 taskParameter.setUid("uid");
 taskParameter.setOmsProjectId("projectId");
 listProjectFullVerifyResultRequest = new ListProjectFullVerifyResultRequest();
 listProjectFullVerifyResultRequest.setProjectId("projectId");
 listProjectFullVerifyResultRequest.setSourceSchemas(new String[]{"databaseName"});
 listProjectFullVerifyResultRequest.setDestSchemas(new String[]{"databaseName"});
 listProjectFullVerifyResultRequest.setStatus(new String[]{"FINISHED", "SUSPEND", "RUNNING"});
 listProjectFullVerifyResultRequest.setPageSize(10);
 listProjectFullVerifyResultRequest.setPageNumber(1);
 projectProgressResponse = new ProjectProgressResponse();
 projectStepVO = new ProjectStepVO();
 projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
 projectStepResult = new ProjectStepResult();
 result = new OnlineSchemaChangeScheduleTaskResult();
 }

 @Test
 public void testInvoke() throws OscException {
 // Given
 when(projectOpenApiService.describeProjectSteps(ArgumentMatchers.eq(projectRequest))).thenReturn(Lists.newArrayList(projectStepVO));
 when(projectOpenApiService.describeProjectProgress(ArgumentMatchers.eq(projectRequest))).thenReturn(projectProgressResponse);
 when(projectOpenApiService.listProjectFullVerifyResult(ArgumentMatchers.eq(listProjectFullVerifyResultRequest))).thenReturn(projectFullVerifyResultResponse);
 when(scheduleTaskRepository.update(ArgumentMatchers.eq(scheduleTask))).thenReturn(scheduleTask);
 // When
 scheduleCheckOmsProjectValve.invoke(new OscValveContext() {
 @Override
 public ScheduleTaskEntity getScheduleTask() {
 return scheduleTask;
 }

 @Override
 public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
 return taskParameter;
 }
 });
 // Then
 // Add assertions here to verify the behavior of the method
 }

 @Test
 public void testHandleOmsProjectStepResult_whenStepDoneAndConsistentOrUncheck_thenSwapTableEnabledAndContinue() throws OscException {
 // Given
 projectStepResult.setCurrentStep(new OmsStepName("PRE_CHECK").toString());
 projectStepResult.setPreCheckResult(PrecheckResult.SUCCESS);
 projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
 projectStepResult.setFullVerificationResultDescription("Consistent");
 result.setManualSwapTableEnabled(false);
 // When
 scheduleCheckOmsProjectValve.handleOmsProjectStepResult(new ValveContext() {
 @Override
 public OnlineSchemaChangeScheduleTaskResult getResult() {
 return result;
 }
 }, projectStepResult, result, SwapTableType.DATABASE, scheduleTask);
 // Then
 Assert.assertTrue(result.isManualSwapTableEnabled());
 }

 @Test(expected = OscException.class)
 public void testHandleOmsProjectStepResult_whenStepDoneAndInconsistent_thenThrowException() throws OscException {
 // Given
 projectStepResult.setCurrentStep(new OmsStepName("FULL_VERIFIER").toString());
 projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
 projectStepResult.setFullVerificationResultDescription("Inconsistent");
 result.setManualSwapTableEnabled(false);
 // When
scheduleCheckOmsProjectValve.handleOmsProjectStepResult(new ValveContext() {
 @Override
 public OnlineSchemaChangeScheduleTaskResult getResult() {
 return result;
 }
 }, projectStepResult, result, SwapTableType.DATABASE, scheduleTask);
 // Then
 // No need to add assertions here as we expect an OscException to be thrown
 }

 @Test
 public void testContinueHandleProjectStepResult_whenPreCheckFailed_thenThrowException() throws OscException {
 // Given
 projectStepResult.setCurrentStep(new OmsStepName("PRE_CHECK").toString());
 projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
 // When
 scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
 // Then
 throw new AssertionError("Expected OscException to be thrown but none was");
 }

 @Test
 public void testListProjectFullVerifyResult_whenProjectHasConsistentFullVerificationResult_thenReturnConsistentResponse() {
 // Given
 projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);
 // When
 OnlineSchemaChangeScheduleTaskResult actualResult = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(taskParameter.getOmsProjectId(),
 taskParameter.getDatabaseName(), taskParameter.getUid());
 // Then
 Assert.assertEquals(FullVerificationResult.CONSISTENT, actualResult.getFullVerificationResult());
 }

 @Test
 public void testListProjectFullVerifyResult_whenProjectHasInconsistentFullVerificationResult_thenReturnInconsistentResponse() {
 // Given
 projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
 // When
 OnlineSchemaChangeScheduleTaskResult actualResult = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(taskParameter.getOmsProjectId(),
 taskParameter.getDatabaseName(), taskParameter.getUid());
 // Then
 Assert.assertEquals(FullVerificationResult.INCONSISTENT, actualResult.getFullVerificationResult());
 }
}