import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve;

    @Before
    public void setUp() {
        scheduleCheckOmsProjectValve = new ScheduleCheckOmsProjectValve();
    }

    @Test
    public void testInvoke_Success() {
        // Typical Use Case
        ValveContext valveContext = mock(ValveContext.class);
        OscValveContext context = mock(OscValveContext.class);

        // Mocking necessary parameters
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setOmsProjectId("123");
        taskParameter.setDatabaseName("test_db");
        taskParameter.setUid("user123");

        when(context.getScheduleTask()).thenReturn(scheduleTask);
        when(context.getTaskParameter()).thenReturn(taskParameter);

        // Execution
        scheduleCheckOmsProjectValve.invoke(valveContext);

        // Verification of expected behavior
        verify(valveContext, times(1)).invoke(any(ValveContext.class));
    }

    @Test
    public void testHandleOmsProjectStepResult_FailedPrecheck() {
        // Error Scenario: Precheck failed
        ValveContext valveContext = mock(ValveContext.class);
        ProjectStepResult projectStepResult = new ProjectStepResult();
        projectStepResult.setPreCheckResult(PrecheckResult.FAILED);
        projectStepResult.setErrorMsg("Precheck failed message");

        // Execution and verification of expected exception
        assertThrows(OscException.class, () -> scheduleCheckOmsProjectValve.continueHandleProjectStepResult(projectStepResult));
    }

    @Test
    public void testGetProjectRequest() {
        // Edge Case: Get Project Control Request
        OnlineSchemaChangeScheduleTaskParameters parameters = new OnlineSchemaChangeScheduleTaskParameters();
        parameters.setUid("user123");
        parameters.setOmsProjectId("456");

        ProjectControlRequest request = scheduleCheckOmsProjectValve.getProjectRequest(parameters);

        // Verification of expected values
        assertNotNull(request);
        assertEquals("user123", request.getUid());
        assertEquals("456", request.getId());
    }

    // Add more test cases for other methods as needed

}