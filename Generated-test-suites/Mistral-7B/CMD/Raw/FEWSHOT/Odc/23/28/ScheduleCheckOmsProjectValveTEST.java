package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
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

public class ScheduleCheckOmsProjectValveTest {

    private ProjectOpenApiService projectOpenApiService;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleCheckOmsProjectValve valve;

    @Before
    public void setup() {
        projectOpenApiService = mock(ProjectOpenApiService.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        valve = new ScheduleCheckOmsProjectValve();
        valve.setProjectOpenApiService(projectOpenApiService);
        valve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
        valve.setScheduleTaskRepository(scheduleTaskRepository);
        valve.setOscTaskCompleteHandler(completeHandler);
    }

    @Test
    public void testInvoke() throws Exception {
        String projectId = "projectId";
        String databaseName = "databaseName";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setOmsProjectId(projectId);
        taskParameters.setDatabaseName(databaseName);
        taskParameters.setUid(UUID.randomUUID().toString());
        taskParameters.setSwapTableType(SwapTableType.MASTER);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus("SCHEDULED");
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = getProjectRequest(taskParameters);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectFullVerifyResultResponse fullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResultResponse);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify()).getCheckerResult();

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameters);
        OnlineSchemaChangeScheduleTaskResult lastResult = mock(OnlineSchemaChangeScheduleTaskResult.class);

        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationProgressPercentage(100);
        projectStepResult.setFullTransferProgressPercentage(100);
        projectStepResult.setFullTransferEstimatedCount(1);
        projectStepResult.setFullTransferFinishedCount(1);
        projectStepResult.setCurrentStep("FULL_VERIFIER");
        projectStepResult.setCurrentStepStatus(TaskStatus.DONE);

        when(fullVerifyResultResponse.getProjectFullVerifyResultList()).thenReturn(Lists.newArrayList(
                new ProjectFullVerifyResultResponse.ProjectFullVerifyResultItem(){{
                    setStatus("FINISHED");
                    setResult(FullVerificationResult.CONSISTENT.name());
                }}));

        assertNotNull(valve.invoke(new OscValveContext(){{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameters);
        }}));

        verify(scheduleTaskRepository).update(scheduleTask);
        verify(projectOpenApiService).resumeProject(projectRequest);
        verify(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
    }

    @Test(expected = OscException.class)
    public void testInvokeWithError() throws Exception {
        String projectId = "projectId";
        String databaseName = "databaseName";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setOmsProjectId(projectId);
        taskParameters.setDatabaseName(databaseName);
        taskParameters.setUid(UUID.randomUUID().toString());
        taskParameters.setSwapTableType(SwapTableType.MASTER);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus("SCHEDULED");
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = getProjectRequest(taskParameters);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectFullVerifyResultResponse fullVerifyResultResponse = mock(ProjectFullVerifyResultResponse.class);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenThrow(new RuntimeException());

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameters);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify()).getCheckerResult();

        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationProgressPercentage(100);
        projectStepResult.setFullTransferProgressPercentage(100);
        projectStepResult.setFullTransferEstimatedCount(1);
        projectStepResult.setFullTransferFinishedCount(1);
        projectStepResult.setCurrentStep("FULL_VERIFIER");
        projectStepResult.setCurrentStepStatus(TaskStatus.DONE);

        assertNotNull(valve.invoke(new OscValveContext(){{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameters);
        }}));

        verify(projectOpenApiService).resumeProject(projectRequest);
        verify(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));

        valve.invoke(new OscValveContext(){{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameters);
        }});

        verify(projectOpenApiService).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)).times(2);
    }

    @Test
    public void testHandleOmsProjectStepResult() throws Exception {
        String projectId = "projectId";
        String databaseName = "databaseName";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setOmsProjectId(projectId);
        taskParameters.setDatabaseName(databaseName);
        taskParameters.setUid(UUID.randomUUID().toString());
        taskParameters.setSwapTableType(SwapTableType.MASTER);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setStatus("SCHEDULED");
        scheduleTask.setResultJson("");

        ProjectControlRequest projectRequest = getProjectRequest(taskParameters);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(mock(ProjectStepVO.class));
        ProjectProgressResponse progress = mock(ProjectProgressResponse.class);
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationResultDescription("Description");

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameters);

        valve.handleOmsProjectStepResult(new OscValveContext(){{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameters);
            setProjectStepResult(projectStepResult);
            setProjectOpenApiService(projectOpenApiService);
            setOscTaskCompleteHandler(completeHandler);
            setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
            setScheduleTaskRepository(scheduleTaskRepository);
        }}, projectStepResult, result, SwapTableType.MASTER, scheduleTask);

        verify(progress).getProjectFullVerifyResultList();
        verify(projectOpenApiService).resumeProject(projectRequest);

        assertEquals(TaskStatus.DONE, result.getCurrentStepStatus());
        assertEquals(ProjectStatus.COMPLETED, result.getStatus());
        assertEquals(projectId, result.getOmsProjectId());
        assertEquals(databaseName, result.getDatabaseName());
        assertNotNull(result.getFullVerificationResultDescription());
        assertEquals(100, result.getFullVerificationProgressPercentage());
        assertEquals(100, result.getFullTransferProgressPercentage());
        assertEquals(1, result.getFullTransferEstimatedCount());
        assertEquals(1, result.getFullTransferFinishedCount());
        assertEquals(SwapTableType.MASTER, result.getSwapTableType());
        assertEquals(true, result.isManualSwapTableEnabled());
        assertEquals(false, result.isManualSwapTableStarted());
    }

    @Test
    public void testContinueHandleProjectStepResult() throws Exception {
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        valve.continueHandleProjectStepResult(projectStepResult);

        verify(projectOpenApiService).resumeProject(any(ProjectControlRequest.class));

        throw new OscException(ErrorCodes.OmsPreCheckFailed, projectStepResult.getErrorMsg());
    }

    @Test
    public void testListProjectFullVerifyResult() throws Exception {
        String projectId = "projectId";
        String databaseName = "databaseName";
        String uid = "uid";

        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        request.setProjectId(projectId);
        request.setSourceSchemas(new String[] {databaseName});
        request.setDestSchemas(new String[] {databaseName});
        request.setStatus(new String[] {"FINISHED", "SUSPEND", "RUNNING"});
        request.setPageSize(10);
        request.setPageNumber(1);
        request.setUid(uid);

        ProjectFullVerifyResultResponse response = mock(ProjectFullVerifyResultResponse.class);

        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(response);

        List<ProjectFullVerifyResultResponse.ProjectFullVerifyResultItem> projectFullVerifyResultList = response
                .getProjectFullVerifyResultList();

        assertEquals(1, projectFullVerifyResultList.size());
        assertEquals("projectId", projectFullVerifyResultList.get(0).getProjectId());
        assertEquals("databaseName", projectFullVerifyResultList.get(0).getSourceSchema());
        assertEquals("databaseName", projectFullVerifyResultList.get(0).getDestSchema());
        assertEquals("FINISHED", projectFullVerifyResultList.get(0).getStatus());

        verify(projectOpenApiService).listProjectFullVerifyResult(request);
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParameters) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParameters.getUid());
        projectRequest.setId(taskParameters.getOmsProjectId());
        return projectRequest;
    }
}