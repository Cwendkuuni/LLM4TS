package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
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
import com.oceanbase.odc.service.onlineschemachange.utils.TestUtil;

/**
 * Test class for ScheduleCheckOmsProjectValve
 */
public class ScheduleCheckOmsProjectValveTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

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

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;

    @Before
    public void setup() {
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setSwapTableType(SwapTableType.SWAP_TABLE_AT_OMS);
        taskParameter.setUid(UUID.randomUUID().toString());
        scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus(TaskStatus.CREATED.toString());
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
    }

    @Test
    public void testInvoke_success() {
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(getProjectProgress());

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(getProjectFullVerifyResult());

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
    }

    @Test(expected = OscException.class)
    public void testInvoke_preCheckFailed() {
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(getProjectProgress());
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(getProjectFullVerifyResultWithFailedPrecheck());

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
    }

    @Test(expected = OscException.class)
    public void testInvoke_dataInconsistent() {
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(getProjectProgressWithInconsistentData());

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
    }

    @Test
    public void testHandleOmsProjectStepResult_swapTableEnabled() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = getProjectStepResultWithSwapTableEnabled();

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(getProjectProgress());
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(getProjectFullVerifyResult());

        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult, result);

        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        assertEquals(true, result.isManualSwapTableEnabled());
        assertEquals(true, result.isManualSwapTableStarted());
    }

    @Test
    public void testHandleOmsProjectStepResult_swapTableDisabled() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = getProjectStepResultWithSwapTableDisabled();

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(getProjectProgress());
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(getProjectFullVerifyResult());

        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult, result);

        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        assertEquals(false, result.isManualSwapTableEnabled());
        assertEquals(false, result.isManualSwapTableStarted());
    }

    @Test
    public void testListProjectFullVerifyResult_success() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        List<ProjectStepVO> projectSteps = getProjectSteps();
        projectFullVerifyResultResponse.setSteps(projectSteps.toArray(new ProjectStepVO[0]));

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        OnlineSchemaChangeScheduleTaskResult result = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(taskParameter);

        assertEquals(projectSteps, result.getFullVerificationSteps());
    }

    private ProjectControlRequest getProjectRequest() {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(taskParameter.getOmsProjectId());
        projectRequest.setUid(taskParameter.getUid());
        return projectRequest;
    }

    private List<ProjectStepVO> getProjectSteps() {
        ProjectStepVO projectStepVO1 = new ProjectStepVO();
        projectStepVO1.setName("preCheck");
        ProjectStepVO projectStepVO2 = new ProjectStepVO();
        projectStepVO2.setName("fullTransfer");
        ProjectStepVO projectStepVO3 = new ProjectStepVO();
        projectStepVO3.setName("fullVerify");

        return Lists.newArrayList(projectStepVO1, projectStepVO2, projectStepVO3);
    }

    private ProjectProgressResponse getProjectProgress() {
        ProjectProgressResponse projectProgressResponse = new ProjectProgressResponse();
        projectProgressResponse.setCurrentStep("fullTransfer");
        projectProgressResponse.setCurrentStepStatus("RUNNING");
        return projectProgressResponse;
    }

    private ProjectProgressResponse getProjectProgressWithInconsistentData() {
        ProjectProgressResponse projectProgressResponse = new ProjectProgressResponse();
        projectProgressResponse.setCurrentStep("fullVerify");
        projectProgressResponse.setCurrentStepStatus("FAILED");
        projectProgressResponse.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        return projectProgressResponse;
    }

    private ProjectFullVerifyResultResponse getProjectFullVerifyResult() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectFullVerifyResultResponse.setSteps(getProjectSteps().toArray(new ProjectStepVO[0]));
        return projectFullVerifyResultResponse;
    }

    private ProjectFullVerifyResultResponse getProjectFullVerifyResultWithFailedPrecheck() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectFullVerifyResultResponse.setSteps(getProjectSteps().toArray(new ProjectStepVO[0]));
        projectFullVerifyResultResponse.getSteps()[0].setStatus("FAILED");
        projectFullVerifyResultResponse.getSteps()[0].setPrecheckResult(PrecheckResult.FAILED);
        return projectFullVerifyResultResponse;
    }

    private ProjectStepResult getProjectStepResultWithSwapTableEnabled() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullTransferEstimatedCount(10);
        projectStepResult.setFullTransferFinishedCount(5);
        projectStepResult.setFullTransferProgressPercentage(50);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationProgressPercentage(100);
        projectStepResult.setCurrentStep("fullTransfer");
        projectStepResult.setCurrentStepStatus("RUNNING");
        projectStepResult.setPrecheckResult(PrecheckResult.PASSED);
        return projectStepResult;
    }

    private ProjectStepResult getProjectStepResultWithSwapTableDisabled() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullTransferEstimatedCount(10);
        projectStepResult.setFullTransferFinishedCount(5);
        projectStepResult.setFullTransferProgressPercentage(50);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationProgressPercentage(100);
        projectStepResult.setCurrentStep("fullTransfer");
        projectStepResult.setCurrentStepStatus("RUNNING");
        projectStepResult.setPrecheckResult(PrecheckResult.FAILED);
        return projectStepResult;
    }
}