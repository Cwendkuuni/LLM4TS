package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

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

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    private ProjectOpenApiService projectOpenApiService;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setup() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
        scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testInvoke() throws Exception {
        String projectId = "testProjectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setOmsProjectId(projectId);
        taskParameters.setDatabaseName("testDatabaseName");
        taskParameters.setUid("testUid");
        taskParameters.setSwapTableType(SwapTableType.SWAP_TABLE_WITH_BACKUP);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameters);

        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        ProjectStepVO projectStepVO1 = new ProjectStepVO();
        projectStepVO1.setName("Step1");
        projectStepVO1.setStatus(TaskStatus.RUNNING.toString());
        ProjectStepVO projectStepVO2 = new ProjectStepVO();
        projectStepVO2.setName("Step2");
        projectStepVO2.setStatus(TaskStatus.DONE.toString());
        projectSteps.add(projectStepVO1);
        projectSteps.add(projectStepVO2);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setTotalSteps(2);
        progress.setCurrentStep(1);
        progress.setCurrentStepStatus(TaskStatus.RUNNING.toString());

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectFullVerifyResultResponse fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        fullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        ListProjectFullVerifyResultRequest fullVerifyResultRequest = new ListProjectFullVerifyResultRequest();
        fullVerifyResultRequest.setProjectId(projectId);
        fullVerifyResultRequest.setSourceSchemas(new String[] {"testDatabaseName"});
        fullVerifyResultRequest.setDestSchemas(new String[] {"testDatabaseName"});
        fullVerifyResultRequest.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        fullVerifyResultRequest.setPageSize(10);
        fullVerifyResultRequest.setPageNumber(1);
        fullVerifyResultRequest.setUid("testUid");

        when(projectOpenApiService.listProjectFullVerifyResult(fullVerifyResultRequest)).thenReturn(fullVerifyResultResponse);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> fullVerifyResultResponse)
                        .withResumeProject(() -> null)
                        .getCheckerResult();

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameters);

        scheduleCheckOmsProjectValve.invoke(new ValveContext() {{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameters);
        }});

        assertEquals(TaskStatus.DONE.toString(), projectStepResult.getCurrentStepStatus());
        assertEquals(1, projectStepResult.getFullTransferFinishedCount());
        assertEquals(1, projectStepResult.getFullTransferEstimatedCount());
        assertEquals(100, projectStepResult.getFullTransferProgressPercentage());
        assertEquals(FullVerificationResult.CONSISTENT, projectStepResult.getFullVerificationResult());
        assertEquals("", result.getResultJson());
        assertEquals(100, result.getProgressPercentage());
        assertNotNull(scheduleTask.getResultJson());
    }

    @Test(expected = OscException.class)
    public void testInvoke_PreCheckFailed() throws Exception {
        String projectId = "testProjectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setOmsProjectId(projectId);
        taskParameters.setDatabaseName("testDatabaseName");
        taskParameters.setUid("testUid");
        taskParameters.setSwapTableType(SwapTableType.SWAP_TABLE_WITH_BACKUP);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameters);

        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        ProjectStepVO projectStepVO1 = new ProjectStepVO();
        projectStepVO1.setName("Step1");
        projectStepVO1.setStatus(TaskStatus.RUNNING.toString());
        projectStepVO1.setPreCheckResult(PrecheckResult.FAILED);
        projectStepVO1.setPreCheckResultDescription("Precheck Failed");

        ProjectStepVO projectStepVO2 = new ProjectStepVO();
        projectStepVO2.setName("Step2");
        projectStepVO2.setStatus(TaskStatus.DONE.toString());

        projectSteps.add(projectStepVO1);
        projectSteps.add(projectStepVO2);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setTotalSteps(2);
        progress.setCurrentStep(1);
        progress.setCurrentStepStatus(TaskStatus.RUNNING.toString());

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectFullVerifyResultResponse fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        fullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        ListProjectFullVerifyResultRequest fullVerifyResultRequest = new ListProjectFullVerifyResultRequest();
        fullVerifyResultRequest.setProjectId(projectId);
        fullVerifyResultRequest.setSourceSchemas(new String[] {"testDatabaseName"});
        fullVerifyResultRequest.setDestSchemas(new String[] {"testDatabaseName"});
        fullVerifyResultRequest.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        fullVerifyResultRequest.setPageSize(10);
        fullVerifyResultRequest.setPageNumber(1);
        fullVerifyResultRequest.setUid("testUid");

        when(projectOpenApiService.listProjectFullVerifyResult(fullVerifyResultRequest)).thenReturn(fullVerifyResultResponse);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> fullVerifyResultResponse)
                        .withResumeProject(() -> null)
                        .getCheckerResult();

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameters);

        scheduleCheckOmsProjectValve.invoke(new ValveContext() {{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameters);
        }});

        throw new AssertionError("Unexpected result: Test case should have thrown OscException");
    }

    @Test(expected = OscException.class)
    public void testInvoke_DataInconsistent() throws Exception {
        String projectId = "testProjectId";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setOmsProjectId(projectId);
        taskParameters.setDatabaseName("testDatabaseName");
        taskParameters.setUid("testUid");
        taskParameters.setSwapTableType(SwapTableType.SWAP_TABLE_WITH_BACKUP);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameters);

        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        ProjectStepVO projectStepVO1 = new ProjectStepVO();
        projectStepVO1.setName("Step1");
        projectStepVO1.setStatus(TaskStatus.RUNNING.toString());
        projectStepVO1.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        projectStepVO1.setFullVerificationResultDescription("Data Inconsistent");

        ProjectStepVO projectStepVO2 = new ProjectStepVO();
        projectStepVO2.setName("Step2");
        projectStepVO2.setStatus(TaskStatus.DONE.toString());

        projectSteps.add(projectStepVO1);
        projectSteps.add(projectStepVO2);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setTotalSteps(2);
        progress.setCurrentStep(1);
        progress.setCurrentStepStatus(TaskStatus.RUNNING.toString());

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectFullVerifyResultResponse fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        fullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        ListProjectFullVerifyResultRequest fullVerifyResultRequest = new ListProjectFullVerifyResultRequest();
        fullVerifyResultRequest.setProjectId(projectId);
        fullVerifyResultRequest.setSourceSchemas(new String[] {"testDatabaseName"});
        fullVerifyResultRequest.setDestSchemas(new String[] {"testDatabaseName"});
        fullVerifyResultRequest.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        fullVerifyResultRequest.setPageSize(10);
        fullVerifyResultRequest.setPageNumber(1);
        fullVerifyResultRequest.setUid("testUid");

        when(projectOpenApiService.listProjectFullVerifyResult(fullVerifyResultRequest)).thenReturn(fullVerifyResultResponse);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> fullVerifyResultResponse)
                        .withResumeProject(() -> null)
                        .getCheckerResult();

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameters);

        scheduleCheckOmsProjectValve.invoke(new ValveContext() {{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameters);
        }});

        throw new AssertionError("Unexpected result: Test case should have thrown OscException");
    }
}