import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0, 3.0};
        EventHandler handler = new EventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 100);

        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(handler.g(tStart, yStart), eventState.getG0(), 0.0);
    }

    @Test
    public void testEvaluateStep() throws EventException, DerivativeException, ConvergenceException {
        double tStart = 0.0;
        double tEnd = 1.0;
        double[] yStart = {1.0, 2.0, 3.0};
        EventHandler handler = new EventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 100);

        eventState.reinitializeBegin(tStart, yStart);
        StepInterpolator interpolator = new StepInterpolator(tStart, yStart, tEnd, yStart, true);
        
        boolean eventOccurred = eventState.evaluateStep(interpolator);

        assertFalse(eventOccurred);
    }

    @Test
    public void testGetEventTime() throws EventException {
        EventHandler handler = new EventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 100);

        double eventTime = eventState.getEventTime();

        assertEquals(Double.NaN, eventTime, 0.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        double tEnd = 1.0;
        double[] yEnd = {2.0, 3.0, 4.0};
        EventHandler handler = new EventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 100);

        eventState.stepAccepted(tEnd, yEnd);

        assertEquals(tEnd, eventState.getT0(), 0.0);
        assertEquals(handler.g(tEnd, yEnd), eventState.getG0(), 0.0);
    }

    @Test
    public void testStop() {
        EventHandler handler = new EventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 100);

        boolean shouldStop = eventState.stop();

        assertFalse(shouldStop);
    }

    @Test
    public void testReset() throws EventException {
        double tEnd = 1.0;
        double[] yEnd = {2.0, 3.0, 4.0};
        EventHandler handler = new EventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 100);

        eventState.stepAccepted(tEnd, yEnd);
        boolean shouldReset = eventState.reset(tEnd, yEnd);

        assertFalse(shouldReset);
    }
}