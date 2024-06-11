package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.solvers.BrentSolver;
import org.apache.commons.math.ode.StepInterpolator;
import org.apache.commons.math.ode.events.EventEventHandler;
import org.apache.commons.math.ode.events.EventState;
import org.junit.Test;

public class EventStateTest {

    @Test
    public void testEventState() {
        EventHandler handler = mock(EventEventHandler.class);
        StepInterpolator interpolator = mock(StepInterpolator.class);
        EventState eventState = new EventState(handler, 0.1, 1e-6, 10);

        // test initial state
        assertFalse(eventState.pendingEvent());
        assertNull(eventState.getEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.nextAction());

        // test reinitialization
        double tStart = 1.0;
        double[] yStart = new double[1];
        try {
            eventState.reinitializeBegin(tStart, yStart);
            assertEquals(tStart, eventState.getT0(), 1e-6);
            assertEquals(handler, eventState.getEventHandler());
            assertEquals(EventHandler.CONTINUE, eventState.nextAction());
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

        // test evaluation of step
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(1.5);
        when(handler.g(1.5, yStart)).thenReturn(0);
        when(handler.g(1.6, yStart)).thenReturn(1e-5);
        try {
            assertTrue(eventState.evaluateStep(interpolator));
            assertEquals(1.6, eventState.getEventTime(), 1e-6);
        } catch (DerivativeException | ConvergenceException | EventException e) {
            throw new RuntimeException(e);
        }

        // test step acceptance
        double tAccepted = 1.61;
        double[] yAccepted = new double[1];
        try {
            eventState.stepAccepted(tAccepted, yAccepted);
            assertEquals(tAccepted, eventState.getT0(), 1e-6);
            assertTrue(eventState.pendingEvent());
            assertEquals(EventHandler.RESET_STATE, eventState.nextAction());
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

        // test step reset
        double tReset = 1.62;
        double[] yReset = new double[1];
        try {
            eventState.reset(tReset, yReset);
            assertFalse(eventState.pendingEvent());
            assertNull(eventState.getEventTime());
            assertEquals(EventHandler.CONTINUE, eventState.nextAction());
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

        // test stop integration
        when(handler.eventOccurred(1.6, yStart, false)).thenReturn(EventHandler.STOP);
        try {
            eventState.stepAccepted(tAccepted, yAccepted);
            assertTrue(eventState.stop());
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

        // test reset state and derivatives
        when(handler.eventOccurred(1.6, yStart, false)).thenReturn(EventHandler.RESET_STATE);
        when(handler.resetState(tReset, yReset)).thenThrow(new EventException());
        try {
            eventState.reset(tReset, yReset);
            assertTrue(eventState.stop());
        } catch (EventException e) {
            // expected
        }

        when(handler.resetState(tReset, yReset)).thenReturn(null);
        try {
            eventState.reset(tReset, yReset);
            assertTrue(eventState.stop());
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

        when(handler.eventOccurred(1.6, yStart, false)).thenReturn(EventHandler.RESET_DERIVATIVES);
        try {
            eventState.reset(tReset, yReset);
            assertFalse(eventState.stop());
        } catch (EventException e) {
            throw new RuntimeException(e);
        }

        when(interpolator.isForward()).thenReturn(false);
        try {
            eventState.evaluateStep(interpolator);
            assertFalse(eventState.pendingEvent());
            assertEquals(EventHandler.CONTINUE, eventState.nextAction());
        } catch (DerivativeException | ConvergenceException | EventException e) {
            throw new RuntimeException(e);
        }

    }
}