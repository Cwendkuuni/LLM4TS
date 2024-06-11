package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.cli.ScheduleTaskEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
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

@Slf4j
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
    private DefaultTableFactory defaultTableFactory;

    @InjectMocks
    private ScheduleCheckOmsProjectValve valveUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_whenProjectRequestSuccessAndProjectProgressIsDone_thenResultIsUpdatedAndNextIsInvoked() {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = createProjectStepResultWithDoneStatus();
        ProjectProgressResponse progress = createProjectProgressResponse();
        ProjectFullVerifyResultResponse fullVerifyResultResponse = createProjectFullVerifyResultResponse();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(createProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        ProjectStepResultChecker projectStepResultChecker = new ProjectStepResultChecker(progress,
                createProjectSteps(), onlineSchemaChangeProperties.isEnableFullVerify());

        // when
        valveUnderTest.invoke(createValveContextWithScheduleTask(taskParameter));

        // then
        AtomicReference<OnlineSchemaChangeScheduleTaskResult> resultReference = new AtomicReference<>();
        ScheduleTaskEntity scheduleTask = createScheduleTaskWithResultJson(resultReference);
        assertEquals(Result.success(), valveUnderTest.handleOmsProjectStepResult(any(ValveContext.class),
                projectStepResult, resultReference.get(), SwapTableType.SCHEMA_CHANGE, scheduleTask));
        verify(scheduleTaskRepository).update(scheduleTask);
        getNext().invoke(valveContextWithScheduleTask(scheduleTask));
    }

    @Test
    public void testInvoke_whenProjectRequestSuccessAndProjectProgressIsRunning_thenWaitForProjectToFinish() {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = createProjectStepResultWithRunningStatus();
        ProjectProgressResponse progress = createProjectProgressResponseWithRunningStatus();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(createProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // when
        valveUnderTest.invoke(createValveContextWithScheduleTask(taskParameter));

        // then
        assertEquals(Result.running(), valveUnderTest.handleOmsProjectStepResult(any(ValveContext.class),
                projectStepResult, null, SwapTableType.SCHEMA_CHANGE, null));
    }

    @Test
    public void testInvoke_whenProjectRequestSuccessAndProjectProgressIsFailed_thenHandleException() {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = createProjectStepResultWithFailedStatus();
        ProjectProgressResponse progress = createProjectProgressResponse();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(createProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // when
        valveUnderTest.invoke(createValveContextWithScheduleTask(taskParameter));

        // then
        assertEquals(Result.failed(), valveUnderTest.handleOmsProjectStepResult(any(ValveContext.class),
                projectStepResult, null, SwapTableType.SCHEMA_CHANGE, null));
    }

    @Test
    public void testInvoke_whenProjectRequestFailed_thenHandleException() {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenThrow(new RuntimeException());

        // when
        valveUnderTest.invoke(createValveContextWithScheduleTask(taskParameter));

        // then
        assertEquals(Result.failed(), valveUnderTest.handleNext(any(ValveContext.class)));
    }

    private List<ProjectStepVO> createProjectSteps() {
        return Lists.newArrayList(mock(ProjectStepVO.class));
    }

    private ProjectProgressResponse createProjectProgressResponse() {
        ProjectProgressResponse projectProgressResponse = mock(ProjectProgressResponse.class);
        when(projectProgressResponse.getStatus()).thenReturn(TaskStatus.DONE.name());
        return projectProgressResponse;
    }

    private ProjectProgressResponse createProjectProgressResponseWithRunningStatus() {
        ProjectProgressResponse projectProgressResponse = mock(ProjectProgressResponse.class);
        when(projectProgressResponse.getStatus()).thenReturn(TaskStatus.RUNNING.name());
        return projectProgressResponse;
    }

    private ProjectStepResult createProjectStepResultWithDoneStatus() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setCurrentStep("FULL_VERIFIER");
        return projectStepResult;
    }

    private ProjectStepResult createProjectStepResultWithFailedStatus() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.FAILED);
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        projectStepResult.setCurrentStep("FULL_TRANSFER");
        return projectStepResult;
    }

    private ProjectStepResult createProjectStepResultWithRunningStatus() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.RUNNING);
        projectStepResult.setCurrentStep("FULL_TRANSFER");
        return projectStepResult;
    }

    private ProjectFullVerifyResultResponse createProjectFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);
        when(projectFullVerifyResultResponse.getProjectSteps()).thenReturn(createProjectSteps());
        return projectFullVerifyResultResponse;
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        when(projectRequest.getUid()).thenReturn(taskParam.getUid());
        when(projectRequest.getId()).thenReturn(taskParam.getOmsProjectId());
        return projectRequest;
    }

    private ScheduleTaskEntity createScheduleTaskWithResultJson(AtomicReference<OnlineSchemaChangeScheduleTaskResult> resultReference) {
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        when(scheduleTask.getId()).thenReturn(1L);
        when(scheduleTask.getResultJson()).thenReturn(null);
        when(scheduleTaskRepository.save(scheduleTask)).thenAnswer(invocation -> scheduleTask);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        resultReference.set(result);
        resultReference.get().setResultJson(JsonUtils.toJson(result));
        return scheduleTask;
    }

    private ValveContext createValveContextWithScheduleTask(OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        ValveContext valveContext = mock(ValveContext.class);
        when(valveContext.getScheduleTask()).thenReturn(createScheduleTaskWithResultJson(new AtomicReference<>()));
        when(valveContext.getTaskParameter()).thenReturn(taskParameter);
        return valveContext;
    }

    private ValveContext createValveContextWithScheduleTask(ScheduleTaskEntity scheduleTask) {
        ValveContext valveContext = mock(ValveContext.class);
        when(valveContext.getScheduleTask()).thenReturn(scheduleTask);
        return valveContext;
    }

    private ValveContext getValveContextWithScheduleTask(Long scheduleTaskId) {
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        when(scheduleTask.getId()).thenReturn(scheduleTaskId);
        ValveContext valveContext = mock(ValveContext.class);
        when(valveContext.getScheduleTask()).thenReturn(scheduleTask);
        return valveContext;
    }
}