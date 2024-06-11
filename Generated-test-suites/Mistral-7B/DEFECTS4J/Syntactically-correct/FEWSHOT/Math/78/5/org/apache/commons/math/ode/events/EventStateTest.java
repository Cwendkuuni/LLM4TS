package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.sampling.StepInterpolator;
import org.apache.commons.math.ode.events.EventHandler;
import org.junit.Test;

public class EventStateTest {

    @Test
    public void testEventState() {
        EventHandler handlerMock = mock(EventHandler.class);
        EventState eventState = new EventState(handlerMock, 0.01, 0.001, 10);

        // Test initial state
        assertTrue(eventState.getEventHandler() == handlerMock);
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getEventTime());
        assertTrue(eventState.stop());
        assertFalse(eventState.pendingEvent);
        assertNull(eventState.pendingEventTime);
        assertFalse(eventState.forward);
        assertTrue(eventState.increasing);
        assertEquals(EventHandler.CONTINUE, eventState.nextAction);

        // Test reinitialization
        double tStart = 0.0;
        double[] yStart = new double[1];
        try {
            eventState.reinitializeBegin(tStart, yStart);
            assertEquals(tStart, eventState.getT0(), 0.0);
            assertEquals(Double.NaN, eventState.getG0());
            assertTrue(eventState.g0Positive);
        } catch (EventException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handlerMock = mock(EventHandler.class);
        StepInterpolator interpolatorMock = mock(StepInterpolator.class);
        when(handlerMock.g(anyDouble(), anyArray())).thenAnswer(i -> Math.sin(i.getArgument()[0]));
        when(interpolatorMock.getCurrentTime()).thenReturn(1.0);
        when(interpolatorMock.isForward()).thenReturn(true);
        EventState eventState = new EventState(handlerMock, 0.01, 0.001, 10);
        eventState.reinitializeBegin(0.0, new double[1]);

        // Test no event during the step
        StepInterpolator interpolator = new StepInterpolator() {};
        boolean result = eventState.evaluateStep(interpolator);
        assertFalse(result);
        assertEquals(Double.NaN, eventState.getEventTime());

        // Test event during the step
        when(interpolatorMock.getCurrentTime()).thenReturn(1.1);
        interpolator = new StepInterpolator() {
            @Override
            public double[] interpolate(double time, int order) throws DerivativeException {
                double[] y = new double[1];
                y[0] = Math.cos(time);
                return y;
            }
        };
        result = eventState.evaluateStep(interpolatorMock);
        assertTrue(result);
        assertEquals(1.054651333456639, eventState.getEventTime(), 0.0001);
    }
}