package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.MockEventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the {@link EventState} class.
 */
public class EventStateTest {

    private EventState state;
    private double[] y;
    private double t;
    private MockEventHandler handler;
    private StepInterpolator interpolator;

    @Before
    public void setUp() {
        handler = new MockEventHandler();
        y = new double[1];
        t = 0.0;

        state = new EventState(handler, 0.1, 1e-6, 10);
    }

    /**
     * Test construction.
     */
    @Test
    public void testConstruction() {
        // nothing to test
    }

    /**
     * Test reinitialization.
     * @throws MaxEvalException if the integrator fails to compute the
     * handler value
     * @throws EventException if the handler value cannot be evaluated
     */
    @Test
    public void testReinitialization() throws MaxEvalException, EventException {
        t = 1.0;
        state.reinitializeBegin(t, y);
        assertEquals(handler.g(t, y), state.g0, 1e-6);
        assertTrue(state.g0Positive);
    }

    /**
     * Test step evaluation.
     * @throws DerivativeException if the integrator fails to compute the
     * switching function
     * @throws EventException if the switching function cannot be evaluated
     * @throws ConvergenceException if an event cannot be located
     */
    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        // basic test: no event
        interpolator = new StepInterpolator(StepInterpolator.Type.COSINE, 0.1);
        interpolator.addStep(0.0, 0.0, 1.0, y);
        assertFalse(state.evaluateStep(interpolator));

        // test event localization
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) throws NumberFormatException {
                y[0] = Math.sin(x);
                return Math.sin(x + 0.1);
            }
        };
        double x0 = 0.0;
        interpolator = new StepInterpolator(StepInterpolator.Type.COSINE, 0.1);
        interpolator.addStep(x0, 0.0, 1.0, y);
        double x1 = x0 + 0.1;
        interpolator.addStep(x1, FunctionUtils.value(f, x0), 1.0, y);
        double x2 = x1 + 0.1;
        interpolator.addStep(x2, FunctionUtils.value(f, x1), 1.0, y);
        state.reinitializeBegin(x0, y);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(x1, state.getEventTime(), 1e-6);

        // test event occurrence during step
        interpolator = new StepInterpolator(StepInterpolator.Type.COSINE, 0.1);
        interpolator.addStep(x0, 0.0, 1.0, y);
        interpolator.addStep(x1, FunctionUtils.value(f, x0), 1.0, y);
        state.reinitializeBegin(x0, y);
        interpolator.setInterpolatedTime(x1);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(x1, state.getEventTime(), 1e-6);
        interpolator.setInterpolatedTime(x1 + 1e-6);
        assertFalse(state.evaluateStep(interpolator));

        // test event occurrence at beginning of step
        interpolator = new StepInterpolator(StepInterpolator.Type.COSINE, 0.1);
        interpolator.addStep(x0, 0.0, 1.0, y);
        state.reinitializeBegin(x0, y);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(x0, state.getEventTime(), 1e-6);

        // test event occurrence at end of step
        interpolator = new StepInterpolator(StepInterpolator.Type.COSINE, 0.1);
        interpolator.addStep(x0, 0.0, 1.0, y);
        interpolator.addStep(x1, FunctionUtils.value(f, x0), 1.0, y);
        state.reinitializeBegin(x0, y);
        interpolator.setInterpolatedTime(x1);
        interpolator.setInterpolatedState(y);
        state.stepAccepted(x1, y);
        assertTrue(state.evaluateStep(interpolator));
        assertEquals(x1, state.getEventTime(), 1e-6);

    }

    /**
     * Test step acceptance.
     * @throws EventException if the
     * event handler value cannot be evaluated
     */
    @Test
    public void testStepAcceptance() throws EventException {

        // basic test: no event
        t += 0.1;
        state.stepAccepted(t, y);
        assertEquals(handler.g(t, y), state.g0, 1e-6);
        assertTrue(state.g0Positive);

        // test event occurrence
        double[] y1 = new double[1];
        state.reinitializeBegin(t, y);
        interpolator.setInterpolatedState(y1);
        state.evaluateStep(interpolator);
        assertEquals(state.getEventTime(), t, 1e-6);
        state.stepAccepted(t, y1);
        assertEquals(handler.g(t, y), state.g0, 1e-6);
        assertTrue(state.g0Positive);

        // test event occurrence with negative derivative
        state.reinitializeBegin(t, y);
        interpolator.setInterpolatedState(y);
        state.evaluateStep(interpolator);
        assertEquals(state.getEventTime(), t, 1e-6);
        state.stepAccepted(t, y);
        assertEquals(handler.g(t, y), state.g0, 1e-6);
        assertFalse(state.g0Positive);

        // test event reset
        state.reset(t, y);
        assertEquals(Double.NaN, state.getEventTime(), 1e-6);
        assertEquals(handler.g(t, y), state.g0, 1e-6);
        assertTrue(state.g0Positive);

    }

    /**
     * Test event handler reset.
     */
    @Test
    public void testEventHandlerReset() {
        // test no event
        handler.resetState(t, y);
        state.reset(t, y);

        // test event occurrence
        handler.resetState(t, y);
        state.reinitializeBegin(t, y);
        interpolator.setInterpolatedState(y);
        state.evaluateStep(interpolator);
        assertEquals(state.getEventTime(), t, 1e-6);
        state.stepAccepted(t, y);
        assertEquals(handler.resetState(t, y), EventHandler.RESET_STATE);
        assertEquals(Double.NaN, state.getEventTime(), 1e-6);

        // test event reset with resetDerivatives option
        handler.resetState(t, y);
        state.reset(t, y);
        assertEquals(handler.resetDerivatives(t, y), EventHandler.RESET_DERIVATIVES);
        assertEquals(Double.NaN, y[0], 1e-6);

        // test event reset with resetState option
        handler.resetDerivatives(t, y);
        state.reset(t, y);
        assertEquals(handler.resetState(t, y), EventHandler.RESET_STATE);
        assertEquals(0.0, y[0], 1e-6);

        // test event reset with resetState and resetDerivatives option
        handler.resetState(t, y);
        state.reset(t, y);
        assertEquals(handler.resetState(t, y), EventHandler.RESET_STATE);
        assertEquals(0.0, y[0], 1e-6);
        assertEquals(Double.NaN, handler.derivative(t, y), 1e-6);

    }

    /**
     * Test stop request.
     */
    @Test
    public void testStopRequest() {
        handler.setStopRequest(EventState.EventHandler.STOP);
        assertTrue(state.stop());
    }

    /**
     * Test stop request with reset.
     */
    @Test
    public void testStopRequestWithReset() {
        handler.setStopRequest(EventState.EventHandler.STOP_AND_RESET);
        assertTrue(state.stop());
        assertEquals(0.0, state.getEventHandler().g(0.0, new double[0]), 1e-6);
    }

    /**
     * Test stop request with resetderivatives.
     */
    @Test
    public void testStopRequestWithResetDerivatives() {
        handler.setStopRequest(EventState.EventHandler.STOP_AND_RESET_DERIVATIVES);
        assertTrue(state.stop());
        assertEquals(Double.NaN, state.getEventHandler().derivative(0.0, new double[0]), 1e-6);
    }

    /**
     * Test stop request with resetstate.
     */
    @Test
    public void testStopRequestWithResetState() {
        handler.setStopRequest(EventState.EventHandler.STOP_AND_RESET_STATE);
        assertTrue(state.stop());
        assertEquals(0.0, state.getEventHandler().g(0.0, new double[0]), 1e-6);
    }

    /**
     * Test stop request with all options.
     */
    @Test
    public void testStopRequestWithAllOptions() {
        handler.setStopRequest(EventState.EventHandler.STOP_AND_RESET_STATE_AND_DERIVATIVES);
        assertTrue(state.stop());
        assertEquals(0.0, state.getEventHandler().g(0.0, new double[0]), 1e-6);
        assertEquals(Double.NaN, state.getEventHandler().derivative(0.0, new double[0]), 1e-6);
    }

    /**
     * Test reset with no event.
     */
    @Test
    public void testResetWithNoEvent() {
        double[] y0 = new double[1];
        state.reset(t, y0);
        assertEquals(0.0, y0[0], 1e-6);
        assertEquals(Double.NaN, state.getEventHandler().g(0.0, y0), 1e-6);
        assertEquals(Double.NaN, state.getEventHandler().derivative(0.0, y0), 1e-6);
    }

    /**
     * Test reset with event.
     */
    @Test
    public void testResetWithEvent() {
        state.reinitializeBegin(t, y);
        interpolator.setInterpolatedState(y);
        state.evaluateStep(interpolator);
        assertEquals(state.getEventTime(), t, 1e-6);
        state.stepAccepted(t, y);
        state.reset(t, y);
        assertEquals(Double.NaN, state.getEventHandler().g(0.0, y), 1e-6);
        assertEquals(Double.NaN, state.getEventHandler().derivative(0.0, y), 1e-6);
    }

}