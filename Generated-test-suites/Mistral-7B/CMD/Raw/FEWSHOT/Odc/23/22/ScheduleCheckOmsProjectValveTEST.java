package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import java.util.UUID;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    private ProjectOpenApiService projectOpenApiService;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private ScheduleCheckOmsProjectValve valveUnderTest;

    @Before
    public void setUp() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        valveUnderTest = new ScheduleCheckOmsProjectValve();
        valveUnderTest.setAutowiredComponents(projectOpenApiService, completeHandler,
                scheduleTaskRepository, onlineSchemaChangeProperties);
    }

    @Test
    public void testInvokingWithSuccess() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTaskParameter();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(getProjectStepVO());
        ProjectProgressResponse progress = getProjectProgressResponse();
        ProjectFullVerifyResultResponse fullVerifyResultResponse = getProjectFullVerifyResultResponse();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> fullVerifyResultResponse)
                        .withResumeProject(() -> {
                            projectOpenApiService.resumeProject(projectRequest);
                            return null;
                        })
                        .getCheckerResult();

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        OnlineSchemaChangeScheduleTaskResult lastResult = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        valveUnderTest.invoke(getValveContext(projectRequest, projectStepResult, result, lastResult));

        assertEquals(TaskStatus.DONE, projectStepResult.getTaskStatus());
        verify(projectOpenApiService, times(3)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(2)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));

        verifyNoMoreInteractions(projectOpenApiService);
        verify(completeHandler).handle(taskParameter, result);
    }

    @Test
    public void testInvokingWithFailedPreCheck() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTaskParameter();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(getProjectStepVO());
        ProjectProgressResponse progress = getProjectProgressResponse();

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> null)
                        .withResumeProject(() -> null)
                        .getCheckerResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        OnlineSchemaChangeScheduleTaskResult lastResult = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        valveUnderTest.invoke(getValveContext(projectRequest, projectStepResult, result, lastResult));

        assertEquals(TaskStatus.FAILED, projectStepResult.getTaskStatus());
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verifyNoMoreInteractions(projectOpenApiService);

        throw new OscException(ErrorCodes.OmsPreCheckFailed, "Pre check failed");
    }

    @Test
    public void testInvokingWithFailedFullVerification() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTaskParameter();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(getProjectStepVO());
        ProjectProgressResponse progress = getProjectProgressResponse();

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> null)
                        .withResumeProject(() -> null)
                        .getCheckerResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        OnlineSchemaChangeScheduleTaskResult lastResult = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        valveUnderTest.invoke(getValveContext(projectRequest, projectStepResult, result, lastResult));

        assertEquals(TaskStatus.FAILED, projectStepResult.getTaskStatus());
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verifyNoMoreInteractions(projectOpenApiService);

        throw new OscException(ErrorCodes.OmsDataCheckInconsistent,
                "Task failed for origin table has inconsistent data with new table");
    }

    private OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
        return new OnlineSchemaChangeScheduleTaskParameters();
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(UUID.randomUUID().toString());
        projectRequest.setId(UUID.randomUUID().toString());
        return projectRequest;
    }

    private ProjectStepVO getProjectStepVO() {
        return new ProjectStepVO();
    }

    private ProjectProgressResponse getProjectProgressResponse() {
        return new ProjectProgressResponse();
    }

    private ProjectFullVerifyResultResponse getProjectFullVerifyResultResponse() {
        return new ProjectFullVerifyResultResponse();
    }

    private ValveContext getValveContext(ProjectControlRequest projectRequest,
            ProjectStepResult projectStepResult, OnlineSchemaChangeScheduleTaskResult result,
            OnlineSchemaChangeScheduleTaskResult lastResult) {
        OscValveContext context = mock(OscValveContext.class);
        when(context.getScheduleTask()).thenReturn(new ScheduleTaskEntity());
        when(context.getTaskParameter()).thenReturn(new OnlineSchemaChangeScheduleTaskParameters());
        when(context.getScheduleTask().getId()).thenReturn(UUID.randomUUID().toString());
        when(context.getScheduleTask().getResultJson()).thenReturn(JsonUtils.toJson(lastResult));
        when(context.getScheduleTask().getStatus()).thenReturn(TaskStatus.NONE.name());

        return context;
    }
}