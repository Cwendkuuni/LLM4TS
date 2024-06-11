import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Component
public class ScheduleCheckOmsProjectValveTest {

 @Autowired
 private ProjectOpenApiService projectOpenApiService;
 @Autowired
 private OscTaskCompleteHandler completeHandler;
 @Autowired
 private ScheduleTaskRepository scheduleTaskRepository;

 @InjectMocks
 private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testInvoke_NoError() {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 projectOpenApiService = mock(ProjectOpenApiService.class);
 when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
 .thenReturn(getTestProjectSteps());
 when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
 .thenReturn(getTestProjectProgressResponse());
 when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
 .thenReturn(getTestProjectFullVerifyResultResponse());

 // When
 scheduleCheckOmsProjectValve.invoke(new ValveContext());

 // Then
 Assert.assertNull(scheduleTask.getError());
 }

 @Test(expected = OscException.class)
 public void testInvoke_PrecheckError() {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 projectOpenApiService = mock(ProjectOpenApiService.class);
 when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
 .thenReturn(getTestProjectSteps());
 when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
 .thenReturn(getTestProjectProgressResponse());
 when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
 .thenThrow(new OscException("Error message"));

 // When
 scheduleCheckOmsProjectValve.invoke(new ValveContext());

 // Then
 Assert.assertEquals("Error message", scheduleTask.getError());
 }

 @Test(expected = OscException.class)
 public void testInvoke_DataCheckInconsistent() {
 // Given
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 projectOpenApiService = mock(ProjectOpenApiService.class);
 when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
 .thenReturn(getTestProjectSteps());
 when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
 .thenReturn(getTestProjectProgressResponseWithInconsistentData());
 when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
 .thenReturn(getTestProjectFullVerifyResultResponse());

 // When
 scheduleCheckOmsProjectValve.invoke(new ValveContext());

 // Then
 Assert.assertEquals(ErrorCodes.OmsDataCheckInconsistent, scheduleTask.getError());
 }

 // Add more test cases for other methods and edge cases here

 private List<ProjectStepVO> getTestProjectSteps() {
 return Lists.newArrayList(getTestProjectStep());
 }

 private ProjectProgressResponse getTestProjectProgressResponse() {
 ProjectProgressResponse response = new ProjectProgressResponse();
 response.setCurrentStep(OmsStepName.FULL_TRANSFER.name());
 response.setCurrentStepStatus("RUNNING");
 response.setFullTransferEstimatedCount(10);
 response.setFullTransferFinishedCount(5);
 response.setFullTransferProgressPercentage(50);
 return response;
 }

 private ProjectProgressResponse getTestProjectProgressResponseWithInconsistentData() {
 ProjectProgressResponse response = new ProjectProgressResponse();
 response.setCurrentStep(OmsStepName.FULL_VERIFIER.name());
 response.setCurrentStepStatus("FAILED");
 response.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
 response.setFullVerificationResultDescription("Data inconsistent");
 return response;
 }

 private ProjectFullVerifyResultResponse getTestProjectFullVerifyResultResponse() {
 ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
 response.setFullVerificationResult(FullVerificationResult.CONSISTENT);
 return response;
 }

 private ProjectFullVerifyResultResponse getTestProjectFullVerifyResultResponseWithError() {
 ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
 response.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
 response.setError("Error message");
 return response;
 }

 private ProjectStepVO getTestProjectStep() {
 ProjectStepVO step = new ProjectStepVO();
 step.setName(OmsStepName.PRE_CHECK.name());
 return step;
 }
}