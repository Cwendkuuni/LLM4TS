package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

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
import com.oceanbase.odc.service.onlineschemachange.model.ProjectFullVerifyResultResponse;
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

@Slf4j
public class ScheduleCheckOmsProjectValveTest {
    private ProjectOpenApiService projectOpenApiService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private SwapTableUtil swapTableUtil;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setup() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        swapTableUtil = mock(SwapTableUtil.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
        scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        scheduleCheckOmsProjectValve.setSwapTableUtil(swapTableUtil);
        scheduleCheckOmsProjectValve.setOscTaskCompleteHandler(completeHandler);
    }

    @Test
    public void testInvoke_success() throws Exception {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(getProjectStepVO("Step1"));
        ProjectProgressResponse projectProgressResponse = getProjectProgressResponse();
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = getProjectFullVerifyResultResponse();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        // When
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ValveContext valveContext = Mockito.mock(ValveContext.class);
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Then
        verify(scheduleTaskRepository, times(1)).update(ArgumentMatchers.eq(scheduleTask));
        verifyNoMoreInteractions(projectOpenApiService);

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.DONE, result.getStatus());
        assertEquals(projectId, result.getOmsProjectId());
    }

    @Test
    public void testInvoke_OscException() throws Exception {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(getProjectStepVO("Step1"));
        ProjectProgressResponse projectProgressResponse = getProjectProgressResponse();
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = getProjectFullVerifyResultResponse();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenThrow(new OscException(ErrorCodes.OmsPreCheckFailed, "PreCheck error"));

        // When
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ValveContext valveContext = Mockito.mock(ValveContext.class);
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Then
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(ArgumentMatchers.any(ListProjectFullVerifyResultRequest.class));
        verify(scheduleTaskRepository, times(1)).update(ArgumentMatchers.eq(scheduleTask));
        verifyNoMoreInteractions(projectOpenApiService);

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.FAILED, result.getStatus());
        assertEquals("PreCheck error", result.getErrorMessage());
    }

    @Test
    public void testInvoke_ProjectStepResult_fail() throws Exception {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(getProjectStepVO("Step1"));
        ProjectProgressResponse projectProgressResponse = getProjectProgressResponse();
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = getProjectFullVerifyResultResponse();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        ProjectStepResult projectStepResult = new ProjectStepResult(projectFullVerifyResultResponse, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify());
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        // When
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ValveContext valveContext = Mockito.mock(ValveContext.class);
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Then
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(ArgumentMatchers.any(ListProjectFullVerifyResultRequest.class));
        verify(scheduleTaskRepository, times(1)).update(ArgumentMatchers.eq(scheduleTask));
        verifyNoMoreInteractions(projectOpenApiService);

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.FAILED, result.getStatus());
        assertEquals("Task failed for origin table has inconsistent data with new table", result.getErrorMessage());
    }

    @Test
    public void testInvoke_ProjectStepResult_fullTransfer() throws Exception {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(getProjectStepVO("Step1"), getProjectStepVO("Step2"));
        ProjectProgressResponse projectProgressResponse = getProjectProgressResponse();
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = getProjectFullVerifyResultResponse();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        ProjectStepResult projectStepResult = new ProjectStepResult(projectFullVerifyResultResponse, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify());
        projectStepResult.setCurrentStep(projectSteps.get(1).getName());
        projectStepResult.setCurrentStepStatus(TaskStatus.RUNNING.name());
        projectStepResult.setFullTransferEstimatedCount(10);
        projectStepResult.setFullTransferFinishedCount(5);
        projectStepResult.setFullTransferProgressPercentage(50);

        // When
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ValveContext valveContext = Mockito.mock(ValveContext.class);
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Then
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(ArgumentMatchers.any(ListProjectFullVerifyResultRequest.class));
        verify(scheduleTaskRepository, times(1)).update(ArgumentMatchers.eq(scheduleTask));
        verifyNoMoreInteractions(projectOpenApiService);

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.RUNNING, result.getStatus());
        assertEquals(projectId, result.getOmsProjectId());
        assertEquals(50, result.getFullTransferProgressPercentage());
        assertEquals(5, result.getFullTransferFinishedCount());
        assertEquals(10, result.getFullTransferEstimatedCount());
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParameter.getUid());
        projectRequest.setId(taskParameter.getOmsProjectId());
        return projectRequest;
    }

    private ProjectStepVO getProjectStepVO(String name) {
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setName(name);
        projectStepVO.setStatus(TaskStatus.RUNNING.name());
        return projectStepVO;
    }

    private ProjectProgressResponse getProjectProgressResponse() {
        ProjectProgressResponse projectProgressResponse = new ProjectProgressResponse();
        return projectProgressResponse;
    }

    private ProjectFullVerifyResultResponse getProjectFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        return projectFullVerifyResultResponse;
    }
}