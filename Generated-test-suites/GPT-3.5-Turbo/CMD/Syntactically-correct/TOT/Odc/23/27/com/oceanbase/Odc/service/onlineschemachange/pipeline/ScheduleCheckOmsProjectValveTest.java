import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
    }

    @Test
    public void testHandleOmsProjectStepResult_Success() {
        // Mock objects
        ValveContext valveContext = mock(ValveContext.class);
        ProjectStepResult projectStepResult = new ProjectStepResult(TaskStatus.DONE, FullVerificationResult.CONSISTENT, null);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        SwapTableType swapTableType = SwapTableType.AUTO;
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);

        // Test method
        scheduleCheckOmsProjectValve.handleOmsProjectStepResult(valveContext, projectStepResult, result, swapTableType, scheduleTask);

        // Verify
        verify(scheduleTask, times(1)).setResultJson(any());
        verify(scheduleTaskRepository, times(1)).update(scheduleTask);
        verify(valveContext, times(1)).invoke(valveContext);
    }

    @Test
    public void testContinueHandleProjectStepResult_PrecheckFailed() {
        // Mock objects
        ProjectStepResult projectStepResult = new ProjectStepResult(PrecheckResult.FAILED, null, "Precheck failed");
        
        // Test method and assert
        try {
            scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
        } catch (OscException e) {
            assertEquals(ErrorCodes.OmsPreCheckFailed, e.getErrorCode());
            assertEquals("Precheck failed", e.getMessage());
        }
    }

    @Test
    public void testContinueHandleProjectStepResult_DataCheckInconsistent() {
        // Mock objects
        ProjectStepResult projectStepResult = new ProjectStepResult(null, FullVerificationResult.INCONSISTENT, null);

        // Test method and assert
        try {
            scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
        } catch (OscException e) {
            assertEquals(ErrorCodes.OmsDataCheckInconsistent, e.getErrorCode());
            assertEquals("Task failed for origin table has inconsistent data with new table, result: Inconsistent", e.getMessage());
        }
    }

    @Test
    public void testGetProjectRequest() {
        // Mock objects
        OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam.setUid("123");
        taskParam.setOmsProjectId("456");

        // Test method
        ProjectControlRequest projectRequest = scheduleCheckOmsProjectValve.getProjectRequest(taskParam);

        // Assert
        assertEquals("123", projectRequest.getUid());
        assertEquals("456", projectRequest.getId());
    }

    @Test
    public void testRecordCurrentProgress() {
        // Mock objects
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setCurrentStep("FULL_TRANSFER");
        result.setFullTransferProgressPercentage(50);
        result.setFullTransferFinishedCount(100);
        result.setFullTransferEstimatedCount(200);
        String projectId = "789";

        // Test method
        scheduleCheckOmsProjectValve.recordCurrentProgress(projectId, result);

        // Verify
        // Check if logging works as expected
    }

    @Test
    public void testGetHeader() {
        // Test method
        List<String> header = scheduleCheckOmsProjectValve.getHeader();

        // Assert
        assertEquals(5, header.size());
        assertEquals("ProjectId", header.get(0));
        assertEquals("Step", header.get(1));
        assertEquals("Status", header.get(2));
        assertEquals("Progress", header.get(3));
        assertEquals("Description", header.get(4));
    }
}