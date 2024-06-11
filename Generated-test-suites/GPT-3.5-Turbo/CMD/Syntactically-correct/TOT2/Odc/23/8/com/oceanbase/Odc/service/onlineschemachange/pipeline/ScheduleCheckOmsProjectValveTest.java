import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setup() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
    }

    @Test
    public void testInvoke_HandleProjectStepResult() {
        // Given
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        projectStepResult.setErrorMsg("Precheck failed");

        // When
        try {
            scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
            fail("Expected OscException to be thrown");
        } catch (OscException e) {
            // Then
            assertEquals(ErrorCodes.OmsPreCheckFailed, e.getErrorCode());
            assertEquals("Precheck failed", e.getMessage());
        }
    }

    @Test
    public void testContinueHandleProjectStepResult_InconsistentData() {
        // Given
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setFullVerificationResult(FullVerificationResult.INCONSISTENT);

        // When
        try {
            scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult);
            fail("Expected OscException to be thrown");
        } catch (OscException e) {
            // Then
            assertEquals(ErrorCodes.OmsDataCheckInconsistent, e.getErrorCode());
            assertEquals("Task failed for origin table has inconsistent data with new table, result: Inconsistent", e.getMessage());
        }
    }

    // More test cases for other methods in the ScheduleCheckOmsProjectValve class

}