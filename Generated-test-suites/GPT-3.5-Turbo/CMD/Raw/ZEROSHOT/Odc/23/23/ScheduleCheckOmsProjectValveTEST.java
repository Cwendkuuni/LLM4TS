import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.BaseValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ValveContext;
import com.oceanbase.odc.service.onlineschemachange.pipeline.OscValveContext;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        // Setup
        ValveContext valveContext = mock(OscValveContext.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        when(scheduleTaskEntity.getId()).thenReturn("1");
        when(((OscValveContext) valveContext).getScheduleTask()).thenReturn(scheduleTaskEntity);
        when(((OscValveContext) valveContext).getTaskParameter()).thenReturn(taskParameter);
        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);

        // Test
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Verify
        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(scheduleTaskEntity, times(1)).getResultJson();
        verify(scheduleTaskEntity, times(1)).setResultJson(anyString());
        verify(scheduleTaskEntity, times(1)).getProgressPercentage();
        verify(scheduleTaskEntity, times(1)).update(scheduleTaskEntity);
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // Setup
        ValveContext valveContext = mock(OscValveContext.class);
        ProjectStepResult projectStepResult = new ProjectStepResult();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setFullTransferProgressPercentage(100);
        result.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);
        SwapTableType swapTableType = SwapTableType.AUTO;
        when(scheduleTaskEntity.getStatus()).thenReturn("RUNNING");
        when(((OscValveContext) valveContext).getParameter().getSwapTableType()).thenReturn(swapTableType);
        when(scheduleTaskEntity.getResultJson()).thenReturn("{\"manualSwapTableEnabled\": false,\"manualSwapTableStarted\": false}");
        when(((OscValveContext) valveContext).getScheduleTask()).thenReturn(scheduleTaskEntity);

        // Test
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(valveContext, projectStepResult, result, swapTableType, scheduleTaskEntity);

        // Verify
        verify(scheduleTaskEntity, times(1)).setResultJson(anyString());
        verify(scheduleTaskEntity, times(1)).update(scheduleTaskEntity);
        verify(((OscValveContext) valveContext), times(1)).invoke(valveContext);
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        // Setup
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        projectStepResult.setErrorMsg("Precheck failed");

        // Test & Verify
        assertThrows(OscException.class, () -> scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult));
    }

    @Test
    public void testListProjectFullVerifyResult() {
        // Setup
        String projectId = "1";
        String databaseName = "test_db";
        String uid = "123";
        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();

        // Test
        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(response);
        ProjectFullVerifyResultResponse actualResponse = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, databaseName, uid);

        // Verify
        assertNotNull(actualResponse);
        assertEquals(response, actualResponse);
    }

    @Test
    public void testAdaptResult() {
        // Setup
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullTransferEstimatedCount(100);
        projectStepResult.setFullTransferFinishedCount(50);
        projectStepResult.setFullTransferProgressPercentage(50);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationResultDescription("Consistent");

        // Test
        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        // Verify
        assertEquals(100, result.getFullTransferEstimatedCount());
        assertEquals(50, result.getFullTransferFinishedCount());
        assertEquals(50, result.getFullTransferProgressPercentage());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals("Consistent", result.getFullVerificationResultDescription());
    }

    @Test
    public void testGetProjectRequest() {
        // Setup
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam.setUid("123");
        taskParam.setOmsProjectId("1");
        ProjectControlRequest expectedRequest = new ProjectControlRequest();
        expectedRequest.setUid("123");
        expectedRequest.setId("1");

        // Test
        ProjectControlRequest actualRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);

        // Verify
        assertNotNull(actualRequest);
        assertEquals(expectedRequest.getUid(), actualRequest.getUid());
        assertEquals(expectedRequest.getId(), actualRequest.getId());
    }

    @Test
    public void testRecordCurrentProgress() {
        // Setup
        String projectId = "1";
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setCurrentStep("PRE_CHECK");
        result.setPrecheckResultDescription("Precheck Passed");
        result.setFullTransferProgressPercentage(50);
        result.setFullTransferFinishedCount(50);
        result.setFullTransferEstimatedCount(100);

        // Test
        scheduleCheckOmsProjectValve.recordCurrentProgress(projectId, result);

        // No assertion for void method, just verify that there are no exceptions thrown
    }
}