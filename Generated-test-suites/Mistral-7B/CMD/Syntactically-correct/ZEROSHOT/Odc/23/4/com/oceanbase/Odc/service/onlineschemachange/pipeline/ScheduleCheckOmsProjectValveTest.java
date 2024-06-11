package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
import com.oceanbase.odc.service.onlineschemachange.testutil.TestData;

public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    private UUID projectId;
    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;
    private ProjectControlRequest projectRequest;
    private List<ProjectStepVO> projectSteps;
    private ProjectProgressResponse projectProgressResponse;
    private ProjectFullVerifyResultResponse projectFullVerifyResultResponse;
    private ProjectStepResult projectStepResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        projectId = UUID.randomUUID();
        taskParameter = TestData.buildOnlineSchemaChangeScheduleTaskParameters();
        scheduleTask = TestData.buildScheduleTaskEntity();
        projectRequest = TestData.buildProjectControlRequest(projectId);
        projectSteps = TestData.buildProjectStepVOS();
        projectProgressResponse = TestData.buildProjectProgressResponse();
        projectFullVerifyResultResponse = TestData.buildProjectFullVerifyResultResponse();
        projectStepResult = TestData.buildProjectStepResult();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);
    }

    @Test
    public void testInvoke() {
        when(scheduleTaskRepository.getOne(anyLong())).thenReturn(scheduleTask);
        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(scheduleTask);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, never()).resumeProject(projectRequest);

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertNotNull(result);
        assertEquals(TaskStatus.RUNNING, result.getStatus());
        assertEquals(0, result.getPrecheckResult().getCode());
        assertEquals(PrecheckResult.PASSED, result.getPrecheckResult());
        assertEquals(FullVerificationResult.UNCHECK, result.getFullVerificationResult());
        assertEquals(0, result.getFullTransferEstimatedCount());
        assertEquals(0, result.getFullTransferFinishedCount());
        assertEquals(0, result.getFullTransferProgressPercentage());
        assertEquals(0, result.getFullVerificationProgressPercentage());
        assertEquals(OmsStepName.UNKNOWN, result.getCurrentStep());
        assertEquals(TaskStatus.UNKNOWN, result.getCurrentStepStatus());
    }

    @Test(expected = OscException.class)
    public void testInvoke_fullVerificationFailed() {
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        projectStepResult.setFullVerificationResultDescription("Data inconsistent");

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);
        doReturn(projectStepResult).when(projectOpenApiService).getProjectStepResult(any(ProjectControlRequest.class));

        when(scheduleTaskRepository.getOne(anyLong())).thenReturn(scheduleTask);
        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(scheduleTask);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(projectOpenApiService, times(1)).getProjectStepResult(projectRequest);

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertNotNull(result);
        assertEquals(TaskStatus.FAILED, result.getStatus());
        assertEquals(ErrorCodes.OmsDataCheckInconsistent.getCode(), result.getPrecheckResult().getCode());
        assertEquals(ErrorCodes.OmsDataCheckInconsistent, result.getPrecheckResult());
        assertEquals(FullVerificationResult.INCONSISTENT, result.getFullVerificationResult());
        assertEquals("Data inconsistent", result.getFullVerificationResultDescription());
        assertEquals(0, result.getFullTransferEstimatedCount());
        assertEquals(0, result.getFullTransferFinishedCount());
        assertEquals(0, result.getFullTransferProgressPercentage());
        assertEquals(0, result.getFullVerificationProgressPercentage());
        assertEquals(OmsStepName.UNKNOWN, result.getCurrentStep());
        assertEquals(TaskStatus.FAILED, result.getCurrentStepStatus());
    }

    @Test(expected = OscException.class)
    public void testInvoke_preCheckFailed() {
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        projectStepResult.setErrorMsg("Precheck failed");

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);
        doReturn(projectStepResult).when(projectOpenApiService).getProjectStepResult(any(ProjectControlRequest.class));

        when(scheduleTaskRepository.getOne(anyLong())).thenReturn(scheduleTask);
        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(scheduleTask);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(projectOpenApiService, times(1)).getProjectStepResult(projectRequest);

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertNotNull(result);
        assertEquals(TaskStatus.FAILED, result.getStatus());
        assertEquals(ErrorCodes.OmsPreCheckFailed.getCode(), result.getPrecheckResult().getCode());
        assertEquals(ErrorCodes.OmsPreCheckFailed, result.getPrecheckResult());
        assertEquals(FullVerificationResult.UNCHECK, result.getFullVerificationResult());
        assertEquals(0, result.getFullTransferEstimatedCount());
        assertEquals(0, result.getFullTransferFinishedCount());
        assertEquals(0, result.getFullTransferProgressPercentage());
        assertEquals(0, result.getFullVerificationProgressPercentage());
        assertEquals(OmsStepName.UNKNOWN, result.getCurrentStep());
        assertEquals(TaskStatus.FAILED, result.getCurrentStepStatus());
    }

    @Test
    public void testInvoke_projectFinished() {
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationResultDescription("Data consistent");
        projectSteps.get(0).setStatus("FINISHED");

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);
        doReturn(projectStepResult).when(projectOpenApiService).getProjectStepResult(any(ProjectControlRequest.class));

        SwapTableType swapTableType = SwapTableType.MANUAL;
        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);

        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        when(scheduleTaskRepository.getOne(anyLong())).thenReturn(scheduleTask);
        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(scheduleTask);

        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(projectOpenApiService, never()).resumeProject(projectRequest);

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertNotNull(result);
        assertEquals(TaskStatus.DONE, result.getStatus());
        assertEquals(0, result.getPrecheckResult().getCode());
        assertEquals(PrecheckResult.PASSED, result.getPrecheckResult());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals("Data consistent", result.getFullVerificationResultDescription());
        assertEquals(0, result.getFullTransferEstimatedCount());
        assertEquals(0, result.getFullTransferFinishedCount());
        assertEquals(100, result.getFullTransferProgressPercentage());
        assertEquals(100, result.getFullVerificationProgressPercentage());
        assertEquals(OmsStepName.FULL_TRANSFER, result.getCurrentStep());
        assertEquals(TaskStatus.DONE, result.getCurrentStepStatus());
        assertTrue(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());

        verify(completeHandler, times(1)).handleTaskCompleted(any(OnlineSchemaChangeScheduleTaskResult.class));
    }
}