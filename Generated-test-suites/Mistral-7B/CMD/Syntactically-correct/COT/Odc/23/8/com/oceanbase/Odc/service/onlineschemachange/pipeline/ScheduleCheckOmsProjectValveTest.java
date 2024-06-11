package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

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
import com.oceanbase.odc.model.OmsProject;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.oms.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

/**
 * Test class for ScheduleCheckOmsProjectValve
 */
public class ScheduleCheckOmsProjectValveTest {
    @Mock
    private ProjectOpenApiService projectOpenApiService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private DefaultTableFactory defaultTableFactory;

    @Mock
    private SwapTableUtil swapTableUtil;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() throws Exception {
        String projectId = "12345";
        String databaseName = "testdb";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName(databaseName);
        taskParameter.setUid("test_uid");
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus(TaskStatus.RUNNING.toString());
        ScheduleTaskEntity expectedScheduleTask = new ScheduleTaskEntity();
        expectedScheduleTask.setId(1L);
        expectedScheduleTask.setStatus(TaskStatus.DONE.toString());
        expectedScheduleTask.setResultJson(JsonUtils.toJson(getExpectedResult()));

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter.getOmsProjectId(),
                taskParameter.getDatabaseName());

        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(projectFullVerifyResultResponse);

        ProjectStepResult projectStepResult = new ProjectStepResult(
                new ProjectStepVO("PRE_CHECK", TaskStatus.DONE, PrecheckResult.PASSED),
                new ProjectStepVO("FULL_TRANSFER", TaskStatus.DONE, 1, 1, 100),
                new ProjectStepVO("FULL_VERIFIER", TaskStatus.DONE, FullVerificationResult.CONSISTENT),
                null);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(
                Lists.newArrayList(projectStepResult.getPrecheckStepVO(),
                        projectStepResult.getFullTransferStepVO(),
                        projectStepResult.getFullVerifierStepVO()));

        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(
                new ProjectProgressResponse(projectStepResult));

        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);

        when(swapTableUtil.isSwapTableEnable(SwapTableType.SCHEMA, scheduleTask.getStatus(), 100,
                FullVerificationResult.CONSISTENT, false)).thenReturn(true);

        when(completeHandler.handleTaskComplete(any())).thenReturn(null);

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(
                OnlineSchemaChangeScheduleTaskResult.class);

        scheduleCheckOmsProjectValve.invoke(null);

        OnlineSchemaChangeScheduleTaskResult result = scheduleCheckOmsProjectValve.getLastResult();
        assertEquals(expectedScheduleTask.getResultJson(), result.getResultJson());
        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    private OnlineSchemaChangeScheduleTaskResult getExpectedResult() {
        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult();
        expectedResult.setFullTransferEstimatedCount(1);
        expectedResult.setFullTransferFinishedCount(1);
        expectedResult.setFullTransferProgressPercentage(100);
        expectedResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        expectedResult.setFullVerificationResultDescription("Consistent");
        expectedResult.setFullVerificationProgressPercentage(100);
        expectedResult.setCurrentStep("FULL_TRANSFER");
        expectedResult.setCurrentStepStatus(TaskStatus.DONE);
        return expectedResult;
    }

    private ProjectControlRequest getProjectRequest(String projectId, String databaseName) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        projectRequest.setSourceSchemas(new String[] {databaseName});
        projectRequest.setDestSchemas(new String[] {databaseName});
        projectRequest.setUid("test_uid");
        return projectRequest;
    }

    @Test(expected = OscException.class)
    public void testInvokeWithOmsPreCheckFailed() throws Exception {
        String projectId = "12345";
        String databaseName = "testdb";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName(databaseName);
        taskParameter.setUid("test_uid");
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus(TaskStatus.RUNNING.toString());
        ScheduleTaskEntity expectedScheduleTask = new ScheduleTaskEntity();
        expectedScheduleTask.setId(1L);
        expectedScheduleTask.setStatus(TaskStatus.FAILED.toString());
        expectedScheduleTask.setResultJson(JsonUtils.toJson(getExpectedResultWithPrecheckFailed()));

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter.getOmsProjectId(),
                taskParameter.getDatabaseName());

        ProjectStepResult projectStepResult = new ProjectStepResult(
                new ProjectStepVO("PRE_CHECK", TaskStatus.FAILED, "Precheck failed"),
                null, null, null);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(
                Lists.newArrayList(projectStepResult.getPrecheckStepVO()));

        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(
                new ProjectProgressResponse(projectStepResult));

        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);

        when(swapTableUtil.isSwapTableEnable(SwapTableType.SCHEMA, scheduleTask.getStatus(), 0,
                FullVerificationResult.INCONSISTENT, false)).thenReturn(false);

        scheduleCheckOmsProjectValve.invoke(null);
    }

    private OnlineSchemaChangeScheduleTaskResult getExpectedResultWithPrecheckFailed() {
        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult();
        expectedResult.setFullTransferEstimatedCount(0);
        expectedResult.setFullTransferFinishedCount(0);
        expectedResult.setFullTransferProgressPercentage(0);
        expectedResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        expectedResult.setFullVerificationResultDescription("Data inconsistent");
        expectedResult.setFullVerificationProgressPercentage(0);
        expectedResult.setCurrentStep("PRE_CHECK");
        expectedResult.setCurrentStepStatus(TaskStatus.FAILED);
        return expectedResult;
    }
}