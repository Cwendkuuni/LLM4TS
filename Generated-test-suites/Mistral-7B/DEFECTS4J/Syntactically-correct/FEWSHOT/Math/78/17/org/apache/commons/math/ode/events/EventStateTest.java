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

    private EventHandler mockEventHandler;
    private StepInterpolator mockInterpolator;

    @Before
    public void setup() {
        mockEventHandler = mock(EventHandler.class);
        mockInterpolator = mock(StepInterpolator.class);
    }

    @Test
    public void testInitialState() {
        EventState eventState = new EventState(mockEventHandler, 1.0, 0.001, 10);
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
    }

    @Test
    public void testEventDuringStep() {
        EventState eventState = new EventState(mockEventHandler, 1.0, 0.001, 10);

        when(mockEventHandler.g(0.0, new double[0])).thenReturn(1.0);
        when(mockEventHandler.g(1.0, new double[0])).thenReturn(-0.1);

        try {
            eventState.reinitializeBegin(0.0, new double[0]);
            assertTrue(eventState.evaluateStep(mockInterpolator));
            assertTrue(eventState.isPendingEvent());
            assertEquals(0.5, eventState.getPendingEventTime(), 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testEventNotDuringStep() {
        EventState eventState = new EventState(mockEventHandler, 1.0, 0.001, 10);

        when(mockEventHandler.g(0.0, new double[0])).thenReturn(1.0);
        when(mockEventHandler.g(1.0, new double[0])).thenReturn(1.1);

        try {
            eventState.reinitializeBegin(0.0, new double[0]);
            assertFalse(eventState.evaluateStep(mockInterpolator));
            assertFalse(eventState.isPendingEvent());
            assertNull(eventState.getPendingEventTime());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        EventState eventState = new EventState(mockEventHandler, 1.0, 0.001, 10);

        when(mockEventHandler.g(0.0, new double[0])).thenReturn(1.0);
        when(mockEventHandler.g(1.0, new double[0])).thenThrow(new ConvergenceException());

        try {
            eventState.reinitializeBegin(0.0, new double[0]);
            assertTrue(eventState.evaluateStep(mockInterpolator));
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(expected = EventException.class)
    public void testEventException() {
        EventState eventState = new EventState(mockEventHandler, 1.0, 0.001, 10);

        when(mockEventHandler.g(0.0, new double[0])).thenReturn(1.0);
        when(mockEventHandler.g(1.0, new double[0])).thenThrow(new EventException("Test"));

        try {
            eventState.reinitializeBegin(0.0, new double[0]);
            assertTrue(eventState.evaluateStep(mockInterpolator));
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void testStepAccepted() {
        EventState eventState = new EventState(mockEventHandler, 1.0, 0.001, 10);

        when(mockEventHandler.g(1.0, new double[0])).thenReturn(-0.1);
        eventState.evaluateStep(mockInterpolator);
        eventState.stepAccepted(1.5, new double[0]);

        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
    }

    @Test
    public void testStopIntegration() {
        EventState eventState = new EventState(mockEventHandler, 1.0, 0.001, 10);

        when(mockEventHandler.g(1.0, new double[0])).thenReturn(-1.0);
        eventState.evaluateStep(mockInterpolator);
        assertTrue(eventState.stop());
    }

    @Test
    public void testResetState() {
        EventState eventState = new EventState(mockEventHandler, 1.0, 0.001, 10);

        when(mockEventHandler.g(1.0, new double[0])).thenReturn(-0.1);
        eventState.evaluateStep(mockInterpolator);
        eventState.stepAccepted(1.5, new double[0]);
        eventState.reset(1.2, new double[0]);

        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertTrue(mockEventHandler.isResetStateCalled());
    }
}