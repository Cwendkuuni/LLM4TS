import org.junit.Test;

public class ScheduleCheckOmsProjectValveTest {

    @Test
    public void testInvoke() {
        // Test the invoke method of ScheduleCheckOmsProjectValve
        // Include typical use cases, edge cases, and error scenarios
        // Ensure proper handling of projectStepResult and result
    }

    @Test
    public void testHandleOmsProjectStepResult() {
        // Test the handleOmsProjectStepResult method of ScheduleCheckOmsProjectValve
        // Include tests for different projectStepResult scenarios
        // Ensure correct handling of manualSwapTable logic
    }

    @Test
    public void testContinueHandleProjectStepResult() {
        // Test the continueHandleProjectStepResult method of ScheduleCheckOmsProjectValve
        // Include tests for different preCheckResult and fullVerificationResult scenarios
        // Ensure proper exception handling
    }

    @Test
    public void testListProjectFullVerifyResult() {
        // Test the listProjectFullVerifyResult method of ScheduleCheckOmsProjectValve
        // Include tests with different request parameters
        // Ensure correct processing of ProjectFullVerifyResultResponse
    }

    @Test
    public void testAdaptResult() {
        // Test the adaptResult method of ScheduleCheckOmsProjectValve
        // Include tests for different ProjectStepResult scenarios
        // Ensure correct adaptation of OnlineSchemaChangeScheduleTaskResult
    }

    @Test
    public void testGetProjectRequest() {
        // Test the getProjectRequest method of ScheduleCheckOmsProjectValve
        // Include tests with different taskParam scenarios
        // Ensure proper creation of ProjectControlRequest
    }

    @Test
    public void testRecordCurrentProgress() {
        // Test the recordCurrentProgress method of ScheduleCheckOmsProjectValve
        // Include tests with different result scenarios
        // Ensure correct logging of progress and description
    }

}