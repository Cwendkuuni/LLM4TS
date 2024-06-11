package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.util.tableformat.Table;
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
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

/**
 * Test class for ScheduleCheckOmsProjectValve
 */
@SuppressWarnings("unchecked")
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_Success() throws Exception {
        // Given
        String projectId = "project-1";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        expectedResult.setFullTransferProgressPercentage(50);
        expectedResult.setFullVerificationProgressPercentage(75);

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid("uid");
        projectRequest.setId(projectId);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setCurrentStep(new ProjectStepVO());
        progress.getCurrentStep().setStatus(TaskStatus.RUNNING.name());

        ProjectFullVerifyResultResponse fullVerifyResult = new ProjectFullVerifyResultResponse();
        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        fullVerifyResult.setSteps(projectSteps);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(fullVerifyResult);

        // When
        ValveContext context = new ValveContext();
        context.setScheduleTask(new ScheduleTaskEntity());
        context.setTaskParameter(taskParameter);
        valve.invoke(context);

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(context.getScheduleTask().getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testInvoke_PrecheckFailed() throws Exception {
        // Given
        String projectId = "project-1";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid("uid");
        projectRequest.setId(projectId);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setCurrentStep(new ProjectStepVO());
        progress.getCurrentStep().setStatus(TaskStatus.FAILED.name());

        ProjectStepVO step = new ProjectStepVO();
        step.setStatus(TaskStatus.FAILED.name());
        step.setErrorMsg("Precheck error message");
        step.setName("Precheck step");

        List<ProjectStepVO> projectSteps = Lists.newArrayList(step);
        progress.setSteps(projectSteps);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        ValveContext context = new ValveContext();
        context.setScheduleTask(new ScheduleTaskEntity());
        context.setTaskParameter(taskParameter);
        valve.invoke(context);

        // Then
        assertEquals(ErrorCodes.OmsPreCheckFailed, context.getException().getErrorCode());
        assertEquals("Precheck error message", context.getException().getMessage());
    }

    @Test
    public void testInvoke_FullVerificationInconsistent() throws Exception {
        // Given
        String projectId = "project-1";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid("uid");
        projectRequest.setId(projectId);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setCurrentStep(new ProjectStepVO());
        progress.getCurrentStep().setStatus(TaskStatus.RUNNING.name());

        ProjectStepVO step = new ProjectStepVO();
        step.setStatus(TaskStatus.FAILED.name());
        step.setErrorMsg("Full verification error message");
        step.setName("Full verification step");

        List<ProjectStepVO> projectSteps = Lists.newArrayList(step);
        progress.setSteps(projectSteps);

        ProjectFullVerifyResultResponse fullVerifyResult = new ProjectFullVerifyResultResponse();
        List<ProjectStepVO> fullVerifySteps = Lists.newArrayList();
        fullVerifyResult.setSteps(fullVerifySteps);
        fullVerifySteps.add(step);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(fullVerifyResult);

        // When
        ValveContext context = new ValveContext();
        context.setScheduleTask(new ScheduleTaskEntity());
        context.setTaskParameter(taskParameter);
        valve.invoke(context);

        // Then
        assertEquals(ErrorCodes.OmsDataCheckInconsistent, context.getException().getErrorCode());
        assertEquals("Task failed for origin table has inconsistent data with new table",
                context.getException().getMessage());
    }
}