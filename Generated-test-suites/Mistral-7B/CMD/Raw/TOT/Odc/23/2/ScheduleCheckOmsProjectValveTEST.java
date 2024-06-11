package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.AutowiredMock;
import org.springframework.stereotype.ComponentMock;

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
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

@Component
public class ScheduleCheckOmsProjectValveTest {

    @ComponentMock
    private ProjectOpenApiService projectOpenApiService;
    @AutowiredMock
    private OscTaskCompleteHandler completeHandler;
    @AutowiredMock
    private ScheduleTaskRepository scheduleTaskRepository;
    @AutowiredMock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    private ScheduleCheckOmsProjectValve valve;
    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;
    private ProjectStepResult projectStepResult;
    private ProjectFullVerifyResultResponse projectFullVerifyResultResponse;
    private ProjectProgressResponse projectProgressResponse;
    private ProjectStepVO projectStepVO;

    @Before
    public void setup() {
        valve = new ScheduleCheckOmsProjectValve();
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        scheduleTask = new ScheduleTaskEntity();
        projectStepResult = Mockito.mock(ProjectStepResult.class);
        projectFullVerifyResultResponse = Mockito.mock(ProjectFullVerifyResultResponse.class);
        projectProgressResponse = Mockito.mock(ProjectProgressResponse.class);
        projectStepVO = new ProjectStepVO();
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);
        when(projectProgressResponse.getProgress()).thenReturn(50);
        when(projectStepVO.getCurrentStep()).thenReturn("FULL_TRANSFER");
        when(projectStepVO.getStatus()).thenReturn(TaskStatus.DONE.toString());
        when(projectStepVO.getProjectId()).thenReturn(taskParameter.getOmsProjectId());
        when(projectStepVO.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);
        when(projectStepVO.getFullTransferProgressPercentage()).thenReturn(50);
        when(projectStepVO.getFullVerificationProgressPercentage()).thenReturn(75);
    }

    @Test
    public void testInvokeWithSuccess() throws Exception {
        taskParameter.setUid(UUID.randomUUID().toString());
        when(projectFullVerifyResultResponse.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);
        taskParameter.setSwapTableType(SwapTableType.NO_SWAP);

        valve.invoke(Mockito.mock(ValveContext.class));
        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(eq(taskParameter.getOmsProjectId()),
                eq(taskParameter.getDatabaseName()), eq(taskParameter.getUid()));

        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        adaptResult(expectedResult, projectStepResult);
        resultEquals(expectedResult, valve.getResult());
    }

    @Test
    public void testInvokeWithFailedPrecheckResult() throws Exception {
        taskParameter.setUid(UUID.randomUUID().toString());
        when(projectStepVO.getPreCheckResult()).thenReturn(PrecheckResult.FAILED);
        projectStepVO.setCurrentStep("PRE_CHECK");

        try {
            valve.invoke(Mockito.mock(ValveContext.class));
            fail("test case should throw exception");
        } catch (OscException e) {
            // expected
            assertEquals(ErrorCodes.OmsPreCheckFailed, e.getErrorCode());
        }

        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, never()).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(eq(taskParameter.getOmsProjectId()),
                eq(taskParameter.getDatabaseName()), eq(taskParameter.getUid()));
    }

    @Test
    public void testInvokeWithFailedFullVerificationResult() throws Exception {
        taskParameter.setUid(UUID.randomUUID().toString());
        when(projectStepVO.getFullVerificationResult()).thenReturn(FullVerificationResult.INCONSISTENT);

        try {
            valve.invoke(Mockito.mock(ValveContext.class));
            fail("test case should throw exception");
        } catch (OscException e) {
            // expected
            assertEquals(ErrorCodes.OmsDataCheckInconsistent, e.getErrorCode());
        }

        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(eq(taskParameter.getOmsProjectId()),
                eq(taskParameter.getDatabaseName()), eq(taskParameter.getUid()));
    }

    @Test
    public void testInvokeWithFailedFullVerificationResultAndPrecheckResult() throws Exception {
        taskParameter.setUid(UUID.randomUUID().toString());
        when(projectStepVO.getPreCheckResult()).thenReturn(PrecheckResult.FAILED);
        projectStepVO.setCurrentStep("FULL_TRANSFER");

        try {
            valve.invoke(Mockito.mock(ValveContext.class));
            fail("test case should throw exception");
        } catch (OscException e) {
            // expected
            assertEquals(ErrorCodes.OmsPreCheckFailed, e.getErrorCode());
        }

        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, never()).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(eq(taskParameter.getOmsProjectId()),
                eq(taskParameter.getDatabaseName()), eq(taskParameter.getUid()));
    }

    @Test
    public void testInvokeWithFailedFullVerificationResultAndSwapTableEnable() throws Exception {
        taskParameter.setUid(UUID.randomUUID().toString());
        taskParameter.setSwapTableType(SwapTableType.SWAP);

        when(projectStepVO.getFullVerificationResult()).thenReturn(FullVerificationResult.INCONSISTENT);

        valve.invoke(Mockito.mock(ValveContext.class));

        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(eq(taskParameter.getOmsProjectId()),
                eq(taskParameter.getDatabaseName()), eq(taskParameter.getUid()));

        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        expectedResult.setManualSwapTableEnabled(false);
        resultEquals(expectedResult, valve.getResult());
    }

    @Test
    public void testInvokeWithSuccessAndSwapTableEnable() throws Exception {
        taskParameter.setUid(UUID.randomUUID().toString());
        taskParameter.setSwapTableType(SwapTableType.SWAP);

        when(projectStepVO.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);

        valve.invoke(Mockito.mock(ValveContext.class));

        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectProgress(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(eq(taskParameter.getOmsProjectId()),
                eq(taskParameter.getDatabaseName()), eq(taskParameter.getUid()));

        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        expectedResult.setManualSwapTableEnabled(true);
        resultEquals(expectedResult, valve.getResult());
    }

    private void resultEquals(OnlineSchemaChangeScheduleTaskResult expected, OnlineSchemaChangeScheduleTaskResult actual) {
        assertEquals(expected.getFullTransferEstimatedCount(), actual.getFullTransferEstimatedCount());
        assertEquals(expected.getFullTransferFinishedCount(), actual.getFullTransferFinishedCount());
        assertEquals(expected.getFullTransferProgressPercentage(), actual.getFullTransferProgressPercentage());
        assertEquals(expected.getFullVerificationResult(), actual.getFullVerificationResult());
        assertEquals(expected.getFullVerificationResultDescription(), actual.getFullVerificationResultDescription());
        assertEquals(expected.getFullVerificationProgressPercentage(), actual.getFullVerificationProgressPercentage());
        assertEquals(expected.getCurrentStep(), actual.getCurrentStep());
        assertEquals(expected.getCurrentStepStatus(), actual.getCurrentStepStatus());
        assertEquals(expected.getPrecheckResult(), actual.getPrecheckResult());
        assertEquals(expected.getPrecheckResultDescription(), actual.getPrecheckResultDescription());
        assertEquals(expected.isManualSwapTableEnabled(), actual.isManualSwapTableEnabled());
        assertEquals(expected.isManualSwapTableStarted(), actual.isManualSwapTableStarted());
    }
}