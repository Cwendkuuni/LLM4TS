package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.model.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.utils.TestUtils;

/**
 * @author yaobin
 */
public class ScheduleCheckOmsProjectValveTest {

    private static final String SCHEDULE_TASK_ID = "1";
    private static final String OMS_PROJECT_ID = "projectId";
    private static final String DATABASE_NAME = "databaseName";
    private static final String UID = UUID.randomUUID().toString();
    private static final String TASK_PARAMETER_JSON = "{\"swpTableType\":\"SWAP_TABLE\",\"databaseName\":\""
            + DATABASE_NAME + "\",\"omsProjectId\":\"" + OMS_PROJECT_ID + "\"}";
    private static final int FULL_TRANSFER_PERCENTAGE = 50;
    private static final int FULL_VERIFICATION_PERCENTAGE = 75;

    private ProjectOpenApiService projectOpenApiService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private OscTaskCompleteHandler completeHandler;
    private DefaultTableFactory defaultTableFactory;
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    private ProjectOpenApiService projectOpenApiServiceMock;
    private ScheduleTaskEntity scheduleTaskEntityMock;

    @Before
    public void setup() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        defaultTableFactory = mock(DefaultTableFactory.class);
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
        scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        scheduleCheckOmsProjectValve.setOscTaskCompleteHandler(completeHandler);
        scheduleCheckOmsProjectValve.setDefaultTableFactory(defaultTableFactory);

        projectOpenApiServiceMock = mock(ProjectOpenApiService.class);
        scheduleTaskEntityMock = mock(ScheduleTaskEntity.class);

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(TestUtils.getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(TestUtils.getProjectProgressResponse());
        when(projectOpenApiService.resumeProject(any(ProjectControlRequest.class)))
                .thenReturn(null);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(TestUtils.getProjectFullVerifyResultResponse());

        when(scheduleTaskRepository.findOne(SCHEDULE_TASK_ID)).thenReturn(scheduleTaskEntityMock);
        when(scheduleTaskEntityMock.getId()).thenReturn(SCHEDULE_TASK_ID);
        when(scheduleTaskEntityMock.getResultJson()).thenReturn(StringUtils.EMPTY);
        when(scheduleTaskEntityMock.getStatus()).thenReturn("RUNNING");
    }

    @Captor
    private ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor;

    @Test
    public void testInvoke_whenInvokeSuccessfully_thenUpdateScheduleTaskResultAndRecordCurrentProgress() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = JsonUtils.fromJson(TASK_PARAMETER_JSON,
                OnlineSchemaChangeScheduleTaskParameters.class);

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = TestUtils.createProjectStepResult(OmsStepName.FULL_TRANSFER,
                FULL_TRANSFER_PERCENTAGE, PrecheckResult.PASSED, FullVerificationResult.CONSISTENT);
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = TestUtils
                .createProjectFullVerifyResultResponse(LISTS_NEW_INSTANCE, FULL_VERIFICATION_PERCENTAGE);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(TestUtils.getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(TestUtils
                .getProjectProgressResponse());
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        scheduleCheckOmsProjectValve.invoke(TestUtils.createValveContext());

        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(scheduleTaskRepository).update(scheduleTaskEntityMock);
        verify(defaultTableFactory).generateTable(anyList(), anyList(), anyList(), anyList(), anyList());

        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
        assertEquals(taskParameter.getDatabaseName(), result.getDatabaseName());
        assertEquals(taskParameter.getOmsProjectId(), result.getOmsProjectId());
        assertEquals(SwapTableType.SWAP_TABLE, result.getSwapTableType());
        assertEquals(taskParameter.getUid(), result.getUid());
        assertEquals(ProjectStatus.SUCCESS, result.getStatus());
        assertEquals(FULL_TRANSFER_PERCENTAGE, result.getFullTransferProgressPercentage());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals(FullVerificationResult.CONSISTENT.name(), result.getFullVerificationResultDescription());
        assertEquals(FULL_VERIFICATION_PERCENTAGE, result.getFullVerificationProgressPercentage());
        assertEquals(OmsStepName.FULL_TRANSFER.name(), result.getCurrentStep());
        assertEquals(TaskStatus.SUCCESS, result.getCurrentStepStatus());
    }

    @Test(expected = OscException.class)
    public void testInvoke_whenPreCheckFailed_thenThrowOscException() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = JsonUtils.fromJson(TASK_PARAMETER_JSON,
                OnlineSchemaChangeScheduleTaskParameters.class);

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = TestUtils.createProjectStepResult(OmsStepName.PRE_CHECK, 0,
                PrecheckResult.FAILED, FullVerificationResult.UNCHECK);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(TestUtils.getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(TestUtils
                .getProjectProgressResponse());

        scheduleCheckOmsProjectValve.invoke(TestUtils.createValveContext());

        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, never()).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(scheduleTaskRepository, never()).update(scheduleTaskEntityMock);

        throw new OscException(ErrorCodes.OmsPreCheckFailed, "Pre check failed message");
    }

    @Test(expected = OscException.class)
    public void testInvoke_whenDataCheckFailed_thenThrowOscException() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = JsonUtils.fromJson(TASK_PARAMETER_JSON,
                OnlineSchemaChangeScheduleTaskParameters.class);

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = TestUtils.createProjectStepResult(OmsStepName.FULL_TRANSFER,
                FULL_TRANSFER_PERCENTAGE, PrecheckResult.PASSED, FullVerificationResult.INCONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(TestUtils.getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(TestUtils
                .getProjectProgressResponse());
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(TestUtils.getProjectFullVerifyResultResponse());

        scheduleCheckOmsProjectValve.invoke(TestUtils.createValveContext());

        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(scheduleTaskRepository, never()).update(scheduleTaskEntityMock);

        throw new OscException(ErrorCodes.OmsDataCheckInconsistent, "Data check failed message");
    }

    @Test
    public void testHandleOmsProjectStepResult_whenInvokeSuccessfullyAndFullTransferCompleted_thenOpenManualSwapTable() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = JsonUtils.fromJson(TASK_PARAMETER_JSON,
                OnlineSchemaChangeScheduleTaskParameters.class);

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = TestUtils.createProjectStepResult(OmsStepName.FULL_TRANSFER,
                100, PrecheckResult.PASSED, FullVerificationResult.CONSISTENT);
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = TestUtils
                .createProjectFullVerifyResultResponse(LISTS_NEW_INSTANCE, FULL_VERIFICATION_PERCENTAGE);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(TestUtils.getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(TestUtils
                .getProjectProgressResponse());
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(TestUtils.createValveContext(), projectStepResult,
                resultCaptor.capture(), SwapTableType.SWAP_TABLE, scheduleTaskEntityMock);

        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
        assertEquals(true, result.isManualSwapTableEnabled());
        assertEquals(false, result.isManualSwapTableStarted());
    }

    @Test
    public void testHandleOmsProjectStepResult_whenInvokeSuccessfullyAndFullTransferNotCompleted_thenContinueHandleProjectStepResult() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = JsonUtils.fromJson(TASK_PARAMETER_JSON,
                OnlineSchemaChangeScheduleTaskParameters.class);

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = TestUtils.createProjectStepResult(OmsStepName.FULL_TRANSFER,
                50, PrecheckResult.PASSED, FullVerificationResult.CONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(TestUtils.getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(TestUtils
                .getProjectProgressResponse());

        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(TestUtils.createValveContext(), projectStepResult,
                resultCaptor.capture(), SwapTableType.SWAP_TABLE, scheduleTaskEntityMock);

        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
        assertEquals(false, result.isManualSwapTableEnabled());
        assertEquals(false, result.isManualSwapTableStarted());
    }
}