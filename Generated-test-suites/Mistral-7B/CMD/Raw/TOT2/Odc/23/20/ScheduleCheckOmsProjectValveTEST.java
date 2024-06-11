import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
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
import com.oceanbase.odc.service.onlineschemachange.test.TestConstants;

@ActiveProfiles("test")
public class ScheduleCheckOmsProjectValveTest {

    @InjectMocks
    private ScheduleCheckOmsProjectValve valve;

    @MockBean
    private ProjectOpenApiService projectOpenApiService;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @MockBean
    private OscTaskCompleteHandler completeHandler;

    @MockBean
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @Autowired
    private DefaultTableFactory defaultTableFactory;

    @Autowired
    private JsonUtils jsonUtils;

    private ScheduleTaskEntity scheduleTask;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        scheduleTask = new ScheduleTaskEntity();
    }

    @Test
    public void testInvokeSuccess() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = TestConstants.createTestTaskParameters();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        List<ProjectStepVO> projectSteps = TestConstants.createTestProjectSteps();
        ProjectProgressResponse progress = TestConstants.createTestProjectProgress();
        ProjectFullVerifyResultResponse fullVerifyResult = TestConstants.createTestProjectFullVerifyResult();

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(fullVerifyResult);

        // When
        valve.invoke(TestConstants.createTestValveContext(scheduleTask, taskParameter));

        // Then
        verify(projectOpenApiService, times(3)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        Assert.assertEquals(TaskStatus.DONE, scheduleTask.getStatus());
    }

    @Test(expected = OscException.class)
    public void testInvokePreCheckFailed() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = TestConstants.createTestTaskParameters();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.FAILED, PrecheckResult.FAILED,
                "Pre check failed");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(TestConstants.createTestProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(TestConstants.createTestProjectProgress());
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(TestConstants.createTestProjectFullVerifyResult());
        when(projectOpenApiService.resumeProject(projectRequest)).thenReturn(null);

        // When
        valve.invoke(TestConstants.createTestValveContext(scheduleTask, taskParameter));

        // Then
        verify(projectOpenApiService, times(3)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class));
        verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
        throw new OscException(ErrorCodes.OmsPreCheckFailed, projectStepResult.getErrorMsg());
    }

    @Test
    public void testInvokeDataCheckInconsistent() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = TestConstants.createTestTaskParameters();
        ProjectControlRequest projectRequest = valve.getProjectRequest(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.FAILED,
                FullVerificationResult.INCONSISTENT, "Data check inconsistent");

        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(TestConstants.createTestProjectSteps());
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(TestConstants.createTestProjectProgress());
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(TestConstants.createTestProjectFullVerifyResult());

        // When
        valve.invoke(TestConstants.createTestValveContext(scheduleTask, taskParameter));

        // Then
        verify(projectOpenApiService, times(3)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any(ListProjectFullVerifyRequest.class));
        throw new OscException(ErrorCodes.OmsDataCheckInconsistent,
                "Task failed for origin table has inconsistent data with new table, result: "
                        + projectStepResult.getFullVerificationResultDescription());
    }

    @Test
    public void testListProjectFullVerifyResult() throws Exception {
        // Given
        String projectId = "testProjectId";
        String databaseName = "testDatabaseName";
        String uid = "testUid";

        // When
        ProjectFullVerifyResultResponse result = valve.listProjectFullVerifyResult(projectId, databaseName, uid);

        // Then
        Assert.assertNotNull(result);
    }

    @Test
    public void testHandleOmsProjectStepResultSuccess() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = TestConstants.createTestTaskParameters();
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.CONSISTENT, null);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        // When
        valve.handleOmsProjectStepResult(TestConstants.createTestValveContext(), projectStepResult, result, SwapTableType.DATABASE, scheduleTask);

        // Then
        Assert.assertEquals(FullVerificationResult.CONSISTENT, result.getFullVerificationResult());
    }

    @Test
    public void testHandleOmsProjectStepResultFailed() throws Exception {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = TestConstants.createTestTaskParameters();
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.FAILED, FullVerificationResult.INCONSISTENT, null);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);

        // When
        valve.handleOmsProjectStepResult(TestConstants.createTestValveContext(), projectStepResult, result, SwapTableType.DATABASE, scheduleTask);

        // Then
        Assert.assertEquals(FullVerificationResult.INCONSISTENT, result.getFullVerificationResult());
    }

    @Test
    public void testRecordCurrentProgress() throws Exception {
        // Given
        String projectId = "testProjectId";
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(new OnlineSchemaChangeScheduleTaskParameters());

        // When
        valve.recordCurrentProgress(projectId, result);

        // Then
        Table table = valve.getDefaultTableFactory().generateTable(5, valve.getHeader(),
                valve.getBody(projectId, result));
        System.out.println(table.render().toString());
    }
}