package org.apache.commons.math.ode.events;

import org.junit.Test;

import static org.junit.Assert.*;

public class EventStateTest {

    @Test
    public void testGetEventHandler() {
        EventState eventState = new EventState(null, 0, 0, 0);
        assertNull(eventState.getEventHandler());
    }

    @Test
    public void testGetMaxCheckInterval() {
        EventState eventState = new EventState(null, 1.0, 0, 0);
        assertEquals(1.0, eventState.getMaxCheckInterval(), 0);
    }

    @Test
    public void testGetConvergence() {
        EventState eventState = new EventState(null, 0, 2.0, 0);
        assertEquals(2.0, eventState.getConvergence(), 0);
    }

    @Test
    public void testGetMaxIterationCount() {
        EventState eventState = new EventState(null, 0, 0, 10);
        assertEquals(10, eventState.getMaxIterationCount());
    }

    @Test
    public void testReinitializeBegin() {
        EventState eventState = new EventState(null, 0, 0, 0);
        eventState.reinitializeBegin(1.0, new double[]{2.0, 3.0});
    }

    @Test
    public void testEvaluateStep() {
        EventState eventState = new EventState(null, 0, 0, 0);
        try {
            assertFalse(eventState.evaluateStep(null));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetEventTime() {
        EventState eventState = new EventState(null, 0, 0, 0);
        assertEquals(0, eventState.getEventTime(), 0);
    }

    @Test
    public void testStepAccepted() {
        EventState eventState = new EventState(null, 0, 0, 0);
        try {
            eventState.stepAccepted(1.0, new double[]{2.0, 3.0});
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testStop() {
        EventState eventState = new EventState(null, 0, 0, 0);
        assertFalse(eventState.stop());
    }

    @Test
    public void testReset() {
        EventState eventState = new EventState(null, 0, 0, 0);
        try {
            assertFalse(eventState.reset(1.0, new double[]{2.0, 3.0}));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}