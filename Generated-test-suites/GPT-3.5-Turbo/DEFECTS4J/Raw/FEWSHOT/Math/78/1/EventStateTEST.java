package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class EventStateTest {
    private EventState eventState;

    @Before
    public void setUp() {
        EventHandler handler = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return 0;
            }

            @Override
            public int eventOccurred(double t, double[] y, boolean increasing) {
                return 0;
            }

            @Override
            public void resetState(double t, double[] y) throws EventException {
            }
        };
        eventState = new EventState(handler, 0.01, 1.0e-6, 100);
    }

    @Test
    public void testGetEventHandler() {
        assertEquals(eventState.getEventHandler().getClass(), EventHandler.class);
    }

    @Test
    public void testGetMaxCheckInterval() {
        assertEquals(eventState.getMaxCheckInterval(), 0.01, 0.0);
    }

    @Test
    public void testGetConvergence() {
        assertEquals(eventState.getConvergence(), 1.0e-6, 0.0);
    }

    @Test
    public void testGetMaxIterationCount() {
        assertEquals(eventState.getMaxIterationCount(), 100);
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        eventState.reinitializeBegin(10.0, new double[]{0.5, 0.6, 0.7});
        assertEquals(eventState.t0, 10.0, 0.0);
        assertEquals(eventState.g0, 0.0, 0.0);
    }

    @Test
    public void testEvaluateStep() {
        // TODO
    }

    @Test
    public void testGetEventTime() {
        eventState.pendingEventTime = 10.0;
        assertEquals(eventState.getEventTime(), 10.0, 0.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        eventState.stepAccepted(10.0, new double[]{0.5, 0.6, 0.7});
        assertEquals(eventState.t0, 10.0, 0.0);
        assertEquals(eventState.g0, 0.0, 0.0);
    }

    @Test
    public void testStop() {
        eventState.nextAction = EventHandler.STOP;
        assertTrue(eventState.stop());

        eventState.nextAction = EventHandler.RESET_STATE;
        assertFalse(eventState.stop());

        eventState.nextAction = EventHandler.RESET_DERIVATIVES;
        assertFalse(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        eventState.pendingEvent = true;
        eventState.pendingEventTime = 10.0;
        eventState.nextAction = EventHandler.RESET_STATE;
        double[] y = new double[]{0.5, 0.6, 0.7};
        assertTrue(eventState.reset(10.0, y));
        assertFalse(eventState.pendingEvent);
        assertTrue(eventState.pendingEventTime != eventState.pendingEventTime);
        assertTrue(eventState.pendingEventTime.isNaN());
        assertTrue(eventState.reset(10.0, y));
        assertFalse(eventState.reset(10.0, y));
    }
}