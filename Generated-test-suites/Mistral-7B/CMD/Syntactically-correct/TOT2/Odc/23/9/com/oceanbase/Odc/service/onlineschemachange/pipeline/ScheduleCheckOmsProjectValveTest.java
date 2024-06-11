import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

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
import com.oceanbase.odc.service.onlineschemachange.utils.ValveContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    @MockBean
    private ProjectOpenApiService projectOpenApiService;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @MockBean
    private OscTaskCompleteHandler completeHandler;

    @MockBean
    private SwapTableUtil swapTableUtil;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setFieldValue(valve, "projectOpenApiService", projectOpenApiService);
        ReflectionTestUtils.setFieldValue(valve, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setFieldValue(valve, "onlineSchemaChangeProperties", onlineSchemaChangeProperties);
        ReflectionTestUtils.setFieldValue(valve, "completeHandler", completeHandler);
        ReflectionTestUtils.setFieldValue(valve, "swapTableUtil", swapTableUtil);
    }

    @Test
    public void testInvoke_Success() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(
                new ProjectStepVO("PreCheck", PrecheckResult.PASSED, 50, 100, "PreCheck Success"),
                new ProjectStepVO("FullTransfer", FullVerificationResult.CONSISTENT, 100, 100,
                        "FullTransfer Success"),
                new ProjectStepVO("FullVerifier", FullVerificationResult.CONSISTENT, 50, 100,
                        "FullVerifier Success")
        );
        ProjectProgressResponse progress = new ProjectProgressResponse();
        progress.setProjectSteps(projectSteps.toArray(new ProjectStepVO[0]));
        ProjectFullVerifyResultResponse verifyResponse = new ProjectFullVerifyResultResponse();
        verifyResponse.setProjectId("projectId");
        verifyResponse.setDatabaseName("databaseName");
        verifyResponse.setUid(UUID.randomUUID().toString());
        FullVerificationResult result = FullVerificationResult.CONSISTENT;

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class))).thenReturn(verifyResponse);

        valve.invoke(getValveContext(taskParameter));

        OnlineSchemaChangeScheduleTaskResult result1 = JsonUtils.fromJson(valve.getLastResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);
        assertEquals(result1.getFullTransferEstimatedCount(), 3);
        assertEquals(result1.getFullTransferFinishedCount(), 3);
        assertEquals(result1.getFullVerificationResult(), result);
        assertEquals(result1.getFullVerificationResultDescription(), "FullVerifier Success");
        assertEquals(result1.getFullVerificationProgressPercentage(), 50);
        assertEquals(result1.getCurrentStep(), "FullVerifier");
        assertEquals(result1.getCurrentStepStatus(), TaskStatus.RUNNING);
        assertEquals(result1.getManualSwapTableEnabled(), false);
        assertEquals(result1.getManualSwapTableStarted(), false);
        assertEquals(valve.getLastStepStatus(), TaskStatus.DONE);

        verify(projectOpenApiService).resumeProject(projectRequest);
    }

    @Test
    public void testInvoke_PreCheckFailed() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(
                new ProjectStepVO("PreCheck", PrecheckResult.FAILED, 50, 100, "PreCheck Failure")
        );

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        assertThrows(OscException.class, () -> valve.invoke(getValveContext(taskParameter)));

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(projectSteps, null, null)
                .withCheckerVerifyResult(() -> null)
                .withResumeProject(() -> null)
                .getCheckerResult();
        assertEquals(projectStepResult.getCurrentStepStatus(), TaskStatus.FAILED);
    }

    @Test
    public void testInvoke_FullVerificationInconsistent() throws Exception {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = Lists.newArrayList(
                new ProjectStepVO("FullVerifier", FullVerificationResult.INCONSISTENT, 50, 100,
                        "FullVerifier Inconsistent")
        );

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);

        assertThrows(OscException.class, () -> valve.invoke(getValveContext(taskParameter)));

        ProjectStepResult projectStepResult = new ProjectStepResultChecker(projectSteps, null, null)
                .withCheckerVerifyResult(() -> null)
                .withResumeProject(() -> null)
                .getCheckerResult();
        assertEquals(projectStepResult.getCurrentStepStatus(), TaskStatus.FAILED);

        String errorMessage = "Task failed for origin table has inconsistent data with new table";
        assertEquals(projectStepResult.getErrorMsg(), errorMessage);
    }

    private ProjectControlRequest getProjectRequest(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid(taskParam.getUid());
        projectRequest.setId(taskParam.getOmsProjectId());
        return projectRequest;
    }

    private ValveContext getValveContext(OnlineSchemaChangeScheduleTaskParameters taskParam) {
        ValveContext context = mock(ValveContext.class);
        ReflectionTestUtils.setFieldValue(context, "scheduleTask", new ScheduleTaskEntity());
        ReflectionTestUtils.setFieldValue(context, "taskParameter", taskParam);
        return context;
    }
}