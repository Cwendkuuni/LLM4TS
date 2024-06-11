import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;

public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setup() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
    }

    @Test
    public void testInvoke() {
        // Mocking necessary dependencies
        ValveContext valveContext = mock(ValveContext.class);
        OscValveContext context = mock(OscValveContext.class);
        ProjectControlRequest projectRequest = mock(ProjectControlRequest.class);
        ProjectFullVerifyResultResponse response = mock(ProjectFullVerifyResultResponse.class);
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);
        ProjectStepResultChecker.ProjectStepResult projectStepResult = mock(ProjectStepResultChecker.ProjectStepResult.class);
        
        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(context.getTaskParameter()).thenReturn(taskParameter);
        when(scheduleTask.getId()).thenReturn(1L);
        when(taskParameter.getOmsProjectId()).thenReturn("123");
        when(projectRequest.getId()).thenReturn("123");
        when(scheduleTask.getResultJson()).thenReturn("{}");
        when(response.getDetail()).thenReturn("Details");
        
        // Testing the invoke method
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Verifying the interactions
        verify(valveContext, times(1)).getScheduleTask();
        verify(valveContext, times(1)).getTaskParameter();
        verify(valveContext, times(1)).getProjectRequest(any());
        verify(scheduleTask, times(2)).getResultJson();
        verify(scheduleTask, times(1)).setResultJson(any());
        verify(scheduleTaskRepository, times(1)).update(scheduleTask);
        verify(projectOpenApiService, times(1)).resumeProject(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectProgress(projectRequest);
        verify(projectOpenApiService, times(1)).describeProjectSteps(projectRequest);
        verify(projectOpenApiService, times(1)).listProjectFullVerifyResult(any());
        verify(projectStepResult, times(1)).getTaskStatus();
        verify(projectStepResult, atLeastOnce()).getFullVerificationResult();
        verify(projectStepResult, atLeastOnce()).getCurrentStep();
        verify(log, atLeastOnce()).debug(anyString());
        verify(log, atLeastOnce()).info(anyString());
        verify(log, atLeastOnce()).warn(anyString());
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // Mocking necessary dependencies
        ValveContext valveContext = mock(ValveContext.class);
        ProjectStepResultChecker.ProjectStepResult projectStepResult = mock(ProjectStepResultChecker.ProjectStepResult.class);
        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);
        SwapTableType swapTableType = SwapTableType.AUTO;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);

        when(projectStepResult.getTaskStatus()).thenReturn(TaskStatus.DONE);
        when(projectStepResult.getFullVerificationResult()).thenReturn(FullVerificationResult.CONSISTENT);
        when(result.isManualSwapTableEnabled()).thenReturn(false);
        when(result.isManualSwapTableStarted()).thenReturn(false);
        when(scheduleTask.getStatus()).thenReturn(TaskStatus.PENDING);

        // Testing the handleOmsProjectStepResult method
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(valveContext, projectStepResult, result, swapTableType, scheduleTask);

        // Verifying the interactions
        verify(projectStepResult, atLeastOnce()).getTaskStatus();
        verify(result, atLeastOnce()).isManualSwapTableEnabled();
        verify(result, atLeastOnce()).isManualSwapTableStarted();
        verify(scheduleTask, atLeastOnce()).getResultJson();
        verify(scheduleTask, atLeastOnce()).setStatus(any());
        verify(scheduleTaskRepository, atLeastOnce()).update(scheduleTask);
        verify(projectOpenApiService, never()).resumeProject(any());
        verify(projectOpenApiService, never()).describeProjectProgress(any());
        verify(log, never()).info(anyString());
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        // Mocking necessary dependencies
        ProjectStepResultChecker.ProjectStepResult projectStepResult = mock(ProjectStepResultChecker.ProjectStepResult.class);

        when(projectStepResult.getPreCheckResult()).thenReturn(PrecheckResult.FAILED);

        // Testing the continueHandleProjectStepResult method
        try {
            scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verifying the interactions
        verify(projectStepResult, atLeastOnce()).getPreCheckResult();
        verify(log, never()).warn(anyString());
    }

    @Test
    public void testAdaptResult() {
        // Mocking necessary dependencies
        ProjectStepResultChecker.ProjectStepResult projectStepResult = mock(ProjectStepResultChecker.ProjectStepResult.class);
        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);

        when(projectStepResult.getFullTransferEstimatedCount()).thenReturn(100);
        when(result.getFullTransferEstimatedCount()).thenReturn(100);

        // Testing the adaptResult method
        scheduleCheckOmsProjectValve.adaptResult(result, projectStepResult);

        // Verifying the interactions
        verify(projectStepResult, atLeastOnce()).getFullTransferEstimatedCount();
        verify(result, atLeastOnce()).setFullTransferEstimatedCount(any());
    }

    @Test
    public void testGetProjectRequest() {
        // Mocking necessary dependencies
        OnlineSchemaChangeScheduleTaskParameters taskParam = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        
        when(taskParam.getUid()).thenReturn("123");
        when(taskParam.getOmsProjectId()).thenReturn("123");

        // Testing the getProjectRequest method
        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);

        // Verifying the interactions
        verify(taskParam, atLeastOnce()).getUid();
        verify(taskParam, atLeastOnce()).getOmsProjectId();
        assert(projectRequest.getId()).equals("123");
    }

    @Test
    public void testRecordCurrentProgress() {
        // Mocking necessary dependencies
        OnlineSchemaChangeScheduleTaskResult result = mock(OnlineSchemaChangeScheduleTaskResult.class);
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);

        when(result.getCurrentStep()).thenReturn("PRE_CHECK");
        when(result.getPrecheckResultDescription()).thenReturn("Description");
        when(log.isDebugEnabled()).thenReturn(true);

        // Testing the recordCurrentProgress method
        scheduleCheckOmsProjectValve.recordCurrentProgress("123", result);

        // Verifying the interactions
        verify(log, atLeastOnce()).info(anyString());
    }

    @Test
    public void testGetHeader() {
        // Testing the getHeader method
        List<String> header = scheduleCheckOmsProjectValve.getHeader();

        // Verifying the interactions
        assert(header.size()).equals(5);
        assert(header.get(0)).equals("ProjectId");
    }
}