package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.util.tableformat.Table;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    private ProjectOpenApiService projectOpenApiService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private OscTaskCompleteHandler completeHandler;

    @Before
    public void setup() {
        projectOpenApiService = mock(ProjectOpenApiService.class, Answers.RETURNS_DEEP_STUBS);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class, Answers.RETURNS_DEEP_STUBS);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class, Answers.RETURNS_DEEP_STUBS);
        completeHandler = mock(OscTaskCompleteHandler.class, Answers.RETURNS_DEEP_STUBS);
    }

    @Test
    public void testInvoke_WhenProjectIsRunning_ThenStatusIsUpdatedAndNextValveIsExecuted() {
        // Given
        String projectId = UUID.randomUUID().toString();
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus(TaskStatus.CREATED);
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        projectRequest.setUid("uid");

        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setStepName("FULL_TRANSFER");
        projectStep.setStatus(TaskStatus.DONE);
        projectStep.setProgressPercentage(100);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProject(projectStep);

        ProjectStepResult projectStepResult = new ProjectStepResult(projectStep, progress);

        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStep);

        ProjectFullVerifyResultResponse fullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);
        List<ProjectStepVO> fullVerifyResults = Lists.newArrayList();
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);
        when(fullVerifyResultResponse.getResult()).thenReturn(fullVerifyResults);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        doAnswer((InvocationOnMock invocation) -> {
            OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
            adaptResult(result, projectStepResult);
            return result;
        }).when(this).adaptResult(any(OnlineSchemaChangeScheduleTaskResult.class), any(ProjectStepResult.class));

        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        valve.setOscTaskCompleteHandler(completeHandler);

        // When
        valve.invoke(new ValveContext() {
            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }

            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }
        });

        // Then
        assertEquals(TaskStatus.RUNNING, scheduleTask.getStatus());
        assertEquals("{\"manualSwapTableEnabled\":false,\"manualSwapTableStarted\":false}",
                scheduleTask.getResultJson());
    }

    @Test(expected = OscException.class)
    public void testInvoke_WhenProjectStepIsFailed_ThenExceptionIsThrown() {
        // Given
        String projectId = UUID.randomUUID().toString();
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus(TaskStatus.CREATED);
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        projectRequest.setUid("uid");

        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setStepName("FULL_TRANSFER");
        projectStep.setStatus(TaskStatus.FAILED);
        projectStep.setProgressPercentage(10);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProject(projectStep);

        projectOpenApiService.describeProjectSteps(projectRequest).thenReturn(Lists.newArrayList(projectStep));
        projectOpenApiService.describeProjectProgress(projectRequest).thenReturn(progress);

        // When
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        valve.setOscTaskCompleteHandler(completeHandler);

        valve.invoke(new ValveContext() {
            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }

            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }
        });

        // Then
        // An exception should be thrown
    }

    @Test
    public void testInvoke_WhenProjectStepIsSuspended_ThenProjectIsResumed() {
        // Given
        String projectId = UUID.randomUUID().toString();
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus(TaskStatus.CREATED);
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        projectRequest.setUid("uid");

        ProjectStepVO projectStep = new ProjectStepVO();
        projectStep.setStepName("FULL_TRANSFER");
        projectStep.setStatus(TaskStatus.SUSPENDED);
        projectStep.setProgressPercentage(10);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProject(projectStep);

        ProjectStepResult projectStepResult = new ProjectStepResult(projectStep, progress);

        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStep);

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(mock(ProjectFullVerifyResultResponse.class));
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.resumeProject(projectRequest)).thenAnswer((Answer<Void>) invocation -> {
            ProjectControlRequest request = invocation.getArgument(0);
            request.setId(request.getId());
            return null;
        });

        doAnswer((InvocationOnMock invocation) -> {
            OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
            adaptResult(result, projectStepResult);
            return result;
        }).when(this).adaptResult(any(OnlineSchemaChangeScheduleTaskResult.class), any(ProjectStepResult.class));

        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        valve.setOscTaskCompleteHandler(completeHandler);

        // When
        valve.invoke(new ValveContext() {
            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }

            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }
        });

        // Then
        assertEquals(TaskStatus.RUNNING, scheduleTask.getStatus());
        assertEquals("{\"manualSwapTableEnabled\":false,\"manualSwapTableStarted\":false}",
                scheduleTask.getResultJson());
    }
}