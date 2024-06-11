package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

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
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.utils.TestUtils;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    private ProjectOpenApiService projectOpenApiService;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private DefaultTableFactory tableFactory;

    @Before
    public void setup() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = new OnlineSchemaChangeProperties();
        tableFactory = new DefaultTableFactory();
    }

    @Test
    public void testInvoke() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        String projectId = "projectId";
        String databaseName = "databaseName";
        String omsProjectId = "omsProjectId";
        String uid = "uid";
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus(TaskStatus.QUEUED.name());
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = TestUtils.createProjectSteps();
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = TestUtils.createProjectFullVerifyResultResponse();
        ProjectProgressResponse projectProgressResponse = TestUtils.createProjectProgressResponse();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(projectProgressResponse, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> projectFullVerifyResultResponse)
                        .withResumeProject(() -> {
                            projectOpenApiService.resumeProject(projectRequest);
                            return null;
                        })
                        .getCheckerResult();

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        OnlineSchemaChangeScheduleTaskResult lastResult = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        expectedResult.setManualSwapTableEnabled(true);

        // When
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.invoke(new ValveContext() {
            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }

            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }

            @Override
            public ProjectOpenApiService getProjectOpenApiService() {
                return projectOpenApiService;
            }

            @Override
            public OscTaskCompleteHandler getOscTaskCompleteHandler() {
                return completeHandler;
            }

            @Override
            public ScheduleTaskRepository getScheduleTaskRepository() {
                return scheduleTaskRepository;
            }

            @Override
            public OnlineSchemaChangeProperties getOnlineSchemaChangeProperties() {
                return onlineSchemaChangeProperties;
            }

            @Override
            public Properties getTaskProperties() {
                return new Properties();
            }
        });

        // Then
        assertEquals(expectedResult, JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class));
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService).listProjectFullVerifyResult(any(ProjectFullVerifyResultRequest.class));
        verifyZeroInteractions(projectOpenApiService).resumeProject(projectRequest);
    }

    @Test
    public void testHandleOmsProjectStepResult() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        String projectId = "projectId";
        String databaseName = "databaseName";
        String omsProjectId = "omsProjectId";
        String uid = "uid";
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus(TaskStatus.QUEUED.name());
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.FAILED, null, null, null, null);
        ProjectStepResult expectedProjectStepResult = new ProjectStepResult(TaskStatus.RUNNING, null, null, null, null);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(TestUtils.createProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(TestUtils.createProjectProgressResponse());
        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        // When
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.handleOmsProjectStepResult(new ValveContext() {
            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }

            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return scheduleTask;
            }

            @Override
            public ProjectOpenApiService getProjectOpenApiService() {
                return projectOpenApiService;
            }

            @Override
            public OscTaskCompleteHandler getOscTaskCompleteHandler() {
                return completeHandler;
            }

            @Override
            public ScheduleTaskRepository getScheduleTaskRepository() {
                return scheduleTaskRepository;
            }

            @Override
            public OnlineSchemaChangeProperties getOnlineSchemaChangeProperties() {
                return onlineSchemaChangeProperties;
            }

            @Override
            public Properties getTaskProperties() {
                return new Properties();
            }
        }, projectStepResult, new OnlineSchemaChangeScheduleTaskResult(taskParameter), SwapTableType.TABLE, scheduleTask);

        // Then
        verify(projectOpenApiService).resumeProject(projectRequest);
        assertNull(projectStepResult.getTaskStatus());
        assertEquals(expectedProjectStepResult.getCurrentStep(), projectStepResult.getCurrentStep());
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        // Given
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.FAILED, null, null, null, null);

        // When
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.continueHandleProjectStepResult(projectStepResult);

        // Then
        assertNull(projectStepResult.getTaskStatus());
    }

    @Test
    public void testListProjectFullVerifyResult() throws Exception {
        // Given
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";
        ProjectFullVerifyResultRequest request = new ProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] {databaseName});
        request.setDestSchemas(new String[] {databaseName});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(10);
        request.setPageNumber(1);
        request.setUid(uid);

        ProjectFullVerifyResultResponse expectedResult = new ProjectFullVerifyResultResponse();
        expectedResult.setData(TestUtils.createProjectFullVerifyResultData());

        // When
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        ProjectFullVerifyResultResponse result = valve.listProjectFullVerifyResult(request);

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAdaptResult() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(new OnlineSchemaChangeScheduleTaskParameters());
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.RUNNING, null, null, null, null);

        // When
        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.adaptResult(result, projectStepResult);

        // Then
        assertEquals(projectStepResult.getFullTransferEstimatedCount(), result.getFullTransferEstimatedCount());
        assertEquals(projectStepResult.getFullTransferFinishedCount(), result.getFullTransferFinishedCount());
        assertEquals(projectStepResult.getFullTransferProgressPercentage(), result.getFullTransferProgressPercentage());
        assertEquals(projectStepResult.getFullVerificationResult(), result.getFullVerificationResult());
        assertEquals(projectStepResult.getFullVerificationResultDescription(), result.getFullVerificationResultDescription());
        assertEquals(projectStepResult.getFullVerificationProgressPercentage(), result.getFullVerificationProgressPercentage());
        assertEquals(projectStepResult.getCurrentStep(), result.getCurrentStep());
        assertEquals(projectStepResult.getCurrentStepStatus(), result.getCurrentStepStatus());
        assertEquals(projectStepResult.getPreCheckResult(), result.getPrecheckResult());
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParam.getUid());
        projectRequest.setId(taskParam.getOmsProjectId());
        return projectRequest;
    }
}