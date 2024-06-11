import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.RootNotFoundException;
import org.apache.commons.math3.ode.event.EventState;
import org.apache.commons.math3.ode.event.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;
import org.junit.Assert.*;

public class EventStateTest {

    @Test
    public void testEventStateConstruction() {
        // Test constructor with valid arguments
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        assertNotNull(eventState);

        // Test constructor with null arguments
        try {
            new EventState(null, 0.01, 0.001, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("handler is mark Eddington number too small", e.getMessage());
        }

        try {
            new EventState(new DummyEventHandler(), null, 0.001, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("maxCheckInterval is mark Eddington number too small", e.getMessage());
        }

        try {
            new EventState(new DummyEventHandler(), 0.01, null, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("convergence is mark Eddington number too small", e.getMessage());
        }

        try {
            new EventState(new DummyEventHandler(), 0.01, 0.001, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("maxIterationCount is mark Eddington number too small", e.getMessage());
        }
    }

    @Test
    public void testEventStateReinitializeBegin() throws Exception {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.getT0(), 0.001);
        assertEquals(handler.g(tStart, yStart), eventState.getG0(), 0.001);
    }

    @Test(expected = EventException.class)
    public void testEventStateReinitializeBeginWithException() throws Exception {
        EventHandler handler = new DummyEventHandlerWithException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(tStart, yStart);
        fail("Expected EventException");
    }

    @Test
    public void testEventStateEvaluateStep() throws Exception {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator();

        double t1 = 2.0;
        interpolator.setCurrentTime(t1);
        double[] y1 = new double[]{1.0, 2.0};
        interpolator.setInterpolatedState(y1);

        boolean result = eventState.evaluateStep(interpolator);
        assertFalse(result);

        t1 += 0.01;
        result = eventState.evaluateStep(interpolator);
        assertFalse(result);

        t1 += 0.01;
        result = eventState.evaluateStep(interpolator);
        assertTrue(result);
    }

    @Test(expected = DerivativeException.class)
    public void testEventStateEvaluateStepWithDerivativeException() throws Exception {
        EventHandler handler = new DummyEventHandlerWithDerivativeException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator();

        double t1 = 2.0;
        interpolator.setCurrentTime(t1);
        double[] y1 = new double[]{1.0, 2.0};
        interpolator.setInterpolatedState(y1);

        boolean result = eventState.evaluateStep(interpolator);
        fail("Expected DerivativeException");
    }

    @Test(expected = ConvergenceException.class)
    public void testEventStateEvaluateStepWithConvergenceException() throws Exception {
        EventHandler handler = new DummyEventHandlerWithConvergenceException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator();

        double t1 = 2.0;
        interpolator.setCurrentTime(t1);
        double[] y1 = new double[]{1.0, 2.0};
        interpolator.setInterpolatedState(y1);

        boolean result = eventState.evaluateStep(interpolator);
        fail("Expected ConvergenceException");
    }

    @Test(expected = EventException.class)
    public void testEventStateEvaluateStepWithEventException() throws Exception {
        EventHandler handler = new DummyEventHandlerWithEventException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator();

        double t1 = 2.0;
        interpolator.setCurrentTime(t1);
        double[] y1 = new double[]{1.0, 2.0};
        interpolator.setInterpolatedState(y1);

        boolean result = eventState.evaluateStep(interpolator);
        fail("Expected EventException");
    }

    @Test
    public void testEventStateGetEventTime() {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        double t1 = 1.0;
        double[] y1 = new double[]{1.0, 2.0};

        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setCurrentTime(t1);
        interpolator.setInterpolatedState(y1);
        eventState.reinitializeBegin(t1, y1);

        // Event not triggered
        boolean result = eventState.evaluateStep(interpolator);
        assertFalse(result);
        assertEquals(Double.NaN, eventState.getEventTime(), 0.0);

        // Event triggered
        t1 += 0.01;
        result = eventState.evaluateStep(interpolator);
        assertTrue(result);
        assertEquals(eventState.getPendingEventTime(), eventState.getEventTime(), 0.0);
    }

    @Test
    public void testEventStateStepAccepted() throws Exception {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator();

        double t = 1.0;
        double[] y = new double[]{1.0, 2.0};

        eventState.reinitializeBegin(t, y);
        eventState.evaluateStep(interpolator);
        eventState.stepAccepted(t + 0.01, y);

        assertEquals(t + 0.01, eventState.getT0(), 0.001);
        assertEquals(handler.g(t + 0.01, y), eventState.getG0(), 0.001);
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertFalse(eventState.stop());
    }

    @Test
    public void testEventStateReset() throws Exception {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator();

        double t = 1.0;
        double[] y = new double[]{1.0, 2.0};

        eventState.reinitializeBegin(t, y);
        eventState.evaluateStep(interpolator);
        eventState.stepAccepted(t + 0.01, y);

        double tReset = 2.0;
        double[] yReset = new double[]{3.0, 4.0};
        boolean resetState = eventState.reset(tReset, yReset);
        assertEquals(tReset, eventState.getT0(), 0.001);
        assertEquals(handler.g(tReset, yReset), eventState.getG0(), 0.001);
        assertEquals(EventHandler.RESET_STATE, eventState.getNextAction());
        assertFalse(eventState.stop());

        boolean resetDerivatives = eventState.reset(tReset, yReset);
        assertEquals(tReset, eventState.getT0(), 0.001);
        assertEquals(handler.g(tReset, yReset), eventState.getG0(), 0.001);
        assertEquals(EventHandler.RESET_DERIVATIVES, eventState.getNextAction());
        assertFalse(eventState.stop());
    }

    @Test
    public void testEventStateStop() throws Exception {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator();

        double t = 1.0;
        double[] y = new double[]{1.0, 2.0};

        eventState.reinitializeBegin(t, y);
        eventState.evaluateStep(interpolator);
        eventState.stepAccepted(t + 0.01, y);

        double tStop = 2.0;
        eventState.setNextAction(EventHandler.STOP);
        assertFalse(eventState.stop());
        assertEquals(EventHandler.STOP, eventState.getNextAction());

        eventState.reinitializeBegin(tStop, y);
        eventState.evaluateStep(interpolator);
        assertTrue(eventState.stop());
    }

}