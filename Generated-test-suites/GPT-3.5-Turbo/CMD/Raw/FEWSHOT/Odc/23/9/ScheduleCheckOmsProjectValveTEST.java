package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
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

public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    private ProjectOpenApiService projectOpenApiService;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleTaskRepository scheduleTaskRepository;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Before
    public void setUp() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        projectOpenApiService = mock(ProjectOpenApiService.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        onlineSchemaChangeProperties = mock(OnlineSchemaChangeProperties.class);
        scheduleCheckOmsProjectValve.projectOpenApiService = projectOpenApiService;
        scheduleCheckOmsProjectValve.completeHandler = completeHandler;
        scheduleCheckOmsProjectValve.scheduleTaskRepository = scheduleTaskRepository;
        scheduleCheckOmsProjectValve.onlineSchemaChangeProperties = onlineSchemaChangeProperties;
    }

    @Test
    public void testInvoke_Success() {
        // Mock data
        ValveContext valveContext = mock(ValveContext.class);
        OscValveContext context = mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("project1");
        taskParameter.setDatabaseName("db1");
        taskParameter.setUid("uid1");
        context.setScheduleTask(scheduleTask);
        context.setTaskParameter(taskParameter);
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(new ArrayList<ProjectStepVO>());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        when(scheduleTaskRepository.update(scheduleTask)).thenReturn(true);
        
        // Test method
        scheduleCheckOmsProjectValve.invoke(valveContext);
        
        // Verify
        verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
        verify(scheduleTaskRepository, times(1)).update(scheduleTask);
        verify(completeHandler, never()).handleComplete(valveContext);
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResult_PrecheckFailed() {
        // Mock data
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        projectStepResult.setErrorMsg("Precheck failed");

        // Test method
        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResult_DataCheckInconsistent() {
        // Mock data
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        projectStepResult.setFullVerificationResultDescription("Inconsistent data");

        // Test method
        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
    }

    @Test
    public void testGetProjectRequest() {
        // Mock data
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam.setUid("uid1");
        taskParam.setOmsProjectId("project1");

        // Test method
        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);

        // Verify
        assertEquals("uid1", projectRequest.getUid());
        assertEquals("project1", projectRequest.getId());
    }

    @Test
    public void testRecordCurrentProgress() {
        // Mock data
        String projectId = "project1";
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setCurrentStep("FULL_TRANSFER");
        result.setFullTransferProgressPercentage(50);
        result.setFullTransferFinishedCount(5);
        result.setFullTransferEstimatedCount(10);

        // Test method
        scheduleCheckOmsProjectValve.recordCurrentProgress(projectId, result);

        // No assertion, just check if log is generated
    }

    @Test
    public void testGetHeader() {
        // Test method
        List<String> header = scheduleCheckOmsProjectValve.getHeader();

        // Verify
        assertEquals(5, header.size());
        assertEquals("ProjectId", header.get(0));
        assertEquals("Step", header.get(1));
        assertEquals("Status", header.get(2));
        assertEquals("Progress", header.get(3));
        assertEquals("Description", header.get(4));
    }
}