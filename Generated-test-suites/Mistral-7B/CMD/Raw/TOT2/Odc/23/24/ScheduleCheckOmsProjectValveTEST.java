package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.oceanbase.odc.service.onlineschemachange.utils.TestUtils;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    @MockitoAnnotations.Mock
    private ProjectOpenApiService projectOpenApiService;

    @MockitoAnnotations.Mock
    private OscTaskCompleteHandler completeHandler;

    @MockitoAnnotations.Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockitoAnnotations.Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Mock
    private DefaultTableFactory defaultTableFactory;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_Success() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = TestUtils.createValidTaskParameter();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setStatus(TaskStatus.RUNNING.getStatus());

        ProjectStepVO stepVO = new ProjectStepVO();
        stepVO.setStepName(OmsStepName.PRE_CHECK.name());
        stepVO.setStepStatus(TaskStatus.RUNNING.getStatus());

        List<ProjectStepVO> projectSteps = Lists.newArrayList(stepVO);

        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        ListProjectFullVerifyResultRequest request = new ListProjectFullVerifyResultRequest();
        response.setResult(Lists.newArrayList(request));

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(response);

        // When
        valve.invoke(null);

        // Then
        OnlineSchemaChangeScheduleTaskResult result = valve.getTaskResult();
        assertEquals(TaskStatus.RUNNING.getStatus(), result.getStatus());
        assertEquals(OmsStepName.PRE_CHECK.name(), result.getCurrentStep());
    }

    @Test(expected = OscException.class)
    public void testInvoke_PreCheckFailed() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = TestUtils.createValidTaskParameter();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);

        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStepStatus(TaskStatus.FAILED.getStatus());
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(null);
        when(projectOpenApiService.listProjectFullVerifyResult(any())).thenReturn(null);

        // When
        valve.invoke(null);

        // Then
        fail("Expected OscException but got none");
    }

    @Test(expected = OscException.class)
    public void testInvoke_DataInconsistent() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = TestUtils.createValidTaskParameter();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);

        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setCurrentStepStatus(TaskStatus.RUNNING.getStatus());
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(null);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        // When
        valve.invoke(null);

        // Then
        fail("Expected OscException but got none");
    }

    @Test(expected = OscException.class)
    public void testInvoke_OmsProjectNotFound() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = TestUtils.createValidTaskParameter();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenThrow(new RuntimeException("Project not found"));

        // When
        valve.invoke(null);

        // Then
        fail("Expected OscException but got none");
    }

    @Test(expected = OscException.class)
    public void testInvoke_ListProjectFullVerifyResultFailed() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = TestUtils.createValidTaskParameter();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(new ProjectProgressResponse());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(new ProjectProgressResponse());
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        // When
        valve.invoke(null);

        // Then
        fail("Expected OscException but got none");
    }

    @Test
    public void testAdaptResult() {
        // Given
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setFullTransferProgressPercentage(80);

        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullTransferProgressPercentage(90);

        // When
        valve.adaptResult(result, projectStepResult);

        // Then
        assertEquals(90, result.getFullTransferProgressPercentage());
    }

    @Test
    public void testRecordCurrentProgress() {
        // Given
        String projectId = "test_project";
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setCurrentStep("FULL_TRANSFER");
        result.setCurrentStepStatus(TaskStatus.RUNNING.getStatus());
        result.setFullTransferProgressPercentage(50);

        SwapTableType swapTableType = SwapTableType.SWAP_TABLE_1;

        // When
        valve.recordCurrentProgress(projectId, result);

        // Then
        String expectedTableContent = "test_project,FULL_TRANSFER,RUNNING,50,Transfering data to new table";
        List<String> header = Lists.newArrayList("ProjectId", "Step", "Status", "Progress", "Description");
        Table table = new DefaultTableFactory().generateTable(5, header, Lists.newArrayList(expectedTableContent));
        assertEquals(expectedTableContent, table.render().toString().split("\n")[0]);
    }
}