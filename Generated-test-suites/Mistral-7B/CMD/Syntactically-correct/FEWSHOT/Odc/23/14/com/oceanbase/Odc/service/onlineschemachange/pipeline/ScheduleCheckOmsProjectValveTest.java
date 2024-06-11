package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.junit.OscTest;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.OpenApiTestUtils;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.utils.JsonUtilsTest;
import org.apache.commons.cli.Util;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

@OscTest
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

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

    @Mock
    private SwapTableUtil swapTableUtil;

    @Mock
    private JsonUtils jsonUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(projectOpenApiService.describeProjectSteps(any())).thenReturn(getTestProjectSteps());
        when(projectOpenApiService.describeProjectProgress(any()))
                .thenReturn(getTestProjectProgress());
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(getTestProjectFullVerifyResult());
        when(jsonUtils.fromJson(anyString(), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(getTestTaskResult());
        when(jsonUtils.toJson(any(OnlineSchemaChangeScheduleTaskResult.class))).thenReturn("{}");
    }

    @Test
    public void testInvoke() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameters = getTestTaskParameters();
        ScheduleTaskEntity scheduleTask = getTestScheduleTask();
        ValveContext valveContext = getTestValveContext(scheduleTask, taskParameters);

        // When
        valve.invoke(valveContext);

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(any());
        verify(projectOpenApiService, times(1)).describeProjectProgress(any());
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());
        verify(completeHandler, times(1)).handle(any(ProjectStepResult.class), any(OnlineSchemaChangeScheduleTaskResult.class));
        verify(scheduleTaskRepository, times(1)).update(scheduleTask);
        verify(log, times(1)).debug("Start execute ScheduleCheckOmsProjectValve, schedule task id {}", eq(scheduleTask.getId()));
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // Given
        ProjectStepResult projectStepResult = getTestProjectStepResult();
        OnlineSchemaChangeScheduleTaskResult result = getTestTaskResult();
        SwapTableType swapTableType = SwapTableType.SWAPPABLE;
        ScheduleTaskEntity scheduleTask = getTestScheduleTask();

        // When
        valve.handleOmsProjectStepResult(any(ValveContext.class), projectStepResult, result, swapTableType, scheduleTask);

        // Then
        if (projectStepResult.getTaskStatus() == TaskStatus.DONE
                && (projectStepResult.getFullVerificationResult() == FullVerificationResult.CONSISTENT ||
                        projectStepResult.getFullVerificationResult() == FullVerificationResult.UNCHECK)) {
            verify(swapTableUtil, times(1)).isSwapTableEnable(swapTableType, eq(scheduleTask.getStatus()),
                    anyDouble(), eq(projectStepResult.getFullVerificationResult()),
                    eq(result.isManualSwapTableStarted()));
            if (!result.isManualSwapTableEnabled()) {
                verify(projectOpenApiService, times(1)).resumeProject(any());
            }
        } else {
            verify(projectOpenApiService, times(0)).resumeProject(any());
        }
        verify(projectOpenApiService, times(0)).listProjectFullVerifyResult(anyString(), anyString(), anyString());
        verify(valve, times(1)).continueHandleProjectStepResult(projectStepResult);
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        // Given
        ProjectStepResult projectStepResult = getTestProjectStepResult();

        // When
        valve.continueHandleProjectStepResult(projectStepResult);

        // Then
        if (projectStepResult.getPreCheckResult() == PrecheckResult.FAILED) {
            throw new OscException(ErrorCodes.OmsPreCheckFailed, projectStepResult.getErrorMsg());
        } else if (projectStepResult.getFullVerificationResult() == FullVerificationResult.INCONSISTENT) {
            throw new OscException(ErrorCodes.OmsDataCheckInconsistent,
                    "Task failed for origin table has inconsistent data with new table, result: "
                            + projectStepResult.getFullVerificationResultDescription());
        }
    }

    @Test
    public void testAdaptResult() {
        // Given
        ProjectStepResult projectStepResult = getTestProjectStepResult();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(getTestTaskParameters());

        // When
        valve.adaptResult(result, projectStepResult);

        // Then
        assertEquals(projectStepResult.getFullTransferEstimatedCount(), result.getFullTransferEstimatedCount());
        assertEquals(projectStepResult.getFullTransferFinishedCount(), result.getFullTransferFinishedCount());
        assertEquals(projectStepResult.getFullTransferProgressPercentage(), result.getFullTransferProgressPercentage());
        assertEquals(projectStepResult.getFullVerificationResult(), result.getFullVerificationResult());
        assertEquals(projectStepResult.getFullVerificationResultDescription(), result.getFullVerificationResultDescription());
        assertEquals(projectStepResult.getFullVerificationProgressPercentage(), result.getFullVerificationProgressPercentage());
        assertEquals(projectStepResult.getCurrentStep(), result.getCurrentStep());
        assertEquals(projectStepResult.getCurrentStepStatus(), result.getCurrentStepStatus());
        assertEquals(projectStepResult.getPrecheckResult(), result.getPrecheckResult());
    }

    @Test
    public void testListProjectFullVerifyResult() {
        // Given
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";
        ProjectFullVerifyResultResponse expectedResponse = getTestProjectFullVerifyResultResponse();

        // When
        ProjectFullVerifyResultResponse response = valve.listProjectFullVerifyResult(projectId, databaseName, uid);

        // Then
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetProjectRequest() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameters = getTestTaskParameters();

        // When
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameters);

        // Then
        assertEquals(taskParameters.getUid(), projectRequest.getUid());
        assertEquals(taskParameters.getOmsProjectId(), projectRequest.getId());
    }

    @Test
    public void testRecordCurrentProgress() {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskResult result = getTestTaskResult();

        // When
        valve.recordCurrentProgress(projectId, result);

        // Then
        List<String> expectedBody = Lists.newArrayList(projectId, "", "", "", "");
        Table expectedTable = new DefaultTableFactory().generateTable(5, getHeader(), expectedBody);
        verify(log, times(1)).info("\n" + expectedTable.render().toString() + "\n");
    }

    private ProjectStepVO[] getTestProjectSteps() {
        return new ProjectStepVO[] {};
    }

    private ProjectProgressResponse getTestProjectProgress() {
        return OpenApiTestUtils.createProjectProgressResponse();
    }

    private ProjectFullVerifyResultResponse getTestProjectFullVerifyResult() {
        return OpenApiTestUtils.createProjectFullVerifyResultResponse();
    }

    private ProjectFullVerifyResultResponse getTestProjectFullVerifyResultResponse() {
        return getTestProjectFullVerifyResult();
    }

    private OnlineSchemaChangeScheduleTaskResult getTestTaskResult() {
        return new OnlineSchemaChangeScheduleTaskResult(getTestTaskParameters());
    }

    private OnlineSchemaChangeScheduleTaskParameters getTestTaskParameters() {
        return new OnlineSchemaChangeScheduleTaskParameters();
    }

    private ScheduleTaskEntity getTestScheduleTask() {
        return new ScheduleTaskEntity();
    }

    private ValveContext getTestValveContext(ScheduleTaskEntity scheduleTask,
            OnlineSchemaChangeScheduleTaskParameters taskParameters) {
        return new ValveContext() {{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameters);
        }};
    }

    private ProjectStepResult getTestProjectStepResult() {
        return new ProjectStepResult();
    }
}