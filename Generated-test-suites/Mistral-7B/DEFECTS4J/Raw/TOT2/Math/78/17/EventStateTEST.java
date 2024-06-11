package org.apache.commons.math.ode.events;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for {@link EventState} class
 */
public class EventStateTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.001, 0.001, 10);
        assertEquals(handler, state.getEventHandler());
        assertEquals(0.001, state.getMaxCheckInterval(), 1e-8);
        assertEquals(0.001, state.getConvergence(), 1e-8);
        assertEquals(10, state.getMaxIterationCount());
        assertTrue(Double.isNaN(state.getEventTime()));
        assertFalse(state.stop());
        assertFalse(state.reset(0.0, new double[0]));
    }

    /**
     * Test reinitializeBegin method
     * @throws Exception
     */
    @Test
    public void testReinitializeBegin() throws Exception {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.001, 0.001, 10);

        state.reinitializeBegin(0.0, new double[0]);
        assertEquals(0.0, state.getT0(), 1e-8);
        assertEquals(Double.NaN, state.getG0(), 1e-8);
        assertTrue(state.isG0Positive());

        state.reinitializeBegin(1.0, new double[0]);
        assertEquals(1.0, state.getT0(), 1e-8);
        assertEquals(Double.NaN, state.getG0(), 1e-8);
        assertTrue(state.isG0Positive());

        state.reinitializeBegin(0.0, new double[] { 0.1 });
        assertEquals(0.0, state.getT0(), 1e-8);
        assertEquals(Double.NaN, state.getG0(), 1e-8);
        assertTrue(state.isG0Positive());

        state.reinitializeBegin(1.0, new double[] { 0.1 });
        assertEquals(1.0, state.getT0(), 1e-8);
        assertEquals(Double.NaN, state.getG0(), 1e-8);
        assertTrue(state.isG0Positive());
    }

    /**
     * Test evaluateStep method
     * @throws Exception
     */
    @Test
    public void testEvaluateStep() throws Exception {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.001, 0.001, 10);
        final StepInterpolator stepInterpolator = new StepInterpolator();

        state.reinitializeBegin(0.0, new double[0]);

        // event occurs at the beginning of the step
        stepInterpolator.addStep(0.0, 0.01, 0.01);
        state.evaluateStep(stepInterpolator);
        assertTrue(state.isPendingEvent());
        assertEquals(0.01, state.getEventTime(), 1e-8);

        // event occurs in the middle of the step
        stepInterpolator.clear();
        stepInterpolator.addStep(0.0, 0.01, 0.005);
        stepInterpolator.addStep(0.005, 0.02, 0.01);
        state.evaluateStep(stepInterpolator);
        assertTrue(state.isPendingEvent());
        assertEquals(0.0055, state.getEventTime(), 1e-8);

        // event occurs at the end of the step
        stepInterpolator.clear();
        stepInterpolator.addStep(0.0, 0.01, 0.015);
        state.evaluateStep(stepInterpolator);
        assertFalse(state.isPendingEvent());
        assertEquals(Double.NaN, state.getEventTime());

        // event does not occur during the step
        stepInterpolator.clear();
        stepInterpolator.addStep(0.0, 0.01, 0.02);
        state.evaluateStep(stepInterpolator);
        assertFalse(state.isPendingEvent());
        assertEquals(Double.NaN, state.getEventTime());

        // check exceptions
        stepInterpolator.clear();
        stepInterpolator.addStep(0.0, 0.01, 0.01);
        try {
            state.evaluateStep(stepInterpolator);
            fail("DerivativeException expected");
        } catch (DerivativeException e) {
            // expected
        }

        stepInterpolator.clear();
        stepInterpolator.addStep(0.0, 0.01, 0.01);
        try {
            state.evaluateStep(stepInterpolator);
            fail("EventException expected");
        } catch (EventException e) {
            // expected
        }

        stepInterpolator.clear();
        stepInterpolator.addStep(0.0, 0.01, 0.01);
        try {
            state.evaluateStep(null);
            fail("ConvergenceException expected");
        } catch (ConvergenceException e) {
            // expected
        }

    }

    /**
     * Test stepAccepted method
     * @throws Exception
     */
    @Test
    public void testStepAccepted() throws Exception {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.001, 0.001, 10);

        state.reinitializeBegin(0.0, new double[0]);

        final StepInterpolator stepInterpolator = new StepInterpolator();
        stepInterpolator.addStep(0.0, 0.01, 0.01);
        state.evaluateStep(stepInterpolator);
        assertTrue(state.isPendingEvent());
        assertEquals(0.01, state.getEventTime(), 1e-8);

        state.stepAccepted(0.01, new double[0]);
        assertFalse(state.isPendingEvent());
        assertEquals(Double.NaN, state.getEventTime());

        // check exceptions
        state.stepAccepted(0.01, new double[0]);
        try {
            state.stepAccepted(0.01, null);
            fail("EventException expected");
        } catch (EventException e) {
            // expected
        }

    }

    /**
     * Test reset method
     * @throws Exception
     */
    @Test
    public void testReset() throws Exception {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.001, 0.001, 10);

        state.reinitializeBegin(0.0, new double[0]);

        final StepInterpolator stepInterpolator = new StepInterpolator();
        stepInterpolator.addStep(0.0, 0.01, 0.01);
        state.evaluateStep(stepInterpolator);
        assertTrue(state.isPendingEvent());
        assertEquals(0.01, state.getEventTime(), 1e-8);

        state.reset(0.01, new double[0]);
        assertFalse(state.isPendingEvent());
        assertEquals(Double.NaN, state.getEventTime());

        // check exceptions
        state.reset(0.01, new double[0]);
        try {
            state.reset(0.01, null);
            fail("EventException expected");
        } catch (EventException e) {
            // expected
        }

    }

    /**
     * Test stop method
     */
    @Test
    public void testStop() {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.001, 0.001, 10);

        state.reinitializeBegin(0.0, new double[0]);
        state.setNextAction(EventHandler.STOP);
        assertTrue(state.stop());

        state.reinitializeBegin(0.0, new double[0]);
        state.setNextAction(EventHandler.RESET_STATE);
        assertFalse(state.stop());

        state.reinitializeBegin(0.0, new double[0]);
        state.setNextAction(EventHandler.RESET_DERIVATIVES);
        assertFalse(state.stop());

        state.reinitializeBegin(0.0, new double[0]);
        state.setNextAction(EventHandler.CONTINUE);
        assertFalse(state.stop());

    }

    /**
     * Test getEventTime method
     */
    @Test
    public void testGetEventTime() {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.001, 0.001, 10);

        state.reinitializeBegin(0.0, new double[0]);
        assertNull(state.getEventTime());

        state.setPendingEvent(true);
        state.setPendingEventTime(0.1);
        assertEquals(0.1, state.getEventTime(), 1e-8);

        state.setPendingEvent(false);
        assertEquals(Double.NaN, state.getEventTime());
    }

    /**
     * Test isPendingEvent method
     */
    @Test
    public void testIsPendingEvent() {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.001, 0.001, 10);

        assertFalse(state.isPendingEvent());

        state.setPendingEvent(true);
        assertTrue(state.isPendingEvent());

        state.setPendingEvent(false);
        assertFalse(state.isPendingEvent());

    }

    /**
     * Test isG0Positive method
     */
    @Test
    public void testIsG0Positive() {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.001, 0.001, 10);

        state.reinitializeBegin(0.0, new double[0]);
        assertTrue(state.isG0Positive());

        state.setG0(-1.0);
        assertFalse(state.isG0Positive());

        state.setG0(1.0);
        assertTrue(state.isG0Positive());

    }

    /**
     * Test getEventHandler method
     */
    @Test
    public void testGetEventHandler() {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.001, 0.001, 10);

        assertEquals(handler, state.getEventHandler());
    }

    /**
     * Test getMaxCheckInterval method
     */
    @Test
    public void testGetMaxCheckInterval() {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.001, 0.001, 10);

        assertEquals(0.001, state.getMaxCheckInterval(), 1e-8);
    }

    /**
     * Test getConvergence method
     */
    @Test
    public void testGetConvergence() {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.001, 0.001, 10);

        assertEquals(0.001, state.getConvergence(), 1e-8);
    }

    /**
     * Test getMaxIterationCount method
     */
    @Test
    public void testGetMaxIterationCount() {
        final EventHandler handler = new DummyEventHandler();
        final EventState state = new EventState(handler, 0.001, 0.001, 10);

        assertEquals(10, state.getMaxIterationCount());
    }

    /**
     * Test DummyEventHandler class
     */
    public static class DummyEventHandler implements EventHandler {

        @Override
        public double g(double t, double[] y) throws EventException {
            return Math.sin(t);
        }

        @Override
        public int eventOccurred(double t, double[] y, boolean isAuto)
                throws EventException {
            return EventHandler.RESET_STATE;
        }

        @Override
        public void resetState(double t, double[] y) {
            // no-op
        }

    }

}