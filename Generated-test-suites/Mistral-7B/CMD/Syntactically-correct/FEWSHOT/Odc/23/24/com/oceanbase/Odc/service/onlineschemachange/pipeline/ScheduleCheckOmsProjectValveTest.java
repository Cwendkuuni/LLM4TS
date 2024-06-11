package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

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

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    @Mock
    private DefaultTableFactory defaultTableFactory;

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class)))
                .thenReturn(getProjectSteps());
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class)))
                .thenReturn(getProjectProgress());
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(getProjectFullVerifyResult());
    }

    @Test
    public void testInvoke() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid(UUID.randomUUID().toString());
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep("Step1");
        projectStepResult.setCurrentStepStatus(TaskStatus.RUNNING);
        projectStepResult.setFullTransferEstimatedCount(2L);
        projectStepResult.setFullTransferFinishedCount(1L);
        projectStepResult.setFullTransferProgressPercentage(50L);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationResultDescription("Success");
        projectStepResult.setPreCheckResult(PrecheckResult.PASSED);

        when(projectOpenApiService.describeProjectProgress(projectRequest))
                .thenReturn(getProjectProgress());
        when(projectOpenApiService.describeProjectSteps(projectRequest))
                .thenReturn(getProjectSteps());
        when(completeHandler.handle(any(OnlineSchemaChangeScheduleTaskResult.class)))
                .thenReturn(true);

        valve.invoke(new OscValveContext() {{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameter);
        }});

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertNotNull(result);
        assertEquals("Step1", result.getCurrentStep());
        assertEquals(TaskStatus.RUNNING, result.getCurrentStepStatus());
        assertEquals(50, result.getFullTransferProgressPercentage().intValue());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals(true, result.isManualSwapTableEnabled());
        assertEquals(false, result.isManualSwapTableStarted());
        assertEquals("Success", result.getFullVerificationResultDescription());
        assertEquals(PrecheckResult.PASSED, result.getPrecheckResult());
    }

    @Test
    public void testInvoke_ProjectStepFailed() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid(UUID.randomUUID().toString());
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStep("Step1");
        projectStepResult.setCurrentStepStatus(TaskStatus.FAILED);
        projectStepResult.setFullTransferEstimatedCount(2L);
        projectStepResult.setFullTransferFinishedCount(1L);
        projectStepResult.setFullTransferProgressPercentage(50L);
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        projectStepResult.setFullVerificationResultDescription("Data Inconsistent");
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        projectStepResult.setErrorMsg("Error Message");

        when(projectOpenApiService.describeProjectProgress(projectRequest))
                .thenReturn(getProjectProgress());
        when(projectOpenApiService.describeProjectSteps(projectRequest))
                .thenReturn(getProjectSteps());

        try {
            valve.invoke(new OscValveContext() {{
                setScheduleTask(scheduleTask);
                setTaskParameter(taskParameter);
            }});
            fail("Expected OscException to be thrown");
        } catch (OscException e) {
            assertEquals(ErrorCodes.OmsDataCheckInconsistent, e.getErrorCode());
            assertEquals("Task failed for origin table has inconsistent data with new table, result: "
                    + projectStepResult.getFullVerificationResultDescription(), e.getMessage());
        }
    }

    private ProjectProgressResponse getProjectProgress() {
        ProjectProgressResponse projectProgress = new ProjectProgressResponse();
        return projectProgress;
    }

    private ProjectFullVerifyResultResponse getProjectFullVerifyResult() {
        ProjectFullVerifyResultResponse projectFullVerifyResult = new ProjectFullVerifyResultResponse();
        return projectFullVerifyResult;
    }

    private ProjectStepVO[] getProjectSteps() {
        ProjectStepVO[] projectSteps = new ProjectStepVO[1];
        projectSteps[0] = new ProjectStepVO();
        projectSteps[0].setStepName("Step1");
        projectSteps[0].setStatus(TaskStatus.RUNNING.name());
        projectSteps[0].setPercentage(50);
        return projectSteps;
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParam.getUid());
        projectRequest.setId(taskParam.getOmsProjectId());
        return projectRequest;
    }
}