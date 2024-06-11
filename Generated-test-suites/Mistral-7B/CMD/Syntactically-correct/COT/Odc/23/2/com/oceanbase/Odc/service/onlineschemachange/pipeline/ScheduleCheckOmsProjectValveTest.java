package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

@RunWith(MockitoJUnitRunner.class)
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
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;

    @Before
    public void setup() {
        taskParameter = TestUtils.generateOnlineSchemaChangeScheduleTaskParameters();
        scheduleTask = TestUtils.generateScheduleTaskEntity();
    }

    @Test
    public void testInvoke() throws Exception {
        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(TestUtils.generateProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(TestUtils.generateProjectProgressResponse());

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(
                TestUtils.generateProjectProgressResponse(), TestUtils.generateProjectSteps(),
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .getCheckerResult();

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        adaptResult(result, projectStepResult);

        scheduleCheckOmsProjectValve.invoke(TestUtils.generateValveContext(scheduleTask));

        ProjectStepResult expectedProjectStepResult = new ProjectStepResult(
                TestUtils.generateProjectStepVO(), TestUtils.generateProjectStepVO(),
                TaskStatus.DONE, FullVerificationResult.CONSISTENT, null, null, null);
        ProjectStepResult actualProjectStepResult = projectStepResult;

        assertEquals(expectedProjectStepResult.getCurrentStep(), actualProjectStepResult.getCurrentStep());
        assertEquals(expectedProjectStepResult.getCurrentStepStatus(), actualProjectStepResult.getCurrentStepStatus());
        assertEquals(expectedProjectStepResult.getFullTransferEstimatedCount(),
                result.getFullTransferEstimatedCount());
        assertEquals(expectedProjectStepResult.getFullTransferFinishedCount(),
                result.getFullTransferFinishedCount());
        assertEquals(expectedProjectStepResult.getFullVerificationResult(), result.getFullVerificationResult());
        assertEquals(expectedProjectStepResult.getFullVerificationResultDescription(),
                result.getFullVerificationResultDescription());
        assertEquals(expectedProjectStepResult.getFullVerificationProgressPercentage(),
                result.getFullVerificationProgressPercentage());
        assertEquals(expectedProjectStepResult.getPreCheckResult(), result.getPrecheckResult());
        assertEquals(expectedProjectStepResult.getPreCheckResultDescription(), result.getPrecheckResultDescription());
    }

    @Test(expected = OscException.class)
    public void testInvokePreCheckFailed() throws Exception {
        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(TestUtils.generateProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest))
                .thenReturn(TestUtils.generateProjectProgressResponseWithPrecheckFailed());

        scheduleCheckOmsProjectValve.invoke(TestUtils.generateValveContext(scheduleTask));
    }

    @Test(expected = OscException.class)
    public void testInvokeDataCheckInconsistent() throws Exception {
        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(TestUtils.generateProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest))
                .thenReturn(TestUtils.generateProjectProgressResponseWithInconsistentData());

        scheduleCheckOmsProjectValve.invoke(TestUtils.generateValveContext(scheduleTask));
    }

    @Test
    public void testAdaptResult() throws Exception {
        ProjectStepResult projectStepResult = new ProjectStepResult(
                TestUtils.generateProjectStepVO(), TestUtils.generateProjectStepVO(),
                TaskStatus.DONE, FullVerificationResult.CONSISTENT, null, null, null);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        adaptResult(result, projectStepResult);

        assertEquals(projectStepResult.getFullTransferEstimatedCount(), result.getFullTransferEstimatedCount());
        assertEquals(projectStepResult.getFullTransferFinishedCount(), result.getFullTransferFinishedCount());
        assertEquals(projectStepResult.getFullTransferProgressPercentage(), result.getFullTransferProgressPercentage());
        assertEquals(projectStepResult.getFullVerificationResult(), result.getFullVerificationResult());
        assertEquals(projectStepResult.getFullVerificationResultDescription(),
                result.getFullVerificationResultDescription());
        assertEquals(projectStepResult.getFullVerificationProgressPercentage(),
                result.getFullVerificationProgressPercentage());
        assertEquals(projectStepResult.getCurrentStep(), result.getCurrentStep());
        assertEquals(projectStepResult.getCurrentStepStatus(), result.getCurrentStepStatus());
        assertEquals(projectStepResult.getPreCheckResult(), result.getPrecheckResult());
        assertEquals(projectStepResult.getPreCheckResultDescription(), result.getPrecheckResultDescription());
    }

    @Test
    public void testListProjectFullVerifyResult() throws Exception {
        String projectId = "project_id";
        String databaseName = "database_name";
        String uid = "uid";

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] {databaseName});
        request.setDestSchemas(new String[] {databaseName});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(10);
        request.setPageNumber(1);
        request.setUid(uid);

        ProjectFullVerifyResultResponse response = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(request);

        assertEquals(response.getTotalCount(), 2);
        assertEquals(response.getData().size(), 2);
        assertEquals(response.getData().get(0).getProjectId(), projectId);
    }

    @Test
    public void testGetProjectRequest() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);

        assertEquals(taskParameter.getProjectId(), projectRequest.getId());
        assertEquals(taskParameter.getUid(), projectRequest.getUid());
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
}