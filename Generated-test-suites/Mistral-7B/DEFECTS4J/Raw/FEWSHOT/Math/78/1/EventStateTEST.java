package org.apache.commons.math.ode.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ode.StepInterpolator;
import org.apache.commons.math.ode.events.EventException;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.sampling.StepInterpolatorFactory;
import org.junit.Before;
import org.junit.Test;

public class EventStateTest {

    private EventHandler handler;
    private StepInterpolator interpolator;
    private EventState eventState;

    @Before
    public void setup() {
        handler = mock(EventHandler.class);
        interpolator = mock(StepInterpolator.class);
        when(interpolator.isForward()).thenReturn(true);
        eventState = new EventState(handler, 1.0, 1e-9, 100);
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double t = 1.0;
        double[] y = new double[1];
        eventState.reinitializeBegin(t, y);
        assertEquals(1.0, eventState.getT0(), 0.0);
        assertEquals(handler, eventState.getEventHandler());
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        UnivariateRealFunction testFunction = mock(UnivariateRealFunction.class);
        when(handler.g(anyDouble(), anyArray())).then(i -> testFunction.value(i.getDoubleValue()));

        StepInterpolatorFactory factory = mock(StepInterpolatorFactory.class);
        when(factory.createStepInterpolator(anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(interpolator);

        eventState.evaluateStep(factory);
        assertFalse(eventState.isPendingEvent());
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 1.0;
        double[] y = new double[1];
        eventState.stepAccepted(t, y);
        assertEquals(1.0, eventState.getT0(), 0.0);
        assertEquals(handler, eventState.getEventHandler());
        assertFalse(eventState.isPendingEvent());
    }

    @Test
    public void testGetEventTime() {
        double eventTime = 2.0;
        eventState.setPendingEventTime(eventTime);
        assertEquals(eventTime, eventState.getEventTime());
    }

    @Test
    public void testStop() {
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() {
        double t = 1.0;
        double[] y = new double[1];
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
    }
}