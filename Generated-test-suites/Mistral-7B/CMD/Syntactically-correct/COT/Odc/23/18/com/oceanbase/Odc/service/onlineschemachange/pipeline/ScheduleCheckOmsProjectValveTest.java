package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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

/**
 * Test class for ScheduleCheckOmsProjectValve class
 */
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Mock
    private ProjectOpenApiService projectOpenApiServiceMock;
    @Mock
    private OscTaskCompleteHandler completeHandlerMock;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepositoryMock;
    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangePropertiesMock;
    @Mock
    private DefaultTableFactory defaultTableFactoryMock;

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;
    private ProjectControlRequest projectRequest;
    private List<ProjectStepVO> projectSteps;
    private ProjectProgressResponse progress;
    private ProjectStepResult projectStepResult;
    private ProjectFullVerifyResultResponse projectFullVerifyResultResponse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        scheduleTask = new ScheduleTaskEntity();
        projectRequest = new ProjectControlRequest();
        projectSteps = Lists.newArrayList();
        progress = new ProjectProgressResponse();
        projectStepResult = new ProjectStepResult();
        projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
    }

    // Test for invoke method
    @Test
    public void testInvoke() throws Exception {
        // Given
        taskParameter.setUid("uid");
        taskParameter.setOmsProjectId("projectId");
        projectRequest.setUid("uid");
        projectRequest.setId("projectId");
        doReturn(projectSteps).when(projectOpenApiServiceMock).describeProjectSteps(projectRequest);
        doReturn(progress).when(projectOpenApiServiceMock).describeProjectProgress(projectRequest);

        scheduleCheckOmsProjectValve.invoke(null);

        // Then
        verify(projectOpenApiServiceMock, times(2)).describeProjectSteps(projectRequest);
        verify(projectOpenApiServiceMock, times(1)).describeProjectProgress(projectRequest);
    }

    // Test for listProjectFullVerifyResult method
    @Test
    public void testListProjectFullVerifyResult() throws Exception {
        // Given
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";
        doReturn(projectFullVerifyResultResponse).when(projectOpenApiServiceMock).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));

        // When
        ScheduleCheckOmsProjectValve.ScheduleCheckOmsProjectValveResult result = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, databaseName, uid);

        // Then
        assertEquals(projectFullVerifyResultResponse, result);
    }

    // Test for getProjectRequest method
    @Test
    public void testGetProjectRequest() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        ProjectControlRequest projectControlRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);

        // Then
        assertEquals(taskParameter.getUid(), projectControlRequest.getUid());
        assertEquals(taskParameter.getOmsProjectId(), projectControlRequest.getId());
    }

    // Test for handleOmsProjectStepResult method - success case
    @Test
    public void testHandleOmsProjectStepResultSuccess() throws Exception {
        // Given
        taskParameter.setSwapTableType(SwapTableType.ALL);
        projectStepResult.setCurrentStep("FULL_TRANSFER");
        projectStepResult.setCurrentStepStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullTransferProgressPercentage(100.0);

        doReturn(projectSteps).when(projectOpenApiServiceMock).describeProjectSteps(projectRequest);
        doReturn(progress).when(projectOpenApiServiceMock).describeProjectProgress(projectRequest);
        doReturn(projectStepResult).when(projectOpenApiServiceMock).listProjectStepResult(any(String.class), any(String.class), any(String.class));

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);

        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, projectStepResult, null, SwapTableType.ALL, scheduleTask);

        verify(completeHandlerMock, times(1)).handleTaskComplete(eq(scheduleTask), resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
        assertEquals(TaskStatus.DONE, result.getStatus());
        assertEquals(100.0, result.getFullVerificationProgressPercentage(), 0.01);
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
    }

    // Test for handleOmsProjectStepResult method - failure case
    @Test(expected = OscException.class)
    public void testHandleOmsProjectStepResultFailure() throws Exception {
        // Given
        projectStepResult.setCurrentStep("FULL_TRANSFER");
        projectStepResult.setCurrentStepStatus(TaskStatus.FAILED);

        doReturn(projectSteps).when(projectOpenApiServiceMock).describeProjectSteps(projectRequest);
        doThrow(new OscException(ErrorCodes.OmsPreCheckFailed, "Error message")).when(projectOpenApiServiceMock).describeProjectProgress(projectRequest);

        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, projectStepResult, null, SwapTableType.ALL, scheduleTask);
    }

    // Test for continueHandleProjectStepResult method - precheck failed case
    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResultPrecheckFailed() throws Exception {
        // Given
        projectStepResult.setCurrentStep("FULL_VERIFIER");
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
    }

    // Test for continueHandleProjectStepResult method - full verification failed case
    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResultFullVerificationFailed() throws Exception {
        // Given
        projectStepResult.setCurrentStep("FULL_VERIFIER");
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
    }

    // Test for recordCurrentProgress method
    @Test
    public void testRecordCurrentProgress() {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setCurrentStep("CURRENT_STEP");
        result.setCurrentStepStatus(TaskStatus.RUNNING);

        // When
        ScheduleCheckOmsProjectValve.recordCurrentProgress(projectId, result);

        // Then
        List<String> body = Lists.newArrayList(projectId, OmsStepName.CURRENT_STEP.name(), "RUNNING", "", "");
        Table table = new DefaultTableFactory().generateTable(5, ScheduleCheckOmsProjectValveTest.getHeader(), body);
        String expectedTable = String.format("%-10s%-10s%-10s%-15s%-30s%n%s%n", "ProjectId", "Step", "Status", "Progress", "Description", "");
        assertEquals(expectedTable, table.render().toString());
    }

    private static List<String> getHeader() {
        return Lists.newArrayList("ProjectId", "Step", "Status", "Progress", "Description");
    }
}