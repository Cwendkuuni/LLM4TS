package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.oceanbase.odc.service.onlineschemachange.utils.OmsProjectTestUtils;

@Component
public class ScheduleCheckOmsProjectValveTest {

    private static final String PROJECT_ID = "testProjectId";
    private static final String DATABASE_NAME = "testDatabaseName";
    private static final String UID = "testUid";

    private ProjectOpenApiService projectOpenApiService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private OscTaskCompleteHandler completeHandler;

    private ProjectCheckOmsProjectValve valve;

    @Before
    public void setup() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = Mockito.spy(new OnlineSchemaChangeProperties());
        completeHandler = mock(OscTaskCompleteHandler.class);

        valve = new ScheduleCheckOmsProjectValve();
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        valve.setOscTaskCompleteHandler(completeHandler);
    }

    @Test
    public void testInvokeSuccess() throws Exception {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = OmsProjectTestUtils.getTaskParameter();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = getProjectSteps();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        ProjectProgressResponse progress = getProjectProgress();
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> getProjectFullVerifyResult())
                        .withResumeProject(() -> {})
                        .getCheckerResult();

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(getProjectFullVerifyResultResponse());

        // when
        valve.invoke(getValveContext(scheduleTask, taskParameter));

        // then
        verify(scheduleTaskRepository).update(scheduleTask);
        assertEquals(TaskStatus.RUNNING, scheduleTask.getStatus());
        assertEquals(TaskStatus.RUNNING, valve.getLastStatus());
        assertEquals(100, valve.getLastProgressPercentage());
    }

    @Test
    public void testInvokeFailedPreCheck() throws Exception {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = OmsProjectTestUtils.getTaskParameter();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(getProjectSteps());
        ProjectProgressResponse progress = getProjectProgress();
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, getProjectSteps(),
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> getProjectFullVerifyResult())
                        .withResumeProject(() -> {})
                        .getCheckerResult();

        ProjectStepVO preCheckStep = getPreCheckStep();
        when(projectOpenApiService.describeProjectStep(new ProjectStepVO().setName("PRE_CHECK")))
                .thenReturn(preCheckStep);

        PrecheckResult precheckResult = PrecheckResult.FAILED;
        when(preCheckStep.getPrecheckResult()).thenReturn(precheckResult.name());

        // when
        valve.invoke(getValveContext(scheduleTask, taskParameter));

        // then
        verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
        assertEquals(TaskStatus.FAILED, scheduleTask.getStatus());
        assertEquals(TaskStatus.FAILED, valve.getLastStatus());
        assertEquals(0, valve.getLastProgressPercentage());
    }

    @Test
    public void testInvokeFailedFullVerification() throws Exception {
        // given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = OmsProjectTestUtils.getTaskParameter();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(getProjectSteps());
        ProjectProgressResponse progress = getProjectProgress();
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, getProjectSteps(),
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> getProjectFullVerifyResult())
                        .withResumeProject(() -> {})
                        .getCheckerResult();

        ProjectStepVO fullVerificationStep = getFullVerificationStep();
        when(projectOpenApiService.describeProjectStep(new ProjectStepVO().setName("FULL_VERIFIER")))
                .thenReturn(fullVerificationStep);

        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        when(fullVerificationStep.getFullVerificationResult()).thenReturn(fullVerificationResult.name());

        // when
        valve.invoke(getValveContext(scheduleTask, taskParameter));

        // then
        verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
        assertEquals(TaskStatus.FAILED, scheduleTask.getStatus());
        assertEquals(TaskStatus.FAILED, valve.getLastStatus());
        assertEquals(0, valve.getLastProgressPercentage());
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParam.getUid());
        projectRequest.setId(taskParam.getOmsProjectId());
        return projectRequest;
    }

    private ProjectStepVO getPreCheckStep() {
        ProjectStepVO preCheckStep = new ProjectStepVO();
        preCheckStep.setName("PRE_CHECK");
        return preCheckStep;
    }

    private ProjectStepVO getFullVerificationStep() {
        ProjectStepVO fullVerificationStep = new ProjectStepVO();
        fullVerificationStep.setName("FULL_VERIFIER");
        return fullVerificationStep;
    }

    private List<ProjectStepVO> getProjectSteps() {
        ProjectStepVO preCheckStep = getPreCheckStep();
        ProjectStepVO fullVerificationStep = getFullVerificationStep();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(preCheckStep, fullVerificationStep);
        return projectSteps;
    }

    private ProjectProgressResponse getProjectProgress() {
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setTotalStepCount(2);
        progress.setCurrentStepIndex(1);
        progress.setCurrentStepName("FULL_TRANSFER");
        progress.setCurrentStepStatus(TaskStatus.RUNNING.name());
        progress.setTotalStepProgress(100);
        progress.setCurrentStepProgress(50);
        return progress;
    }

    private ProjectFullVerifyResultResponse getProjectFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        response.setTotalCount(1);
        response.setItems(new FullVerificationResult[] {FullVerificationResult.CONSISTENT.name()});
        return response;
    }

    private OnlineSchemaChangeScheduleTaskResult getTaskResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setFullTransferProgressPercentage(50);
        result.setFullTransferEstimatedCount(10);
        result.setFullTransferFinishedCount(5);
        result.setFullVerificationProgressPercentage(50);
        result.setFullVerificationResult(FullVerificationResult.CONSISTENT.name());
        return result;
    }

    private OscValveContext getValveContext(ScheduleTaskEntity scheduleTask,
            OnlineSchemaChangeScheduleTaskParameters taskParameter) {
        OscValveContext context = new OscValveContext();
        context.setScheduleTask(scheduleTask);
        context.setTaskParameter(taskParameter);
        return context;
    }
}