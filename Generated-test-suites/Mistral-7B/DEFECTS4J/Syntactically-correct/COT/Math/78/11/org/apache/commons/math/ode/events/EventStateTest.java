package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.special.Erf;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link EventState}.
 */
public class EventStateTest {

    private StepInterpolator interpolator;
    private EventHandler handler;
    private EventState state;

    @Before
    public void setUp() {
        interpolator = new StepInterpolator();
        handler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return BesselFunctions.firstKind(0, t) * Erf.erf(y[0]) - Math.sin(t);
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forward) {
                // no-op
            }

            @Override
            public void resetState(double t, double[] y) {
                y[0] = 0;
            }
        };

        state = new EventState(handler, 0.01, 1e-5, 100);
    }

    /**
     * Test constructor.
     */
    @Test
    public void constructor() {
        assertEquals(EventHandler.CONTINUE, state.getNextAction());
        assertFalse(state.hasPendingEvent());
    }

    /**
     * Test {@link EventState#reinitializeBegin(double, double[])}.
     */
    @Test
    public void reinitializeBegin() {
        double[] y = new double[] { 0 };
        state.reinitializeBegin(0.0, y);
        assertEquals(0.0, state.getG0(), 1e-5);
        assertEquals(true, state.isG0Positive());
    }

    /**
     * Test {@link EventState#evaluateStep(StepInterpolator)}.
     */
    @Test
    public void evaluateStep() throws Exception {
        double[] y = new double[] { 0.5 };
        interpolator.addStep(0.1, y);
        state.reinitializeBegin(0.0, y);

        // check no event during the first step
        state.evaluateStep(interpolator);
        assertEquals(false, state.hasPendingEvent());

        // check an event during the second step
        interpolator.addStep(0.1, y);
        state.evaluateStep(interpolator);
        assertEquals(true, state.hasPendingEvent());
        assertEquals(0.2, state.getPendingEventTime(), 1e-5);
    }

    /**
     * Test {@link EventState#stepAccepted(double, double[])}.
     */
    @Test
    public void stepAccepted() throws Exception {
        double[] y = new double[] { 0.5 };
        interpolator.addStep(0.1, y);
        state.reinitializeBegin(0.0, y);

        // check no event during the first step
        state.evaluateStep(interpolator);
        state.stepAccepted(0.1, y);
        assertEquals(false, state.hasPendingEvent());

        // check an event during the second step
        interpolator.addStep(0.1, y);
        state.evaluateStep(interpolator);
        assertEquals(true, state.hasPendingEvent());
        state.stepAccepted(0.2, y);
        assertEquals(false, state.hasPendingEvent());
        assertEquals(0.2, state.getEventTime(), 1e-5);
    }

    /**
     * Test {@link EventState#stop()}.
     */
    @Test
    public void stop() {
        // no event
        assertTrue(state.stop());

        // event
        interpolator.addStep(0.1, new double[] { 0.5 });
        state.reinitializeBegin(0.0, new double[] { 0.5 });
        state.evaluateStep(interpolator);
        state.stepAccepted(0.1, new double[] { 0.5 });
        assertFalse(state.stop());
    }

    /**
     * Test {@link EventState#reset(double, double[])}.
     */
    @Test
    public void reset() throws Exception {
        double[] y = new double[] { 0.5 };
        interpolator.addStep(0.1, y);
        state.reinitializeBegin(0.0, y);

        // no event
        state.evaluateStep(interpolator);
        assertEquals(false, state.hasPendingEvent());
        assertTrue(state.reset(0.1, y));
        assertEquals(0.0, y[0], 1e-5);

        // event
        state.evaluateStep(interpolator);
        assertEquals(true, state.hasPendingEvent());
        assertEquals(0.2, state.getPendingEventTime(), 1e-5);
        assertTrue(state.reset(0.2, y));
        assertEquals(false, state.hasPendingEvent());
        assertEquals(0.0, y[0], 1e-5);
        assertEquals(false, state.reset(0.2, y));
    }

    /**
     * Test edge cases.
     */
    @Test
    public void edgeCases() throws Exception {
        double[] y = new double[] { 0.5 };
        interpolator.addStep(0.1, y);
        state.reinitializeBegin(0.0, y);

        // convergence threshold is zero
        try {
            state = new EventState(handler, 0.0, 0.0, 100);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // maximal time interval between checks is zero
        try {
            state = new EventState(handler, 0.0, 1e-5, 0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // convergence threshold is negative
        try {
            state = new EventState(handler, 0.0, -1e-5, 100);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // maximal iteration count is negative
        try {
            state = new EventState(handler, 0.0, 1e-5, -100);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // event handler is null
        try {
            state = new EventState(null, 0.01, 1e-5, 100);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        // event handler returns NaN
        handler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                throw new ArithmeticException("Divide by zero");
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forward) {
                // no-op
            }

            @Override
            public void resetState(double t, double[] y) {
                // no-op
            }
        };
        state = new EventState(handler, 0.01, 1e-5, 100);
        interpolator.addStep(0.1, y);
        state.reinitializeBegin(0.0, y);
        try {
            state.evaluateStep(interpolator);
            fail();
        } catch (FunctionEvaluationException e) {
            // expected
        }

        // event handler throws an exception
        handler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                throw new MathRuntimeException("Boom");
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forward) {
                // no-op
            }

            @Override
            public void resetState(double t, double[] y) {
                // no-op
            }
        };
        state = new EventState(handler, 0.01, 1e-5, 100);
        interpolator.addStep(0.1, y);
        state.reinitializeBegin(0.0, y);
        try {
            state.evaluateStep(interpolator);
            fail();
        } catch (EventException e) {
            // expected
        }

        // event handler throws a ConvergenceException
        handler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                throw new ConvergenceException("Could not converge");
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forward) {
                // no-op
            }

            @Override
            public void resetState(double t, double[] y) {
                // no-op
            }
        };
        state = new EventState(handler, 0.01, 1e-5, 100);
        interpolator.addStep(0.1, y);
        state.reinitializeBegin(0.0, y);
        try {
            state.evaluateStep(interpolator);
            fail();
        } catch (ConvergenceException e) {
            // expected
        }

        // event handler throws a FunctionEvaluationException
        handler = new EventHandler() {
            @Override
            public double g(double t, double[] y) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(new DerivativeException());
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean forward) {
                // no-op
            }

            @Override
            public void resetState(double t, double[] y) {
                // no-op
            }
        };
        state = new EventState(handler, 0.01, 1e-5, 100);
        interpolator.addStep(0.1, y);
        state.reinitializeBegin(0.0, y);
        try {
            state.evaluateStep(interpolator);
            fail();
        } catch (DerivativeException e) {
            // expected
        }
    }

}