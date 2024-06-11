package com.oceanbase.odc.service.onlineschemachange.pipeline;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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

@SuppressWarnings("ResultOfMethodCallIgnored")
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

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid("uid");
        taskParameter.setSwapTableType(SwapTableType.NORMAL);

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId("projectId");
        projectRequest.setUid("uid");

        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setStep("step1");
        projectStepVO.setCurrentStep(true);
        projectStepVO.setCurrentStepStatus(TaskStatus.RUNNING.toString());
        projectStepVO.setStepStatus("RUNNING");
        projectSteps.add(projectStepVO);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProjectStatus(TaskStatus.RUNNING.toString());
        progress.setCurrentProjectStep(projectStepVO);
        progress.setProjectProgress(1);

        ProjectFullVerifyResultResponse fullVerifyResponse = new ProjectFullVerifyResultResponse();
        fullVerifyResponse.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        fullVerifyResponse.setFullVerificationResultDescription("Description");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any()))
                .thenReturn(fullVerifyResponse);

        ValveContext context = new ValveContext() {
            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return new ScheduleTaskEntity();
            }

            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }
        };

        valve.invoke(context);

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());

        OnlineSchemaChangeScheduleTaskResult result = JsonUtils.fromJson(context.getScheduleTask().getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(TaskStatus.RUNNING, result.getStatus());
        assertEquals("projectId", result.getOmsProjectId());
        assertEquals("databaseName", result.getDatabaseName());
        assertEquals("uid", result.getUid());
        assertEquals(SwapTableType.NORMAL, result.getSwapTableType());
        assertEquals(TaskStatus.RUNNING, result.getCurrentStepStatus());
        assertEquals("step1", result.getCurrentStep());
        assertEquals(1, result.getProgressPercentage());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals("Description", result.getFullVerificationResultDescription());
    }

    @Test(expected = OscException.class)
    public void testInvoke_precheckFailed() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid("uid");
        taskParameter.setSwapTableType(SwapTableType.NORMAL);

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId("projectId");
        projectRequest.setUid("uid");

        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setStep("step1");
        projectStepVO.setCurrentStep(true);
        projectStepVO.setCurrentStepStatus(TaskStatus.RUNNING.toString());
        projectStepVO.setStepStatus("RUNNING");
        projectSteps.add(projectStepVO);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProjectStatus(TaskStatus.RUNNING.toString());
        progress.setCurrentProjectStep(projectStepVO);
        progress.setProjectProgress(1);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> listProjectFullVerifyResult(taskParameter.getOmsProjectId(),
                                taskParameter.getDatabaseName(), taskParameter.getUid()))
                        .withResumeProject(() -> {
                }).getCheckerResult();

        PrecheckResult precheckResult = PrecheckResult.FAILED;
        projectStepResult.setPreCheckResult(precheckResult);

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ValveContext context = new ValveContext() {
            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return new ScheduleTaskEntity();
            }

            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }
        };

        valve.invoke(context);

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);

        throw new OscException(ErrorCodes.OmsPreCheckFailed, projectStepResult.getErrorMsg());
    }

    @Test(expected = OscException.class)
    public void testInvoke_dataInconsistent() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("databaseName");
        taskParameter.setUid("uid");
        taskParameter.setSwapTableType(SwapTableType.NORMAL);

        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId("projectId");
        projectRequest.setUid("uid");

        List<ProjectStepVO> projectSteps = Lists.newArrayList();
        ProjectStepVO projectStepVO = new ProjectStepVO();
        projectStepVO.setStep("step1");
        projectStepVO.setCurrentStep(true);
        projectStepVO.setCurrentStepStatus(TaskStatus.RUNNING.toString());
        projectStepVO.setStepStatus("RUNNING");
        projectSteps.add(projectStepVO);

        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProjectStatus("RUNNING");
        progress.setCurrentProjectStep(projectStepVO);
        progress.setProjectProgress(1);

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(progress, projectSteps,
                onlineSchemaChangeProperties.isEnableFullVerify())
                        .withCheckerVerifyResult(() -> listProjectFullVerifyResult(taskParameter.getOmsProjectId(),
                                taskParameter.getDatabaseName(), taskParameter.getUid()))
                        .withResumeProject(() -> {
                }).getCheckerResult();

        FullVerificationResult fullVerificationResult = FullVerificationResult.INCONSISTENT;
        projectStepResult.setFullVerificationResult(fullVerificationResult);
        projectStepResult.setFullVerificationResultDescription("Description");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);

        ValveContext context = new ValveContext() {
            @Override
            public ScheduleTaskEntity getScheduleTask() {
                return new ScheduleTaskEntity();
            }

            @Override
            public OnlineSchemaChangeScheduleTaskParameters getTaskParameter() {
                return taskParameter;
            }
        };

        valve.invoke(context);

        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);

        throw new OscException(ErrorCodes.OmsDataCheckInconsistent,
                "Task failed for origin table has inconsistent data with new table, result: "
                        + projectStepResult.getFullVerificationResultDescription());
    }

    @Test
    public void testListProjectFullVerifyResult() {
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

        ProjectFullVerifyResultResponse response = new ProjectFullVerifyResultResponse();
        response.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        when(projectOpenApiService.listProjectFullVerifyResult(request)).thenReturn(response);

        valve.listProjectFullVerifyResult(projectId, databaseName, uid);

        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(request);
        verifyNoMoreInteractions(projectOpenApiService);
    }

    @Test
    public void testRecordCurrentProgress() {
        String projectId = "projectId";
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setCurrentStep("step1");
        result.setCurrentStepStatus(TaskStatus.RUNNING.toString());
        result.setProgressPercentage(1);
        result.setSwapTableType(SwapTableType.NORMAL);

        String[] header = {"ProjectId", "Step", "Status", "Progress", "Description"};
        Table table = new DefaultTableFactory().generateTable(5, header, null);

        valve.recordCurrentProgress(projectId, result);

        String expectedTable = "\n" + table.render().toString() + "\n";
        String actualTable = valve.recordCurrentProgress(projectId, result).toString();
        assertEquals(expectedTable, actualTable);
    }
}