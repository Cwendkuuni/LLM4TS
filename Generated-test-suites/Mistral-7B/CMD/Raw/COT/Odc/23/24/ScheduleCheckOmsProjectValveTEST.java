package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        scheduleTask = new ScheduleTaskEntity();
    }

    @Test
    public void testInvoke_whenAllStepsDone_thenSwapTablesAndComplete() {
        // Given
        ProjectProgressResponse progress = getMockProjectProgressResponse();
        List<ProjectStepVO> projectSteps = getMockProjectSteps();
        ProjectFullVerifyResultResponse fullVerifyResult = getMockProjectFullVerifyResultResponse();
        ProjectStepResult projectStepResult = new ProjectStepResult(progress, projectSteps, fullVerifyResult, true);

        Mockito.when(projectOpenApiService.describeProjectSteps(Mockito.any(ProjectControlRequest.class)))
                .thenReturn(projectSteps);
        Mockito.when(projectOpenApiService.describeProjectProgress(Mockito.any(ProjectControlRequest.class)))
                .thenReturn(progress);
        Mockito.when(projectOpenApiService.listProjectFullVerifyResult(Mockito.any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResult);

        // When
        valve.invoke(getValveContext());

        // Then
        assertEquals(TaskStatus.DONE, projectStepResult.getTaskStatus());
        assertTrue(projectStepResult.isFullTransferDone());
        assertTrue(projectStepResult.isFullVerificationDone());
        assertTrue(projectStepResult.isProjectDone());
    }

    @Test
    public void testInvoke_whenFullTransferFailed_thenContinue() {
        // Given
        ProjectProgressResponse progress = getMockProjectProgressResponse();
        List<ProjectStepVO> projectSteps = getMockProjectSteps();
        ProjectStepResult projectStepResult = new ProjectStepResult(progress, projectSteps, null, false);

        Mockito.when(projectOpenApiService.describeProjectSteps(Mockito.any(ProjectControlRequest.class)))
                .thenReturn(projectSteps);
        Mockito.when(projectOpenApiService.describeProjectProgress(Mockito.any(ProjectControlRequest.class)))
                .thenReturn(progress);

        // When
        valve.invoke(getValveContext());

        // Then
        assertEquals(TaskStatus.RUNNING, projectStepResult.getTaskStatus());
    }

    @Test(expected = OscException.class)
    public void testInvoke_whenPreCheckFailed_thenThrowException() {
        // Given
        ProjectProgressResponse progress = getMockProjectProgressResponse();
        List<ProjectStepVO> projectSteps = getMockProjectSteps();
        ProjectStepResult projectStepResult = new ProjectStepResult(progress, projectSteps, null, false) {
            @Override
            public PrecheckResult getPreCheckResult() {
                return PrecheckResult.FAILED;
            }
        };

        Mockito.when(projectOpenApiService.describeProjectSteps(Mockito.any(ProjectControlRequest.class)))
                .thenReturn(projectSteps);
        Mockito.when(projectOpenApiService.describeProjectProgress(Mockito.any(ProjectControlRequest.class)))
                .thenReturn(progress);

        // When
        valve.invoke(getValveContext());

        // Then
        fail("Precheck failed should throw exception");
    }

    @Test(expected = OscException.class)
    public void testInvoke_whenFullVerificationInconsistent_thenThrowException() {
        // Given
        ProjectProgressResponse progress = getMockProjectProgressResponse();
        List<ProjectStepVO> projectSteps = getMockProjectSteps();
        ProjectStepResult projectStepResult = new ProjectStepResult(progress, projectSteps, null, false) {
            @Override
            public FullVerificationResult getFullVerificationResult() {
                return FullVerificationResult.INCONSISTENT;
            }
        };

        Mockito.when(projectOpenApiService.describeProjectSteps(Mockito.any(ProjectControlRequest.class)))
                .thenReturn(projectSteps);
        Mockito.when(projectOpenApiService.describeProjectProgress(Mockito.any(ProjectControlRequest.class)))
                .thenReturn(progress);

        // When
        valve.invoke(getValveContext());

        // Then
        fail("Full verification inconsistent should throw exception");
    }

    @Test
    public void testAdaptResult() {
        // Given
        ProjectStepResult projectStepResult = new ProjectStepResult(null, null, null, false);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

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
        assertEquals(projectStepResult.getPreCheckResult(), result.getPrecheckResult());
    }

    @Test
    public void testGetProjectRequest() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParam);

        // Then
        assertEquals(taskParam.getUid(), projectRequest.getUid());
        assertEquals(taskParam.getOmsProjectId(), projectRequest.getId());
    }

    @Test
    public void testRecordCurrentProgress() {
        // Given
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        OmsStepName step = OmsStepName.FULL_TRANSFER;
        String status = "status";
        double progress = 1.0;
        String description = "description";

        // When
        valve.recordCurrentProgress(projectId, result);

        // Then
        List<String> body = valve.getHeader();
        assertEquals(5, body.size());
        assertEquals("ProjectId", body.get(0));
        assertEquals("Step", body.get(1));
        assertEquals("Status", body.get(2));
        assertEquals("Progress", body.get(3));
        assertEquals("Description", body.get(4));

        Table table = valve.tableFactory.generateTable(5, body,
                Lists.newArrayList(projectId, step.name(), status, progress + "", description));
        assertEquals(projectId, table.get(0, 0));
        assertEquals(step.name(), table.get(0, 1));
        assertEquals(status, table.get(0, 2));
        assertEquals(String.valueOf(progress), table.get(0, 3));
        assertEquals(description, table.get(0, 4));
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        return valve.getProjectRequest(taskParam);
    }

    private ValveContext getValveContext() {
        OscValveContext context = new OscValveContext();
        context.setScheduleTask(scheduleTask);
        context.setTaskParameter(taskParameter);
        return context;
    }

    private ProjectProgressResponse getMockProjectProgressResponse() {
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setStatus("status");
        progress.setCurrentStep(new ProjectStepVO());
        return progress;
    }

    private List<ProjectStepVO> getMockProjectSteps() {
        ProjectStepVO step1 = new ProjectStepVO();
        step1.setName("step1");
        ProjectStepVO step2 = new ProjectStepVO();
        step2.setName("step2");
        List<ProjectStepVO> projectSteps = Lists.newArrayList(step1, step2);
        return projectSteps;
    }

    private ProjectFullVerifyResultResponse getMockProjectFullVerifyResultResponse() {
        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        response.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        response.setFullVerificationResultDescription("description");
        return response;
    }
}