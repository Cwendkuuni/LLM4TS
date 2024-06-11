import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.logger.DefaultTableFactory;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.PrecheckResult;
import com.oceanbase.odc.service.onlineschemachange.model.ProjectStepResultChecker;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ListProjectFullVerifyResultRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.request.ProjectControlRequest;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.BaseValve;
import com.oceanbase.odc.service.onlineschemachange.pipeline.OscValveContext;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ScheduleCheckOmsProjectValve;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

@RunWith(MockitoJUnitRunner.class)
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
    private OscValveContext valveContext;

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
        scheduleCheckOmsProjectValve.setProjectOpenApiService(projectOpenApiService);
        scheduleCheckOmsProjectValve.setCompleteHandler(completeHandler);
        scheduleCheckOmsProjectValve.setScheduleTaskRepository(scheduleTaskRepository);
        scheduleCheckOmsProjectValve.setOnlineSchemaChangeProperties(onlineSchemaChangeProperties);
    }

    @Test
    public void testInvoke_SuccessfulExecution() {
        // Mock data
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("12345");
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("98765");

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId("67890");

        ProjectStepResultChecker.ProjectStepResult projectStepResult = new ProjectStepResultChecker.ProjectStepResult();
        projectStepResult.setTaskStatus(TaskStatus.DONE);
        projectStepResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);

        // Mock behaviors
        when(valveContext.getScheduleTask()).thenReturn(scheduleTask);
        when(valveContext.getTaskParameter()).thenReturn(taskParameter);
        when(onlineSchemaChangeProperties.isEnableFullVerify()).thenReturn(true);
        when(projectOpenApiService.describeProjectSteps(any())).thenReturn(new ArrayList<>());
        when(projectOpenApiService.describeProjectProgress(any())).thenReturn(new ProjectProgressResponse());
        when(scheduleTaskRepository.update(any())).thenReturn(scheduleTask);

        // Test the method
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Verify the interactions
        verify(projectOpenApiService, times(1)).describeProjectSteps(any());
        verify(projectOpenApiService, times(1)).describeProjectProgress(any());
        verify(scheduleTaskRepository, times(1)).update(any());
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResult_OmsPreCheckFailed_Exception() {
        ProjectStepResultChecker.ProjectStepResult projectStepResult = new ProjectStepResultChecker.ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);

        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
    }

    @Test(expected = OscException.class)
    public void testContinueHandleProjectStepResult_OmsDataCheckInconsistent_Exception() {
        ProjectStepResultChecker.ProjectStepResult projectStepResult = new ProjectStepResultChecker.ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
    }

    @Test
    public void testRecordCurrentProgress() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setCurrentStep("PRE_CHECK");
        result.setPrecheckResultDescription("Precheck successful");

        scheduleCheckOmsProjectValve.recordCurrentProgress("12345", result);
        // Verify the log output
    }

    // Add more test cases for edge cases and error scenarios

}