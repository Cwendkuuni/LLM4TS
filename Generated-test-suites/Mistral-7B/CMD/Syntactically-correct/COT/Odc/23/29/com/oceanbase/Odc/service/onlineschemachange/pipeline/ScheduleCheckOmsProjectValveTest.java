package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_Success() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        ProjectStepVO[] projectSteps = new ProjectStepVO[1];
        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(taskParameter, projectSteps, progress);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(new ProjectFullVerifyResultResponse());

        when(defaultTableFactory.generateTable(anyInt(), anyList(), anyList())).thenReturn(new Table("", ""));

        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);

        // When
        scheduleCheckOmsProjectValve.invoke(null);

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test(expected = OscException.class)
    public void testInvoke_OmsPreCheckFailed() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        ProjectStepVO[] projectSteps = new ProjectStepVO[1];
        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(taskParameter, projectSteps, progress);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenThrow(new OscException(ErrorCodes.OmsPreCheckFailed, "Precheck failed."));

        // When
        scheduleCheckOmsProjectValve.invoke(null);

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test(expected = OscException.class)
    public void testInvoke_OmsDataCheckInconsistent() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        ProjectStepVO[] projectSteps = new ProjectStepVO[1];
        ProjectProgressResponse progress = new ProjectProgressResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(taskParameter, projectSteps, progress);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenThrow(new OscException(ErrorCodes.OmsDataCheckInconsistent,
                        "Task failed for origin table has inconsistent data with new table."));

        // When
        scheduleCheckOmsProjectValve.invoke(null);

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test
    public void testListProjectFullVerifyResult_Success() {
        // Given
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";
        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();

        // When
        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(response);

        // Then
        OnlineSchemaChangeScheduleTaskResult result = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId,
                databaseName, uid);

        // Then
        assertEquals(result.getFullVerificationResult(), FullVerificationResult.FINISHED);
        assertEquals(StringUtils.isNotBlank(result.getResultJson()), true);
    }

    @Test(expected = OscException.class)
    public void testListProjectFullVerifyResult_Failure() {
        // Given
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";
        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();

        // When
        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenThrow(new OscException(ErrorCodes.OmsError,
                "Error occurred."));

        // Then
        try {
            scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, databaseName, uid);
            fail("Expected an OscException.");
        } catch (OscException e) {
            // Then
            verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(request);
        }
    }
}