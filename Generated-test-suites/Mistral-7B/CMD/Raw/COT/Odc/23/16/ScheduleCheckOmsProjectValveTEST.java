import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
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
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

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
    private DefaultTableFactory defaultTableFactory;

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    private OnlineSchemaChangeScheduleTaskParameters taskParameter;
    private ScheduleTaskEntity scheduleTask;
    private ProjectControlRequest projectRequest;
    private ProjectStepResult projectStepResult;
    private ProjectFullVerifyResultResponse projectFullVerifyResultResponse;
    private ProjectProgressResponse projectProgressResponse;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setSwapTableType(SwapTableType.ONE_WAY);
        scheduleTask = new ScheduleTaskEntity();
        projectRequest = new ProjectControlRequest();
        projectStepResult = new ProjectStepResult();
        projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        projectProgressResponse = new ProjectProgressResponse();
    }

    @Test
    public void testInvoke_success() {
        // Given
        when(projectOpenApiService.describeProjectSteps(projectRequest))
                .thenReturn(new ArrayList<ProjectStepVO>() {{
                    add(new ProjectStepVO() {{
                        setCurrentStep(OmsStepName.PRE_CHECK.name());
                        setCurrentStepStatus("RUNNING");
                    }});
                }});
        when(projectOpenApiService.describeProjectProgress(projectRequest))
                .thenReturn(projectProgressResponse);

        // When
        valve.invoke(createValveContext());

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verifyNoMoreInteractions(projectOpenApiService);

        // Additional Asserts
        assertNotNull(scheduleTask.getResultJson());
        assertTrue(StringUtils.isNotBlank(scheduleTask.getResultJson()));
    }

    @Test(expected = OscException.class)
    public void testInvoke_preCheckFailed() {
        // Given
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        when(projectOpenApiService.describeProjectSteps(projectRequest))
                .thenReturn(new ArrayList<ProjectStepVO>() {{
                    add(projectStepResult);
                }});

        // When
        valve.invoke(createValveContext());

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, never()).describeProjectProgress(projectRequest);
        verifyNoMoreInteractions(projectOpenApiService);

        // Additional Asserts
        assertEquals(ErrorCodes.OmsPreCheckFailed, projectStepResult.getErrorMsg());
    }

    @Test(expected = OscException.class)
    public void testInvoke_dataCheckInconsistent() {
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);
        when(projectOpenApiService.describeProjectSteps(projectRequest))
                .thenReturn(new ArrayList<ProjectStepVO>() {{
                    add(projectStepResult);
                }});

        // When
        valve.invoke(createValveContext());

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, never()).describeProjectProgress(projectRequest);
        verifyNoMoreInteractions(projectOpenApiService);

        // Additional Asserts
        assertEquals(ErrorCodes.OmsDataCheckInconsistent, projectStepResult.getErrorMsg());
    }

    @Test
    public void testInvoke_fullVerifyResultListProjectFullVerifyResult() {
        projectRequest.setId("testProjectId");
        projectRequest.setUid("testUid");
        ListProjectFullVerifyResultRequest listProjectFullVerifyResultRequest = new ListProjectFullVerifyResultRequest();
        listProjectFullVerifyResultRequest.setProjectId("testProjectId");
        listProjectFullVerifyResultRequest.setSourceSchemas(new String[]{"testDatabaseName"});
        listProjectFullVerifyResultRequest.setDestSchemas(new String[]{"testDatabaseName"});

        projectFullVerifyResultResponse.setFullVerificationResultList(Lists.newArrayList(
                new ProjectFullVerifyResultResponse.FullVerificationResultItem() {{
                    setDatabaseName("testDatabaseName");
                    setResult(FullVerificationResult.CONSISTENT.name());
                    setTransferProgressPercentage(100);
                    setTransferResultDescription("Full Verification successful");
                }}));

        when(projectOpenApiService.listProjectFullVerifyResult(listProjectFullVerifyResultRequest))
                .thenReturn(projectFullVerifyResultResponse);

        // When
        valve.invoke(createValveContext());

        // Then
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(listProjectFullVerifyResultRequest);
        verifyNoMoreInteractions(projectOpenApiService);

        // Additional Asserts
        assertTrue(StringUtils.isNotBlank(scheduleTask.getResultJson()));
    }

    @Test
    public void testAdaptResult() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullTransferEstimatedCount(100);
        projectStepResult.setFullTransferFinishedCount(50);
        projectStepResult.setFullTransferProgressPercentage(50);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        projectStepResult.setFullVerificationProgressPercentage(75);
        projectStepResult.setCurrentStep(OmsStepName.FULL_TRANSFER.name());
        projectStepResult.setCurrentStepStatus(TaskStatus.RUNNING.name());

        valve.adaptResult(result, projectStepResult);

        assertEquals(50, result.getFullTransferFinishedCount());
        assertEquals(100, result.getFullTransferEstimatedCount());
        assertEquals(50, result.getFullTransferProgressPercentage());
        assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
        assertEquals(75, result.getFullVerificationProgressPercentage());
        assertEquals(OmsStepName.FULL_TRANSFER, result.getCurrentStep());
        assertEquals(TaskStatus.RUNNING, result.getCurrentStepStatus());
    }

    @Test
    public void testGetProjectRequest() {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setSwapTableType(SwapTableType.ONE_WAY);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        assertEquals(taskParameter.getUid(), projectRequest.getUid());
        assertEquals(taskParameter.getOmsProjectId(), projectRequest.getId());
    }

    private void recordCurrentProgress(String projectId, OnlineSchemaChangeScheduleTaskResult result) {
        valve.recordCurrentProgress(projectId, result);
    }

    private ValveContext createValveContext() {
        ValveContext valveContext = new ValveContext() {{
            setScheduleTask(scheduleTask);
            setTaskParameter(taskParameter);
        }};
        return valveContext;
    }
}