package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.oceanbase.odc.service.onlineschemachange.utils.OmsProjectUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScheduleCheckOmsProjectValveTest {

    @Autowired
    private ProjectOpenApiService projectOpenApiService;
    @Autowired
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;
    @Autowired
    private DefaultTableFactory defaultTableFactory;

    private ProjectOpenApiService mockProjectOpenApiService() {
        ProjectOpenApiService mockProjectOpenApiService = mock(ProjectOpenApiService.class);
        when(mockProjectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(getProjectSteps());
        when(mockProjectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(getProjectProgressResponse());
        when(mockProjectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(getProjectFullVerifyResultResponse());
        return mockProjectOpenApiService;
    }

    private List<ProjectStepVO> getProjectSteps() {
        ProjectStepVO projectStepVO1 = new ProjectStepVO();
        projectStepVO1.setStep("Step1");
        ProjectStepVO projectStepVO2 = new ProjectStepVO();
        projectStepVO2.setStep("Step2");
        return Lists.newArrayList(projectStepVO1, projectStepVO2);
    }

    private ProjectProgressResponse getProjectProgressResponse() {
        ProjectProgressResponse projectProgressResponse = new ProjectProgressResponse();
        projectProgressResponse.setCurrentStep("Step1");
        projectProgressResponse.setCurrentStepStatus(TaskStatus.RUNNING.name());
        projectProgressResponse.setFullTransferProgressPercentage(50);
        projectProgressResponse.setFullVerificationProgressPercentage(10);
        return projectProgressResponse;
    }

    private ProjectFullVerifyResultResponse getProjectFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectFullVerifyResultResponse.setFullVerificationResultDescription("Verify Successful");
        return projectFullVerifyResultResponse;
    }

    @Test
    public void testInvoke() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.projectOpenApiService = mockProjectOpenApiService();
        scheduleCheckOmsProjectValve.scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleCheckOmsProjectValve.onlineSchemaChangeProperties = onlineSchemaChangeProperties;
        scheduleCheckOmsProjectValve.defaultTableFactory = defaultTableFactory;

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        when(scheduleTaskRepository.getOne(1L)).thenReturn(scheduleTask);

        // When
        ValveContext valveContext = new ValveContext();
        valveContext.setScheduleTask(scheduleTask);
        valveContext.setTaskParameter(taskParameter);
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.RUNNING.name(), result.getCurrentStepStatus());
        assertEquals(50, result.getFullTransferProgressPercentage());
        assertEquals(10, result.getFullVerificationProgressPercentage());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals("Verify Successful", result.getFullVerificationResultDescription());
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // Given
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.RUNNING);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setFullTransferProgressPercentage(50);
        result.setFullVerificationProgressPercentage(10);

        SwapTableType swapTableType = SwapTableType.MYSQL;
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();

        // When
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(any(ValveContext.class), projectStepResult, result,
                swapTableType, scheduleTask());

        // Then
        verify(scheduleTaskRepository).update(any(ScheduleTaskEntity.class));
    }

    private ScheduleTaskEntity scheduleTask() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        return scheduleTask;
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        // Given
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        // When
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();

        // Then
        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
        verify(projectOpenApiService).resumeProject(any(ProjectControlRequest.class));
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResult_precheckFailed() {
        // Given
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        // When
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();

        // Then
        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);

        throw new OscException(ErrorCodes.OmsPreCheckFailed, "Precheck Failed");
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResult_dataInconsistent() {
        // Given
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        // When
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();

        // Then
        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);

        throw new OscException(ErrorCodes.OmsDataCheckInconsistent,
                "Task failed for origin table has inconsistent data with new table");
    }

    @Test
    public void testListProjectFullVerifyResult() {
        // Given
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";
        ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();

        // When
        ProjectFullVerifyResultResponse result = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId,
                databaseName, uid);

        // Then
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals("Verify Successful", result.getFullVerificationResultDescription());
    }
}