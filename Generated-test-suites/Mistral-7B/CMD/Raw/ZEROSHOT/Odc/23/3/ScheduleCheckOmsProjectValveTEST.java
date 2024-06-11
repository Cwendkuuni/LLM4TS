package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.MockitoAnnotations;

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

    @InjectMocks
    private ScheduleCheckOmsProjectValve testee;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    private ScheduleTaskEntity createScheduleTaskEntity() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        return scheduleTask;
    }

    private OnlineSchemaChangeScheduleTaskParameters createTaskParameter() {
        return new OnlineSchemaChangeScheduleTaskParameters();
    }

    private List<ProjectStepVO> createProjectStepsList() {
        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        return projectSteps;
    }

    private ProjectProgressResponse createProjectProgressResponse() {
        ProjectProgressResponse projectProgressResponse = new ProjectProgressResponse();
        return projectProgressResponse;
    }

    private ProjectStepResult createProjectStepResult() {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        return projectStepResult;
    }

    private ProjectControlRequest createProjectControlRequest() {
        ProjectControlRequest projectControlRequest = new ProjectControlRequest();
        return projectControlRequest;
    }

    private ProjectFullVerifyResultResponse createProjectFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        return projectFullVerifyResultResponse;
    }

    private OnlineSchemaChangeScheduleTaskResult createOnlineSchemaChangeScheduleTaskResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        return result;
    }

    private void whenListProjectFullVerifyResultReturns(ProjectFullVerifyResultResponse expected) {
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(expected);
    }

    private void whenDescribeProjectProgressReturns(ProjectProgressResponse expected) {
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(expected);
    }

    private void whenDescribeProjectStepsReturns(List<ProjectStepVO> expected) {
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(expected);
    }

    private void whenResumeProjectReturnsNull() {
        when(projectOpenApiService.resumeProject(any(ProjectControlRequest.class)))
                .thenReturn(null);
    }

    @Test
    public void testInvoke_WhenProjectStepsAreDone_ThenResultIsUpdated() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        ProjectControlRequest projectRequest = createProjectControlRequest();
        List<ProjectStepVO> projectSteps = createProjectStepsList();
        ProjectProgressResponse projectProgress = createProjectProgressResponse();
        ProjectStepResult projectStepResult = createProjectStepResult();
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = createProjectFullVerifyResultResponse();
        whenListProjectFullVerifyResultReturns(projectFullVerifyResultResponse);
        whenDescribeProjectProgressReturns(projectProgress);
        whenDescribeProjectStepsReturns(projectSteps);
        whenResumeProjectReturnsNull();

        when(onlineSchemaChangeProperties.isEnableFullVerify())
                .thenReturn(true);

        // When
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        testee.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals(1.0f, result.getProgressPercentage(), 0.01);
    }

    @Test(expected = OscException.class)
    public void testInvoke_WhenProjectStepsIsFailed_ThenExceptionIsThrown() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        ProjectControlRequest projectRequest = createProjectControlRequest();
        List<ProjectStepVO> projectSteps = createProjectStepsList();
        ProjectProgressResponse projectProgress = createProjectProgressResponse();
        ProjectStepResult projectStepResult = createProjectStepResult();
        whenListProjectFullVerifyResultReturns(null);
        whenDescribeProjectProgressReturns(projectProgress);
        whenDescribeProjectStepsReturns(projectSteps);

        // When
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        testee.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        // Then
        // An exception should be thrown
    }

    @Test
    public void testInvoke_WhenProjectStepsAreRunning_ThenResultIsUpdated() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        ProjectControlRequest projectRequest = createProjectControlRequest();
        List<ProjectStepVO> projectSteps = createProjectStepsList();
        ProjectProgressResponse projectProgress = createProjectProgressResponse();
        ProjectStepResult projectStepResult = createProjectStepResult();
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = createProjectFullVerifyResultResponse();
        whenListProjectFullVerifyResultReturns(projectFullVerifyResultResponse);
        whenDescribeProjectProgressReturns(projectProgress);
        whenDescribeProjectStepsReturns(projectSteps);
        whenResumeProjectReturnsNull();

        when(onlineSchemaChangeProperties.isEnableFullVerify())
                .thenReturn(false);

        // When
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        testee.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(FullVerificationResult.UNCHECK, result.getFullVerificationResult());
        assertEquals(1.0f, result.getProgressPercentage(), 0.01);
    }

    @Test
    public void testInvoke_WhenProjectStepsAreSuspended_ThenResultIsUpdated() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = createTaskParameter();
        ProjectControlRequest projectRequest = createProjectControlRequest();
        List<ProjectStepVO> projectSteps = createProjectStepsList();
        ProjectProgressResponse projectProgress = createProjectProgressResponse();
        ProjectStepResult projectStepResult = createProjectStepResult();
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = createProjectFullVerifyResultResponse();
        whenListProjectFullVerifyResultReturns(projectFullVerifyResultResponse);
        whenDescribeProjectProgressReturns(projectProgress);
        whenDescribeProjectStepsReturns(projectSteps);
        whenResumeProjectReturnsNull();

        when(onlineSchemaChangeProperties.isEnableFullVerify())
                .thenReturn(true);

        when(projectProgress.getStatus()).thenReturn(TaskStatus.SUSPENDED.toString());

        // When
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        testee.invoke(new ValveContext() {
            {
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }
        });

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(FullVerificationResult.SUSPENDED, result.getFullVerificationResult());
        assertEquals(1.0f, result.getProgressPercentage(), 0.01);
    }
}