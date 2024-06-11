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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvokeThrowsOscExceptionOnPreCheckFailed() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTaskParameter();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult(PrecheckResult.FAILED, "Precheck failed");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenThrow(new RuntimeException());

        // When
        OscValveContext valveContext = new OscValveContext(null, null, null, null, null);
        valveContext.setScheduleTask(new ScheduleTaskEntity());
        valveContext.setTaskParameter(taskParameter);

        // Then
        assertThrows(OscException.class, () -> scheduleCheckOmsProjectValve.invoke(valveContext));
    }

    @Test
    public void testInvokeThrowsOscExceptionOnDataCheckInconsistent() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTaskParameter();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult(FullVerificationResult.INCONSISTENT,
                "Data check inconsistent");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

        // When
        OscValveContext valveContext = new OscValveContext(null, null, null, null, null);
        valveContext.setScheduleTask(new ScheduleTaskEntity());
        valveContext.setTaskParameter(taskParameter);

        // Then
        assertThrows(OscException.class, () -> scheduleCheckOmsProjectValve.invoke(valveContext));
    }

    @Test
    public void testInvokeContinuesWhenProjectIsDone() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTaskParameter();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult(PrecheckResult.PASSED, null);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

        // When
        OscValveContext valveContext = new OscValveContext(null, null, null, null, null);
        valveContext.setScheduleTask(new ScheduleTaskEntity());
        valveContext.setTaskParameter(taskParameter);

        // Then
        scheduleCheckOmsProjectValve.invoke(valveContext);
        verify(scheduleTaskRepository, times(1)).update(any(ScheduleTaskEntity.class));
    }

    @Test
    public void testInvokeContinuesWhenProjectIsSuspended() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTaskParameter();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult(PrecheckResult.PASSED, null);
        projectStepResult.setFullVerificationResult(FullVerificationResult.SUSPENDED);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

        // When
        OscValveContext valveContext = new OscValveContext(null, null, null, null, null);
        valveContext.setScheduleTask(new ScheduleTaskEntity());
        valveContext.setTaskParameter(taskParameter);

        // Then
        scheduleCheckOmsProjectValve.invoke(valveContext);
        verify(scheduleTaskRepository, times(1)).update(any(ScheduleTaskEntity.class));
    }

    @Test
    public void testInvokeContinuesWhenProjectIsRunning() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTaskParameter();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult(PrecheckResult.PASSED, null);
        projectStepResult.setFullVerificationResult(FullVerificationResult.RUNNING);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

        // When
        OscValveContext valveContext = new OscValveContext(null, null, null, null, null);
        valveContext.setScheduleTask(new ScheduleTaskEntity());
        valveContext.setTaskParameter(taskParameter);

        // Then
        scheduleCheckOmsProjectValve.invoke(valveContext);
        verify(scheduleTaskRepository, times(1)).update(any(ScheduleTaskEntity.class));
    }

    @Test
    public void testInvokeSwapsTablesWhenProjectIsDoneAndManualSwapEnabled() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTaskParameter();
        taskParameter.setSwapTableType(SwapTableType.MANUAL);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult(PrecheckResult.PASSED, null);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        // When
        OscValveContext valveContext = new OscValveContext(null, null, null, null, null);
        valveContext.setScheduleTask(new ScheduleTaskEntity());
        valveContext.setTaskParameter(taskParameter);

        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Then
        assertTrue(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
        verify(completeHandler, times(1)).swapTables(any());
    }

    @Test
    public void testInvokeDoesNotSwapTablesWhenProjectIsDoneButManualSwapDisabled() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = getTaskParameter();
        taskParameter.setSwapTableType(SwapTableType.AUTOMATIC);
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult(PrecheckResult.PASSED, null);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        // When
        OscValveContext valveContext = new OscValveContext(null, null, null, null, null);
        valveContext.setScheduleTask(new ScheduleTaskEntity());
        valveContext.setTaskParameter(taskParameter);

        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Then
        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
        verifyZeroInteractions(completeHandler);
    }

    private OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
        return new OnlineSchemaChangeScheduleTaskParameters();
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParameter.getUid());
        projectRequest.setId(taskParameter.getOmsProjectId());
        return projectRequest;
    }
}