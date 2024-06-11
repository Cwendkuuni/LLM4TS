import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.ode.events.EventState;

public class EventStateTest {

    @Test
    public void testReinitializeBegin() {
        EventState eventState = new EventState(null, 0, 0, 0);
        double tStart = 0;
        double[] yStart = new double[0];
        try {
            eventState.reinitializeBegin(tStart, yStart);
        } catch (EventException e) {
            fail("Unexpected EventException");
        }
    }

    @Test
    public void testEvaluateStep() {
        EventState eventState = new EventState(null, 0, 0, 0);
        try {
            boolean result = eventState.evaluateStep(null);
            assertFalse(result);
        } catch (DerivativeException e) {
            fail("Unexpected DerivativeException");
        } catch (EventException e) {
            fail("Unexpected EventException");
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException");
        }
    }

    @Test
    public void testGetEventTime() {
        EventState eventState = new EventState(null, 0, 0, 0);
        double result = eventState.getEventTime();
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testStepAccepted() {
        EventState eventState = new EventState(null, 0, 0, 0);
        double t = 0;
        double[] y = new double[0];
        try {
            eventState.stepAccepted(t, y);
        } catch (EventException e) {
            fail("Unexpected EventException");
        }
    }

    @Test
    public void testStop() {
        EventState eventState = new EventState(null, 0, 0, 0);
        boolean result = eventState.stop();
        assertFalse(result);
    }

    @Test
    public void testReset() {
        EventState eventState = new EventState(null, 0, 0, 0);
        double t = 0;
        double[] y = new double[0];
        try {
            boolean result = eventState.reset(t, y);
            assertFalse(result);
        } catch (EventException e) {
            fail("Unexpected EventException");
        }
    }

}