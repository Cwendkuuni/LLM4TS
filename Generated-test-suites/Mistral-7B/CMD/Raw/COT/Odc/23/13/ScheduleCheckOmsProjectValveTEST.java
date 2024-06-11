package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
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
import com.oceanbase.odc.service.onlineschemachange.utils.TestUtils;

/**
 * Test class for ScheduleCheckOmsProjectValve.
 */
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    private ProjectOpenApiService projectOpenApiService;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private DefaultTableFactory tableFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        projectOpenApiService = Mockito.mock(ProjectOpenApiService.class);
        completeHandler = Mockito.mock(OscTaskCompleteHandler.class);
        scheduleTaskRepository = Mockito.mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = Mockito.mock(OnlineSchemaChangeProperties.class);
        tableFactory = Mockito.mock(DefaultTableFactory.class);

        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
        scheduleCheckOmsProjectValve.setCompleteHandler(completeHandler);
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
        scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        scheduleCheckOmsProjectValve.setTableFactory(tableFactory);
    }

    @Test
    public void testInvoke_whenInvokeSuccessfully_thenReturn() {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = TestUtils.createScheduleTaskEntity();

        ProjectControlRequest projectRequest = Mockito.mock(ProjectControlRequest.class);
        ProjectStepVO projectStepVO = TestUtils.createProjectStepVO();
        ProjectProgressResponse progressResponse = TestUtils.createProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(projectStepVO, progressResponse, null, null);

        Mockito.when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(
                Lists.newArrayList(projectStepVO));
        Mockito.when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progressResponse);

        // when
        scheduleCheckOmsProjectValve.invoke(TestUtils.createValveContext(taskParameter, scheduleTask));

        // then
        verifyInvokeSuccessfully(scheduleTask);
    }

    @Test(expected = OscException.class)
    public void testInvoke_whenInvokeWithPreCheckFailed_thenThrowOscException() {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = TestUtils.createScheduleTaskEntity();

        ProjectControlRequest projectRequest = Mockito.mock(ProjectControlRequest.class);
        ProjectStepVO projectStepVO = TestUtils.createProjectStepVO();
        ProjectProgressResponse progressResponse = TestUtils.createProjectProgressResponse();

        ProjectStepResult projectStepResult = new ProjectStepResult(projectStepVO, progressResponse,
                PrecheckResult.FAILED, "pre check failed error message");

        Mockito.when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(
                Lists.newArrayList(projectStepVO));
        Mockito.when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progressResponse);

        // when
        scheduleCheckOmsProjectValve.invoke(TestUtils.createValveContext(taskParameter, scheduleTask));

        // then
        verifyInvokeWithPrecheckFailed(scheduleTask, projectStepResult);
    }

    @Test(expected = OscException.class)
    public void testInvoke_whenInvokeWithInconsistentData_thenThrowOscException() {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = TestUtils.createScheduleTaskEntity();

        ProjectControlRequest projectRequest = Mockito.mock(ProjectControlRequest.class);
        ProjectStepVO projectStepVO = TestUtils.createProjectStepVO();
        ProjectProgressResponse progressResponse = TestUtils.createProjectProgressResponse();

        ProjectStepResult projectStepResult = new ProjectStepResult(projectStepVO, progressResponse,
                FullVerificationResult.INCONSISTENT, "data inconsistent error message");

        Mockito.when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(
                Lists.newArrayList(projectStepVO));
        Mockito.when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progressResponse);

        // when
        scheduleCheckOmsProjectValve.invoke(TestUtils.createValveContext(taskParameter, scheduleTask));

        // then
        verifyInvokeWithInconsistentData(scheduleTask, projectStepResult);
    }

    @Test
    public void testHandleOmsProjectStepResult_whenHandleDoneAndConsistent_thenSwapTableEnabled() {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = TestUtils.createScheduleTaskEntity();

        ProjectControlRequest projectRequest = Mockito.mock(ProjectControlRequest.class);
        ProjectStepVO projectStepVO1 = TestUtils.createProjectStepVO();
        ProjectStepVO projectStepVO2 = TestUtils.createProjectStepVO();
        ProjectProgressResponse progressResponse1 = TestUtils.createProjectProgressResponse();
        ProjectProgressResponse progressResponse2 = TestUtils.createProjectProgressResponse();

        ProjectStepResult projectStepResult = new ProjectStepResult(projectStepVO1, progressResponse1,
                FullVerificationResult.CONSISTENT, null);
        ProjectStepResult projectStepResult2 = new ProjectStepResult(projectStepVO2, progressResponse2,
                FullVerificationResult.CONSISTENT, null);

        Mockito.when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(
                Lists.newArrayList(projectStepVO1, projectStepVO2));
        Mockito.when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progressResponse1);
        Mockito.when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progressResponse2);

        SwapTableType swapTableType = SwapTableType.NONE;

        // when
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(TestUtils.createValveContext(), projectStepResult,
                TestUtils.createOnlineSchemaChangeScheduleTaskResult(), swapTableType, scheduleTask);

        // then
        verifySwapTableEnabled(scheduleTask);
    }

    @Test
    public void testHandleOmsProjectStepResult_whenHandleDoneButUncheck_thenSwapTableNotEnabled() {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = TestUtils.createScheduleTaskEntity();

        ProjectControlRequest projectRequest = Mockito.mock(ProjectControlRequest.class);
        ProjectStepVO projectStepVO1 = TestUtils.createProjectStepVO();
        ProjectStepVO projectStepVO2 = TestUtils.createProjectStepVO();
        ProjectProgressResponse progressResponse1 = TestUtils.createProjectProgressResponse();
        ProjectProgressResponse progressResponse2 = TestUtils.createProjectProgressResponse();

        ProjectStepResult projectStepResult = new ProjectStepResult(projectStepVO1, progressResponse1,
                FullVerificationResult.UNCHECK, null);
        ProjectStepResult projectStepResult2 = new ProjectStepResult(projectStepVO2, progressResponse2,
                FullVerificationResult.UNCHECK, null);

        Mockito.when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(
                Lists.newArrayList(projectStepVO1, projectStepVO2));
        Mockito.when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progressResponse1);
        Mockito.when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progressResponse2);

        SwapTableType swapTableType = SwapTableType.NONE;

        // when
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(TestUtils.createValveContext(), projectStepResult,
                TestUtils.createOnlineSchemaChangeScheduleTaskResult(), swapTableType, scheduleTask);

        // then
        verifySwapTableNotEnabled(scheduleTask);
    }

    @Test
    public void testListProjectFullVerifyResult_whenListSuccessfully_thenReturn() {
        // given
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();

        Mockito.when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(response);

        // when
        OnlineSchemaChangeScheduleTaskResult result = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId,
                databaseName, uid);

        // then
        verifyListProjectFullVerifyResultSuccessfully(result);
    }

    @Test
    public void testAdaptResult_whenAdaptSuccessfully_thenReturn() {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult(null, null, null, null);

        // when
        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        // then
        verifyAdaptResultSuccessfully(result);
    }

    private void verifyInvokeSuccessfully(ScheduleTaskEntity scheduleTask) {
        assertEquals(TaskStatus.RUNNING.name(), scheduleTask.getStatus());
        assertNull(scheduleTask.getResultJson());
    }

    private void verifyInvokeWithPrecheckFailed(ScheduleTaskEntity scheduleTask, ProjectStepResult projectStepResult) {
        assertEquals(TaskStatus.FAILED.name(), scheduleTask.getStatus());
        assertSame(ErrorCodes.OmsPreCheckFailed.getCode(), JsonUtils.fromJson(scheduleTask.getErrorJson(),
                ErrorCodes.class).getCode());
        assertEquals(projectStepResult.getErrorMsg(), JsonUtils.fromJson(scheduleTask.getErrorJson(),
                ErrorCodes.class).getMessage());
    }

    private void verifyInvokeWithInconsistentData(ScheduleTaskEntity scheduleTask, ProjectStepResult projectStepResult) {
        assertEquals(TaskStatus.FAILED.name(), scheduleTask.getStatus());
        assertSame(ErrorCodes.OmsDataCheckInconsistent.getCode(), JsonUtils.fromJson(scheduleTask.getErrorJson(),
                ErrorCodes.class).getCode());
        assertEquals(projectStepResult.getFullVerificationResultDescription(), JsonUtils.fromJson(scheduleTask.getErrorJson(),
                ErrorCodes.class).getMessage());
    }

    private void verifySwapTableEnabled(ScheduleTaskEntity scheduleTask) {
        assertTrue(JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class)
                .isManualSwapTableEnabled());
        assertTrue(JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class)
                .isManualSwapTableStarted());
    }

    private void verifySwapTableNotEnabled(ScheduleTaskEntity scheduleTask) {
        assertFalse(JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class)
                .isManualSwapTableEnabled());
        assertNull(JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class)
                .isManualSwapTableStarted());
    }

    private void verifyListProjectFullVerifyResultSuccessfully(OnlineSchemaChangeScheduleTaskResult result) {
        assertNull(result.getFullVerificationResult());
        assertNull(result.getFullVerificationResultDescription());
    }

    private void verifyAdaptResultSuccessfully(OnlineSchemaChangeScheduleTaskResult result) {
        assertNull(result.getFullTransferEstimatedCount());
        assertNull(result.getFullTransferFinishedCount());
        assertNull(result.getFullVerificationResult());
        assertNull(result.getFullVerificationResultDescription());
        assertNull(result.getPrecheckResult());
        assertNull(result.getPrecheckResultDescription());
        assertNull(result.getCurrentStep());
        assertNull(result.getCurrentStepStatus());
    }
}