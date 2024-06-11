package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the {@link EventState} class.
 */
public class EventStateTest {

    /** Event handler for testing. */
    private static class TestEventHandler implements EventHandler {

        /** Value for testing. */
        private double value;

        /**
         * Constructor.
         * @param value value for testing
         */
        public TestEventHandler(final double value) {
            this.value = value;
        }

        /** {@inheritDoc} */
        @Override
        public double g(final double t, final double[] y) {
            return value;
        }

        /** {@inheritDoc} */
        @Override
        public void eventOccurred(final double t, final double[] y, final boolean forward) {
            // no-op
        }

        /** {@inheritDoc} */
        @Override
        public void resetState(final double t, final double[] y) {
            // no-op
        }

        /** {@inheritDoc} */
        @Override
        public void resetDerivatives(final double t, final double[] y) {
            // no-op
        }

    }

    /** Event state for testing. */
    private EventState eventState;

    /** Interpolator for testing. */
    private StepInterpolator interpolator;

    /**
     * Set up for each test method.
     * @throws EventException if initialization fails
     */
    @Before
    public void setUp() throws EventException {
        eventState = new EventState(new TestEventHandler(1.0), 0.01, 1e-6, 10);
        interpolator = new StepInterpolator();
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(eventState);
    }

    /**
     * Test getters.
     */
    @Test
    public void testGetters() {
        assertEquals(EventHandler.class, eventState.getEventHandler().getClass());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 1e-6);
        assertEquals(1e-6, eventState.getConvergence(), 1e-6);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler().g(0.0, new double[0]));
        assertEquals(Double.NaN, eventState.getEventTime(), 1e-6);
        assertFalse(eventState.stop());
        assertEquals(EventHandler.CONTINUE, eventState.nextAction);
    }

    /**
     * Test reinitialization.
     * @throws EventException if initialization fails
     */
    @Test
    public void testReinitializeBegin() throws EventException {
        eventState.reinitializeBegin(0.0, new double[0]);
        assertEquals(0.0, eventState.getEventHandler().g(0.0, new double[0]), 1e-6);
        assertTrue(eventState.g0Positive);
    }

    /**
     * Test evaluation step with no event.
     * @throws DerivativeException if evaluation fails
     * @throws EventException if evaluation fails
     * @throws ConvergenceException if evaluation fails
     */
    @Test
    public void testEvaluateStepNoEvent()
        throws DerivativeException, EventException, ConvergenceException {
        interpolator.setCurrentState(new double[0]);
        interpolator.setInterpolatedState(new double[0]);
        interpolator.setInterpolatedTime(0.0);

        eventState.evaluateStep(interpolator);
        assertFalse(eventState.pendingEvent);
        assertNull(eventState.getEventTime());
        assertFalse(eventState.stop());
        assertEquals(EventHandler.CONTINUE, eventState.nextAction);
    }

    /**
     * Test evaluation step with event.
     * @throws DerivativeException if evaluation fails
     * @throws EventException if evaluation fails
     * @throws ConvergenceException if evaluation fails
     */
    @Test
    public void testEvaluateStepEvent()
        throws DerivativeException, EventException, ConvergenceException {
        interpolator.setCurrentState(new double[0]);
        interpolator.setInterpolatedState(new double[0]);
        interpolator.setInterpolatedTime(0.5);

        eventState.evaluateStep(interpolator);
        assertTrue(eventState.pendingEvent);
        assertEquals(0.5, eventState.getEventTime(), 1e-6);
        assertFalse(eventState.stop());
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.nextAction);
    }

}