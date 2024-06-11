package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
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
import com.oceanbase.odc.service.onlineschemachange.utils.OnlineSchemaChangeTestUtils;

class ScheduleCheckOmsProjectValveTest {

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

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_whenProjectStepsAndProgressIsSuccess_thenUpdateScheduleTask() {
        // given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = OnlineSchemaChangeTestUtils
                .getOnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = OnlineSchemaChangeTestUtils.getScheduleTaskEntity(projectId);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameters);
        List<ProjectStepVO> projectSteps = OnlineSchemaChangeTestUtils.getProjectSteps();
        ProjectProgressResponse progress = OnlineSchemaChangeTestUtils.getProjectProgressResponse();
        ProjectFullVerifyResultResponse fullVerifyResult = OnlineSchemaChangeTestUtils.getProjectFullVerifyResult();
        ProjectStepResult projectStepResult = OnlineSchemaChangeTestUtils.getProjectStepResult(projectSteps,
                progress, fullVerifyResult);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResult);

        // when
        scheduleCheckOmsProjectValve.invoke(null);

        // then
        verify(scheduleTaskRepository).update(scheduleTask);
        verifyNoMoreInteractions(projectOpenApiService);

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        Assertions.assertEquals(projectId, result.getOmsProjectId());
        Assertions.assertEquals(projectStepResult.getFullVerificationResult(), result.getFullVerificationResult());
        Assertions.assertEquals(projectStepResult.getFullVerificationResultDescription(),
                result.getFullVerificationResultDescription());
        Assertions.assertEquals(projectStepResult.getFullTransferEstimatedCount(),
                result.getFullTransferEstimatedCount());
        Assertions.assertEquals(projectStepResult.getFullTransferFinishedCount(),
                result.getFullTransferFinishedCount());
        Assertions.assertEquals(projectStepResult.getFullTransferProgressPercentage(),
                result.getFullTransferProgressPercentage());
        Assertions.assertEquals(projectStepResult.getCurrentStep(), result.getCurrentStep());
        Assertions.assertEquals(projectStepResult.getCurrentStepStatus(), result.getCurrentStepStatus());
        Assertions.assertEquals(projectStepResult.getPrecheckResult(), result.getPrecheckResult());
        Assertions.assertEquals(projectStepResult.getPrecheckResultDescription(),
                result.getPrecheckResultDescription());
    }

    @Test
    public void testInvoke_whenFullVerificationResultIsInconsistent_thenThrowOscException() {
        // given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = OnlineSchemaChangeTestUtils
                .getOnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = OnlineSchemaChangeTestUtils.getScheduleTaskEntity(projectId);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameters);
        List<ProjectStepVO> projectSteps = OnlineSchemaChangeTestUtils.getProjectSteps();
        ProjectProgressResponse progress = OnlineSchemaChangeTestUtils.getProjectProgressResponse();
        ProjectFullVerifyResultResponse fullVerifyResult = OnlineSchemaChangeTestUtils
                .getProjectFullVerifyResultWithInconsistentData();
        ProjectStepResult projectStepResult = OnlineSchemaChangeTestUtils.getProjectStepResult(projectSteps,
                progress, fullVerifyResult);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyRequest.class)))
                .thenReturn(fullVerifyResult);

        // when, then
        Assertions.assertThrows(OscException.class, () -> scheduleCheckOmsProjectValve.invoke(null));
        verifyNoMoreInteractions(scheduleTaskRepository);
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test
    public void testInvoke_whenPreCheckResultIsFailed_thenThrowOscException() {
        // given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = OnlineSchemaChangeTestUtils
                .getOnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = OnlineSchemaChangeTestUtils.getScheduleTaskEntity(projectId);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameters);
        List<ProjectStepVO> projectSteps = OnlineSchemaChangeTestUtils.getProjectSteps();
        ProjectProgressResponse progress = OnlineSchemaChangeTestUtils.getProjectProgressResponse();
        ProjectStepResult projectStepResult = OnlineSchemaChangeTestUtils.getProjectStepResultWithPreCheckFailed();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // when, then
        Assertions.assertThrows(OscException.class, () -> scheduleCheckOmsProjectValve.invoke(null));
        verifyNoMoreInteractions(scheduleTaskRepository);
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test
    public void testInvoke_whenProjectStepsAndProgressIsFailed_thenLeave() {
        // given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = OnlineSchemaChangeTestUtils
                .getOnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = OnlineSchemaChangeTestUtils.getScheduleTaskEntity(projectId);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameters);
        List<ProjectStepVO> projectSteps = OnlineSchemaChangeTestUtils.getProjectStepsWithFailedStep();
        ProjectProgressResponse progress = OnlineSchemaChangeTestUtils.getProjectProgressResponseWithFailedStep();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // when
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();

        // then
        Assertions.assertDoesNotHappenAsync(() -> scheduleCheckOmsProjectValve.invoke(null));
        verifyNoMoreInteractions(scheduleTaskRepository);
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test
    public void testHandleOmsProjectStepResult_whenProjectStepStatusIsDoneAndFullVerificationResultIsConsistentOrUncheck_thenOpenManualSwapTable() {
        // given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = OnlineSchemaChangeTestUtils
                .getOnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = OnlineSchemaChangeTestUtils.getScheduleTaskEntity(projectId);
        List<ProjectStepVO> projectSteps = OnlineSchemaChangeTestUtils.getProjectSteps();
        ProjectProgressResponse progress = OnlineSchemaChangeTestUtils.getProjectProgressResponse();
        ProjectStepResult projectStepResult = OnlineSchemaChangeTestUtils.getProjectStepResultWithConsistentData();
        SwapTableType swapTableType = SwapTableType.MANUAL_SWAP;

        when(projectOpenApiService.describeProjectSteps(projectRequest(taskParameters))).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest(taskParameters))).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectStepResult.getFullVerifyResult());

        // when
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, projectStepResult,
                new OnlineSchemaChangeScheduleTaskResult(taskParameters), swapTableType, scheduleTask);

        // then
        verify(scheduleTask, times(2)).setResultJson(anyString());
        verify(scheduleTaskRepository).update(scheduleTask);
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test
    public void testHandleOmsProjectStepResult_whenProjectStepStatusIsDoneButFullVerificationResultIsInconsistent_thenLeave() {
        // given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = OnlineSchemaChangeTestUtils
                .getOnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = OnlineSchemaChangeTestUtils.getScheduleTaskEntity(projectId);
        List<ProjectStepVO> projectSteps = OnlineSchemaChangeTestUtils.getProjectSteps();
        ProjectProgressResponse progress = OnlineSchemaChangeTestUtils.getProjectProgressResponse();
        ProjectStepResult projectStepResult = OnlineSchemaChangeTestUtils.getProjectStepResultWithInconsistentData();

        when(projectOpenApiService.describeProjectSteps(projectRequest(taskParameters))).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest(taskParameters))).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectStepResult.getFullVerifyResult());

        // when
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();

        // then
        Assertions.assertDoesNotHappenAsync(() -> scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, projectStepResult,
                new OnlineSchemaChangeScheduleTaskResult(taskParameters), SwapTableType.MANUAL_SWAP, scheduleTask));
        verifyNoMoreInteractions(scheduleTask);
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test
    public void testHandleOmsProjectStepResult_whenProjectStepStatusIsFailed_thenLeave() {
        // given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = OnlineSchemaChangeTestUtils
                .getOnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = OnlineSchemaChangeTestUtils.getScheduleTaskEntity(projectId);
        List<ProjectStepVO> projectSteps = OnlineSchemaChangeTestUtils.getProjectStepsWithFailedStep();
        ProjectProgressResponse progress = OnlineSchemaChangeTestUtils.getProjectProgressResponseWithFailedStep();

        when(projectOpenApiService.describeProjectSteps(projectRequest(taskParameters))).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest(taskParameters))).thenReturn(progress);

        // when
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();

        // then
        Assertions.assertDoesNotHappenAsync(() -> scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, null,
                new OnlineSchemaChangeScheduleTaskResult(taskParameters), SwapTableType.MANUAL_SWAP, scheduleTask));
        verifyNoMoreInteractions(scheduleTask);
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    private ProjectRequest projectRequest(OnlineSchemaChangeScheduleTaskParameters taskParameters) {
        return new ProjectControlRequest().setId(taskParameters.getOmsProjectId()).setUid(taskParameters.getUid());
    }
}