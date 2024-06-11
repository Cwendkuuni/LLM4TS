
package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;

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
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Test
    public void testHandleOmsProjectStepResult() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        SwapTableType swapTableType = SwapTableType.AUTO;

        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, projectStepResult, result, swapTableType, scheduleTask);

        assertNotNull(result);
    }

    @Test
    public void testContinueHandleProjectStepResultPreCheckFailed() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        OscException exception = null;
        try {
            scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
        } catch (OscException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorCodes.OmsPreCheckFailed, exception.getErrorCode());
    }

    @Test
    public void testContinueHandleProjectStepResultInconsistentDataCheck() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        OscException exception = null;
        try {
            scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
        } catch (OscException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorCodes.OmsDataCheckInconsistent, exception.getErrorCode());

    }

    @Test
    public void testAdaptResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullTransferEstimatedCount(100);
        projectStepResult.setFullTransferFinishedCount(50);
        projectStepResult.setFullTransferProgressPercentage(50);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationResultDescription("Consistent");
        projectStepResult.setFullVerificationProgressPercentage(100);
        projectStepResult.setCurrentStep("FULL_TRANSFER");
        projectStepResult.setCurrentStepStatus("SUCCESS");
        projectStepResult.setPreCheckResult(PrecheckResult.PASSED);

        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        assertEquals(100, result.getFullTransferEstimatedCount());
        assertEquals(50, result.getFullTransferFinishedCount());
        assertEquals(50, result.getFullTransferProgressPercentage());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals("Consistent", result.getFullVerificationResultDescription());
        assertEquals(100, result.getFullVerificationProgressPercentage());
        assertEquals("FULL_TRANSFER", result.getCurrentStep());
        assertEquals("SUCCESS", result.getCurrentStepStatus());
        assertEquals(PrecheckResult.PASSED, result.getPrecheckResult());
    }

    @Test
    public void testGetProjectRequest() {
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam.setUid("123");
        taskParam.setOmsProjectId("456");

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid("123");
        projectRequest.setId("456");

        ProjectControlRequest actualRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);

        assertEquals(projectRequest.getUid(), actualRequest.getUid());
        assertEquals(projectRequest.getId(), actualRequest.getId());
    }

    @Test
    public void testRecordCurrentProgress() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setCurrentStep("PRE_CHECK");
        result.setPrecheckResultDescription("Precheck successful");
        result.setFullTransferFinishedCount(50);
        result.setFullTransferEstimatedCount(100);
        result.setFullTransferProgressPercentage(50);
        result.setFullVerificationResultDescription("Consistent");
        result.setFullVerificationProgressPercentage(100);

        scheduleCheckOmsProjectValve.recordCurrentProgress("789", result);

        assertNotNull(result);
    }

    @Test
    public void testGetHeader() {
        List<String> expectedHeader = Lists.newArrayList("ProjectId", "Step", "Status", "Progress", "Description");

        List<String> actualHeader = scheduleCheckOmsProjectValve.getHeader();

        assertEquals(expectedHeader, actualHeader);
    }

}
