package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
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
    private ScheduleCheckOmsProjectValve testee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_whenInvokeIsCalled_thenExpectedMethodsAreCalledAndResultIsSaved() {
        // Given
        String projectId = "testProjectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        ProjectStepVO projectStepVO1 = createProjectStepVO("Step1", FullVerificationResult.CONSISTENT, 50, 100);
        ProjectProgressResponse progress1 = createProjectProgressResponse(projectStepVO1);
        ProjectStepVO projectStepVO2 = createProjectStepVO("Step2", FullVerificationResult.INCONSISTENT, 80, 100);
        ProjectProgressResponse progress2 = createProjectProgressResponse(projectStepVO2);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO1, projectStepVO2));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress1, progress2);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class))).thenReturn(createProjectFullVerifyResultResponse());
        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(scheduleTask);

        // When
        testee.invoke(createValveContext(projectId, taskParameters, scheduleTask));

        // Then
        verify(projectOpenApiService, times(2)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(2)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(scheduleTaskRepository, times(1)).update(scheduleTask);
    }

    @Test
    public void testInvoke_whenInvokeIsCalledWithInconsistentData_thenExceptionIsThrown() {
        // Given
        String projectId = "testProjectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        ProjectStepVO projectStepVO1 = createProjectStepVO("Step1", FullVerificationResult.CONSISTENT, 50, 100);
        ProjectStepVO projectStepVO2 = createProjectStepVO("Step2", FullVerificationResult.INCONSISTENT, 80, 100);
        ProjectProgressResponse progress1 = createProjectProgressResponse(projectStepVO1);
        ProjectProgressResponse progress2 = createProjectProgressResponse(projectStepVO2);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO1, projectStepVO2));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress1, progress2);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class))).thenThrow(new RuntimeException());

        // When & Then
        assertThrows(OscException.class, () -> testee.invoke(createValveContext(projectId, taskParameters, scheduleTask)));
    }

    @Test
    public void testInvoke_whenInvokeIsCalledWithFailedPrecheck_thenExceptionIsThrown() {
        // Given
        String projectId = "testProjectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        ProjectStepVO projectStepVO1 = createProjectStepVO("Step1", FullVerificationResult.CONSISTENT, 50, 100);
        ProjectStepVO projectStepVO2 = createProjectStepVO("Step2", PrecheckResult.FAILED, 80, 100);
        ProjectProgressResponse progress1 = createProjectProgressResponse(projectStepVO1);
        ProjectProgressResponse progress2 = createProjectProgressResponse(projectStepVO2);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO1, projectStepVO2));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress1, progress2);

        // When & Then
        assertThrows(OscException.class, () -> testee.invoke(createValveContext(projectId, taskParameters, scheduleTask)));
    }

    private ProjectControlRequest createProjectControlRequest(String projectId, String uid) {
        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        when(projectRequest.getId()).thenReturn(projectId);
        when(projectRequest.getUid()).thenReturn(uid);
        return projectRequest;
    }

    private List<ProjectStepVO> createProjectSteps(ProjectStepVO... projectStepVOs) {
        return Lists.newArrayList(projectStepVOs);
    }

    private ProjectProgressResponse createProjectProgressResponse(ProjectStepVO projectStepVO) {
        ProjectStepVO step = projectStepVO;
        ProjectProgressResponse projectProgressResponse = mock(ProjectProgressResponse.class);
        when(projectProgressResponse.getSteps()).thenReturn(createProjectSteps(step));
        return projectProgressResponse;
    }

    private ProjectFullVerifyResultResponse createProjectFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);
        return projectFullVerifyResultResponse;
    }

    private ProjectStepVO createProjectStepVO(String name, FullVerificationResult result, int percentage, int total) {
        ProjectStepVO projectStepVO = mock(ProjectStepVO.class);
        when(projectStepVO.getName()).thenReturn(name);
        when(projectStepVO.getStatus()).thenReturn(result.name());
        when(projectStepVO.getPercentage()).thenReturn(percentage);
        when(projectStepVO.getTotal()).thenReturn(total);
        return projectStepVO;
    }

    private ScheduleTaskEntity createScheduleTaskEntity() {
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        return scheduleTask;
    }

    private ValveContext createValveContext(String projectId, OnlineSchemaChangeScheduleTaskParameters taskParameters,
            ScheduleTaskEntity scheduleTask) {
        ValveContext valveContext = mock(ValveContext.class);
        when(valveContext.getScheduleTask()).thenReturn(scheduleTask);
        when(valveContext.getTaskParameter()).thenReturn(taskParameters);
        when(valveContext.getParameter()).thenReturn(new OnlineSchemaChangePipelineParameters());
        when(valveContext.getOmsProjectId()).thenReturn(projectId);
        return valveContext;
    }

    private OnlineSchemaChangePipelineParameters createOnlineSchemaChangePipelineParameters() {
        OnlineSchemaChangePipelineParameters onlineSchemaChangePipelineParameters = mock(OnlineSchemaChangePipelineParameters.class);
        return onlineSchemaChangePipelineParameters;
    }
}