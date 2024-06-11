package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
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
import com.oceanbase.odc.service.onlineschemachange.utils.JsonUtils;

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
    private ScheduleCheckOmsProjectValve valveUnderTest;

    private OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
    private ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(projectOpenApiService);
        verifyNoMoreInteractions(completeHandler);
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    @Test
    public void testInvokeWithSuccess() throws Exception {
        // Given
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(createProjectStepVO()));

        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(createProjectProgressResponse());

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(createProjectFullVerifyResultResponse());

        // When
        valveUnderTest.invoke(createValveContextWithScheduleTaskAndTaskParameter());

        // Then
        verify(scheduleTaskRepository, times(1)).update(any(ScheduleTaskEntity.class));
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test
    public void testInvokeWithPrecheckFailed() throws Exception {
        // Given
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(createProjectStepVOWithPrecheckFailed()));

        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(createProjectProgressResponse());

        // When/Then
        assertThrows(OscException.class, () -> valveUnderTest.invoke(createValveContextWithScheduleTaskAndTaskParameter()));

        verify(projectOpenApiService, times(1)).resumeProject(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
    }

    @Test
    public void testInvokeWithFullVerificationInconsistent() throws Exception {
        // Given
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(createProjectStepVO()));

        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(createProjectProgressResponse());

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(createProjectFullVerifyResultResponseWithFullVerificationInconsistent());

        // When/Then
        assertThrows(OscException.class,
                () -> valveUnderTest.invoke(createValveContextWithScheduleTaskAndTaskParameter()));

        verify(projectOpenApiService, times(1)).resumeProject(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
    }

    @Test
    public void testInvokeWithFullVerificationFailed() throws Exception {
        // Given
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(Lists.newArrayList(createProjectStepVO()));

        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(createProjectProgressResponse());

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenThrow(new RuntimeException());

        // When/Then
        assertThrows(OscException.class,
                () -> valveUnderTest.invoke(createValveContextWithScheduleTaskAndTaskParameter()));

        verify(projectOpenApiService, times(1)).resumeProject(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).describeProjectSteps(any(ProjectControlRequest.class));
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
    }

    private ValveContext createValveContextWithScheduleTaskAndTaskParameter() {
        return new ValveContext() {
            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }

            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }
        };
    }

    private ProjectStepVO createProjectStepVO() {
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setCurrentStep(OmsStepName.PRE_CHECK.name());
        projectStepVO.setCurrentStepStatus(TaskStatus.DONE.name());
        return projectStepVO;
    }

    private ProjectStepVO createProjectStepVOWithPrecheckFailed() {
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setCurrentStep(OmsStepName.PRE_CHECK.name());
        projectStepVO.setCurrentStepStatus(TaskStatus.FAILED.name());
        projectStepVO.setPrecheckResult(PrecheckResult.FAILED.name());
        return projectStepVO;
    }

    private ProjectProgressResponse createProjectProgressResponse() {
        ProjectProgressResponse projectProgressResponse = new ProjectProgressResponse();
        projectProgressResponse.setProjectId("projectId");
        projectProgressResponse.setCurrentStep(OmsStepName.PRE_CHECK.name());
        projectProgressResponse.setCurrentStepStatus(TaskStatus.DONE.name());
        return projectProgressResponse;
    }

    private ProjectFullVerifyResultResponse createProjectFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT.name());
        projectFullVerifyResultResponse.setFullVerificationProgressPercentage(100);
        return projectFullVerifyResultResponse;
    }

    private ProjectFullVerifyResultResponse createProjectFullVerifyResultResponseWithFullVerificationInconsistent() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.INCONSISTENT.name());
        projectFullVerifyResultResponse.setFullVerificationDescription("description");
        return projectFullVerifyResultResponse;
    }
}