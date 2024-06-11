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

import com.google.common.collect.Lists;
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
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.utils.JsonUtils;

/**
 * Test class for ScheduleCheckOmsProjectValve.
 */
public class ScheduleCheckOmsProjectValveTest {

    private ProjectOpenApiService projectOpenApiService;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
        scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
        scheduleCheckOmsProjectValve.setOscTaskCompleteHandler(completeHandler);
    }

    @Test
    public void testInvoke_normal() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = getProjectSteps();
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectFullVerifyResultResponse fullVerifyResult = getProjectFullVerifyResultResponse();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(fullVerifyResult);

        // When
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleCheckOmsProjectValve.invoke(getValveContext(scheduleTask, taskParameter));

        // Then
        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).update(resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    @Test(expected = OscException.class)
    public void testInvoke_precheckFailed() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = getProjectSteps();
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progress, true, null,
                PrecheckResult.FAILED, "Precheck failed");
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleCheckOmsProjectValve.invoke(getValveContext(scheduleTask, taskParameter));

        // Then
        throw new AssertionError("Should throw OscException");
    }

    @Test(expected = OscException.class)
    public void testInvoke_dataInconsistent() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = getProjectSteps();
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progress, null, null,
                FullVerificationResult.INCONSISTENT, "Data inconsistent");
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleCheckOmsProjectValve.invoke(getValveContext(scheduleTask, taskParameter));

        // Then
        throw new AssertionError("Should throw OscException");
    }

    @Test
    public void testHandleOmsProjectStepResult_enableSwapTable() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult(null, null, null, null,
                FullVerificationResult.CONSISTENT, null);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(getProjectProgressResponse());
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(getProjectFullVerifyResultResponse());
        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        // When
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(getValveContext(scheduleTask, taskParameter),
                projectStepResult, result, SwapTableType.AUTO, scheduleTask);

        // Then
        assertEquals(true, result.isManualSwapTableEnabled());
        assertEquals(true, result.isManualSwapTableStarted());
    }

    @Test
    public void testHandleOmsProjectStepResult_disableSwapTable() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult(null, null, null, null,
                FullVerificationResult.CONSISTENT, null);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(true);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(getProjectProgressResponse());
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(getProjectFullVerifyResultResponse());

        // When
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(getValveContext(scheduleTask, taskParameter),
                projectStepResult, result, SwapTableType.AUTO, scheduleTask);

        // Then
        assertNull(result.getManualSwapTableEnabled());
        assertNull(result.getManualSwapTableStarted());
    }

    private ValveContext getValveContext(ScheduleTaskEntity scheduleTask,
            OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        ValveContext valveContext = mock(ValveContext.class);
        when(valveContext.getScheduleTask()).thenReturn(scheduleTask);
        when(valveContext.getTaskParameter()).thenReturn(taskParameter);
        return valveContext;
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParam.getUid());
        projectRequest.setId(taskParam.getOmsProjectId());
        return projectRequest;
    }

    private List<ProjectStepVO> getProjectSteps() {
        ProjectStepVO projectStepVO1 = new ProjectStepVO();
        ProjectStepVO projectStepVO2 = new ProjectStepVO();
        return Lists.newArrayList(projectStepVO1, projectStepVO2);
    }

    private ProjectProgressResponse getProjectProgressResponse() {
        ProjectProgressResponse projectProgressResponse = new ProjectProgressResponse();
        return projectProgressResponse;
    }

    private ProjectFullVerifyResultResponse getProjectFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setStepName("FULL_TRANSFER");
        projectStepVO.setCurrentState("RUNNING");
        projectStepVO.setCurrentStep(1);
        projectStepVO.setFullTransferProgressPercentage(50);
        projectStepVO.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepVO.setFullVerificationResultDescription("Transfer data successful");
        projectFullVerifyResultResponse.setProjectStepVOList(Lists.newArrayList(projectStepVO));
        return projectFullVerifyResultResponse;
    }
}