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

    @InjectMocks
    private ScheduleCheckOmsProjectValve valveUnderTest;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_WhenProjectStepsAndProgressAreReturned_ThenTaskIsCompletedWithResult() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(UUID.randomUUID().toString());
        projectRequest.setId("projectId");

        List<ProjectStepVO> projectSteps = Lists.newArrayList(new ProjectStepVO());
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setStatus("RUNNING");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        // When
        valveUnderTest.invoke(null);

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(valveUnderTest.getLastResult(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.DONE, result.getTaskStatus());
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test(expected = OscException.class)
    public void testInvoke_WhenPreCheckResultIsFailed_ThenExceptionIsThrown() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(UUID.randomUUID().toString());
        projectRequest.setId("projectId");

        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Collections.singletonList(projectStepResult
                .getProjectStepVO()));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

        // When & Then
        valveUnderTest.invoke(null);
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        throw new OscException(ErrorCodes.OmsPreCheckFailed, "Precheck failed error message");
    }

    @Test
    public void testInvoke_WhenFullVerificationResultIsInconsistent_ThenExceptionIsThrown() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(UUID.randomUUID().toString());
        projectRequest.setId("projectId");

        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Collections.singletonList(projectStepResult
                .getProjectStepVO()));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

        // When & Then
        valveUnderTest.invoke(null);
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        throw new OscException(ErrorCodes.OmsDataCheckInconsistent,
                "Task failed for origin table has inconsistent data with new table");
    }

    @Test
    public void testInvoke_WhenFullTransferProgressIs100_ThenSwapTableIsEnabledAndStarted() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(UUID.randomUUID().toString());
        projectRequest.setId("projectId");

        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullTransferProgressPercentage(100);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Collections.singletonList(projectStepResult
                .getProjectStepVO()));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

        // When
        valveUnderTest.invoke(null);

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(valveUnderTest.getLastResult(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertTrue(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
    }

    @Test
    public void testInvoke_WhenFullTransferProgressIsLessThan100_ThenTaskIsContinued() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(UUID.randomUUID().toString());
        projectRequest.setId("projectId");

        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullTransferProgressPercentage(50);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Collections.singletonList(projectStepResult
                .getProjectStepVO()));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());

        // When
        valveUnderTest.invoke(null);

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(valveUnderTest.getLastResult(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        getNext().invoke(null);
    }

    @Test
    public void testListProjectFullVerifyResult_WhenItReturnsEmptyList_ThenEmptyListIsReturned() {
        // Given
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = UUID.randomUUID().toString();

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] {databaseName});
        request.setDestSchemas(new String[] {databaseName});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(10);
        request.setPageNumber(1);
        request.setUid(uid);

        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();

        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(response);

        // When
        List<ProjectFullVerifyResultResponse> result = valveUnderTest.listProjectFullVerifyResult(projectId,  databaseName, uid);

        // Then
        assertEquals(0, result.size());
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(request);
    }
}