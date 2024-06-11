package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Properties;

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

/**
 * Test class for ScheduleCheckOmsProjectValve
 */
@SuppressWarnings("unchecked")
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
    private DefaultTableFactory tableFactory;

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;
    private ProjectControlRequest projectRequest;
    private ProjectStepVO projectStepVO;
    private ProjectProgressResponse projectProgressResponse;
    private ProjectFullVerifyResultResponse projectFullVerifyResultResponse;
    private ProjectStepResult projectStepResult;
    private OnlineSchemaChangeScheduleTaskResult result;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        scheduleTask = new ScheduleTaskEntity();
        projectRequest = new ProjectControlRequest();
        projectStepVO = new ProjectStepVO();
        projectProgressResponse = new ProjectProgressResponse();
        projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        result = new OnlineSchemaChangeScheduleTaskResult();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);
    }

    @Test
    public void testInvoke() {
        // Given
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid("uid");
        scheduleTask.setId("scheduleTaskId");
        projectRequest.setUid("uid");
        projectRequest.setId("projectId");
        projectStepVO.setName("stepName");
        projectProgressResponse.setCurrentStep(projectStepVO.getName());
        projectProgressResponse.setCurrentStepStatus(TaskStatus.RUNNING.name());
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectFullVerifyResultResponse.setFullVerificationResultDescription("description");
        projectStepResult = new ProjectStepResult(projectStepVO, projectProgressResponse,
                projectFullVerifyResultResponse, onlineSchemaChangeProperties.isEnableFullVerify());
        when(scheduleTaskRepository.findById("scheduleTaskId")).thenReturn(java.util.Optional.of(scheduleTask));

        // When
        valve.invoke(null);

        // Then
        assertEquals(TaskStatus.RUNNING, projectStepResult.getCurrentStepStatus());
        assertEquals(FullVerificationResult.CONSISTENT, projectStepResult.getFullVerificationResult());
        assertEquals("description", projectStepResult.getFullVerificationResultDescription());
        assertEquals(1.0, projectStepResult.getFullTransferProgressPercentage().floatValue(), 0.0);
        assertEquals(1, projectStepResult.getFullTransferFinishedCount());
        assertEquals(1, projectStepResult.getFullTransferEstimatedCount());
    }

    @Test(expected = OscException.class)
    public void testInvokeWithPrecheckFailed() {
        // Given
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid("uid");
        scheduleTask.setId("scheduleTaskId");
        projectRequest.setUid("uid");
        projectRequest.setId("projectId");
        projectStepVO.setName("stepName");
        projectProgressResponse.setCurrentStep(projectStepVO.getName());
        projectProgressResponse.setCurrentStepStatus(TaskStatus.RUNNING.name());
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.FAILED);
        projectFullVerifyResultResponse.setFullVerificationResultDescription("description");
        projectStepResult = new ProjectStepResult(projectStepVO, projectProgressResponse,
                projectFullVerifyResultResponse, onlineSchemaChangeProperties.isEnableFullVerify());
        when(scheduleTaskRepository.findById("scheduleTaskId")).thenReturn(java.util.Optional.of(scheduleTask));

        // When
        valve.invoke(null);

        // Then
        throw new AssertionError("Test should throw OscException with PrecheckFailed error code");
    }

    @Test(expected = OscException.class)
    public void testInvokeWithDataInconsistent() {
        // Given
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid("uid");
        scheduleTask.setId("scheduleTaskId");
        projectRequest.setUid("uid");
        projectRequest.setId("projectId");
        projectStepVO.setName("stepName");
        projectProgressResponse.setCurrentStep(projectStepVO.getName());
        projectProgressResponse.setCurrentStepStatus(TaskStatus.RUNNING.name());
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        projectFullVerifyResultResponse.setFullVerificationResultDescription("description");
        projectStepResult = new ProjectStepResult(projectStepVO, projectProgressResponse,
                projectFullVerifyResultResponse, onlineSchemaChangeProperties.isEnableFullVerify());
        when(scheduleTaskRepository.findById("scheduleTaskId")).thenReturn(java.util.Optional.of(scheduleTask));

        // When
        valve.invoke(null);

        // Then
        throw new AssertionError("Test should throw OscException with DataCheckInconsistent error code");
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // Given
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid("uid");
        scheduleTask.setId("scheduleTaskId");
        projectRequest.setUid("uid");
        projectRequest.setId("projectId");
        projectStepVO.setName("stepName");
        projectProgressResponse.setCurrentStep(projectStepVO.getName());
        projectProgressResponse.setCurrentStepStatus(TaskStatus.RUNNING.name());
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectFullVerifyResultResponse.setFullVerificationResultDescription("description");
        projectStepResult = new ProjectStepResult(projectStepVO, projectProgressResponse,
                projectFullVerifyResultResponse, onlineSchemaChangeProperties.isEnableFullVerify());
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);

        // When
        valve.handleOmsProjectStepResult(null, projectStepResult, result, SwapTableType.AUTO, scheduleTask);

        // Then
        assertEquals(true, result.isManualSwapTableEnabled());
        assertEquals(true, result.isManualSwapTableStarted());

        // Given
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.UNCHECK);

        // When
        valve.handleOmsProjectStepResult(null, projectStepResult, result, SwapTableType.AUTO, scheduleTask);

        // Then
        assertEquals(true, result.isManualSwapTableEnabled());
        assertEquals(false, result.isManualSwapTableStarted());
    }

    @Test
    public void testListProjectFullVerifyResult() {
        // Given
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";
        List<ProjectFullVerifyResultResponse> projectFullVerifyResultResponses = Lists.newArrayList();
        projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectFullVerifyResultResponse.setFullVerificationResultDescription("description");
        projectFullVerifyResultResponse.setProjectId(projectId);
        projectFullVerifyResultResponse.setDatabaseName(databaseName);
        projectFullVerifyResultResponses.add(projectFullVerifyResultResponse);

        // When
        List<ProjectFullVerifyResultResponse> responses = valve.listProjectFullVerifyResult(taskParameter.getOmsProjectId(),
                taskParameter.getDatabaseName(), taskParameter.getUid());

        // Then
        assertEquals(1, responses.size());
        assertEquals(FullVerificationResult.CONSISTENT, responses.get(0).getFullVerificationResult());
        assertEquals("description", responses.get(0).getFullVerificationResultDescription());
    }
}