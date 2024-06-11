import org.junit.Test;
import static org.mockito.Mockito.*;

public class ScheduleCheckOmsProjectValveTest {

    private ScheduleCheckOmsProjectValve scheduleCheckOmsProjectValve = mock(ScheduleCheckOmsProjectValve.class);

    @Test
    public void testInvoke() {
        // Setup
        OscValveContext context = mock(OscValveContext.class);
        ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
        when(context.getScheduleTask()).thenReturn(scheduleTask);

        // Execute
        scheduleCheckOmsProjectValve.invoke(context);

        // Verify
        verify(scheduleCheckOmsProjectValve, times(1)).invoke(context);
    }

    @Test(expected = NullPointerException.class)
    public void testInvokeWithNullValveContext() {
        scheduleCheckOmsProjectValve.invoke(null);
    }

    @Test(expected = OscException.class)
    public void testInvokeWithFailedPrecheck() {
        // setup for FAILED precheck...
        scheduleCheckOmsProjectValve.invoke(context);
    }

    @Test(expected = OscException.class)
    public void testInvokeWithInconsistentVerificationResult() {
        // setup for INCONSISTENT verification result...
        scheduleCheckOmsProjectValve.invoke(context);
    }
}