import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.events.EventException;
import org.apache.commons.math.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventStateTest {

    private EventState eventState;
    private EventHandler eventHandler;

    @Before
    public void setUp() throws Exception {
        eventHandler = mock(EventHandler.class);
        eventState = new EventState(eventHandler, 0.1, 0.01, 100);
    }

    @Test
    public void testConstructor() {
        assertEquals(eventHandler, eventState.getEventHandler());
        assertEquals(0.1, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.01, eventState.getConvergence(), 0.0);
        assertEquals(100, eventState.getMaxIterationCount());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 0.0;
        double[] yStart = new double[]{1.0, 2.0, 3.0};
        when(eventHandler.g(tStart, yStart)).thenReturn(1.0);

        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(tStart, eventState.t0, 0.0);
        assertEquals(1.0, eventState.g0, 0.0);
        assertTrue(eventState.g0Positive);
    }

    @Test
    public void testEvaluateStep_NoEvent() throws EventException, DerivativeException {
        StepInterpolator interpolator = mock(StepInterpolator.class);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(0.0);
        when(interpolator.getInterpolatedState()).thenReturn(new double[]{1.0, 2.0, 3.0});

        when(eventHandler.g(any(Double.TYPE), any(double[].class))).thenReturn(-1.0);

        boolean result = eventState.evaluateStep(interpolator);

        assertFalse(result);
        assertFalse(eventState.pendingEvent);
        assertTrue(Double.isNaN(eventState.pendingEventTime));
    }

    @Test
    public void testEvaluateStep_EventOccurred() throws EventException, DerivativeException {
        StepInterpolator interpolator = mock(StepInterpolator.class);
        when(interpolator.isForward()).thenReturn(true);
        when(interpolator.getCurrentTime()).thenReturn(0.0);
        when(interpolator.getInterpolatedState()).thenReturn(new double[]{1.0, 2.0, 3.0});

        when(eventHandler.g(any(Double.TYPE), any(double[].class))).thenReturn(-1.0, 1.0);

        boolean result = eventState.evaluateStep(interpolator);

        assertTrue(result);
        assertTrue(eventState.pendingEvent);
        assertTrue(Double.isNaN(eventState.pendingEventTime));
    }

    @Test
    public void testStepAccepted_NoEvent() throws EventException {
        double t = 1.0;
        double[] y = new double[]{2.0, 3.0, 4.0};
        when(eventHandler.g(t, y)).thenReturn(-1.0);

        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.t0, 0.0);
        assertEquals(-1.0, eventState.g0, 0.0);
        assertTrue(eventState.g0Positive);
        assertEquals(EventHandler.CONTINUE, eventState.nextAction);
    }

    @Test
    public void testStepAccepted_EventOccurred() throws EventException {
        double t = 1.0;
        double[] y = new double[]{2.0, 3.0, 4.0};
        when(eventHandler.g(t, y)).thenReturn(-1.0);
        when(eventHandler.eventOccurred(t, y, true)).thenReturn(EventHandler.STOP);

        eventState.pendingEvent = true;
        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.t0, 0.0);
        assertEquals(-1.0, eventState.g0, 0.0);
        assertTrue(eventState.g0Positive);
        assertEquals(EventHandler.STOP, eventState.nextAction);
    }

    @Test
    public void testStop() {
        eventState.nextAction = EventHandler.STOP;

        assertTrue(eventState.stop());
    }

    @Test
    public void testReset_NoPendingEvent() throws EventException {
        double t = 1.0;
        double[] y = new double[]{2.0, 3.0, 4.0};

        boolean result = eventState.reset(t, y);

        assertFalse(result);
        assertFalse(eventState.pendingEvent);
        assertTrue(Double.isNaN(eventState.pendingEventTime));
    }

    @Test
    public void testReset_StateReset() throws EventException {
        double t = 1.0;
        double[] y = new double[]{2.0, 3.0, 4.0};

        eventState.pendingEvent = true;
        when(eventHandler.eventOccurred(t, y, false)).thenReturn(EventHandler.RESET_STATE);

        boolean result = eventState.reset(t, y);

        assertTrue(result);
        assertFalse(eventState.pendingEvent);
        assertTrue(Double.isNaN(eventState.pendingEventTime));
    }

    @Test
    public void testReset_DerivativesReset() throws EventException {
        double t = 1.0;
        double[] y = new double[]{2.0, 3.0, 4.0};

        eventState.pendingEvent = true;
        when(eventHandler.eventOccurred(t, y, false)).thenReturn(EventHandler.RESET_DERIVATIVES);

        boolean result = eventState.reset(t, y);

        assertTrue(result);
        assertFalse(eventState.pendingEvent);
        assertTrue(Double.isNaN(eventState.pendingEventTime));
    }
}