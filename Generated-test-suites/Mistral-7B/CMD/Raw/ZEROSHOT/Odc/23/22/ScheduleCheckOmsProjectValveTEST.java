package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.model.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.model.ProjectStepVO;
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

/**
 * Unit test for {@link ScheduleCheckOmsProjectValve} class.
 */
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Mock
    private DefaultTableFactory defaultTableFactory;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvokeWithSuccess() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameters = createTaskParameters();
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        ProjectControlRequest projectRequest = createProjectRequest(taskParameters);
        ProjectProgressResponse progress = createProjectProgressResponse();
        ProjectFullVerifyResultResponse fullVerifyResultResponse = createProjectFullVerifyResultResponse();
        ProjectStepVO projectStepVO = createProjectStepVO();
        ProjectStepResult projectStepResult = createProjectStepResult(progress, projectSteps(projectStepVO));

        when(projectOpenApiService.describeProjectSteps(projectRequest))
                .thenReturn(projectSteps(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(fullVerifyResultResponse);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());

        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult(taskParameters);
        adaptResult(expectedResult, projectStepResult);

        assertEquals("Expected result is not the same as actual result", expectedResult,
                JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class));
        verify(scheduleTaskRepository, times(1)).update(scheduleTask);
    }

    @Test
    public void testInvokeWithError() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameters = createTaskParameters();
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        ProjectControlRequest projectRequest = createProjectRequest(taskParameters);
        ProjectProgressResponse progress = createProjectProgressResponse();
        ProjectStepVO projectStepVO = createProjectStepVO();
        ProjectStepResult projectStepResult = createProjectStepResult(progress, projectSteps(projectStepVO));

        when(projectOpenApiService.describeProjectSteps(projectRequest))
                .thenReturn(projectSteps(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenThrow(new OscException(ErrorCodes.OmsPreCheckFailed, "Pre check failed"));

        try {
            scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));
            fail("Expected Exception was not thrown");
        } catch (OscException e) {
            assertEquals("Pre check failed", e.getMessage());
        }

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());
        verify(scheduleTaskRepository, never()).update(scheduleTask);
    }

    @Test
    public void testHandleOmsProjectStepResultWithSuccess() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameters = createTaskParameters();
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        ProjectControlRequest projectRequest = createProjectRequest(taskParameters);
        ProjectProgressResponse progress = createProjectProgressResponse();
        ProjectStepVO projectStepVO = createProjectStepVO();
        ProjectStepResult projectStepResult = createProjectStepResult(progress, projectSteps(projectStepVO));
        SwapTableType swapTableType = SwapTableType.SWAP_TABLE_RENAME;

        when(projectOpenApiService.describeProjectSteps(projectRequest))
                .thenReturn(projectSteps(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(createProjectFullVerifyResultResponse());

        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult,
                new OnlineSchemaChangeScheduleTaskResult(taskParameters), swapTableType, scheduleTask);

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());
        verify(scheduleTaskRepository, times(1)).update(scheduleTask);

        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult(taskParameters);
        adaptResult(expectedResult, projectStepResult);
        assertEquals("Expected result is not the same as actual result", expectedResult,
                JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class));

        boolean isSwapTableEnabled = SwapTableUtil.isSwapTableEnable(swapTableType,
                scheduleTask.getStatus(), expectedResult.getFullTransferProgressPercentage(),
                expectedResult.getFullVerificationResult(), expectedResult.isManualSwapTableStarted());
        assertTrue("Swap table should be enabled", isSwapTableEnabled);
    }

    @Test
    public void testHandleOmsProjectStepResultWithError() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameters = createTaskParameters();
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        ProjectControlRequest projectRequest = createProjectRequest(taskParameters);
        ProjectProgressResponse progress = createProjectProgressResponse();
        ProjectStepVO projectStepVO = createProjectStepVO();
        ProjectStepResult projectStepResult = createProjectStepResult(progress, projectSteps(projectStepVO));
        SwapTableType swapTableType = SwapTableType.SWAP_TABLE_RENAME;

        when(projectOpenApiService.describeProjectSteps(projectRequest))
                .thenReturn(projectSteps(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenThrow(new OscException(ErrorCodes.OmsPreCheckFailed, "Pre check failed"));

        try {
            scheduleCheckOmsProjectValve.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult,
                    new OnlineSchemaChangeScheduleTaskResult(taskParameters), swapTableType, scheduleTask);
            fail("Expected Exception was not thrown");
        } catch (OscException e) {
            assertEquals("Pre check failed", e.getMessage());
        }

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());
        verify(scheduleTaskRepository, never()).update(scheduleTask);
    }

    private OnlineSchemaChangeScheduleTaskParameters createTaskParameters() {
        return new OnlineSchemaChangeScheduleTaskParameters();
    }

    private ScheduleTaskEntity createScheduleTaskEntity() {
        return new ScheduleTaskEntity();
    }

    private ProjectControlRequest createProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParameters) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(UUID.randomUUID().toString());
        projectRequest.setId(taskParameters.getOmsProjectId());
        return projectRequest;
    }

    private ProjectProgressResponse createProjectProgressResponse() {
        return new ProjectProgressResponse();
    }

    private List<ProjectStepVO> projectSteps(ProjectStepVO projectStepVO) {
        return Lists.newArrayList(projectStepVO);
    }

    private ProjectStepVO createProjectStepVO() {
        return new ProjectStepVO();
    }

    private ProjectStepResult createProjectStepResult(ProjectProgressResponse progress, List<ProjectStepVO> projectSteps) {
        return new ProjectStepResult(progress, projectSteps, onlineSchemaChangeProperties.isEnableFullVerify());
    }

    private ProjectFullVerifyResultResponse createProjectFullVerifyResultResponse() {
        return new ProjectFullVerifyResultResponse();
    }

    private void adaptResult(OnlineSchemaChangeScheduleTaskResult result, ProjectStepResult projectStepResult) {
        result.setFullTransferEstimatedCount(projectStepResult.getFullTransferEstimatedCount());
        result.setFullTransferFinishedCount(projectStepResult.getFullTransferFinishedCount());
        result.setFullTransferProgressPercentage(projectStepResult.getFullTransferProgressPercentage());
        result.setFullVerificationResult(projectStepResult.getFullVerificationResult());
        result.setFullVerificationResultDescription(projectStepResult.getFullVerificationResultDescription());
        result.setFullVerificationProgressPercentage(projectStepResult.getFullVerificationProgressPercentage());
        result.setCurrentStep(projectStepResult.getCurrentStep());
        result.setCurrentStepStatus(projectStepResult.getCurrentStepStatus());
        result.setPrecheckResult(projectStepResult.getPreCheckResult());
    }
}