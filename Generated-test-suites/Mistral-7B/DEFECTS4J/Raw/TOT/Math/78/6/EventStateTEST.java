package org.apache.commons.math.ode.events;

import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link EventState} class.
 */
public class EventStateTest {

    /** Test event handler. */
    private static class TestEventHandler implements EventHandler {

        /** Event occurrence time. */
        private double eventTime;

        /**
         * Constructor.
         * @param eventTime event occurrence time
         */
        public TestEventHandler(final double eventTime) {
            this.eventTime = eventTime;
        }

        /** {@inheritDoc} */
        public double g(final double t, final double[] y) {
            return t - eventTime;
        }

        /** {@inheritDoc} */
        public void resetState(final double t, final double[] y) {
            // no-op
        }

        /** {@inheritDoc} */
        public void eventOccurred(final double t, final double[] y, final boolean forward) {
            // no-op
        }

        /** {@inheritDoc} */
        public int getNextAction() {
            return forward ? EventHandler.RESET_DERIVIVATIVES : EventHandler.STOP;
        }

    }

    /**
     * Test basic functionality.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBasic()
        throws Exception {

        final double eventTime = 3.0;
        final double convergence = 0.001;
        final double maxCheckInterval = 0.1;
        final int maxIterationCount = 10;
        final double initialTime = 0.0;

        final StepInterpolator interpolator = new StepInterpolator();

        final EventState state = new EventState(new TestEventHandler(eventTime),
                                                maxCheckInterval,
                                                convergence,
                                                maxIterationCount);
        state.reinitializeBegin(initialTime, new double[0]);

        interpolator.setInitialStep(0.1);
        interpolator.addStep(1.0);
        interpolator.addStep(2.0);
        interpolator.addStep(3.0);
        interpolator.addStep(4.0);

        try {
            Assert.assertFalse(state.evaluateStep(interpolator));
            Assert.assertEquals(EventState.EventAction.CONTINUE, state.getNextAction());
            interpolator.nextStep();
            Assert.assertEquals(eventTime, state.getEventTime(), 0.0);
            Assert.assertEquals(EventHandler.RESET_DERIVIVATIVES, state.getNextAction());
        } catch (DerivativeException | ConvergenceException | EventException e) {
            throw new MathRuntimeException(e);
        }

    }

    /**
     * Test multiple events.
     * @throws Exception if an error occurs
     */
    @Test
    public void testMultipleEvents()
        throws Exception {

        final double convergence = 0.001;
        final double maxCheckInterval = 0.1;
        final int maxIterationCount = 10;
        final double initialTime = 0.0;

        final StepInterpolator interpolator = new StepInterpolator();

        final EventState state = new EventState(new TestEventHandler(1.0),
                                               maxCheckInterval,
                                               convergence,
                                               maxIterationCount);
        state.reinitializeBegin(initialTime, new double[0]);

        interpolator.setInitialStep(0.1);
        interpolator.addStep(1.0);
        interpolator.addStep(1.5);
        interpolator.addStep(2.5);

        try {
            Assert.assertFalse(state.evaluateStep(interpolator));
            Assert.assertEquals(EventHandler.CONTINUE, state.getNextAction());
            interpolator.nextStep();
            Assert.assertEquals(1.0, state.getEventTime(), 0.0);
            Assert.assertEquals(EventHandler.RESET_DERIVIVATIVES, state.getNextAction());

            state.reset(interpolator.getCurrentTime(), interpolator.getInterpolatedState());
            interpolator.nextStep();
            Assert.assertEquals(1.5, state.getEventTime(), 0.0);
            Assert.assertEquals(EventHandler.STOP, state.getNextAction());

        } catch (DerivativeException | ConvergenceException | EventException e) {
            throw new MathRuntimeException(e);
        }

    }

}