package com.oceanbase.odc.service.onlineschemachange.pipeline;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
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
import com.oceanbase.odc.service.onlineschemachange.utils.TestConstants;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Component
public class ScheduleCheckOmsProjectValveTest {

    @Autowired
    private ProjectOpenApiService projectOpenApiService;
    @Autowired
    private OscTaskCompleteHandler completeHandler;
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    @Autowired
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    private ScheduleCheckOmsProjectValve valve;
    private ProjectOpenApiService projectOpenApiServiceMock;
    private ScheduleTaskEntity scheduleTask;
    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ProjectControlRequest projectRequest;
    private ProjectFullVerifyResultResponse projectFullVerifyResultResponse;
    private ProjectProgressResponse projectProgressResponse;
    private ProjectStepVO projectStepVO;
    private ProjectStepResult projectStepResult;
    private OnlineSchemaChangeScheduleTaskResult result;

    @BeforeEach
    public void setUp() {
        projectOpenApiServiceMock = mock(ProjectOpenApiService.class);
        valve = new ScheduleCheckOmsProjectValve() {
            @Autowired
            public void setProjectOpenApiService(ProjectOpenApiService projectOpenApiService) {
                this.projectOpenApiService = projectOpenApiServiceMock;
            }
            @Autowired
            public void setCompleteHandler(OscTaskCompleteHandler completeHandler) {
                this.completeHandler = completeHandler;
            }
            @Autowired
            public void setScheduleTaskRepository(ScheduleTaskRepository scheduleTaskRepository) {
                this.scheduleTaskRepository = scheduleTaskRepository;
            }

            @Autowired
            public void setOnlineSchemaChangeProperties(OnlineSchemaChangeProperties onlineSchemaChangeProperties) {
                this.onlineSchemaChangeProperties = onlineSchemaChangeProperties;
            }
        };
        scheduleTask = new ScheduleTaskEntity();
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        projectRequest = new ProjectControlRequest();
        projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectProgressResponse = new ProjectProgressResponse();
        projectStepVO = new ProjectStepVO();
        projectStepResult = new ProjectStepResult();
        result = new OnlineSchemaChangeScheduleTaskResult();
    }

    @Test
    public void testInvoke_success() throws Exception {
        // Given
        String projectId = TestConstants.PROJECT_ID;
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        projectRequest.setId(projectId);
        projectRequest.setUid("test_uid");
        valve.invoke(mock(ValveContext.class));

        // Then
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        assertNotNull(scheduleTask.getResultJson());
        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
    }

    @Test
    public void testInvoke_projectStepFailed() throws Exception {
        // Given
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenThrow(new OscException("Project step failed"));

        taskParameter.setOmsProjectId(TestConstants.PROJECT_ID);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        projectRequest.setId(projectId);
        projectRequest.setUid("test_uid");

        assertThrows(OscException.class, () -> valve.invoke(mock(ValveContext.class)));

        // Then
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
    }

    @Test
    public void testInvoke_projectProgressFailed() throws Exception {
        // Given
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenThrow(new OscException("Project progress failed"));

        taskParameter.setOmsProjectId(TestConstants.PROJECT_ID);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        projectRequest.setId(projectId);
        projectRequest.setUid("test_uid");

        assertThrows(OscException.class, () -> valve.invoke(mock(ValveContext.class)));

        // Then
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
    }

    @Test
    public void testInvoke_listProjectFullVerifyResultFailed() throws Exception {
        // Given
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenThrow(new OscException("Project full verify result failed"));

        taskParameter.setOmsProjectId(TestConstants.PROJECT_ID);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        projectRequest.setId(projectId);
        projectRequest.setUid("test_uid");

        assertThrows(OscException.class, () -> valve.invoke(mock(ValveContext.class)));

        // Then
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
    }

    @Test
    public void testInvoke_fullTransferProgressGreaterThanEstimated() throws Exception {
        // Given
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);
        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        taskParameter.setOmsProjectId(TestConstants.PROJECT_ID);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        projectRequest.setId(projectId);
        projectRequest.setUid("test_uid");

        projectStepResult = new ProjectStepResult(null, 10, TaskStatus.DONE, FullVerificationResult.CONSISTENT,
                "");
        projectStepVO.setStatus(JsonUtils.toJson(projectStepResult));

        result.setCurrentStep(projectStepVO.getStatus());
        result.setFullTransferProgressPercentage(120);
        result.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        scheduleTask.setResultJson(JsonUtils.toJson(result));

        valve.invoke(mock(ValveContext.class));

        // Then
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(projectOpenApiService).resumeProject(projectRequest);
        assertTrue(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
    }

    @Test
    public void testInvoke_fullTransferProgressLessThanEstimated() throws Exception {
        // Given
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);
        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        taskParameter.setOmsProjectId(TestConstants.PROJECT_ID);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        projectRequest.setId(projectId);
        projectRequest.setUid("test_uid");

        projectStepResult = new ProjectStepResult(null, 10, TaskStatus.DONE, FullVerificationResult.CONSISTENT,
                "");
        projectStepVO.setStatus(JsonUtils.toJson(projectStepResult));

        result.setCurrentStep(projectStepVO.getStatus());
        result.setFullTransferProgressPercentage(50);
        result.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        scheduleTask.setResultJson(JsonUtils.toJson(result));

        valve.invoke(mock(ValveContext.class));

        // Then
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(projectOpenApiService).resumeProject(projectRequest);
        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
    }

    @Test
    public void testInvoke_fullVerificationResultInconsistent() throws Exception {
        // Given
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList(projectStepVO));
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(projectProgressResponse);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);
        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        taskParameter.setOmsProjectId(TestConstants.PROJECT_ID);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        projectRequest.setId(projectId);
        projectRequest.setUid("test_uid");

        projectStepResult = new ProjectStepResult(null, 10, TaskStatus.DONE, FullVerificationResult.INCONSISTENT,
                "Error message");
        projectStepVO.setStatus(JsonUtils.toJson(projectStepResult));

        result.setCurrentStep(projectStepVO.getStatus());
        result.setFullTransferProgressPercentage(50);
        result.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        result.setFullVerificationResultDescription("Error message");

        scheduleTask.setResultJson(JsonUtils.toJson(result));

        assertThrows(OscException.class, () -> valve.invoke(mock(ValveContext.class)));

        // Then
        verify(projectOpenApiService).describeProjectSteps(projectRequest);
        verify(projectOpenApiService).describeProjectProgress(projectRequest);
        verify(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(projectOpenApiService).resumeProject(projectRequest);
    }

    @AfterEach
    public void tearDown() {
        reset(projectOpenApiServiceMock);
    }
}