package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.model.*;
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
import com.oceanbase.odc.common.util.tableformat.Table;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.util.JsonUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@Slf4j
@Component
public class ScheduleCheckOmsProjectValveTest {

    @Autowired
    private ProjectOpenApiService projectOpenApiService;
    @Autowired
    private OscTaskCompleteHandler completeHandler;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.projectOpenApiService = mock(ProjectOpenApiService.class);
        scheduleCheckOmsProjectValve.completeHandler = mock(OscTaskCompleteHandler.class);
        scheduleCheckOmsProjectValve.scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleCheckOmsProjectValve.onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        SwapTableType swapTableType = SwapTableType.AUTO;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(valveContext, projectStepResult, result, swapTableType, scheduleTask);

        assertTrue(result.isManualSwapTableEnabled());
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPrecheckResult(PrecheckResult.FAILED);
        projectStepResult.setErrorMsg("Precheck failed");

        try {
            scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
            fail("Exception not thrown");
        } catch (OscException e) {
            assertEquals(ErrorCodes.OmsPreCheckFailed, e.getErrorCode());
            assertEquals("Precheck failed", e.getMessage());
        }
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

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);

        assertEquals("123", projectRequest.getUid());
        assertEquals("456", projectRequest.getId());
    }

    @Test
    public void testRecordCurrentProgress() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setCurrentStep("FULL_TRANSFER");
        result.setCurrentStepStatus("SUCCESS");
        result.setPrecheckResultDescription("Precheck Passed");
        result.setFullTransferProgressPercentage(50);
        result.setFullTransferFinishedCount(50);
        result.setFullTransferEstimatedCount(100);
        result.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        result.setFullVerificationResultDescription("Consistent");

        scheduleCheckOmsProjectValve.recordCurrentProgress("789", result);

        // Verify log.info was called with correct parameters
        verify(log).info(anyString());
    }

    @Test
    public void testGetHeader() {
        List<String> header = scheduleCheckOmsProjectValve.getHeader();

        assertEquals(5, header.size());
        assertEquals("ProjectId", header.get(0));
        assertEquals("Step", header.get(1));
        assertEquals("Status", header.get(2));
        assertEquals("Progress", header.get(3));
        assertEquals("Description", header.get(4));
    }
}