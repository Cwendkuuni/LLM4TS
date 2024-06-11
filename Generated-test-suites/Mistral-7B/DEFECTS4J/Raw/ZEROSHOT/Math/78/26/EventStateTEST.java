import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventStateTest {

    @Test
    public void testEventState() throws Exception {

        // Test constructor
        EventHandler handler = new Handler();
        EventState eventState = new EventState(handler, 0.001, 0.01, 10);

        // Test getters
        assertEquals(handler, eventState.getEventHandler());
        assertEquals(0.001, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.01, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());

        // Test reinitializeBegin
        double[] yStart = {1.0, 2.0};
        eventState.reinitializeBegin(0.0, yStart);
        assertEquals(Double.NaN, eventState.getT0(), 0.0);
        assertEquals(Double.NaN, eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.hasPendingEvent());
        assertFalse(eventState.hasPendingEventTime());
        assertFalse(eventState.hasPreviousEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        // Test evaluateStep
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setFunction(new Function());
        interpolator.addStep(0.0, 0.0, 1.0);

        double t1 = 0.5;
        eventState.evaluateStep(interpolator);
        assertEquals(true, eventState.hasPendingEvent());
        assertEquals(t1, eventState.getPendingEventTime(), 0.0);
        assertEquals(EventHandler.EVENT, eventState.getNextAction());

        // Test getEventTime
        assertEquals(t1, eventState.getEventTime(), 0.0);

        // Test stepAccepted
        double[] y = {3.0, 4.0};
        eventState.stepAccepted(t1, y);
        assertEquals(t1, eventState.getT0(), 0.0);
        assertEquals(0.0, eventState.getG0(), 0.0);
        assertTrue((eventState.isG0Positive() == handler.isG0Positive(t1, y)) || (eventState.isG0Positive() == !handler.isG0Positive(t1, y)));
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        // Test stop
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());

        // Test reset
        eventState.reset(t1 + 0.01, y);
        assertFalse(eventState.hasPendingEvent());
        assertFalse(eventState.hasPendingEventTime());
        assertFalse(eventState.hasPreviousEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

    }

    private static class Handler implements EventHandler {

        boolean isG0Positive(double t, double[] y) {
            return y[0] > 0.0;
        }

        void resetState(double t, double[] y) {
            y[0] = 0.0;
        }

        int eventOccurred(double t, double[] y, boolean crossesEvent) {
            if (crossesEvent && y[0] < 0.0) {
                return EventHandler.RESET_STATE;
            }
            return EventHandler.RESET_DERIVATIVES;
        }

    }

    private static class Function implements UnivariateRealFunction {

        public double value(double t) throws FunctionEvaluationException {
            throw new DerivativeException("Function evaluation error");
        }

    }

}