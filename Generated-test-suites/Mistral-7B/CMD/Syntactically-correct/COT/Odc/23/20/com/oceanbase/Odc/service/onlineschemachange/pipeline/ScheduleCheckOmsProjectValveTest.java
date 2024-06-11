package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

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
import com.oceanbase.odc.service.onlineschemachange.util.TestValveContext;

public class ScheduleCheckOmsProjectValveTest {
    private ProjectOpenApiService projectOpenApiService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleCheckOmsProjectValve valve;

    @Before
    public void setup() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        valve = new ScheduleCheckOmsProjectValve() {
            @Override
            protected ProjectOpenApiService getProjectOpenApiService() {
                return projectOpenApiService;
            }

            @Override
            protected ScheduleTaskRepository getScheduleTaskRepository() {
                return scheduleTaskRepository;
            }

            @Override
            protected OnlineSchemaChangeProperties getOnlineSchemaChangeProperties() {
                return onlineSchemaChangeProperties;
            }

            @Override
            protected OscTaskCompleteHandler getOscTaskCompleteHandler() {
                return completeHandler;
            }
        };
    }

    @Test
    public void testInvoke_whenNormal_thenSuccess() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepResult));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        valve.invoke(new TestValveContext(new ScheduleTaskEntity()).setTaskParameter(taskParameter));

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).update(resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    @Test
    public void testInvoke_whenProjectStepResultIsFailed_thenThrowOscException() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepResult));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(null);

        assertThrows(OscException.class, () -> {
            valve.invoke(new TestValveContext(new ScheduleTaskEntity()).setTaskParameter(taskParameter));
        });
    }

    @Test
    public void testInvoke_whenProjectStepResultIsFailedPreCheck_thenThrowOscException() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepResult));
        PrecheckResult precheckResult = PrecheckResult.FAILED;
        when(projectStepResult.getPreCheckResult()).thenReturn(precheckResult);

        assertThrows(OscException.class, () -> {
            valve.invoke(new TestValveContext(new ScheduleTaskEntity()).setTaskParameter(taskParameter));
        });
    }

    @Test
    public void testInvoke_whenProjectStepResultIsFailedFullVerifier_thenThrowOscException() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectFullVerifyResultResponse fullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepResult));
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(fullVerifyResultResponse);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.INCONSISTENT);

        assertThrows(OscException.class, () -> {
            valve.invoke(new TestValveContext(new ScheduleTaskEntity()).setTaskParameter(taskParameter));
        });
    }

    @Test
    public void testInvoke_whenProjectStepResultIsDone_thenUpdateScheduleTask() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepResult));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        valve.invoke(new TestValveContext(new ScheduleTaskEntity()).setTaskParameter(taskParameter));

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).update(resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    @Test
    public void testInvoke_whenProjectStepResultIsRunning_thenUpdateScheduleTask() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepResult));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        valve.invoke(new TestValveContext(new ScheduleTaskEntity()).setTaskParameter(taskParameter));

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).update(resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
        assertEquals(TaskStatus.RUNNING, result.getStatus());
    }

    @Test
    public void testInvoke_whenProjectStepResultIsSuspended_thenUpdateScheduleTask() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepResult));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        valve.invoke(new TestValveContext(new ScheduleTaskEntity()).setTaskParameter(taskParameter));

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).update(resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
        assertEquals(TaskStatus.SUSPENDED, result.getStatus());
    }

    @Test
    public void testInvoke_whenProjectStepResultIsDoneAndFullVerificationResultIsConsistent_thenNoSwapTableOperation() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectFullVerifyResultResponse fullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepResult));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(fullVerifyResultResponse);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);

        valve.invoke(new TestValveContext(new ScheduleTaskEntity()).setTaskParameter(taskParameter));

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).update(resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
        assertFalse(result.isManualSwapTableStarted());
    }

    @Test
    public void testInvoke_whenProjectStepResultIsDoneAndFullVerificationResultIsUnchecked_thenNoSwapTableOperation() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectFullVerifyResultResponse fullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepResult));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(fullVerifyResultResponse);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.UNCHECK);

        valve.invoke(new TestValveContext(new ScheduleTaskEntity()).setTaskParameter(taskParameter));

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository).update(resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
        assertFalse(result.isManualSwapTableStarted());
    }

    @Test
    public void testInvoke_whenProjectStepResultIsDoneAndFullVerificationResultIsInconsistent_thenThrowOscException() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectFullVerifyResultResponse fullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepResult));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(fullVerifyResultResponse);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.INCONSISTENT);

        assertThrows(OscException.class, () -> {
            valve.invoke(new TestValveContext(new ScheduleTaskEntity()).setTaskParameter(taskParameter));
        });
    }
}