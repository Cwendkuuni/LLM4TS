import org.apache.commons.math3.exception.DerivativeException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.ode.EventsState;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.interpolators.StepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

    @Test
    public void testEventState() throws Exception {
        double convergence = 1e-6;
        double maxIterationCount = 100;
        double maxCheckInterval = 1e-2;

        EventHandler handler = new EventHandler() {
            @Override
            public double g(double t, double[] y) throws EventException {
                if (t >= 0 && t <= 1) {
                    return Math.sin(t) - 2 * Math.sin(2 * t);
                } else {
                    throw new EventException("Event not defined for t = " + t);
                }
            }

            @Override
            public void resetState(double t, double[] y) throws EventException {
                y[0] = 0;
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean increaseEvent) throws EventException {
                nextAction = increaseEvent ? EventHandler.RESET_STATE : EventHandler.RESET_DERIVATIVES;
            }
        };

        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        // Test initialization
        assertNull(eventState.getEventHandler());
        assertEquals(maxCheckInterval, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(convergence, eventState.getConvergence(), 0.0);
        assertEquals(maxIterationCount, eventState.getMaxIterationCount());
        assertNull(eventState.getEventTime());
        assertFalse(eventState.stop());

        // Test reinitialization
        double[] initialState = new double[1];
        eventState.reinitializeBegin(0, initialState);
        assertEquals(0.0, eventState.getEventHandler().g(0, initialState), 0.0);
        assertEquals(true, eventState.g0Positive);
        assertTrue(eventState.stop());

        // Test step acceptance
        double[] finalState = new double[1];
        double[] currentState = new double[1];
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.addEvaluationPoint(0, initialState);
        interpolator.addEvaluationPoint(1, new double[]{Math.PI / 2, 0});
        interpolator.addEvaluationPoint(2, new double[]{Math.PI, 0});
        interpolator.addEvaluationPoint(3, new double[]{Math.PI + Math.PI / 2, 0});
        interpolator.interpolate(1.5, currentState);

        eventState.stepAccepted(interpolator.getCurrentTime(), currentState);
        assertEquals(Math.sin(1.5) - 2 * Math.sin(3 * 1.5), eventState.getEventHandler().g(1.5, currentState), 0.0);
        assertEquals(true, eventState.g0Positive);
        assertFalse(eventState.stop());

        // Test event detection
        eventState.evaluateStep(interpolator);
        assertTrue(eventState.pendingEvent);
        assertEquals(Precision.round(1.5213, 4), eventState.getEventTime(), 0.0);
        assertEquals(EventHandler.CONTINUE, eventState.nextAction);

        // Test event handling
        eventState.stepAccepted(eventState.getEventTime(), currentState);
        assertFalse(eventState.pendingEvent);
        assertEquals(EventHandler.RESET_STATE, eventState.nextAction);
        assertArrayEquals(new double[]{0}, currentState, 0.0);

        // Test event resetting
        eventState.reset(eventsState.getEventTime(), currentState);
        assertFalse(eventState.pendingEvent);
        assertEquals(EventHandler.RESET_STATE, eventState.nextAction);
        assertEquals(0.0, currentState[0], 0.0);

        // Test convergence exception
        StepInterpolator interpolator2 = new StepInterpolator();
        interpolator2.addEvaluationPoint(0, initialState);
        interpolator2.addEvaluationPoint(1, new double[]{Math.PI / 2, 0});
        interpolator2.addEvaluationPoint(2, new double[]{Math.PI / 2, 0});
        interpolator2.interpolate(1.5, currentState);
        eventState.evaluateStep(interpolator2);
        assertTrue(eventState.pendingEvent);
        assertEquals(Precision.round(1.5213, 4), eventState.getEventTime(), 0.0);
        try {
            eventState.stepAccepted(eventState.getEventTime(), currentState);
            fail();
        } catch (EventException e) {
            assertEquals("Event cannot be located", e.getMessage());
        }
        assertTrue(eventState.pendingEvent);
    }

}