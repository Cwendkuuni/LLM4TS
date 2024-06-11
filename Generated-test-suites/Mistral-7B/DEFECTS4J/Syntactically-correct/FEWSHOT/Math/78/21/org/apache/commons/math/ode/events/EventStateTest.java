package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

public class EventStateTest {

    private EventState eventState;
    private StepInterpolator interpolatorMock;

    @Before
    public void setUp() {
        interpolatorMock = mock(StepInterpolator.class);
        eventState = new EventState(mock(EventHandler.class), 0.1, 0.001, 10) {
            @Override
            public boolean evaluateStep(StepInterpolator interpolator)
                    throws DerivativeException, EventException, ConvergenceException {
                this.interpolator = interpolatorMock;
                return super.evaluateStep(interpolator);
            }
        };
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 1.0;
        double[] yStart = new double[1];
        eventState.reinitializeBegin(tStart, yStart);
        assertTrue(Double.isFinite(eventState.getT0()));
        assertTrue(Double.isFinite(eventState.getG0()));
    }

    @Test
    public void testEvaluateStep_noEvent()
            throws DerivativeException, EventException, ConvergenceException {
        when(interpolatorMock.isForward()).thenReturn(true);
        when(interpolatorMock.getCurrentTime()).thenReturn(1.2);
        eventState.evaluateStep(interpolatorMock);
        assertFalse(eventState.isPendingEvent());
    }

    @Test
    public void testEvaluateStep_eventOccurs()
            throws DerivativeException, EventException, ConvergenceException {
        when(interpolatorMock.isForward()).thenReturn(true);
        when(interpolatorMock.getCurrentTime()).thenReturn(1.2);
        when(interpolatorMock.getInterpolatedState()).thenReturn(new double[1]);
        when(interpolatorMock.getInterpolatedState()[0]).thenReturn(-0.1);
        eventState.evaluateStep(interpolatorMock);
        assertTrue(eventState.isPendingEvent());
    }

    @Test(expected = EventException.class)
    public void testEvaluateStep_eventHandlerFailedToEvaluate()
            throws DerivativeException, EventException, ConvergenceException {
        when(interpolatorMock.isForward()).thenReturn(true);
        when(interpolatorMock.getCurrentTime()).thenThrow(new EventException());
        eventState.evaluateStep(interpolatorMock);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStep_eventCannotBeLocated()
            throws DerivativeException, EventException, ConvergenceException {
        when(interpolatorMock.isForward()).thenReturn(true);
        when(interpolatorMock.getCurrentTime()).thenReturn(1.2);
        when(interpolatorMock.getInterpolatedState()).thenReturn(new double[1]);
        eventState.evaluateStep(interpolatorMock);
        when(interpolatorMock.getInterpolatedState()[0]).thenThrow(new ConvergenceException());
        eventState.evaluateStep(interpolatorMock);
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 1.5;
        double[] y = new double[1];
        eventState.stepAccepted(t, y);
        assertTrue(Double.isFinite(eventState.getT0()));
        assertTrue(Double.isFinite(eventState.getG0()));
    }

    @Test
    public void testStop() {
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset_stateReseted() throws EventException {
        double t = 1.0;
        double[] y = new double[1];
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
        assertTrue(eventState.getEventHandler().isResetStateRequested());
    }

    @Test
    public void testReset_noStateReseted() throws EventException {
        double t = 1.0;
        double[] y = new double[1];
        eventState.reset(t, y);
        assertFalse(eventState.isPendingEvent());
        assertFalse(eventState.getEventHandler().isResetStateRequested());
    }
}