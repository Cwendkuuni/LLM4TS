import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

    @Test
    public void testEventStateConstructor() {
        EventHandler mockEventHandler = arg -> 0; // a mock event handler
        EventState eventState = new EventState(mockEventHandler, 0.001, 0.001, 10);
        assertEquals(mockEventHandler, eventState.getEventHandler());
        assertEquals(0.001, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getT0());
        assertNull(eventState.getG0());
        assertFalse(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler mockEventHandler = arg -> {
            throw new EventException("Mock event handler failure");
        };
        EventState eventState = new EventState(mockEventHandler, 0.001, 0.001, 10);
        double[] yStart = new double[1];
        eventState.reinitializeBegin(1.0, yStart);
        assertEquals(1.0, eventState.getT0(), 0.0);
        assertEquals(mockEventHandler.g(1.0, yStart), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        EventHandler mockEventHandler = arg -> {
            throw new EventException("Mock event handler failure");
        };
        EventState eventState = new EventState(mockEventHandler, 0.001, 0.001, 10);
        double[] yStart = new double[1];
        eventState.reinitializeBegin(1.0, yStart);
        assertFalse(eventState.isPendingEvent());
        eventState.evaluateStep(new StepInterpolator(1.0, 1.1, 1.001));
        assertTrue(eventState.isPendingEvent());
        eventState.reinitializeBegin(1.1, yStart); // should throw an exception
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockEventHandler = t -> t > 0 ? 1 : -1;
        EventState eventState = new EventState(mockEventHandler, 0.001, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator(0.0, 0.1, 0.05);
        interpolator.setForward(true);
        interpolator.setInterpolatedTime(0.05);

        // Check no event during the step
        assertFalse(eventState.evaluateStep(interpolator));
        assertEquals(0.05, eventState.getT0(), 0.0);
        assertEquals(mockEventHandler.g(0.05, interpolator.getInterpolatedState()), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());

        // Check event during the step
        interpolator.setForward(false);
        interpolator.setInterpolatedTime(0.06);
        assertTrue(eventState.evaluateStep(interpolator));
        assertEquals(0.06, eventState.getPendingEventTime(), 0.0);
        assertTrue(eventState.isPendingEvent());
        assertFalse(eventState.isG0Positive());
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockEventHandler = t -> t > 0 ? 1 : -1;
        EventState eventState = new EventState(mockEventHandler, 0.001, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator(0.0, 0.1, 0.05);
        interpolator.setForward(true);
        interpolator.setInterpolatedTime(0.05);
        UnivariateRealFunction mockFunction = t -> {
            throw new DerivativeException("Mock function failure");
        };
        BrentSolver solver = new BrentSolver();
        solver.setAbsoluteAccuracy(0.001);
        solver.setMaximalIterationCount(10);
        try {
            eventState.evaluateStep(interpolator);
        } catch (ConvergenceException e) {
            throw new AssertionError("Unexpected convergence exception");
        }
        interpolator.setFunction(mockFunction);
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockEventHandler = t -> {
            throw new EventException("Mock event handler failure");
        };
        EventState eventState = new EventState(mockEventHandler, 0.001, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator(0.0, 0.1, 0.05);
        interpolator.setForward(true);
        interpolator.setInterpolatedTime(0.05);
        try {
            eventState.evaluateStep(interpolator);
        } catch (ConvergenceException e) {
            throw new AssertionError("Unexpected convergence exception");
        }
        interpolator.setFunction(t -> {
            throw new EventException("Mock event handler failure");
        });
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockEventHandler = t -> t > 0 ? 1 : -1;
        EventState eventState = new EventState(mockEventHandler, 0.001, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator(0.0, 0.1, 0.05);
        interpolator.setForward(true);
        interpolator.setInterpolatedTime(0.05);
        UnivariateRealFunction mockFunction = t -> {
            throw new MaxIterationsExceededException("Mock function failure");
        };
        BrentSolver solver = new BrentSolver();
        solver.setAbsoluteAccuracy(0.001);
        solver.setMaximalIterationCount(10);
        try {
            eventState.evaluateStep(interpolator);
        } catch (DerivativeException e) {
            throw new AssertionError("Unexpected derivative exception");
        }
        interpolator.setFunction(mockFunction);
        eventState.evaluateStep(interpolator);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler mockEventHandler = t -> t > 0 ? 1 : -1;
        EventState eventState = new EventState(mockEventHandler, 0.001, 0.001, 10);
        double[] y = new double[1];
        double t = 0.1;
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(mockEventHandler.g(t, y), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
    }

    @Test
    public void testStop() {
        EventHandler mockEventHandler = arg -> 0;
        EventState eventState = new EventState(mockEventHandler, 0.001, 0.001, 10);
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler mockEventHandler = t -> {
            double[] y = new double[1];
            y[0] = Math.sin(t);
            return mockEventHandler.g(t, y);
        };
        EventState eventState = new EventState(mockEventHandler, 0.001, 0.001, 10);
        double t = 1.0;
        double[] y = new double[1];
        eventState.stepAccepted(t, y);
        eventState.reset(t + 0.01, y);
        assertEquals(t + 0.01, eventState.getT0(), 0.0);
        assertArrayEquals(y, eventState.getInterpolatedState(), 0.0);
        assertFalse(eventState.isPendingEvent());
    }

}