package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_whenProjectStepsIsEmpty_thenReturnResultWithStatusRunning() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        ProjectProgressResponse progress = new ProjectProgressResponse();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        valveUnderTest.invoke(createValveContext(taskParameter));

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(Mockito.argumentCaptor(String.class)
                .capture(Mockito.any()), OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.RUNNING, result.getStatus());
    }

    @Test
    public void testInvoke_whenProjectStepsHasRunningStep_thenReturnResultWithStatusRunning() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(createProjectStepVO(OmsStepName.FULL_TRANSFER));
        ProjectProgressResponse progress = new ProjectProgressResponse();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        valveUnderTest.invoke(createValveContext(taskParameter));

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(Mockito.argumentCaptor(String.class)
                .capture(Mockito.any()), OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.RUNNING, result.getStatus());
    }

    @Test
    public void testInvoke_whenProjectStepsHasDoneStep_thenReturnResultWithStatusDone() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(createProjectStepVO(OmsStepName.FULL_TRANSFER,
                TaskStatus.DONE));
        ProjectProgressResponse progress = new ProjectProgressResponse();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        valveUnderTest.invoke(createValveContext(taskParameter));

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(Mockito.argumentCaptor(String.class)
                .capture(Mockito.any()), OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    @Test(expected = OscException.class)
    public void testInvoke_whenProjectStepsHasFailedStep_thenThrowException() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(createProjectStepVO(OmsStepName.FULL_TRANSFER,
                TaskStatus.FAILED));
        ProjectProgressResponse progress = new ProjectProgressResponse();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        valveUnderTest.invoke(createValveContext(taskParameter));

        // Then
        throw new AssertionError(); // should not reach this point
    }

    @Test
    public void testInvoke_whenProjectStepsHasPrecheckFailedStep_thenThrowException() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(createProjectStepVO(OmsStepName.PRE_CHECK,
                PrecheckResult.FAILED));
        ProjectProgressResponse progress = new ProjectProgressResponse();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When & Then
        assertThrows(OscException.class, () -> valveUnderTest.invoke(createValveContext(taskParameter)));
    }

    @Test
    public void testInvoke_whenProjectStepsHasInconsistentStep_thenThrowException() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(createProjectStepVO(OmsStepName.FULL_VERIFIER,
                FullVerificationResult.INCONSISTENT));
        ProjectProgressResponse progress = new ProjectProgressResponse();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When & Then
        assertThrows(OscException.class, () -> valveUnderTest.invoke(createValveContext(taskParameter)));
    }

    @Test
    public void testInvoke_whenProjectStepsHasSuspendStep_thenReturnResultWithStatusSuspended() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(createProjectStepVO(OmsStepName.FULL_TRANSFER,
                TaskStatus.SUSPENDED));
        ProjectProgressResponse progress = new ProjectProgressResponse();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        valveUnderTest.invoke(createValveContext(taskParameter));

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(Mockito.argumentCaptor(String.class)
                .capture(Mockito.any()), OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.SUSPENDED, result.getStatus());
    }

    @Test
    public void testRecordCurrentProgress_whenCurrentStepIsNotNull_thenRecordCurrentProgress() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        String projectId = UUID.randomUUID().toString();
        when(scheduleTaskRepository.update(Mockito.any())).thenReturn(scheduleTask);

        // When
        valveUnderTest.recordCurrentProgress(projectId, result);

        // Then
        String progress = result.getCurrentStepProgressPercentage().toString();
        String description = result.getCurrentStepStatus().name();
        String step = result.getCurrentStep().name();
        verify(defaultTableFactory).generateTable(eq(5), eq(getHeader()), isA(List.class));
        verify(log).info(startsWith("ProjectId, Step, Status, Progress, Description\n"));
        verify(log).info(containsString(String.format("%s, %s, %s, %s, %s\n", projectId, step, description, progress,
                result.getCurrentStepDescription())));
    }

    @Test
    public void testInvoke_whenProjectStepsHasNoStep_thenReturnResultWithStatusRunning() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        ProjectProgressResponse progress = new ProjectProgressResponse();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        valveUnderTest.invoke(createValveContext(taskParameter));

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(Mockito.argumentCaptor(String.class)
                .capture(Mockito.any()), OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.RUNNING, result.getStatus());
    }

    private ProjectStepVO createProjectStepVO(OmsStepName step, TaskStatus status) {
        return new ProjectStepVO("step1", step.name(), status.name(), 1);
    }

    private ProjectStepVO createProjectStepVO(OmsStepName step) {
        return new ProjectStepVO("step1", step.name(), TaskStatus.RUNNING.name(), 1);
    }

    private ProjectStepVO createProjectStepVO(OmsStepName step, PrecheckResult precheckResult) {
        return new ProjectStepVO("step1", step.name(), precheckResult.name(), 1, precheckResult.getErrorMsg());
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        return new ProjectControlRequest();
    }

    private OnlineSchemaChangeScheduleTaskParameters createTaskParameters() {
        return new OnlineSchemaChangeScheduleTaskParameters();
    }

    private ValveContext createValveContext(OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        return new ValveContext() {
            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }
        };
    }
}