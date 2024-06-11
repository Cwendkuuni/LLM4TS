package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

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
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    private ScheduleTaskEntity scheduleTask;

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;

    private ProjectControlRequest projectRequest;

    private List<ProjectStepVO> projectSteps;

    private ProjectProgressResponse progress;

    private ProjectStepResult projectStepResult;

    private OnlineSchemaChangeScheduleTaskResult result;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        projectRequest = new ProjectControlRequest();
        projectSteps = Lists.newArrayList();
        progress = new ProjectProgressResponse();
        projectStepResult = new ProjectStepResult();
        result = new OnlineSchemaChangeScheduleTaskResult();
        scheduleTask = new ScheduleTaskEntity();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(scheduleTaskRepository.update(scheduleTask)).thenReturn(true);
    }

    @Test
    public void testInvoke() throws Exception {
        // Given
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid("uid");
        scheduleTask.setId("taskId");
        scheduleTask.setStatus("SCHEDULED");
        scheduleTask.setResultJson("{}");

        when(projectOpenApiService.listProjectFullVerifyResult(any())).thenReturn(new ProjectFullVerifyResultResponse());

        // When
        scheduleCheckOmsProjectValve.invoke(null);

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(scheduleTaskRepository, times(1)).update(scheduleTask);

        // Test handleOmsProjectStepResult
        when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);
        when(projectStepResult.getFullTransferProgressPercentage()).thenReturn(100L);
        when(SwapTableUtil.isSwapTableEnable(SwapTableType.SWAP_TABLE_SWAPPABLE, "SCHEDULED", 100L, FullVerificationResult.CONSISTENT, false))
                .thenReturn(true);
        handleOmsProjectStepResultTest();

        // Test handleOmsProjectStepResult with not consistent result
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.INCONSISTENT);
        handleOmsProjectStepResultTest();

        // Test handleOmsProjectStepResult with failed precheck result
        when(projectStepResult.getPreCheckResult()).thenReturn(PrecheckResult.FAILED);
        handleOmsProjectStepResultTest();
    }

    private void handleOmsProjectStepResultTest() throws Exception {
        // Given
        result.setManualSwapTableEnabled(false);
        result.setManualSwapTableStarted(false);

        // When
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, projectStepResult, result, SwapTableType.SWAP_TABLE_SWAPPABLE, scheduleTask);

        // Then
        if (projectStepResult.getTaskStatus() == TaskStatus.DONE
                && (projectStepResult.getFullVerificationResult() == FullVerificationResult.CONSISTENT ||
                        projectStepResult.getFullVerificationResult() == FullVerificationResult.UNCHECK)) {
            verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
            verify(projectOpenApiService, times(0)).listProjectFullVerifyResult(any());
            verify(scheduleTaskRepository, times(1)).update(scheduleTask);
        } else {
            verify(projectOpenApiService, times(0)).resumeProject(projectRequest);
            verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());
            verify(scheduleTaskRepository, times(1)).update(scheduleTask);
            getNext().invoke(null);
        }

        // Test adaptResult
        adaptResultTest();
    }

    private void adaptResultTest() {
        // Given
        projectStepResult.setFullTransferEstimatedCount(10L);
        projectStepResult.setFullTransferFinishedCount(5L);

        // When
        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        // Then
        assertEquals(5, result.getFullTransferFinishedCount());
        assertEquals(10, result.getFullTransferEstimatedCount());
        assertEquals(100, result.getFullTransferProgressPercentage().intValue());
        assertEquals(projectStepResult.getFullVerificationResult(), result.getFullVerificationResult());
        assertEquals(projectStepResult.getFullVerificationResultDescription(), result.getFullVerificationResultDescription());
        assertEquals(projectStepResult.getFullVerificationProgressPercentage().intValue(), result.getFullVerificationProgressPercentage().intValue());
        assertEquals(projectStepResult.getCurrentStep(), result.getCurrentStep());
        assertEquals(projectStepResult.getCurrentStepStatus(), result.getCurrentStepStatus());
        assertEquals(projectStepResult.getPreCheckResult(), result.getPrecheckResult());
    }

    @Test
    public void testGetProjectRequest() {
        // Given
        String omsProjectId = "projectId";

        // When
        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);

        // Then
        assertEquals(omsProjectId, projectRequest.getId());
        assertEquals(taskParameter.getUid(), projectRequest.getUid());
    }

    @Test
    public void testRecordCurrentProgress() {
        // Given
        String projectId = "projectId";
        String step = OmsStepName.FULL_TRANSFER.name();
        String status = TaskStatus.RUNNING.toString();
        long progress = 50;
        String description = "description";

        // When
        List<String> body = scheduleCheckOmsProjectValve.recordCurrentProgress(projectId, result);

        // Then
        assertEquals(5, body.size());
        assertEquals(projectId, body.get(0));
        assertEquals(step, body.get(1));
        assertEquals(status, body.get(2));
        assertEquals(progress + "", body.get(3));
        assertEquals(description, body.get(4));
    }

    @Test
    public void testListProjectFullVerifyResult() throws Exception {
        // Given
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";

        // When
        ProjectFullVerifyResultResponse response = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, databaseName, uid);

        // Then
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());
        assertNotNull(response);
    }
}