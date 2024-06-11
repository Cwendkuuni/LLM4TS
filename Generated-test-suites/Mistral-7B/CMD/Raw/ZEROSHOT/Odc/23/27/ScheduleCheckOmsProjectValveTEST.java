package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.MockitoAnnotations;

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
    private DefaultTableFactory tableFactory;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        // Given
        String projectId = "project-id";
        String databaseName = "database-name";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setOmsProjectId(projectId);
        taskParameters.setDatabaseName(databaseName);

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        projectRequest.setUid("uid");

        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progress, onlineSchemaChangeProperties);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameters);

        ProjectFullVerifyResultResponse fullVerifyResult = mock(ProjectFullVerifyResultResponse.class);
        when(projectOpenApiService.listProjectFullVerifyResult(mock(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResult);

        SwapTableType swapTableType = SwapTableType.SCHEMA;

        when(completeHandler.handleTaskComplete(projectId, result)).thenReturn(true);

        // When
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // Then
        assertEquals(TaskStatus.SUCCEEDED, result.getStatus());
    }

    @Test(expected = OscException.class)
    public void testInvoke_OmsPreCheckFailed() {
        // Given
        String projectId = "project-id";
        String databaseName = "database-name";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setOmsProjectId(projectId);
        taskParameters.setDatabaseName(databaseName);

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        projectRequest.setUid("uid");

        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectStepResult projectStepResultPrecheckFailed = new ProjectStepResult(projectSteps, progress,
                onlineSchemaChangeProperties);
        projectStepResultPrecheckFailed.setPreCheckResult(PrecheckResult.FAILED);

        ProjectStepResult projectStepResultFullVerificationInconsistent = new ProjectStepResult(projectSteps,
                progress, onlineSchemaChangeProperties);
        projectStepResultFullVerificationInconsistent.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectStepResultPrecheckFailed);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectStepResultFullVerificationInconsistent);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameters);

        when(projectOpenApiService.listProjectFullVerifyResult(mock(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(mock(ProjectFullVerifyResultResponse.class));

        SwapTableType swapTableType = SwapTableType.SCHEMA;

        when(completeHandler.handleTaskComplete(projectId, result)).thenReturn(true);

        // When
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // Then
        throw new AssertionError();
    }

    @Test(expected = OscException.class)
    public void testInvoke_OmsProjectStepResult_OmsDataCheckInconsistent() {
        // Given
        String projectId = "project-id";
        String databaseName = "database-name";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setOmsProjectId(projectId);
        taskParameters.setDatabaseName(databaseName);

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        projectRequest.setUid("uid");

        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ProjectStepResult projectStepResultPrecheckFailed = new ProjectStepResult(projectSteps, progress,
                onlineSchemaChangeProperties);
        projectStepResultPrecheckFailed.setPreCheckResult(PrecheckResult.PASSED);

        ProjectStepResult projectStepResultFullVerificationInconsistent = new ProjectStepResult(projectSteps,
                progress, onlineSchemaChangeProperties);
        projectStepResultFullVerificationInconsistent.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        projectStepResultFullVerificationInconsistent.setFullVerificationResultDescription("description");

        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectStepResultPrecheckFailed);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectStepResultFullVerificationInconsistent);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameters);

        when(projectOpenApiService.listProjectFullVerifyResult(mock(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(mock(ProjectFullVerifyResultResponse.class));

        SwapTableType swapTableType = SwapTableType.SCHEMA;

        when(completeHandler.handleTaskComplete(projectId, result)).thenReturn(true);

        // When
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // Then
        throw new AssertionError();
    }

    @Test
    public void testListProjectFullVerifyResult() {
        // Given
        String projectId = "project-id";
        String databaseName = "database-name";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setOmsProjectId(projectId);
        taskParameters.setDatabaseName(databaseName);

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] {databaseName});
        request.setDestSchemas(new String[] {databaseName});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(10);
        request.setPageNumber(1);
        request.setUid("uid");

        ProjectFullVerifyResultResponse expectedResponse = mock(ProjectFullVerifyResultResponse.class);

        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(expectedResponse);

        // When
        ScheduleTaskEntity scheduleTask = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(taskParameters);

        // Then
        assertEquals(projectId, scheduleTask.getId());
        assertEquals(JsonUtils.toJson(expectedResponse), scheduleTask.getResultJson());
    }

    @Test
    public void testInvoke_handleOmsProjectStepResult_SwapTableEnable() {
        // Given
        String projectId = "project-id";
        String databaseName = "database-name";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setOmsProjectId(projectId);
        taskParameters.setDatabaseName(databaseName);
        taskParameters.setSwapTableType(SwapTableType.SCHEMA);

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        projectRequest.setUid("uid");

        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progress, onlineSchemaChangeProperties);
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectStepResult);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameters);

        OnlineSchemaChangeScheduleTaskResult lastResult = new OnlineSchemaChangeScheduleTaskResult(taskParameters);
        lastResult.setManualSwapTableEnabled(true);
        lastResult.setManualSwapTableStarted(true);

        when(scheduleTaskRepository.findById(projectId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleTaskRepository.save(result)).thenReturn(scheduleTask);

        // When
        ScheduleTaskEntity scheduleTask = scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // Then
        assertEquals(projectId, scheduleTask.getId());
        assertEquals(JsonUtils.toJson(result), scheduleTask.getResultJson());
        assertTrue(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
    }

    @Test
    public void testInvoke_handleOmsProjectStepResult_SwapTableDisable() {
        // Given
        String projectId = "project-id";
        String databaseName = "database-name";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setOmsProjectId(projectId);
        taskParameters.setDatabaseName(databaseName);
        taskParameters.setSwapTableType(SwapTableType.SCHEMA);

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        projectRequest.setUid("uid");

        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectStepResult projectStepResult = new ProjectStepResult(projectSteps, progress, onlineSchemaChangeProperties);
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.UNCHECK);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectStepResult);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameters);

        OnlineSchemaChangeScheduleTaskResult lastResult = new OnlineSchemaChangeScheduleTaskResult(taskParameters);
        lastResult.setManualSwapTableEnabled(false);
        lastResult.setManualSwapTableStarted(false);

        when(scheduleTaskRepository.findById(projectId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleTaskRepository.save(result)).thenReturn(scheduleTask);

        // When
        ScheduleTaskEntity scheduleTask = scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // Then
        assertEquals(projectId, scheduleTask.getId());
        assertEquals(JsonUtils.toJson(result), scheduleTask.getResultJson());
        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
    }
}