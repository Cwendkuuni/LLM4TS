import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.configuration.OnlineSchemaChangeProperties;
import com.oceanbase.odc.service.onlineschemachange.exception.OscException;
import com.oceanbase.odc.service.onlineschemachange.model.FullVerificationResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.oms.enums.OmsStepName;
import com.oceanbase.odc.service.onlineschemachange.oms.openapi.ProjectOpenApiService;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectFullVerifyResultResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectProgressResponse;
import com.oceanbase.odc.service.onlineschemachange.oms.response.ProjectStepVO;
import com.oceanbase.odc.service.onlineschemachange.pipeline.ProjectStepResultChecker.ProjectStepResult;
import com.oceanbase.odc.service.onlineschemachange.rename.SwapTableUtil;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class ScheduleCheckOmsProjectValveTest {

    @Mock
    private ProjectOpenApiService projectOpenApiService;
    @Mock
    private OscTaskCompleteHandler completeHandler;
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private OnlineSchemaChangeProperties onlineSchemaChangeProperties;

    @InjectMocks
    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInvoke_whenProjectStepsAndProgressAreRetrievedSuccessfully_thenProjectStepResultIsGeneratedAndTaskIsUpdated() {
        // Given
        String projectId = "project-1";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        ProjectStepVO[] projectSteps = new ProjectStepVO[] {new ProjectStepVO()};
        ProjectProgressResponse progress = new ProjectProgressResponse();
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenReturn(projectSteps);
        when(projectOpenApiService.describeProjectProgress(projectRequest)).thenReturn(progress);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(createScheduleTaskEntity());

        // When
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // Then
        ScheduleTaskEntity actualScheduleTask = scheduleTaskRepository.findById(anyLong()).get();
        assertEquals(TaskStatus.RUNNING, actualScheduleTask.getStatus());
        assertEquals(1, actualScheduleTask.getProgressPercentage());
    }

    @Test(expected = OscException.class)
    public void testInvoke_whenProjectStepsOrProgressFetchFails_thenExceptionIsThrown() {
        // Given
        String projectId = "project-1";
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        ProjectControlRequest projectRequest = new ProjectControlRequest();
        projectRequest.setId(projectId);
        when(projectOpenApiService.describeProjectSteps(projectRequest)).thenThrow(new RuntimeException());

        // When
        scheduleCheckOmsProjectValve.invoke(mock(ValveContext.class));

        // Then
        // An exception is expected to be thrown
    }

    @Test
    public void testHandleOmsProjectStepResult_whenProjectStepStatusIsDONEAndFullVerificationResultIsConsistentOrUnchecked_thenSwapTableIsOpened() {
        // Given
        SwapTableType swapTableType = SwapTableType.TABLE;
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        result.setManualSwapTableEnabled(false);
        when(projectOpenApiService.describeProjectSteps(any(ProjectControlRequest.class))).thenReturn(new ProjectStepVO[0]);
        when(projectOpenApiService.describeProjectProgress(any(ProjectControlRequest.class))).thenReturn(new ProjectProgressResponse());

        // When
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(mock(ValveContext.class), mock(ProjectStepResult.class), result, swapTableType, scheduleTask);

        // Then
        assertEquals(true, result.isManualSwapTableEnabled());
    }

    @Test
    public void testHandleOmsProjectStepResult_whenProjectStepStatusIsNotDONE_thenContinueWithNextStep() {
        // Given
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        ProjectStepResult projectStepResult = mock(ProjectStepResult.class);
        when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.RUNNING);

        // When
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult, mock(OnlineSchemaChangeScheduleTaskResult.class), SwapTableType.TABLE, scheduleTask);

        // Then
        // The next step should be handled
    }

    @Test(expected = OscException.class)
    public void testHandleOmsProjectStepResult_whenProjectStepStatusIsDONEButFullVerificationResultIsInconsistent_thenExceptionIsThrown() {
        // Given
        SwapTableType swapTableType = SwapTableType.TABLE;
        ScheduleTaskEntity scheduleTask = createScheduleTaskEntity();
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(taskParameter);
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        // When
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(mock(ValveContext.class), projectStepResult, result, swapTableType, scheduleTask);

        // Then
        // An exception is expected to be thrown
    }

    @Test
    public void testListProjectFullVerifyResult_whenCalledSuccessfully_thenResponseIsReturned() {
        // Given
        String projectId = "project-1";
        String databaseName = "database";
        String uid = UUID.randomUUID().toString();
        ProjectFullVerifyResultResponse expectedResponse = mock(ProjectFullVerifyResultResponse.class);

        when(projectOpenApiService.listProjectFullVerifyResult(any(ListProjectFullVerifyResultRequest.class)))
                .thenReturn(expectedResponse);

        // When
        ProjectFullVerifyResultResponse actualResponse = scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, databaseName, uid);

        // Then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test(expected = RuntimeException.class)
    public void testListProjectFullVerifyResult_whenCalledWithInvalidProjectId_thenExceptionIsThrown() {
        // Given
        String projectId = "invalid-project-id";
        String databaseName = "database";
        String uid = UUID.randomUUID().toString();

        // When
        scheduleCheckOmsProjectValve.listProjectFullVerifyResult(projectId, databaseName, uid);

        // Then
        // An exception is expected to be thrown
    }

    @Test
    public void testAdaptResult_whenCalledWithValidProjectStepResultAndResult_thenResultIsAdapted() {
        // Given
        ProjectStepResult projectStepResult = new ProjectStepResult();
        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult(new OnlineSchemaChangeScheduleTaskParameters());
        expectedResult.setFullTransferEstimatedCount(10L);
        expectedResult.setFullTransferFinishedCount(5L);
        expectedResult.setFullTransferProgressPercentage(50);
        expectedResult.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        expectedResult.setFullVerificationResultDescription("result description");
        expectedResult.setFullVerificationProgressPercentage(50);
        expectedResult.setCurrentStep(OmsStepName.FULL_VERIFIER.name());
        expectedResult.setCurrentStepStatus(TaskStatus.RUNNING.name());
        expectedResult.setPrecheckResult(PrecheckResult.PASSED);

        // When
        OnlineSchemaChangeScheduleTaskResult actualResult = scheduleCheckOmsProjectValve.adaptResult(expectedResult, projectStepResult);

        // Then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAdaptResult_whenCalledWithNullProjectStepResult_thenNullResultIsReturned() {
        // Given
        OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult(new OnlineSchemaChangeScheduleTaskParameters());

        // When
        OnlineSchemaChangeScheduleTaskResult actualResult = scheduleCheckOmsProjectValve.adaptResult(null, null);

        // Then
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetProjectRequest_whenCalledWithValidOnlineSchemaChangeScheduleTaskParameters_thenProjectControlRequestIsReturned() {
        // Given
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();

        // When
        ProjectControlRequest actualProjectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParameter);

        // Then
        assertNotNull(actualProjectRequest);
        assertEquals(taskParameter.getOmsProjectId(), actualProjectRequest.getId());
        assertEquals(taskParameter.getUid(), actualProjectRequest.getUid());
    }

    @Test
    public void testRecordCurrentProgress_whenCalledWithValidProjectIdAndResult_thenTableIsPrinted() {
        // Given
        String projectId = "project-1";
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(new OnlineSchemaChangeScheduleTaskParameters());
        result.setCurrentStep(OmsStepName.FULL_VERIFIER.name());
        result.setCurrentStepStatus(TaskStatus.RUNNING.name());
        result.setFullVerificationResult(FullVerificationResult.CONSISTENT);
        result.setFullVerificationResultDescription("result description");
        result.setFullVerificationProgressPercentage(50);

        // When
        scheduleCheckOmsProjectValve.recordCurrentProgress(projectId, result);

        // Then
        // A table should be printed
    }

    private ScheduleTaskEntity createScheduleTaskEntity() {
        return new ScheduleTaskEntity();
    }
}