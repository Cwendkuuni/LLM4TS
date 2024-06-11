package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.ode.StepInterpolator;
import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;
import org.junit.Before;
import org.junit.Test;

public class EventStateTest {

    private EventHandler handler;
    private StepInterpolator interpolator;

    @Before
    public void setUp() {
        handler = mock(EventHandler.class);
        interpolator = mock(StepInterpolator.class);
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventState eventState = new EventState(handler, 1.0, 0.001, 10);
        double tStart = 1.0;
        double[] yStart = new double[1];
        eventState.reinitializeBegin(tStart, yStart);
        assertTrue(Double.isFinite(eventState.getEventHandler().g(eventState.t0, eventState.getInterpolatedState())));
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        EventState eventState = new EventState(mock(EventHandler.class), 1.0, 0.001, 10);
        double tStart = 1.0;
        double[] yStart = new double[1];
        eventState.reinitializeBegin(tStart, yStart);
        eventState.getEventHandler().g(eventState.t0, eventState.getInterpolatedState()); // this line causes the exception
    }

    @Test
    public void testEvaluateStepNoEvent() throws DerivativeException, EventException, ConvergenceException {
        EventState eventState = new EventState(handler, 1.0, 0.001, 10);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(1.001);
        eventState.evaluateStep(interpolator);
        assertFalse(eventState.isPendingEvent());
    }

    @Test
    public void testEvaluateStepWithEvent() throws DerivativeException, EventException, ConvergenceException {
        EventState eventState = new EventState(handler, 1.0, 0.001, 10);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(1.002);

        when(handler.g(1.0, new double[]{}))
                .thenReturn(Double.NaN)
                .thenReturn(0.0)
                .thenReturn(1.0);

        eventState.evaluateStep(interpolator);
        assertTrue(eventState.isPendingEvent());
        assertEquals(1.001666666666667, eventState.getPendingEventTime(), 0.001);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventState eventState = new EventState(handler, 1.0, 0.001, 10);
        double t = 1.0;
        double[] y = new double[1];
        eventState.stepAccepted(t, y);
        assertTrue(Double.isFinite(eventState.getEventHandler().g(eventState.t0, eventState.getInterpolatedState())));
    }

    @Test(expected = EventException.class)
    public void testStepAcceptedWithException() throws EventException {
        EventState eventState = new EventState(mock(EventHandler.class), 1.0, 0.001, 10);
        double t = 1.0;
        double[] y = new double[1];
        eventState.stepAccepted(t, y);
        eventState.getEventHandler().g(eventState.t0, eventState.getInterpolatedState()); // this line causes the exception
    }

    @Test
    public void testStop() {
        EventState eventState = new EventState(handler, 1.0, 0.001, 10);
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() {
        EventState eventState = new EventState(handler, 1.0, 0.001, 10);
        double t = 1.0;
        double[] y = new double[1];
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
    }

    @Test
    public void testResetWithException() throws EventException {
        EventState eventState = new EventState(mock(EventHandler.class), 1.0, 0.001, 10);
        double t = 1.0;
        double[] y = new double[1];
        eventState.reset(t, y);
        eventState.getEventHandler().resetState(t, y); // this line causes the exception
    }

    private boolean isPendingEvent() {
        return ((EventState) this.getClass().getDeclaredField("pendingEvent").get(this)).pendingEvent;
    }

}