package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
import com.oceanbase.odc.service.onlineschemachange.utils.ValveContext;

public class ScheduleCheckOmsProjectValveTest {
    private ProjectOpenApiService projectOpenApiService;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    private DefaultTableFactory defaultTableFactory;

    @Before
    public void setUp() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        defaultTableFactory = mock(DefaultTableFactory.class);

        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
        scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
        scheduleCheckOmsProjectValve.setOscTaskCompleteHandler(completeHandler);
        scheduleCheckOmsProjectValve.setDefaultTableFactory(defaultTableFactory);
    }

    @After
    public void tearDown() {
        reset(projectOpenApiService, onlineSchemaChangeProperties, scheduleTaskRepository, completeHandler,
                defaultTableFactory);
    }

    @Test
    public void testInvoke_ProjectHasRunningTasks() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepVO projectStep = new ProjectStepVO();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStep));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        ProjectStepResult projectStepResult = new ProjectStepResult(projectStep, progress,
                onlineSchemaChangeProperties.isEnableFullVerify());

        ValveContext valveContext = new ValveContext();
        valveContext.setScheduleTask(new ScheduleTaskEntity());
        valveContext.setTaskParameter(taskParameter);

        // When
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Then
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
    }

    @Test(expected = OscException.class)
    public void testInvoke_ProjectHasFailedPreCheck() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setPrecheckResult(PrecheckResult.FAILED);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStep));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ValveContext valveContext = new ValveContext();
        valveContext.setScheduleTask(new ScheduleTaskEntity());
        valveContext.setTaskParameter(taskParameter);

        // When
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Then
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService).resumeProject(projectRequest);
        throw new OscException(ErrorCodes.OmsPreCheckFailed, "Test Precheck Failed");
    }

    @Test(expected = OscException.class)
    public void testInvoke_ProjectHasInconsistentData() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStep));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ValveContext valveContext = new ValveContext();
        valveContext.setScheduleTask(new ScheduleTaskEntity());
        valveContext.setTaskParameter(taskParameter);

        // When
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Then
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService).resumeProject(projectRequest);
        throw new OscException(ErrorCodes.OmsDataCheckInconsistent, "Test Data Inconsistent");
    }

    @Test
    public void testInvoke_ProjectHasSucceeded() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStep));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ValveContext valveContext = new ValveContext();
        valveContext.setScheduleTask(new ScheduleTaskEntity());
        valveContext.setTaskParameter(taskParameter);

        // When
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Then
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, never()).resumeProject(projectRequest);
    }

    @Test
    public void testAdaptResult() {
        // Given
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        ProjectStepResult projectStepResult = new ProjectStepResult(null, null, null);

        // When
        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        // Then
        assertEquals(projectStepResult.getFullTransferEstimatedCount(), result.getFullTransferEstimatedCount());
        assertEquals(projectStepResult.getFullTransferFinishedCount(), result.getFullTransferFinishedCount());
        assertEquals(projectStepResult.getFullTransferProgressPercentage(), result.getFullTransferProgressPercentage());
        assertEquals(projectStepResult.getFullVerificationResult(), result.getFullVerificationResult());
        assertEquals(projectStepResult.getFullVerificationResultDescription(), result.getFullVerificationResultDescription());
        assertEquals(projectStepResult.getFullVerificationProgressPercentage(), result.getFullVerificationProgressPercentage());
        assertEquals(projectStepResult.getCurrentStep(), result.getCurrentStep());
        assertEquals(projectStepResult.getCurrentStepStatus(), result.getCurrentStepStatus());
        assertEquals(projectStepResult.getPrecheckResult(), result.getPrecheckResult());
    }

    @Test
    public void testListProjectFullVerifyResult() throws Exception {
        // Given
        String projectId = "TestProjectId";
        String databaseName = "TestDatabaseName";

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(new ProjectFullVerifyResultResponse());

        // When
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName(databaseName);
        taskParameter.setUid(UUID.randomUUID().toString());
        OnlineSchemaChangeScheduleTaskResult result = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(
                taskParameter.getOmsProjectId(), taskParameter.getDatabaseName(), taskParameter.getUid());

        // Then
        verify(projectOpenApiService).listProjectFullVerifyResult(request);
        assertEquals(result, new OnlineSchemaChangeScheduleTaskResult());
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParam.getUid());
        projectRequest.setId(taskParam.getOmsProjectId());
        return projectRequest;
    }
}