package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.constant.ErrorCodes;
import com.oceanbase.odc.common.exception.OscException;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscExceptionType;
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
import com.oceanbase.odc.service.onlineschemachange.utils.JsonTestUtil;

@SuppressWarnings("unchecked")
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
    public void testInvoke_success() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setUid("123456");
        taskParameter.setOmsProjectId("project-1");

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = getMockProjectSteps();
        ProjectProgressResponse progress = getMockProjectProgressResponse();

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                true).withCheckerVerifyResult(getMockFullVerifyResultResponse())
                .withResumeProject(mock(Runnable.class)).getCheckerResult();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        valveUnderTest.invoke(mock(ValveContext.class));

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(
                valveUnderTest.getClass().getDeclaredField("scheduleTask").get(valveUnderTest),
                OnlineSchemaChangeScheduleTaskResult.class);

        assertEquals(projectStepResult.getTaskPercentage(), result.getProgressPercentage());
        assertEquals(projectStepResult.getCurrentStep(), result.getCurrentStep());

        ProjectStepResult expectedStepResult = new ProjectStepResult(projectSteps.get(0), projectStepResult.getFullVerificationResult(),
                projectStepResult.getFullVerificationResultDescription(), projectStepResult.getFullTransferEstimatedCount(),
                projectStepResult.getFullTransferFinishedCount(), projectStepResult.getFullTransferProgressPercentage(),
                projectStepResult.getPreCheckResult(), projectStepResult.getPrecheckResultDescription(),
                projectStepResult.getCurrentStepStatus());
        assertEquals(expectedStepResult, result.getCurrentStepResult());
    }

    @Test(expected = OscException.class)
    public void testInvoke_precheck_failed() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setUid("123456");
        taskParameter.setOmsProjectId("project-1");

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = getMockProjectSteps();
        ProjectProgressResponse progress = getMockProjectProgressResponse();

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                true).withCheckerVerifyResult(getMockFullVerifyResultResponseWithFailedPrecheck())
                .withResumeProject(mock(Runnable.class)).getCheckerResult();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        valveUnderTest.invoke(mock(ValveContext.class));

        fail("Expected OscException to be thrown but it wasn't");
    }

    @Test(expected = OscException.class)
    public void testInvoke_full_verification_failed() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setUid("123456");
        taskParameter.setOmsProjectId("project-1");

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = getMockProjectSteps();
        ProjectProgressResponse progress = getMockProjectProgressResponse();

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                true).withCheckerVerifyResult(getMockFullVerifyResultResponseWithFailedFullVerification())
                .withResumeProject(mock(Runnable.class)).getCheckerResult();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        valveUnderTest.invoke(mock(ValveContext.class));

        fail("Expected OscException to be thrown but it wasn't");
    }

    @Test
    public void testInvoke_next_step() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setUid("123456");
        taskParameter.setOmsProjectId("project-1");

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = getMockProjectSteps();
        ProjectProgressResponse progress = getMockProjectProgressResponse();

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                true).withCheckerVerifyResult(getMockFullVerifyResultResponse())
                .withResumeProject(mock(Runnable.class)).getCheckerResult();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        valveUnderTest.invoke(mock(ValveContext.class));

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(
                valveUnderTest.getClass().getDeclaredField("scheduleTask").get(valveUnderTest),
                OnlineSchemaChangeScheduleTaskResult.class);

        assertEquals(projectStepResult.getCurrentStep(), result.getCurrentStep());
        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());

        ProjectStepResult nextStepResult = new ProjectStepResultChecker(progress, projectSteps,
                true).withCheckerVerifyResult(getMockFullVerifyResultResponse())
                .withResumeProject(mock(Runnable.class)).getCheckerResult();

        valveUnderTest.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult, result,
                SwapTableType.MANUAL, mock(ScheduleTaskEntity.class));

        assertEquals(nextStepResult.getCurrentStep(), result.getCurrentStep());
        assertTrue(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
    }

    @Test
    public void testInvoke_not_next_step() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setUid("123456");
        taskParameter.setOmsProjectId("project-1");

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = getMockProjectSteps();
        ProjectProgressResponse progress = getMockProjectProgressResponse();

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                true).withCheckerVerifyResult(getMockFullVerifyResultResponse())
                .withResumeProject(mock(Runnable.class)).getCheckerResult();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectStepResult nextStepResult = new ProjectStepResultChecker(progress, projectSteps,
                true).withCheckerVerifyResult(getMockFullVerifyResultResponse())
                .withResumeProject(mock(Runnable.class)).getCheckerResult();

        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        valveUnderTest.invoke(mock(ValveContext.class));

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(
                valveUnderTest.getClass().getDeclaredField("scheduleTask").get(valveUnderTest),
                OnlineSchemaChangeScheduleTaskResult.class);

        assertEquals(projectStepResult.getCurrentStep(), result.getCurrentStep());
        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());

        valveUnderTest.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult, result,
                SwapTableType.MANUAL, mock(ScheduleTaskEntity.class));

        assertEquals(projectStepResult.getCurrentStep(), result.getCurrentStep());
        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParam.getUid());
        projectRequest.setId(taskParam.getOmsProjectId());
        return projectRequest;
    }

    private List<ProjectStepVO> getMockProjectSteps() {
        String currentStepName = OmsStepName.FULL_TRANSFER.name();
        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        ProjectStepVO projectStep1 = new ProjectStepVO();
        projectStep1.setStepStatus(TaskStatus.RUNNING.name());
        projectStep1.setStepName(OmsStepName.PRE_CHECK.name());
        projectStep1.setStepDescription("Pre check description");
        ProjectStepVO projectStep2 = new ProjectStepVO();
        projectStep2.setStepStatus(StringUtils.isBlank(currentStepName) ? TaskStatus.RUNNING.name() : TaskStatus.PENDING.name());
        projectStep2.setStepName(currentStepName);
        projectStep2.setStepDescription(currentStepName + " description");
        projectSteps.add(projectStep1);
        projectSteps.add(projectStep2);
        return projectSteps;
    }

    private ProjectProgressResponse getMockProjectProgressResponse() {
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProjectId("project-1");
        progress.setStatus("RUNNING");
        return progress;
    }

    private ProjectFullVerifyResultResponse getMockFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        response.setProjectId("project-1");
        response.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        response.setFullVerificationResultDescription("Consistent result description");
        return response;
    }

    private ProjectFullVerifyResultResponse getMockFullVerifyResultResponseWithFailedPrecheck() {
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        response.setProjectId("project-1");
        response.setFullVerificationResult(FullVerificationResult.FAILED);
        response.setFullVerificationResultDescription("Failed precheck result description");
        return response;
    }

    private ProjectFullVerifyResultResponse getMockFullVerifyResultResponseWithFailedFullVerification() {
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        response.setProjectId("project-1");
        response.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        response.setFullVerificationResultDescription("Inconsistent result description");
        return response;
    }
}