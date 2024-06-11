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
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    private DefaultTableFactory tableFactory;
    private ScheduleCheckOmsProjectValve scheduler;
    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;
    private ProjectControlRequest projectRequest;
    private ProjectStepVO projectStepVO;
    private ProjectProgressResponse projectProgressResponse;
    private ProjectFullVerifyResultResponse projectFullVerifyResultResponse;
    private List<ProjectStepVO> projectSteps;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tableFactory = new DefaultTableFactory();
        scheduler = new ScheduleCheckOmsProjectValve();
        scheduler.setProjectOpenApiService(projectOpenApiService);
        scheduler.setOscTaskCompleteHandler(completeHandler);
        scheduler.setScheduleTaskRepository(scheduleTaskRepository);
        scheduler.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);

        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        scheduleTask = new ScheduleTaskEntity();
        projectRequest = new ProjectControlRequest();
        projectStepVO = new ProjectStepVO();
        projectProgressResponse = new ProjectProgressResponse();
        projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectSteps = Lists.newArrayList(projectStepVO);
    }

    @Test
    public void testInvoke_whenProjectIsNotFound_thenThrowException() {
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenThrow(new RuntimeException());

        OscValveContext context = new OscValveContext();
        context.setScheduleTask(scheduleTask);
        context.setTaskParameter(taskParameter);

        assertThrows(OscException.class, () -> scheduler.invoke(context));
    }

    @Test
    public void testInvoke_whenProjectStepsAreNotAvailable_thenContinue() {
        projectProgressResponse.setSteps(projectSteps);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);

        OscValveContext context = new OscValveContext();
        context.setScheduleTask(scheduleTask);
        context.setTaskParameter(taskParameter);

        scheduler.invoke(context);

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
    }

    @Test
    public void testInvoke_whenProjectIsRunning_thenContinue() {
        projectProgressResponse.setStatus("RUNNING");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectProgressResponse);

        OscValveContext context = new OscValveContext();
        context.setScheduleTask(scheduleTask);
        context.setTaskParameter(taskParameter);

        scheduler.invoke(context);

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
    }

    @Test
    public void testInvoke_whenProjectIsSuspended_thenResumeProject() {
        projectProgressResponse.setStatus("SUSPEND");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectProgressResponse);
        doReturn(null).when(completeHandler).handle(ArgumentMatchers.any(ValveContext.class));

        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        OscValveContext context = new OscValveContext();
        context.setScheduleTask(scheduleTask);
        context.setTaskParameter(taskParameter);

        scheduler.invoke(context);

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
        verify(completeHandler, never()).handle(ArgumentMatchers.any(ValveContext.class));
    }

    @Test
    public void testInvoke_whenProjectIsFinished_thenCheckFullVerificationResult() {
        projectProgressResponse.setStatus("FINISHED");
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        OscValveContext context = new OscValveContext();
        context.setScheduleTask(scheduleTask);
        context.setTaskParameter(taskParameter);

        scheduler.invoke(context);

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(ArgumentMatchers.any(ListProjectFullVerifyRequest.class));
    }

    @Test
    public void testInvoke_whenProjectIsFailed_thenThrowException() {
        projectProgressResponse.setStatus("FINISHED");
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        OscValveContext context = new OscValveContext();
        context.setScheduleTask(scheduleTask);
        context.setTaskParameter(taskParameter);

        assertThrows(OscException.class, () -> scheduler.invoke(context));
    }

    @Test
    public void testInvoke_whenProjectIsInconsistent_thenThrowException() {
        projectProgressResponse.setStatus("FINISHED");
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        projectFullVerifyResultResponse.setFullVerificationResultDescription("Description of inconsistency");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        OscValveContext context = new OscValveContext();
        context.setScheduleTask(scheduleTask);
        context.setTaskParameter(taskParameter);

        assertThrows(OscException.class, () -> scheduler.invoke(context),
                "Task failed for origin table has inconsistent data with new table, result: "
                        + projectFullVerifyResultResponse.getFullVerificationResultDescription());
    }

    @Test
    public void testInvoke_whenProjectIsPrecheckFailed_thenThrowException() {
        projectProgressResponse.setStatus("FINISHED");
        projectStepVO.setPreCheckResult(PrecheckResult.FAILED);
        projectStepVO.setErrorMsg("Precheck error message");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(new ProjectProgressResponse());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);

        OscValveContext context = new OscValveContext();
        context.setScheduleTask(scheduleTask);
        context.setTaskParameter(taskParameter);

        assertThrows(OscException.class, () -> scheduler.invoke(context),
                "Precheck failed, error message: Precheck error message");
    }

    @Test
    public void testInvoke_whenProjectIsSuccessAndSwapTableEnable_thenStartManualSwapTable() {
        projectProgressResponse.setStatus("FINISHED");
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        SwapTableType swapTableType = SwapTableType.FORCE;

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);
        doReturn(true).when(SwapTableUtil).isSwapTableEnable(swapTableType, TaskStatus.FINISHED, 100,
                FullVerificationResult.CONSISTENT, false);

        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        OscValveContext context = new OscValveContext();
        context.setScheduleTask(scheduleTask);
        context.setTaskParameter(taskParameter);
        context.setSwapTableType(swapTableType);

        scheduler.invoke(context);

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(ArgumentMatchers.any(ListProjectFullVerifyRequest.class));
        verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
        verify(completeHandler, never()).handle(ArgumentMatchers.any(ValveContext.class));
        verify(scheduleTaskRepository, times(1)).update(ArgumentMatchers.eq(scheduleTask));
    }

    @Test
    public void testInvoke_whenProjectIsSuccessAndSwapTableDisabled_thenContinue() {
        projectProgressResponse.setStatus("FINISHED");
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        SwapTableType swapTableType = SwapTableType.FORCE;

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);
        doReturn(false).when(SwapTableUtil).isSwapTableEnable(swapTableType, TaskStatus.FINISHED, 100,
                FullVerificationResult.CONSISTENT, false);

        OscValveContext context = new OscValveContext();
        context.setScheduleTask(scheduleTask);
        context.setTaskParameter(taskParameter);
        context.setSwapTableType(swapTableType);

        scheduler.invoke(context);

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(ArgumentMatchers.any(ListProjectFullVerifyRequest.class));
        verify(projectOpenApiService, never()).resumeProject(projectRequest);
        verify(completeHandler, never()).handle(ArgumentMatchers.any(ValveContext.class));
        verify(scheduleTaskRepository, times(1)).update(ArgumentMatchers.eq(scheduleTask));
    }

}