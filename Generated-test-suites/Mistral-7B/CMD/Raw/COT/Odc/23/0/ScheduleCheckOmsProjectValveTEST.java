package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

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

/**
 * Test class for ScheduleCheckOmsProjectValve
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
    private DefaultTableFactory tableFactory;

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        ScheduleTaskEntity scheduleTask = createScheduleTask();
        ProjectControlRequest projectRequest = createProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = createProjectStepResult();
        ProjectProgressResponse progress = createProjectProgressResponse();
        ProjectFullVerifyResultResponse fullVerifyResultResponse = createProjectFullVerifyResultResponse();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(createProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        doNothing().when(completeHandler).handleTaskComplete(any(ValveContext.class));

        // When
        valve.invoke(mock(ValveContext.class));

        // Then
        ScheduleTaskEntity actualScheduleTask = scheduleTaskRepository.findById(scheduleTask.getId());
        assertEquals(scheduleTask, actualScheduleTask);
        assertEquals(projectStepResult.getTaskPercentage(), actualScheduleTask.getProgressPercentage());
        assertEquals(JsonUtils.toJson(createResult()), actualScheduleTask.getResultJson());
    }

    @Test(expected = OscException.class)
    public void testInvokePreCheckFailed() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        ScheduleTaskEntity scheduleTask = createScheduleTask();
        ProjectControlRequest projectRequest = createProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = createProjectStepResultWithPrecheckFailed();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(createProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectStepResult);

        // When and Then
        valve.invoke(mock(ValveContext.class));
    }

    @Test(expected = OscException.class)
    public void testInvokeDataCheckInconsistent() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        ScheduleTaskEntity scheduleTask = createScheduleTask();
        ProjectControlRequest projectRequest = createProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = createProjectStepResultWithFullVerificationInconsistent();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(createProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectStepResult);

        // When and Then
        valve.invoke(mock(ValveContext.class));
    }

    @Test
    public void testHandleOmsProjectStepResult() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        OnlineSchemaChangeScheduleTaskResult result = createResult();
        ProjectStepResult projectStepResult = createProjectStepResultWithDoneAndInconsistent();
        ScheduleTaskEntity scheduleTask = createScheduleTask();

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class))).thenReturn(createProjectSteps());
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(createProjectProgressResponse());

        doNothing().when(completeHandler).handleTaskComplete(any(ValveContext.class));

        valve.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult, result, SwapTableType.DEFAULT, scheduleTask);

        assertEquals(Boolean.TRUE, result.isManualSwapTableEnabled());
        assertEquals(Boolean.TRUE, result.isManualSwapTableStarted());
    }

    @Test
    public void testHandleOmsProjectStepResultContinue() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        OnlineSchemaChangeScheduleTaskResult result = createResult();
        ProjectStepResult projectStepResult = createProjectStepResult();

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class))).thenReturn(createProjectSteps());
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(projectStepResult);

        doNothing().when(completeHandler).handleTaskComplete(any(ValveContext.class));

        valve.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult, result, SwapTableType.DEFAULT, createScheduleTask());

        assertEquals(Boolean.FALSE, result.isManualSwapTableEnabled());
        assertEquals(Boolean.FALSE, result.isManualSwapTableStarted());
    }

    @Test
    public void testListProjectFullVerifyResult() throws Exception {
        // Given
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] {databaseName});
        request.setDestSchemas(new String[] {databaseName});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(1);
        request.setPageNumber(1);
        request.setUid(uid);

        ProjectFullVerifyResultResponse expectedResponse = new ProjectFullVerifyResultResponse();
        expectedResponse.setProjectId(projectId);
        expectedResponse.setResultList(new ProjectFullVerifyResultResponse.ResultItem[] {new ProjectFullVerifyResultResponse.ResultItem()});

        // When
        ProjectFullVerifyResultResponse actualResponse = valve.listProjectFullVerifyResult(projectId, databaseName, uid);

        // Then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test(expected = OscException.class)
    public void testListProjectFullVerifyResultFailure() throws Exception {
        // Given
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] {databaseName});
        request.setDestSchemas(new String[] {databaseName});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(1);
        request.setPageNumber(1);
        request.setUid(uid);

        doThrow(new RuntimeException("Test exception"))
                .when(projectOpenApiService)
                .listProjectFullVerifyResult(request);

        // When and Then
        valve.listProjectFullVerifyResult(projectId, databaseName, uid);
    }

    private ProjectProgressResponse createProjectProgressResponse() {
        ProjectProgressResponse response = new ProjectProgressResponse();
        response.setProjectId("projectId");
        response.setProgress("progress");
        response.setErrorMsg("errorMsg");
        response.setCurrentStep("currentStep");
        response.setCurrentStepStatus("currentStepStatus");
        return response;
    }

    private ProjectStepVO[] createProjectSteps() {
        ProjectStepVO[] projectSteps = new ProjectStepVO[1];
        projectSteps[0] = new ProjectStepVO();
        projectSteps[0].setStepName("stepName");
        projectSteps[0].setStatus("status");
        projectSteps[0].setPercentage(100.0);
        projectSteps[0].setDescription("description");
        return projectSteps;
    }

    private ProjectStepResult createProjectStepResult() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskPercentage(100.0);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setPreCheckResult(PrecheckResult.PASSED);
        projectStepResult.setFullTransferProgressPercentage(100.0);
        projectStepResult.setFullVerificationProgressPercentage(100.0);
        projectStepResult.setCurrentStep(OmsStepName.FULL_TRANSFER.name());
        projectStepResult.setCurrentStepStatus(TaskStatus.DONE.toString());
        projectStepResult.setFullVerificationResultDescription("description");
        projectStepResult.setErrorMsg("errorMsg");
        return projectStepResult;
    }

    private ProjectStepResult createProjectStepResultWithPrecheckFailed() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskPercentage(100.0);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        projectStepResult.setFullTransferProgressPercentage(100.0);
        projectStepResult.setFullVerificationProgressPercentage(100.0);
        projectStepResult.setCurrentStep(OmsStepName.FULL_TRANSFER.name());
        projectStepResult.setCurrentStepStatus(TaskStatus.FAILED.toString());
        projectStepResult.setFullVerificationResultDescription("description");
        projectStepResult.setErrorMsg("errorMsg");
        return projectStepResult;
    }

    private ProjectStepResult createProjectStepResultWithFullVerificationInconsistent() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskPercentage(100.0);
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        projectStepResult.setPreCheckResult(PrecheckResult.PASSED);
        projectStepResult.setFullTransferProgressPercentage(100.0);
        projectStepResult.setFullVerificationProgressPercentage(100.0);
        projectStepResult.setCurrentStep(OmsStepName.FULL_VERIFIER.name());
        projectStepResult.setCurrentStepStatus(TaskStatus.FAILED.toString());
        projectStepResult.setFullVerificationResultDescription("description");
        projectStepResult.setErrorMsg("errorMsg");
        return projectStepResult;
    }

    private ProjectStepResult createProjectStepResultWithDoneAndInconsistent() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskPercentage(100.0);
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        projectStepResult.setPreCheckResult(PrecheckResult.PASSED);
        projectStepResult.setFullTransferProgressPercentage(100.0);
        projectStepResult.setFullVerificationProgressPercentage(100.0);
        projectStepResult.setCurrentStep(OmsStepName.FULL_VERIFIER.name());
        projectStepResult.setCurrentStepStatus(TaskStatus.DONE.toString());
        projectStepResult.setFullVerificationResultDescription("description");
        projectStepResult.setErrorMsg("errorMsg");
        return projectStepResult;
    }

    private OnlineSchemaChangeScheduleTaskResult createResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setFullTransferEstimatedCount(1);
        result.setFullTransferFinishedCount(1);
        result.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        result.setFullVerificationResultDescription("description");
        result.setFullVerificationProgressPercentage(100.0);
        result.setCurrentStep(OmsStepName.FULL_TRANSFER.name());
        result.setCurrentStepStatus(TaskStatus.DONE.toString());
        return result;
    }

    private OnlineSchemaChangeScheduleTaskParameters createTaskParameter() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid("uid");
        return taskParameter;
    }

    private ScheduleTaskEntity createScheduleTask() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus("status");
        scheduleTask.setResultJson(StringUtils.EMPTY);
        scheduleTask.setProgressPercentage(100.0);
        return scheduleTask;
    }

    private ProjectControlRequest createProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParameter.getUid());
        projectRequest.setId(taskParameter.getOmsProjectId());
        return projectRequest;
    }
}