package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.model.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;

/**
 * Test class for ScheduleCheckOmsProjectValve
 */
public class ScheduleCheckOmsProjectValveTest {
    @Mock
    private ProjectOpenApiService projectOpenApiService;
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
    public void testInvoke() throws OscException {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTestTaskParameter();
        ProjectControlRequest projectRequest = getTestProjectRequest(taskParameter);
        ProjectProgressResponse progress = getTestProjectProgressResponse();
        ProjectStepVO projectStepVO = getTestProjectStepVO();
        ProjectFullVerifyResultResponse fullVerifyResultResponse = getTestProjectFullVerifyResultResponse();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        ScheduleTaskEntity scheduleTask = getTestScheduleTaskEntity();
        when(scheduleTaskRepository.getById(anyLong())).thenReturn(scheduleTask);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).update(resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();

        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals(TaskStatus.DONE, result.getCurrentStepStatus());
        assertNull(result.getErrorMessage());
    }

    @Test(expected = OscException.class)
    public void testInvokeWithPrecheckFailed() throws OscException {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTestTaskParameter();
        ProjectControlRequest projectRequest = getTestProjectRequest(taskParameter);
        ProjectProgressResponse progress = getTestProjectProgressResponse();
        ProjectStepVO projectStepVO = getTestProjectStepVO();
        ProjectStepResult projectStepResult = new ProjectStepResult(projectStepVO, FullVerificationResult.INCONSISTENT, "Error Message");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));
    }

    @Test(expected = OscException.class)
    public void testInvokeWithFullVerificationFailed() throws OscException {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTestTaskParameter();
        ProjectControlRequest projectRequest = getTestProjectRequest(taskParameter);
        ProjectProgressResponse progress = getTestProjectProgressResponse();
        ProjectStepVO projectStepVO = getTestProjectStepVO();
        ProjectFullVerifyResultResponse fullVerifyResultResponse = getTestProjectFullVerifyResultResponseWithError();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));
    }

    @Test
    public void testInvokeWithFullVerificationSkipped() throws OscException {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTestTaskParameter();
        ProjectControlRequest projectRequest = getTestProjectRequest(taskParameter);
        ProjectProgressResponse progress = getTestProjectProgressResponse();
        ProjectStepVO projectStepVO = getTestProjectStepVO();
        ProjectStepResult projectStepResult = new ProjectStepResult(projectStepVO, FullVerificationResult.SKIPPED, null);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        OnlineSchemaChangeScheduleTaskResult result = getTestScheduleTaskResult();
        result.setCurrentStepStatus(TaskStatus.DONE);
        result.setFullVerificationResult(FullVerificationResult.SKIPPED);

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).update(resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult testResult = resultCaptor.getValue();
        assertEquals(result, testResult);
    }

    @Test
    public void testInvokeWithFullVerificationInProgress() throws OscException {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTestTaskParameter();
        ProjectControlRequest projectRequest = getTestProjectRequest(taskParameter);
        ProjectProgressResponse progress = getTestProjectProgressResponseWithFullVerificationInProgress();
        ProjectStepVO projectStepVO = getTestProjectStepVO();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        OnlineSchemaChangeScheduleTaskResult result = getTestScheduleTaskResult();
        result.setCurrentStepStatus(TaskStatus.RUNNING);

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).update(resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult testResult = resultCaptor.getValue();
        assertEquals(result, testResult);
    }

    @Test
    public void testInvokeWithManualSwapTableEnabled() throws OscException {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTestTaskParameter();
        ProjectControlRequest projectRequest = getTestProjectRequest(taskParameter);
        ProjectProgressResponse progress = getTestProjectProgressResponse();
        ProjectStepVO projectStepVO = getTestProjectStepVO();
        ProjectStepResult projectStepResult = new ProjectStepResult(projectStepVO, FullVerificationResult.CONSISTENT, null);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        OnlineSchemaChangeScheduleTaskResult result = getTestScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).update(resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult testResult = resultCaptor.getValue();
        assertEquals(result, testResult);
    }

    @Test
    public void testInvokeWithManualSwapTableStarted() throws OscException {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTestTaskParameter();
        ProjectControlRequest projectRequest = getTestProjectRequest(taskParameter);
        ProjectProgressResponse progress = getTestProjectProgressResponse();
        ProjectStepVO projectStepVO = getTestProjectStepVO();
        ProjectStepResult projectStepResult = new ProjectStepResult(projectStepVO, FullVerificationResult.CONSISTENT, null);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        OnlineSchemaChangeScheduleTaskResult result = getTestScheduleTaskResult();
        result.setManualSwapTableStarted(true);

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).update(resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult testResult = resultCaptor.getValue();
        assertEquals(result, testResult);
    }

    @Test
    public void testInvokeWithNoProjectStep() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTestTaskParameter();
        ProjectControlRequest projectRequest = getTestProjectRequest(taskParameter);
        ProjectProgressResponse progress = getTestProjectProgressResponse();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(null);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        try {
            scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));
            fail("Expected OscException to be thrown");
        } catch (OscException e) {
            assertEquals(ErrorCodes.OmsProjectStepNotFound, e.getErrorCode());
        }
    }

    private OnlineSchemaChangeScheduleTaskParameters getTestTaskParameter() {
        return new OnlineSchemaChangeScheduleTaskParameters();
    }

    private ProjectControlRequest getTestProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(UUID.randomUUID().toString());
        projectRequest.setId(taskParameter.getOmsProjectId());
        return projectRequest;
    }

    private ProjectProgressResponse getTestProjectProgressResponse() {
        return new ProjectProgressResponse();
    }

    private ProjectStepVO getTestProjectStepVO() {
        return new ProjectStepVO();
    }

    private ProjectFullVerifyResultResponse getTestProjectFullVerifyResultResponse() {
        return new ProjectFullVerifyResultResponse();
    }

    private ProjectFullVerifyResultResponse getTestProjectFullVerifyResultResponseWithError() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectFullVerifyResultResponse.setErrorMsg("Error Message");
        return projectFullVerifyResultResponse;
    }

    private OnlineSchemaChangeScheduleTaskResult getTestScheduleTaskResult() {
        return new OnlineSchemaChangeScheduleTaskResult();
    }

    private ScheduleTaskEntity getTestScheduleTaskEntity() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson("");
        scheduleTaskEntity.setStatus(TaskStatus.CREATED.name());
        return scheduleTaskEntity;
    }

    private ProjectProgressResponse getTestProjectProgressResponseWithFullVerificationInProgress() {
        ProjectProgressResponse projectProgressResponse = new ProjectProgressResponse();
        projectProgressResponse.setProjectStatus(TaskStatus.RUNNING.name());
        return projectProgressResponse;
    }
}