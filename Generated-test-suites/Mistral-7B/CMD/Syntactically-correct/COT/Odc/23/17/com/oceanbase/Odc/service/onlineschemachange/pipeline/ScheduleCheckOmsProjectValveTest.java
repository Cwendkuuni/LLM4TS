package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.constant.ErrorCodes;
import com.oceanbase.odc.common.exception.OscException;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscExceptionFactory;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.validate.OnlineSchemaChangeValidator;

public class ScheduleCheckOmsProjectValveTest {
    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;
    private ProjectOpenApiService projectOpenApiService;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OscTaskCompleteHandler completeHandler;
    private DefaultTableFactory defaultTableFactory;
    private SwapTableUtil swapTableUtil;
    private ProjectOpenApiService projectOpenApiServiceMock;
    private OnlineSchemaChangeValidator onlineSchemaChangeValidatorMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        projectOpenApiServiceMock = mock(ProjectOpenApiService.class);
        onlineSchemaChangeValidatorMock = mock(OnlineSchemaChangeValidator.class);
        valve.setProjectOpenApiService(projectOpenApiServiceMock);
        valve.setOnlineSchemaChangeValidator(onlineSchemaChangeValidatorMock);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setCompleteHandler(completeHandler);
        defaultTableFactory = mock(DefaultTableFactory.class);
        valve.setDefaultTableFactory(defaultTableFactory);
        swapTableUtil = mock(SwapTableUtil.class);
        valve.setSwapTableUtil(swapTableUtil);
    }

    @Test
    public void testInvoke() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        when(projectOpenApiServiceMock.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(new ProjectStepVO()));
        when(projectOpenApiServiceMock.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

        ProjectStepResult projectStepResult = valve.new ProjectStepResultChecker(null, null, null).getCheckerResult();
        valve.invoke(mock(ValveContext.class));

        assertEquals(TaskStatus.DONE, projectStepResult.getTaskStatus());
        assertNull(projectStepResult.getErrorMsg());
        assertFalse(projectStepResult.isFullVerificationNeeded());
    }

    @Test
    public void testInvokeWithFullVerificationNeeded() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setEnableFullVerify(true);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        when(projectOpenApiServiceMock.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(new ProjectStepVO()));
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectFullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        when(projectOpenApiServiceMock.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        ProjectStepResult projectStepResult = valve.new ProjectStepResultChecker(null, null, null).getCheckerResult();
        OscException exception = assertThrows(OscException.class, () -> valve.invoke(mock(ValveContext.class)));
        assertEquals(ErrorCodes.OmsDataCheckInconsistent, exception.getErrorCode());
        assertEquals("Task failed for origin table has inconsistent data with new table", exception.getMessage());
    }

    @Test
    public void testInvokeWithPrecheckFailed() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setCurrentStep(OmsStepName.PRE_CHECK.name());
        projectStepVO.setPreCheckResult(PrecheckResult.FAILED);
        when(projectOpenApiServiceMock.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));

        OscException exception = assertThrows(OscException.class, () -> valve.invoke(mock(ValveContext.class)));
        assertEquals(ErrorCodes.OmsPreCheckFailed, exception.getErrorCode());
        assertEquals(projectStepVO.getErrorMsg(), exception.getMessage());
    }

    @Test
    public void testInvokeWithProjectNotFound() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        when(projectOpenApiServiceMock.describeProjectSteps(projectRequest)).thenThrow(new OscException(ErrorCodes.ProjectNotFound));

        OscException exception = assertThrows(OscException.class, () -> valve.invoke(mock(ValveContext.class)));
        assertEquals(ErrorCodes.ProjectNotFound, exception.getErrorCode());
    }

    @Test
    public void testHandleOmsProjectStepResultWithSwapTableEnabledAndFinished() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setSwapTableType(SwapTableType.ALL);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.CONSISTENT, null, null);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        valve.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult, result, SwapTableType.ALL, scheduleTask);
        assertTrue(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
    }

    @Test
    public void testHandleOmsProjectStepResultWithSwapTableNotEnabledAndNotStarted() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.CONSISTENT, null, null);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        when(swapTableUtil.isSwapTableEnable(SwapTableType.ALL, TaskStatus.DONE, 100, FullVerificationResult.CONSISTENT, false))
                .thenReturn(false);
        valve.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult, result, SwapTableType.ALL, scheduleTask);

        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
    }

    @Test
    public void testHandleOmsProjectStepResultWithSwapTableEnabledAndNotFinished() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.RUNNING, FullVerificationResult.UNCHECK, null, null);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        valve.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult, result, SwapTableType.ALL, scheduleTask);
        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
    }

    @Test
    public void testHandleOmsProjectStepResultWithSwapTableDisabledAndFinished() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.CONSISTENT, null, null);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        when(swapTableUtil.isSwapTableEnable(SwapTableType.ALL, TaskStatus.DONE, 100, FullVerificationResult.CONSISTENT, false))
                .thenReturn(false);
        valve.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult, result, SwapTableType.ALL, scheduleTask);

        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
    }

    @Test
    public void testHandleOmsProjectStepResultWithSwapTableDisabledAndNotFinished() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.RUNNING, FullVerificationResult.UNCHECK, null, null);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        when(swapTableUtil.isSwapTableEnable(SwapTableType.ALL, TaskStatus.DONE, 100, FullVerificationResult.CONSISTENT, false))
                .thenReturn(false);
        valve.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult, result, SwapTableType.ALL, scheduleTask);

        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
    }

    @Test
    public void testListProjectFullVerifyResult() {
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] {databaseName});
        request.setDestSchemas(new String[] {databaseName});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(1);
        request.setPageNumber(1);
        request.setUid(uid);

        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        response.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        when(projectOpenApiServiceMock.listProjectFullVerifyResult(request)).thenReturn(response);

        valve.listProjectFullVerifyResult(projectId, databaseName, uid);
        assertEquals(FullVerificationResult.CONSISTENT, valve.fullVerificationResult);
    }

    @Test
    public void testListProjectFullVerifyResultWithError() {
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] {databaseName});
        request.setDestSchemas(new String[] {databaseName});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(1);
        request.setPageNumber(1);
        request.setUid(uid);

        OscException exception = new OscException(ErrorCodes.OmsProjectNotFound);
        when(projectOpenApiServiceMock.listProjectFullVerifyResult(request)).thenThrow(exception);

        assertThrows(OscException.class, () -> valve.listProjectFullVerifyResult(projectId, databaseName, uid));
        assertEquals(ErrorCodes.OmsProjectNotFound, valve.errorCode);
    }

    @Test
    public void testListProjectFullVerifyResultWithNoResult() {
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] {databaseName});
        request.setDestSchemas(new String[] {databaseName});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(1);
        request.setPageNumber(1);
        request.setUid(uid);

        when(projectOpenApiServiceMock.listProjectFullVerifyResult(request)).thenReturn(null);

        valve.listProjectFullVerifyResult(projectId, databaseName, uid);
        assertNull(valve.fullVerificationResult);
    }
}