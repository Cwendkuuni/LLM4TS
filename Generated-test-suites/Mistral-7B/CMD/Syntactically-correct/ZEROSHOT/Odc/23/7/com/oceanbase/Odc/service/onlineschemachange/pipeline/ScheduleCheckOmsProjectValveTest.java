package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;

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
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.testutils.TestUtil;

@SuppressWarnings("unchecked")
@SuppressWarnings("ResultOfMethodCallIgnored")
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
    private ScheduleCheckOmsProjectValve valveUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = TestUtil.generateOnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = TestUtil.generateScheduleTaskEntity(taskParameter);

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(TestUtil.generateProjectSteps());

        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(TestUtil.generateProjectProgressResponse());

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(TestUtil.generateProjectFullVerifyResultResponse());

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class)))
                .thenReturn(scheduleTask);

        valveUnderTest.invoke(mock(ValveContext.class));

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);

        assertEquals(TaskStatus.DONE, result.getStatus());
        assertEquals(100.0, result.getProgressPercentage(), 0.01);
    }

    @Test(expected = OscException.class)
    public void testInvokeWhenProjectStepResultFail() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = TestUtil.generateOnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = TestUtil.generateScheduleTaskEntity(taskParameter);

        ProjectStepResult projectStepResult = new ProjectStepResult(
                TestUtil.generateProjectProgressResponse(), TestUtil.generateProjectSteps(),
                FullVerificationResult.INCONSISTENT, "test error message");

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(TestUtil.generateProjectSteps());

        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(projectStepResult.getProgress());

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenThrow(new RuntimeException());

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class)))
                .thenReturn(scheduleTask);

        valveUnderTest.invoke(mock(ValveContext.class));
    }

    @Test(expected = OscException.class)
    public void testInvokeWhenProjectStepResultPrecheckFailed() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = TestUtil.generateOnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = TestUtil.generateScheduleTaskEntity(taskParameter);

        ProjectStepResult projectStepResult = new ProjectStepResult(
                TestUtil.generateProjectProgressResponse(), TestUtil.generateProjectSteps(),
                PrecheckResult.FAILED, "test error message");

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(TestUtil.generateProjectSteps());

        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(projectStepResult.getProgress());

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(TestUtil.generateProjectFullVerifyResultResponse());

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class)))
                .thenReturn(scheduleTask);

        valveUnderTest.invoke(mock(ValveContext.class));
    }

    @Test
    public void testAdaptResult() {
        ProjectStepResult projectStepResult = new ProjectStepResult(
                TestUtil.generateProjectProgressResponse(), TestUtil.generateProjectSteps(),
                FullVerificationResult.CONSISTENT, null, 50, 100, 1, 10);
        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult(
                TestUtil.generateOnlineSchemaChangeScheduleTaskParameters());
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(
                TestUtil.generateOnlineSchemaChangeScheduleTaskParameters());

        adaptResult(result, projectStepResult);

        assertEquals(projectStepResult.getFullTransferEstimatedCount(), result.getFullTransferEstimatedCount());
        assertEquals(projectStepResult.getFullTransferFinishedCount(), result.getFullTransferFinishedCount());
        assertEquals(projectStepResult.getFullTransferProgressPercentage(), result.getFullTransferProgressPercentage());
        assertEquals(projectStepResult.getFullVerificationResult(), result.getFullVerificationResult());
        assertEquals(projectStepResult.getFullVerificationResultDescription(), result.getFullVerificationResultDescription());
        assertEquals(projectStepResult.getFullVerificationProgressPercentage(), result.getFullVerificationProgressPercentage());
        assertEquals(projectStepResult.getCurrentStep(), result.getCurrentStep());
        assertEquals(projectStepResult.getCurrentStepStatus(), result.getCurrentStepStatus());
        assertEquals(projectStepResult.getPreCheckResult(), result.getPrecheckResult());
        assertEquals(projectStepResult.getPreCheckResultDescription(), result.getPrecheckResultDescription());

        assertEquals(expectedResult, result);
    }

    @Test
    public void testListProjectFullVerifyResult() {
        String projectId = "testProjectId";
        String databaseName = "testDatabaseName";
        String uid = "testUid";

        ProjectFullVerifyResultResponse expectedResponse = new ProjectFullVerifyResultResponse();
        expectedResponse.setProjectId(projectId);
        expectedResponse.setRecords(new ProjectFullVerifyResultResponse.Record[]{
            new ProjectFullVerifyResultResponse.Record(1L, 1L, FullVerificationResult.CONSISTENT, "test description")
        });

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[]{databaseName});
        request.setDestSchemas(new String[]{databaseName});
        request.setStatus(new String[]{"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(1);
        request.setPageNumber(1);
        request.setUid(uid);

        ProjectFullVerifyResultResponse result = valveUnderTest.listProjectFullVerifyResult(request);

        assertEquals(expectedResponse.getProjectId(), result.getProjectId());
        assertEquals(Objects.requireNonNull(expectedResponse.getRecords()).length,
                Objects.requireNonNull(result.getRecords()).length);
    }

    private void adaptResult(OnlineSchemaChangeScheduleTaskResult result, ProjectStepResult projectStepResult) {
        result.setFullTransferEstimatedCount(projectStepResult.getFullTransferEstimatedCount());
        result.setFullTransferFinishedCount(projectStepResult.getFullTransferFinishedCount());
        result.setFullTransferProgressPercentage(projectStepResult.getFullTransferProgressPercentage());
        result.setFullVerificationResult(projectStepResult.getFullVerificationResult());
        result.setFullVerificationResultDescription(projectStepResult.getFullVerificationResultDescription());
        result.setFullVerificationProgressPercentage(projectStepResult.getFullVerificationProgressPercentage());
        result.setCurrentStep(projectStepResult.getCurrentStep());
        result.setCurrentStepStatus(projectStepResult.getCurrentStepStatus());
        result.setPrecheckResult(projectStepResult.getPreCheckResult());
        result.setPrecheckResultDescription(projectStepResult.getPreCheckResultDescription());
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParam.getUid());
        projectRequest.setId(taskParam.getOmsProjectId());
        return projectRequest;
    }
}