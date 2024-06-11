import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
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
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.pipeline.BaseValve;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;
    private ProjectOpenApiService projectOpenApiService;
    private OscTaskCompleteHandler completeHandler;
    private ScheduleTaskEntity scheduleTask;
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;
    
    @Before
    public void setUp() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        projectOpenApiService = mock(ProjectOpenApiService.class);
        completeHandler = mock(OscTaskCompleteHandler.class);
        scheduleTask = new ScheduleTaskEntity();
        onlineSchemaChangeProperties = new OnlineSchemaChangeProperties();
    }

    @Test
    public void testInvoke_Successful() {
        // Setup
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("projectId");
        taskParameter.setDatabaseName("dbName");
        taskParameter.setUid("uid");

        when(scheduleTask.getResultJson()).thenReturn("resultJson");

        // Preparing mock data for projectOpenApiService
        ProjectProgressResponse progressResponse = new ProjectProgressResponse();
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class))).thenReturn(mock(List.class));
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class))).thenReturn(progressResponse);

        // Preparing mock data for listProjectFullVerifyResult
        ProjectFullVerifyResultResponse projectFullVerifyResultResponse = new ProjectFullVerifyResultResponse();
        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(projectFullVerifyResultResponse);

        // Execute
        scheduleCheckOmsProjectValve.invoke(someValveContext);

        // Verify
        // Add verification code here
    }

    @Test(expected = OscException.class)
    public void testHandleOmsProjectStepResult_PreCheckFailed_ThrowsOscException() {
        // Setup
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        projectStepResult.setErrorMsg("Precheck failed message");

        // Execute
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(someValveContext, projectStepResult, someResult,
                someSwapTableType, someScheduleTask);

        // OscException expected
    }

    @Test(expected = OscException.class)
    public void testHandleOmsProjectStepResult_InconsistentData_ThrowsOscException() {
        // Setup
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.PASSED);
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        // Execute
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(someValveContext, projectStepResult, someResult,
                someSwapTableType, someScheduleTask);

        // OscException expected
    }

    @Test
    public void testContinueHandleProjectStepResult_UnhandledCases() {
        // Setup
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.PASSED);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        // Execute
        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);

        // No exception expected
    }
}