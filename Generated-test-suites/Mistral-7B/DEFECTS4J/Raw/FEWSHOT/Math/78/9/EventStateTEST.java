package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.MockEventHandler;

public class EventStateTest {

    @Test
    public void testEventStateConstruction() {
        MockEventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 1e-12, 100);
        assertEquals(handler, eventState.getEventHandler());
        assertEquals(0.001, eventState.getMaxCheckInterval(), 1e-12);
        assertEquals(1e-12, eventState.getConvergence(), 1e-12);
        assertEquals(100, eventState.getMaxIterationCount());
        assertEquals(Double.NaN, eventState.getT0());
        assertEquals(Double.NaN, eventState.getG0());
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.hasPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime());
        assertEquals(Double.NaN, eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        MockEventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 1e-12, 100);
        double tStart = 1.0;
        double[] yStart = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, eventState.getT0(), 1e-12);
        assertEquals(handler.g(tStart, yStart), eventState.getG0(), 1e-12);
        assertTrue(eventState.isG0Positive());
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        MockEventHandler handler = new MockEventHandler();
        StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getInterpolatedTime() {
                return 1.0;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[]{1.0, 2.0};
            }

            @Override
            public boolean isForward() {
                return true;
            }
        };
        EventState eventState = new EventState(handler, 0.001, 1e-12, 100);
        eventState.reinitializeBegin(0.0, new double[]{0.0, 0.0});
        assertTrue(eventState.evaluateStep(interpolator));
        assertTrue(eventState.hasPendingEvent());
        assertTrue(eventState.isIncreasing());
        assertEquals(1.0, eventState.getPendingEventTime(), 1e-12);
    }

    @Test
    public void testStepAccepted() throws EventException {
        MockEventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 1e-12, 100);
        double t = 1.0;
        double[] y = new double[]{1.0, 2.0};
        eventState.stepAccepted(t, y);
        assertEquals(t, eventState.getT0(), 1e-12);
        assertEquals(handler.g(t, y), eventState.getG0(), 1e-12);
        if (eventState.hasPendingEvent()) {
            assertTrue(eventState.isG0Positive() ^ handler.isEventOccurred(t, y, false));
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        } else {
            assertTrue(eventState.isG0Positive());
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        }
    }

    @Test
    public void testStop() {
        MockEventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 1e-12, 100);
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        MockEventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 1e-12, 100);
        double t = 1.0;
        double[] y = new double[]{1.0, 2.0};
        eventState.setNextAction(EventHandler.RESET_STATE);
        assertTrue(eventState.reset(t, y));
        assertEquals(t, eventState.getT0(), 1e-12);
        assertArrayEquals(new double[]{0.0, 0.0}, eventState.getEventHandler().getState(t), 1e-12);
        assertFalse(eventState.hasPendingEvent());
    }

}