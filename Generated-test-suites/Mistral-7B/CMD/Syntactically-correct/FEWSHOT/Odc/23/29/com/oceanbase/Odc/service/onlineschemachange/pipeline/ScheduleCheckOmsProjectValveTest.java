package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import org.junit.Before;
import org.junit.Test;

import java.util.List;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    private ProjectOpenApiService projectOpenApiService;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    private ProjectOpenApiService projectOpenApiServiceSpy;

    @Before
    public void setup() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        projectOpenApiServiceSpy = mock(ProjectOpenApiService.class);

        when(projectOpenApiService.describeProjectSteps(eq(getProjectControlRequest()))).thenReturn(getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(eq(getProjectControlRequest()))).thenReturn(getProjectProgressResponse());

        when(projectOpenApiServiceSpy.listProjectFullVerifyResult(eq(getListProjectFullVerifyResultRequest()))).thenReturn(getProjectFullVerifyResultResponse());

        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
        scheduleCheckOmsProjectValve.setOscTaskCompleteHandler(completeHandler);
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
        scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
    }

    @Test
    public void testInvoke() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTaskParameter();
        ScheduleTaskEntity scheduleTask = getScheduleTask();

        when(scheduleTaskRepository.getOne(eq(scheduleTask.getId()))).thenReturn(scheduleTask);

        scheduleCheckOmsProjectValve.invoke(getValveContext(scheduleTask, taskParameter));

        verifyInvokeMethods(scheduleTask, taskParameter);
    }

    @Test
    public void testInvoke_preCheckFailed() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTaskParameter();
        ScheduleTaskEntity scheduleTask = getScheduleTask();

        ProjectStepResult projectStepResult = getProjectStepResult_preCheckFailed();

        when(projectOpenApiService.describeProjectSteps(eq(getProjectControlRequest()))).thenReturn(getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(eq(getProjectControlRequest()))).thenReturn(getProjectProgressResponse());
        when(projectOpenApiServiceSpy.listProjectFullVerifyResult(eq(getListProjectFullVerifyResultRequest()))).thenThrow(new RuntimeException());

        scheduleCheckOmsProjectValve.invoke(getValveContext(scheduleTask, taskParameter));

        verifyInvokeMethods_preCheckFailed(scheduleTask, projectStepResult);
    }

    @Test
    public void testInvoke_fullVerificationInconsistent() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTaskParameter();
        ScheduleTaskEntity scheduleTask = getScheduleTask();

        ProjectStepResult projectStepResult = getProjectStepResult_fullVerificationInconsistent();

        when(projectOpenApiService.describeProjectSteps(eq(getProjectControlRequest()))).thenReturn(getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(eq(getProjectControlRequest()))).thenReturn(getProjectProgressResponse());
        when(projectOpenApiServiceSpy.listProjectFullVerifyResult(eq(getListProjectFullVerifyResultRequest()))).thenReturn(getProjectFullVerifyResultResponseWithInconsistentData());

        scheduleCheckOmsProjectValve.invoke(getValveContext(scheduleTask, taskParameter));

        verifyInvokeMethods_fullVerificationInconsistent(scheduleTask, projectStepResult);
    }

    @Test
    public void testInvoke_fullVerificationDone() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTaskParameter();
        ScheduleTaskEntity scheduleTask = getScheduleTask();

        ProjectStepResult projectStepResult = getProjectStepResult_fullVerificationDone();

        when(projectOpenApiService.describeProjectSteps(eq(getProjectControlRequest()))).thenReturn(getProjectSteps());
        when(projectApiService.describeProjectProgress(eq(getProjectControlRequest()))).thenReturn(getProjectProgressResponse());
        when(projectOpenApiServiceSpy.listProjectFullVerifyResult(eq(getListProjectFullVerifyResultRequest()))).thenReturn(getProjectFullVerifyResultResponse());

        scheduleCheckOmsProjectValve.invoke(getValveContext(scheduleTask, taskParameter));

        verifyInvokeMethods_fullVerificationDone(scheduleTask, projectStepResult);
    }

    private ProjectControlRequest getProjectControlRequest() {
        return new ProjectControlRequest();
    }

    private List<ProjectStepVO> getProjectSteps() {
        return Lists.newArrayList();
    }

    private ProjectProgressResponse getProjectProgressResponse() {
        ProjectProgressResponse projectProgressResponse = new ProjectProgressResponse();
        projectProgressResponse.setProjectId("projectId");
        projectProgressResponse.setCurrentStep(new ProjectStepVO());
        projectProgressResponse.getCurrentStep().setStepName("StepName");
        projectProgressResponse.getCurrentStep().setStatus(TaskStatus.RUNNING);
        projectProgressResponse.getCurrentStep().setFullVerificationResult(FullVerificationResult.UNCHECK);
        projectProgressResponse.getCurrentStep().setFullVerificationProgressPercentage(50);
        projectProgressResponse.getCurrentStep().setFullTransferProgressPercentage(50);
        projectProgressResponse.getCurrentStep().setPrecheckResult(PrecheckResult.PASSED);
        return projectProgressResponse;
    }

    private ProjectFullVerifyResultResponse getProjectFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectFullVerifyResultResponse.setStatus("SUCCESS");
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectFullVerifyResultResponse.setFullVerificationResultDescription("Description");
        projectFullVerifyResultResponse.setFullVerificationProgressPercentage(100);
        return projectFullVerifyResultResponse;
    }

    private ProjectFullVerifyResultResponse getProjectFullVerifyResultResponseWithInconsistentData() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectFullVerifyResultResponse.setStatus("SUCCESS");
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        projectFullVerifyResultResponse.setFullVerificationResultDescription("Description");
        projectFullVerifyResultResponse.setFullVerificationProgressPercentage(100);
        return projectFullVerifyResultResponse;
    }

    private ListProjectFullVerifyResultRequest getListProjectFullVerifyResultRequest() {
        ListProjectFullVerifyResultRequest projectFullVerifyResultRequest = new ListProjectFullVerifyResultRequest();
        projectFullVerifyResultRequest.setUid("uid");
        projectFullVerifyResultRequest.setProjectId("projectId");
        projectFullVerifyResultRequest.setSourceSchemas(new String[]{"databaseName"});
        projectFullVerifyResultRequest.setDestSchemas(new String[]{"databaseName"});
        projectFullVerifyResultRequest.setStatus(new String[]{"FINISHED", "SUSPEND", "RUNNING"});
        projectFullVerifyResultRequest.setPageSize(10);
        projectFullVerifyResultRequest.setPageNumber(1);
        return projectFullVerifyResultRequest;
    }

    private OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid("uid");
        taskParameter.setSwapTableType(SwapTableType.MANUAL);
        return taskParameter;
    }

    private ScheduleTaskEntity getScheduleTask() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1);
        scheduleTask.setStatus(TaskStatus.PENDING);
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        return scheduleTask;
    }

    private ValveContext getValveContext(ScheduleTaskEntity scheduleTask, OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        OscValveContext oscValveContext = new OscValveContext();
        oscValveContext.setScheduleTask(scheduleTask);
        oscValveContext.setTaskParameter(taskParameter);
        return oscValveContext;
    }

    private ProjectStepResult getProjectStepResult_preCheckFailed() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep(new ProjectStepVO());
        projectStepResult.setCurrentStepStatus(TaskStatus.FAILED);
        projectStepResult.setPrecheckResult(PrecheckResult.FAILED);
        projectStepResult.setPrecheckResultDescription("PrecheckResultDescription");
        projectStepResult.setErrorMsg("ErrorMsg");
        return projectStepResult;
    }

    private ProjectStepResult getProjectStepResult_fullVerificationInconsistent() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep(new ProjectStepVO());
        projectStepResult.setCurrentStepStatus(TaskStatus.FAILED);
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        projectStepResult.setFullVerificationResultDescription("Description");
        return projectStepResult;
    }

    private ProjectStepResult getProjectStepResult_fullVerificationDone() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep(new ProjectStepVO());
        projectStepResult.setCurrentStepStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationResultDescription("Description");
        return projectStepResult;
    }

    private void verifyInvokeMethods(ScheduleTaskEntity scheduleTask, OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        assertEquals("projectId", scheduleTask.getResultJson().getProjectId());
        assertEquals(SwapTableType.MANUAL, scheduleTask.getResultJson().getSwapTableType());
        assertEquals(TaskStatus.IN_PROGRESS, scheduleTask.getStatus());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()), scheduleTask.getResultJson());
    }

    private void verifyInvokeMethods_preCheckFailed(ScheduleTaskEntity scheduleTask, ProjectStepResult projectStepResult) {
        assertEquals("projectId", scheduleTask.getResultJson().getProjectId());
        assertEquals(SwapTableType.MANUAL, scheduleTask.getResultJson().getSwapTableType());
        assertEquals(TaskStatus.FAILED, scheduleTask.getStatus());
        assertEquals("ErrorMsg", scheduleTask.getErrorMsg());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()), scheduleTask.getResultJson());
    }

    private void verifyInvokeMethods_fullVerificationInconsistent(ScheduleTaskEntity scheduleTask, ProjectStepResult projectStepResult) {
        assertEquals("projectId", scheduleTask.getResultJson().getProjectId());
        assertEquals(SwapTableType.MANUAL, scheduleTask.getResultJson().getSwapTableType());
        assertEquals(TaskStatus.FAILED, scheduleTask.getStatus());
        assertEquals("ErrorMsg", scheduleTask.getErrorMsg());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()), scheduleTask.getResultJson());
    }

    private void verifyInvokeMethods_fullVerificationDone(ScheduleTaskEntity scheduleTask, ProjectStepResult projectStepResult) {
        assertEquals("projectId", scheduleTask.getResultJson().getProjectId());
        assertEquals(SwapTableType.MANUAL, scheduleTask.getResultJson().getSwapTableType());
        assertEquals(TaskStatus.DONE, scheduleTask.getStatus());
        assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()), scheduleTask.getResultJson());
    }
}