import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    @MockBean
    private ProjectOpenApiService projectOpenApiService;
    @MockBean
    private OscTaskCompleteHandler completeHandler;
    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @Autowired
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Value("${test.projectId}")
    private String projectId;

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    private ScheduleTaskEntity scheduleTask;

    @Before
    public void setup() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
        scheduleCheckOmsProjectValve.setCompleteHandler(completeHandler);
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
        scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);

        scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setResultJson("");
        scheduleTask.setProgressPercentage(0);
        scheduleTask.setStatus("SCHEDULED");
    }

    @Test
    public void testInvoke_whenProjectStepsAreDone_shouldUpdateScheduleTaskAndRecordCurrentProgress() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(
                new ProjectStepVO() {{
                    setName("Pre Check");
                    setStatus(TaskStatus.DONE.name());
                    setFullVerificationResult(FullVerificationResult.CONSISTENT.name());
                }},
                new ProjectStepVO() {{
                    setName("Full Transfer");
                    setStatus(TaskStatus.DONE.name());
                    setFullTransferProgressPercentage(100);
                }},
                new ProjectStepVO() {{
                    setName("Full Verifier");
                    setStatus(TaskStatus.DONE.name());
                    setFullVerificationProgressPercentage(100);
                }});

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(getProjectProgressResponse());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        taskParameter.setSwapTableType(SwapTableType.SWAP_TABLE_1);

        // When
        scheduleCheckOmsProjectValve.invoke(Mockito.mock(ValveContext.class));

        // Then
        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor = ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).update(resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
        assertEquals(TaskStatus.COMPLETED.name(), result.getStatus());
        assertFalse(result.isManualSwapTableEnabled());
        assertFalse(result.isManualSwapTableStarted());
    }

    @Test
    public void testInvoke_whenProjectStepsAreNotDone_shouldContinueHandleProjectStepResult() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(
                new ProjectStepVO() {{
                    setName("Pre Check");
                    setStatus(TaskStatus.FAILED.name());
                    setPreCheckResult(PrecheckResult.FAILED.name());
                    setErrorMsg("Precheck error message");
                }});

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(getProjectProgressResponse());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        taskParameter.setSwapTableType(SwapTableType.SWAP_TABLE_1);

        // When / Then
        assertThrows(OscException.class, () -> scheduleCheckOmsProjectValve.invoke(Mockito.mock(ValveContext.class)));
    }

    @Test
    public void testInvoke_whenProjectStepsAreRunning_shouldContinueHandleProjectStepResult() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(
                new ProjectStepVO() {{
                    setName("Pre Check");
                    setStatus(TaskStatus.RUNNING.name());
                }},
                new ProjectStepVO() {{
                    setName("Full Transfer");
                    setStatus(TaskStatus.RUNNING.name());
                }},
                new ProjectStepVO() {{
                    setName("Full Verifier");
                    setStatus(TaskStatus.RUNNING.name());
                }});

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(getProjectProgressResponse());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        taskParameter.setSwapTableType(SwapTableType.SWAP_TABLE_1);

        // When / Then
        scheduleCheckOmsProjectValve.invoke(Mockito.mock(ValveContext.class));
    }

    @Test
    public void testInvoke_whenProjectStepsAreSuspended_shouldContinueHandleProjectStepResult() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(
                new ProjectStepVO() {{
                    setName("Pre Check");
                    setStatus(TaskStatus.SUSPENDED.name());
                }},
                new ProjectStepVO() {{
                    setName("Full Transfer");
                    setStatus(TaskStatus.RUNNING.name());
                }},
                new ProjectStepVO() {{
                    setName("Full Verifier");
                    setStatus(TaskStatus.RUNNING.name());
                }});

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(getProjectProgressResponse());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        taskParameter.setSwapTableType(SwapTableType.SWAP_TABLE_1);

        // When / Then
        scheduleCheckOmsProjectValve.invoke(Mockito.mock(ValveContext.class));
    }

    @Test
    public void testInvoke_whenProjectStepsAreFailed_shouldContinueHandleProjectStepResult() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(
                new ProjectStepVO() {{
                    setName("Pre Check");
                    setStatus(TaskStatus.FAILED.name());
                    setPreCheckResult(PrecheckResult.FAILED.name());
                    setErrorMsg("Precheck error message");
                }},
                new ProjectStepVO() {{
                    setName("Full Transfer");
                    setStatus(TaskStatus.RUNNING.name());
                }},
                new ProjectStepVO() {{
                    setName("Full Verifier");
                    setStatus(TaskStatus.RUNNING.name());
                }});

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(getProjectProgressResponse());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        taskParameter.setSwapTableType(SwapTableType.SWAP_TABLE_1);

        // When / Then
        assertThrows(OscException.class, () -> scheduleCheckOmsProjectValve.invoke(Mockito.mock(ValveContext.class)));
    }

    @Test
    public void testInvoke_whenProjectProgressIsNull_shouldContinueHandleProjectStepResult() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        List<ProjectStepVO> projectSteps = Lists.newArrayList(
                new ProjectStepVO() {{
                    setName("Pre Check");
                    setStatus(TaskStatus.DONE.name());
                }},
                new ProjectStepVO() {{
                    setName("Full Transfer");
                    setStatus(TaskStatus.RUNNING.name());
                }},
                new ProjectStepVO() {{
                    setName("Full Verifier");
                    setStatus(TaskStatus.RUNNING.name());
                }});

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(null);

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        taskParameter.setSwapTableType(SwapTableType.SWAP_TABLE_1);

        // When / Then
        scheduleCheckOmsProjectValve.invoke(Mockito.mock(ValveContext.class));
    }

    @Test
    public void testInvoke_whenProjectStepsAreNotAvailable_shouldContinueHandleProjectStepResult() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenThrow(new RuntimeException());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(getProjectProgressResponse());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        taskParameter.setSwapTableType(SwapTableType.SWAP_TABLE_1);

        // When / Then
        scheduleCheckOmsProjectValve.invoke(Mockito.mock(ValveContext.class));
    }

    @Test
    public void testInvoke_whenProjectStepsAreEmpty_shouldContinueHandleProjectStepResult() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(Lists.newArrayList());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(getProjectProgressResponse());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId(projectId);
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        taskParameter.setSwapTableType(SwapTableType.SWAP_TABLE_1);

        // When / Then
        scheduleCheckOmsProjectValve.invoke(Mockito.mock(ValveContext.class));
    }

    @Test
    public void testInvoke_whenProjectIsNotFound_shouldContinueHandleProjectStepResult() {
        // Given
        ProjectControlRequest projectRequest = getProjectRequest();
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(getProjectProgressResponse());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("non_existing_project");
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("test_uid");
        taskParameter.setSwapTableType(SwapTableType.SWAP_TABLE_1);

        // When / Then
        scheduleCheckOmsProjectValve.invoke(Mockito.mock(ValveContext.class));
    }

    private ProjectControlRequest getProjectRequest() {
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setUid("test_uid");
        projectRequest.setId(projectId);
        return projectRequest;
    }

    private ProjectProgressResponse getProjectProgressResponse() {
        ProjectProgressResponse projectProgressResponse = new ProjectProgressResponse();
        projectProgressResponse.setStatus("RUNNING");
        projectProgressResponse.setErrorMsg("");
        projectProgressResponse.setTotalStepCount(3);
        projectProgressResponse.setCurrentStepCount(3);
        projectProgressResponse.setStep(Lists.newArrayList(new ProjectStepVO()));
        return projectProgressResponse;
    }
}