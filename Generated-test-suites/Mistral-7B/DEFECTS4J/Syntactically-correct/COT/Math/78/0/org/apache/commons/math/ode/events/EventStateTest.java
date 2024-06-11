package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.function.SingularFunctionException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.TestEventHandler;
import org.apache.commons.math3.ode.interpolators.StepInterpolator;
import org.apache.commons.math3.solvers.BrentSolver;

/**
 * Test class for {@link EventState}.
 */
public class EventStateTest {

    /** Test event handler. */
    private static final EventHandler handler = new TestEventHandler();

    /** Test event state. */
    private EventState state;

    /** Test constructor. */
    @org.junit.Before
    public void setUp() {
        state = new EventState(handler, 0.01, 1.0e-12, 100);
    }

    /** Test getEventHandler(). */
    @org.junit.Test
    public void testGetEventHandler() {
        assertEquals(handler, state.getEventHandler());
    }

    /** Test getMaxCheckInterval(). */
    @org.junit.Test
    public void testGetMaxCheckInterval() {
        assertEquals(0.01, state.getMaxCheckInterval(), 1e-14);
    }

    /** Test getConvergence(). */
    @org.junit.Test
    public void testGetConvergence() {
        assertEquals(1.0e-12, state.getConvergence(), 1e-14);
    }

    /** Test getMaxIterationCount(). */
    @org.junit.Test
    public void testGetMaxIterationCount() {
        assertEquals(100, state.getMaxIterationCount());
    }

    /** Test reinitializeBegin(). */
    @org.junit.Test
    public void testReinitializeBegin() throws Exception {
        double[] y = new double[3];
        state.reinitializeBegin(0.0, y);
        assertEquals(0.0, state.getT0(), 1e-14);
        assertEquals(Double.NaN, state.getG0(), 1e-14);
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertNull(state.getPreviousEventTime());
        assertFalse(state.isForward());
        assertFalse(state.isIncreasing());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

    /** Test evaluateStep(). */
    @org.junit.Test
    public void testEvaluateStep() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolatedTime(0.1);
        double[] y = new double[3];

        // Test no event.
        state.setT0(0.0);
        state.setG0(1.0);
        state.setState(interpolator, y);
        assertFalse(state.evaluateStep(interpolator));

        // Test event occurrence.
        state.setT0(0.0);
        state.setG0(-1.0);
        state.setState(interpolator, y);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(-0.5, state.getPendingEventTime(), 1e-14);

        // Test event occurrence with previous event.
        state.setT0(0.0);
        state.setG0(-1.0);
        state.setPendingEvent(true);
        state.setPendingEventTime(0.5);
        state.setState(interpolator, y);
        assertEquals(0.5, state.getPendingEventTime(), 1e-14);

        // Test event occurrence with previous event close.
        state.setT0(0.0);
        state.setG0(-1.0);
        state.setPendingEvent(true);
        state.setPendingEventTime(0.499999);
        state.setState(interpolator, y);
        assertEquals(0.5, state.getPendingEventTime(), 1e-14);

        // Test event occurrence with previous event far.
        state.setT0(0.0);
        state.setG0(-1.0);
        state.setPendingEvent(true);
        state.setPendingEventTime(0.999999);
        state.setState(interpolator, y);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());

        // Test convergence exception.
        state.setT0(0.0);
        state.setG0(0.0);
        interpolator.setConvergenceFlag(true);
        state.setState(interpolator, y);
        assertTrue(state.isPendingEvent());
        assertEquals(Double.NaN, state.getPendingEventTime(), 1e-14);

        // Test maximum iteration count exception.
        state.setT0(0.0);
        state.setG0(0.0);
        interpolator.setMaxIterations(1);
        state.setState(interpolator, y);
        assertTrue(state.isPendingEvent());
        assertEquals(Double.NaN, state.getPendingEventTime(), 1e-14);

        // Test bracketing exception.
        state.setT0(0.0);
        state.setG0(0.0);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double t) throws FunctionEvaluationException {
                throw new NoBracketingException();
            }
        };
        interpolator.setInterpolatedFunction(f);
        state.setState(interpolator, y);
        assertTrue(state.isPendingEvent());
        assertEquals(Double.NaN, state.getPendingEventTime(), 1e-14);

        // Test singularity exception.
        state.setT0(0.0);
        state.setG0(0.0);
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double t) throws FunctionEvaluationException {
                throw new SingularFunctionException();
            }
        };
        interpolator.setInterpolatedFunction(f2);
        state.setState(interpolator, y);
        assertTrue(state.isPendingEvent());
        assertEquals(Double.NaN, state.getPendingEventTime(), 1e-14);

        // Test derivative exception.
        state.setT0(0.0);
        state.setG0(0.0);
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            @Override
            public double value(double t) throws FunctionEvaluationException {
                throw new DerivativeException(new MathRuntimeException("Test"));
            }
        };
        interpolator.setInterpolatedFunction(f3);
        state.setState(interpolator, y);
        assertTrue(state.isPendingEvent());
        assertEquals(Double.NaN, state.getPendingEventTime(), 1e-14);

        // Test event exception.
        state.setT0(0.0);
        state.setG0(0.0);
        UnivariateRealFunction f4 = new UnivariateRealFunction() {
            @Override
            public double value(double t) throws FunctionEvaluationException {
                throw new EventException(new MathRuntimeException("Test"));
            }
        };
        interpolator.setInterpolatedFunction(f4);
        state.setState(interpolator, y);
        assertTrue(state.isPendingEvent());
        assertEquals(Double.NaN, state.getPendingEventTime(), 1e-14);

        // Test derivative function evaluation exception.
        state.setT0(0.0);
        state.setG0(0.0);
        UnivariateRealFunction f5 = new UnivariateRealFunction() {
            @Override
            public double value(double t) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(new MathRuntimeException("Test"));
            }
        };
        interpolator.setInterpolatedFunction(f5);
        state.setState(interpolator, y);
        assertTrue(state.isPendingEvent());
        assertEquals(Double.NaN, state.getPendingEventTime(), 1e-14);

        // Test convergence with convergence flag.
        state.setT0(0.0);
        state.setG0(0.0);
        interpolator.setConvergenceFlag(true);
        state.setState(interpolator, y);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());

        // Test convergence with solver.
        state.setT0(0.0);
        state.setG0(0.0);
        BrentSolver solver = new BrentSolver();
        solver.setAbsoluteAccuracy(1.0e-13);
        solver.setMaximalIterationCount(101);
        UnivariateRealFunction f6 = new UnivariateRealFunction() {
            @Override
            public double value(double t) throws FunctionEvaluationException {
                return Math.sin(t) - t;
            }
        };
        double root = solver.solve(f6, 0.0, 1.0);
        state.setPendingEvent(true);
        state.setPendingEventTime(root);
        state.setState(interpolator, y);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());

        // Test convergence with solver and convergence flag.
        state.setT0(0.0);
        state.setG0(0.0);
        interpolator.setConvergenceFlag(true);
        state.setState(interpolator, y);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());

        // Test maximum iteration count with solver.
        state.setT0(0.0);
        state.setG0(0.0);
        state.setState(interpolator, y);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());

        // Test maximum iteration count with solver and convergence flag.
        state.setT0(0.0);
        state.setG0(0.0);
        interpolator.setMaxIterations(1);
        state.setState(interpolator, y);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());

    }

    /** Test getEventTime(). */
    @org.junit.Test
    public void testGetEventTime() {
        assertNull(state.getEventTime());
    }

    /** Test stepAccepted(). */
    @org.junit.Test
    public void testStepAccepted() throws Exception {
        double[] y = new double[3];
        state.setT0(0.1);
        state.setState(new StepInterpolator(), y);
        state.stepAccepted(0.2, y);
        assertEquals(0.2, state.getT0(), 1e-14);
        assertEquals(-0.1, state.getG0(), 1e-14);
        assertTrue(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertNotNull(state.getPreviousEventTime());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());

        state.setPendingEvent(true);
        state.setPendingEventTime(0.2);
        state.setState(new StepInterpolator(), y);
        state.stepAccepted(0.2, y);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(EventHandler.EVENT_OCCURRED, state.getNextAction());

        state.setState(new StepInterpolator(), y);
        state.stepAccepted(0.2, y);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(EventHandler.RESET_STATE, state.getNextAction());

        state.setState(new StepInterpolator(), y);
        state.stepAccepted(0.2, y);
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertEquals(EventHandler.RESET_DERIVATIVES, state.getNextAction());

    }

    /** Test stop(). */
    @org.junit.Test
    public void testStop() {
        assertFalse(state.stop());
    }

    /** Test reset(). */
    @org.junit.Test
    public void testReset() throws Exception {
        state.setT0(0.1);
        state.setState(new StepInterpolator(), new double[3]);
        state.reset(0.2, new double[3]);
        assertEquals(0.2, state.getT0(), 1e-14);
        assertEquals(Double.NaN, state.getG0(), 1e-14);
        assertFalse(state.isG0Positive());
        assertFalse(state.isPendingEvent());
        assertNull(state.getPendingEventTime());
        assertNull(state.getPreviousEventTime());
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
    }

}