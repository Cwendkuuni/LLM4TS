package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.EventHandler;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link EventState}.
 */
public class EventStateTest {

    /** Test handler. */
    private EventHandler handler;

    /** Test event state. */
    private EventState eventState;

    /** Test interpolator. */
    private StepInterpolator interpolator;

    /** Test function. */
    private UnivariateRealFunction testFunction;

    /** Set up test environment. */
    @Before
    public void setUp() {
        // Create a mock event handler.
        handler = mock(EventHandler.class);
        testFunction = mock(UnivariateRealFunction.class);

        // Create test event state.
        eventState = new EventState(handler, 0.01, 1.0e-6, 100);

        // Create test interpolator.
        interpolator = new StepInterpolator();
    }

    /** Test constructor. */
    @Test
    public void testConstructor() {
        assertNotNull(eventState);
    }

    /** Test getters. */
    @Test
    public void testGetters() {
        assertNotNull(eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 1e-7);
        assertEquals(1.0e-6, eventState.getConvergence(), 1e-7);
        assertEquals(100, eventState.getMaxIterationCount());
        assertEquals(Double.NaN, eventState.getT0(), 1e-7);
        assertEquals(Double.NaN, eventState.getG0(), 1e-7);
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-7);
        assertEquals(Double.NaN, eventState.getPreviousEventTime(), 1e-7);
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /** Test reinitializeBegin. */
    @Test
    public void testReinitializeBegin() throws EventException {
        // Set up test data.
        when(handler.g(anyDouble(), anyArray())).thenReturn(1.0);
        double tStart = 0.0;
        double[] yStart = new double[1];

        // Test method.
        eventState.reinitializeBegin(tStart, yStart);

        // Check results.
        assertEquals(tStart, eventState.getT0(), 1e-7);
        assertEquals(1.0, eventState.getG0(), 1e-7);
    }

    /** Test evaluateStep. */
    @Test(expected = DerivativeException.class)
    public void testEvaluateStep_DerivativeException()
        throws DerivativeException, EventException, ConvergenceException {

        // Set up test data.
        when(handler.g(anyDouble(), anyArray()))
            .thenThrow(new DerivativeException("test"));
        interpolator.setInterpolatedState(new double[1]);

        // Test method.
        eventState.evaluateStep(interpolator);

    }

    /** Test evaluateStep. */
    @Test(expected = EventException.class)
    public void testEvaluateStep_EventException()
        throws DerivativeException, EventException, ConvergenceException {

        // Set up test data.
        when(handler.g(anyDouble(), anyArray()))
            .thenThrow(new EventException("test"));
        interpolator.setInterpolatedState(new double[1]);

        // Test method.
        eventState.evaluateStep(interpolator);

    }

    /** Test evaluateStep. */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateStep_ConvergenceException()
        throws DerivativeException, EventException, ConvergenceException {

        // Set up test data.
        when(handler.g(anyDouble(), anyArray()))
            .thenReturn(0.0);
        interpolator.setInterpolatedState(new double[1]);
        when(testFunction.value(anyDouble()))
            .thenThrow(new ConvergenceException("test"));

        // Test method.
        eventState.evaluateStep(interpolator);

    }

    /** Test evaluateStep. */
    @Test
    public void testEvaluateStep_NoEvent()
        throws DerivativeException, EventException, ConvergenceException {

        // Set up test data.
        when(handler.g(anyDouble(), anyArray()))
            .thenReturn(1.0);
        interpolator.setInterpolatedState(new double[1]);

        // Test method.
        eventState.evaluateStep(interpolator);

        // Check results.
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-7);
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

    }

    /** Test evaluateStep. */
    @Test
    public void testEvaluateStep_Event()
        throws DerivativeException, EventException, ConvergenceException {

        // Set up test data.
        when(handler.g(anyDouble(), anyArray()))
            .thenReturn(0.0);
        interpolator.setInterpolatedState(new double[1]);
        when(testFunction.value(anyDouble()))
            .thenReturn(1.0);

        // Test method.
        eventState.evaluateStep(interpolator);

        // Check results.
        assertTrue(eventState.isPendingEvent());
        assertEquals(1.0, eventState.getPendingEventTime(), 1e-7);
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());

    }

    /** Test getEventTime. */
    @Test
    public void testGetEventTime() {
        when(handler.g(anyDouble(), anyArray()))
            .thenReturn(0.0);
        interpolator.setInterpolatedState(new double[1]);
        when(testFunction.value(anyDouble()))
            .thenReturn(1.0);

        eventState.pendingEventTime = 2.0;
        eventState.pendingEvent = true;

        assertEquals(2.0, eventState.getEventTime(), 1e-7);
    }

    /** Test stepAccepted. */
    @Test
    public void testStepAccepted() throws EventException {
        when(handler.g(anyDouble(), anyArray()))
            .thenReturn(1.0);
        double t = 1.0;
        double[] y = new double[1];

        // Test method.
        eventState.stepAccepted(t, y);

        // Check results.
        assertEquals(t, eventState.getT0(), 1e-7);
        assertEquals(1.0, eventState.getG0(), 1e-7);
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-7);
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    /** Test stop. */
    @Test
    public void testStop() {
        eventState.nextAction = EventHandler.STOP;

        assertTrue(eventState.stop());
    }

    /** Test reset. */
    @Test
    public void testReset() throws EventException {
        when(handler.resetState(anyDouble(), anyArray()))
            .thenReturn(null);

        double t = 1.0;
        double[] y = new double[1];

        // Test method.
        eventState.reset(t, y);

        // Check results.
        assertFalse(eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 1e-7);
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

    }

}