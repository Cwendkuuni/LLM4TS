package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

/**
 * Test class for ScheduleCheckOmsProjectValve
 */
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
    private DefaultTableFactory tableFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_success() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid(UUID.randomUUID().toString());

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);

        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setName("StepName");
        projectStepVO.setStatus(TaskStatus.DONE.name());
        projectStepVO.setPercentage(100);

        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setSteps(projectSteps);

        ProjectFullVerifyResultResponse fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        fullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        fullVerifyResultResponse.setFullVerificationResultDescription("Success");

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class))).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class))).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        // When
        scheduleCheckOmsProjectValve.invoke(null);

        // Then
        ScheduleTaskEntity scheduleTask = scheduleCheckOmsProjectValve.getScheduleTask();
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    @Test(expected = OscException.class)
    public void testInvoke_preCheckFailed() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid(UUID.randomUUID().toString());

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);

        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setName("StepName");
        projectStepVO.setStatus(TaskStatus.FAILED.name());
        projectStepVO.setPercentage(0);
        projectStepVO.setErrorMsg("PreCheck Failed");

        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setSteps(projectSteps);

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class))).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class))).thenReturn(progress);

        // When & Then
        scheduleCheckOmsProjectValve.invoke(null);
    }

    @Test(expected = OscException.class)
    public void testInvoke_dataInconsistent() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid(UUID.randomUUID().toString());

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);

        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setName("StepName");
        projectStepVO.setStatus(TaskStatus.DONE.name());
        projectStepVO.setPercentage(100);

        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setSteps(projectSteps);

        ProjectFullVerifyResultResponse fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        fullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        fullVerifyResultResponse.setFullVerificationResultDescription("Data inconsistent");

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class))).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class))).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        // When & Then
        scheduleCheckOmsProjectValve.invoke(null);
    }

    @Test
    public void testHandleOmsProjectStepResult_enableSwapTable() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid(UUID.randomUUID().toString());
        taskParameter.setSwapTableType(SwapTableType.MANUAL);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setStatus("SCHEDULED");
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);

        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setName("StepName");
        projectStepVO.setStatus(TaskStatus.DONE.name());
        projectStepVO.setPercentage(100);
        projectStepVO.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setSteps(projectSteps);

        ProjectFullVerifyResultResponse fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        fullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        fullVerifyResultResponse.setFullVerificationResultDescription("Success");

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class))).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class))).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(scheduleTask);

        // When
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, new ProjectStepResult(projectStepVO,
                FullVerificationResult.CONSISTENT), new OnlineSchemaChangeScheduleTaskResult(), SwapTableType.MANUAL,
                scheduleTask);

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(true, result.isManualSwapTableEnabled());
        assertEquals(false, result.isManualSwapTableStarted());
    }

    @Test
    public void testHandleOmsProjectStepResult_disableSwapTable() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid(UUID.randomUUID().toString());
        taskParameter.setSwapTableType(SwapTableType.AUTO);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setStatus("SCHEDULED");
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);

        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setName("StepName");
        projectStepVO.setStatus(TaskStatus.DONE.name());
        projectStepVO.setPercentage(100);
        projectStepVO.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        List<ProjectStepVO> projectSteps = Lists.newArrayList(projectStepVO);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setSteps(projectSteps);

        ProjectFullVerifyResultResponse fullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        fullVerifyResultResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        fullVerifyResultResponse.setFullVerificationResultDescription("Success");

        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class))).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class))).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        when(scheduleTaskRepository.update(any(ScheduleTaskEntity.class))).thenReturn(scheduleTask);

        // When
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(null, new ProjectStepResult(projectStepVO,
                FullVerificationResult.CONSISTENT), new OnlineSchemaChangeScheduleTaskResult(), SwapTableType.AUTO,
                scheduleTask);

        // Then
        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(scheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(false, result.isManualSwapTableEnabled());
        assertEquals(false, result.isManualSwapTableStarted());
    }
}