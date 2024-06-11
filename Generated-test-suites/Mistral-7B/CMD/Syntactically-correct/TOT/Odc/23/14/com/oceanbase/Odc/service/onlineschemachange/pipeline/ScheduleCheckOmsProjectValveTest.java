package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.constant.ErrorCodes;
import com.oceanbase.odc.common.util.tableformat.Table;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    private static final String PROJECT_ID = "projectId";
    private static final String DATABASE_NAME = "databaseName";
    private static final String UID = UUID.randomUUID().toString();
    private static final int PROGRESS_PERCENTAGE = 50;
    private static final FullVerificationResult FULL_VERIFICATION_RESULT = FullVerificationResult.CONSISTENT;
    private static final String FULL_VERIFICATION_RESULT_DESCRIPTION = "full verification result description";
    private static final int FULL_TRANSFER_FINISHED_COUNT = 10;
    private static final int FULL_TRANSFER_ESTIMATED_COUNT = 20;
    private static final int FULL_TRANSFER_PROGRESS_PERCENTAGE = 50;
    private static final String CURRENT_STEP = OmsStepName.FULL_TRANSFER.name();
    private static final String CURRENT_STEP_STATUS = TaskStatus.DONE.name();
    private static final PrecheckResult PRECHECK_RESULT = PrecheckResult.PASSED;

    private ProjectOpenApiService projectOpenApiService;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OscTaskCompleteHandler completeHandler;

    @Before
    public void setup() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
    }

    @Test
    public void testInvokeWithSuccess() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(new ProjectStepVO());
        ProjectProgressResponse progress = new ProjectProgressResponse();
        when(projectOpenApiService.describeProjectSteps(any()))
                .thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(any()))
                .thenReturn(progress);
        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps.get(0), FULL_VERIFICATION_RESULT,
                FULL_VERIFICATION_RESULT_DESCRIPTION, PROGRESS_PERCENTAGE, CURRENT_STEP,
                CURRENT_STEP_STATUS, PRECHECK_RESULT);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");
        scheduleTask.setProgressPercentage(PROGRESS_PERCENTAGE);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        adaptResult(result, projectStepResult);

        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setOscTaskCompleteHandler(completeHandler);

        // When
        valve.invoke(new OscValveContext() {{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameter);
        }});

        // Then
        assertEquals(PROGRESS_PERCENTAGE, result.getProgressPercentage());
        assertEquals(FULL_VERIFICATION_RESULT, result.getFullVerificationResult());
        assertEquals(FULL_VERIFICATION_RESULT_DESCRIPTION, result.getFullVerificationResultDescription());
        assertEquals(CURRENT_STEP, result.getCurrentStep());
        assertEquals(CURRENT_STEP_STATUS, result.getCurrentStepStatus());
        assertEquals(PRECHECK_RESULT, result.getPrecheckResult());
        assertEquals(PROGRESS_PERCENTAGE, scheduleTask.getProgressPercentage());
        assertEquals(JsonUtils.toJson(result), scheduleTask.getResultJson());

        verify(projectOpenApiService, times(1)).describeProjectSteps(any());
        verify(projectOpenApiService, times(1)).describeProjectProgress(any());
    }

    @Test(expected = OscException.class)
    public void testInvokeWithError() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(new ProjectStepVO());
        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps.get(0),
                FullVerificationResult.INCONSISTENT, "error message", PROGRESS_PERCENTAGE, CURRENT_STEP,
                CURRENT_STEP_STATUS, PRECHECK_RESULT);

        when(projectOpenApiService.describeProjectSteps(any()))
                .thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(any()))
                .thenReturn(progress);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        adaptResult(result, projectStepResult);

        ScheduleCheckOmsProjectValve valve = new ScheduleCheckOmsProjectValve();
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setOscTaskCompleteHandler(completeHandler);

        // When
        valve.invoke(new OscValveContext() {{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameter);
        }});

        // Then
        throw new AssertionError("Should throw OscException");
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(UID);
        projectRequest.setId(PROJECT_ID);
        return projectRequest;
    }

    private void adaptResult(OnlineSchemaChangeScheduleTaskResult result, ProjectStepResult projectStepResult) {
        result.setFullTransferEstimatedCount(FULL_TRANSFER_ESTIMATED_COUNT);
        result.setFullTransferFinishedCount(FULL_TRANSFER_FINISHED_COUNT);
        result.setFullTransferProgressPercentage(FULL_TRANSFER_PROGRESS_PERCENTAGE);
        result.setFullVerificationResult(projectStepResult.getFullVerificationResult());
        result.setFullVerificationResultDescription(projectStepResult.getFullVerificationResultDescription());
        result.setFullVerificationProgressPercentage(projectStepResult.getFullVerificationProgressPercentage());
        result.setCurrentStep(projectStepResult.getCurrentStep());
        result.setCurrentStepStatus(projectStepResult.getCurrentStepStatus());
        result.setPrecheckResult(projectStepResult.getPrecheckResult());
    }
}