package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.interpolators.StepInterpolator;
import org.mockito.Mockito;

import java.util.Arrays;

public class EventStateTest {

    @Test
    public void testEventStateConstructor() {
        EventHandler mockHandler = Mockito.mock(EventHandler.class);
        EventState testee = new EventState(mockHandler, 1.0, 0.001, 100);
        assertEquals(mockHandler, testee.getEventHandler());
        assertEquals(1.0, testee.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, testee.getConvergence(), 0.0);
        assertEquals(100, testee.getMaxIterationCount());
        assertNull(testee.getEventHandler().getState());
        assertEquals(EventHandler.CONTINUE, testee.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler mockHandler = Mockito.mock(EventHandler.class);
        double[] mockState = new double[3];
        StepInterpolator mockInterpolator = Mockito.mock(StepInterpolator.class);
        EventState testee = new EventState(mockHandler, 1.0, 0.001, 100);

        Mockito.when(mockHandler.g(Mockito.anyDouble(), Mockito.any(double[][].class))).thenThrow(new EventException());
        assertThrows(EventException.class, () -> testee.reinitializeBegin(0.0, mockState));

        Mockito.when(mockHandler.g(Mockito.anyDouble(), Mockito.any(double[][].class))).thenReturn(1.0);
        double tStart = 0.0;
        double[] yStart = new double[]{1.0, 2.0, 3.0};
        testee.reinitializeBegin(tStart, yStart);
        assertEquals(tStart, testee.getT0(), 0.0);
        assertEquals(1.0, testee.getG0(), 0.0);
        assertTrue(testee.isG0Positive());
        assertFalse(testee.isPendingEvent());
        assertNull(testee.getPendingEventTime());
        assertNull(testee.getPreviousEventTime());
        assertEquals(EventHandler.CONTINUE, testee.getNextAction());
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockHandler = Mockito.mock(EventHandler.class);
        double[] mockState = new double[3];
        StepInterpolator mockInterpolator = Mockito.mock(StepInterpolator.class);
        EventState testee = new EventState(mockHandler, 1.0, 0.001, 100);

        Mockito.when(mockHandler.g(Mockito.anyDouble(), Mockito.any(double[][].class))).thenReturn(1.0);
        Mockito.when(mockInterpolator.isForward()).thenReturn(true);
        Mockito.when(mockInterpolator.getCurrentTime()).thenReturn(1.1);

        testee.reinitializeBegin(0.0, mockState);
        boolean result = testee.evaluateStep(mockInterpolator);
        assertFalse(result);

        Mockito.when(mockHandler.g(Mockito.anyDouble(), Mockito.any(double[][].class))).thenReturn(0.0);
        result = testee.evaluateStep(mockInterpolator);
        assertFalse(result);

        Mockito.when(mockHandler.g(Mockito.anyDouble(), Mockito.any(double[][].class))).thenReturn(1.0);
        result = testee.evaluateStep(mockInterpolator);
        assertTrue(result);

        Mockito.when(mockHandler.g(Mockito.anyDouble(), Mockito.any(double[][].class))).thenReturn(-1.0);
        result = testee.evaluateStep(mockInterpolator);
        assertTrue(result);
    }

    @Test
    public void testGetEventTime() {
        EventHandler mockHandler = Mockito.mock(EventHandler.class);
        EventState testee = new EventState(mockHandler, 1.0, 0.001, 100);
        assertEquals(Double.NaN, testee.getEventTime());

        testee.setPendingEventTime(1.0);
        assertEquals(1.0, testee.getEventTime());
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler mockHandler = Mockito.mock(EventHandler.class);
        double[] mockState = new double[3];
        StepInterpolator mockInterpolator = Mockito.mock(StepInterpolator.class);
        EventState testee = new EventState(mockHandler, 1.0, 0.001, 100);

        Mockito.when(mockHandler.g(Mockito.anyDouble(), Mockito.any(double[][].class))).thenReturn(1.0);
        Mockito.when(mockInterpolator.isForward()).thenReturn(true);
        Mockito.when(mockInterpolator.getInterpolatedState()).thenReturn(mockState);

        testee.reinitializeBegin(0.0, mockState);
        testee.evaluateStep(mockInterpolator);
        testee.stepAccepted(1.1, mockState);

        assertEquals(1.1, testee.getT0(), 0.0);
        assertEquals(1.0, testee.getG0(), 0.0);
        assertTrue(testee.isG0Positive());
        assertFalse(testee.isPendingEvent());
        assertNull(testee.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, testee.getNextAction());
    }

    @Test
    public void testStop() {
        EventHandler mockHandler = Mockito.mock(EventHandler.class);
        EventState testee = new EventState(mockHandler, 1.0, 0.001, 100);
        testee.setNextAction(EventHandler.STOP);
        assertTrue(testee.stop());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler mockHandler = Mockito.mock(EventHandler.class);
        double[] mockState = new double[3];
        StepInterpolator mockInterpolator = Mockito.mock(StepInterpolator.class);
        EventState testee = new EventState(mockHandler, 1.0, 0.001, 100);

        Mockito.when(mockHandler.resetState(Mockito.anyDouble(), Mockito.any(double[][].class))).thenReturn(true);

        testee.setPendingEventTime(1.0);
        testee.setNextAction(EventHandler.RESET_STATE);
        double t = 1.2;
        double[] y = new double[]{1.0, 2.0, 3.0};
        boolean result = testee.reset(t, y);
        assertTrue(result);
        assertEquals(1.2, testee.getT0(), 0.0);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, testee.getState(), 0.0);
        assertFalse(testee.isPendingEvent());
        assertNull(testee.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, testee.getNextAction());

        testee.setNextAction(EventHandler.RESET_DERIVATIVES);
        result = testee.reset(t, y);
        assertTrue(result);
        assertEquals(1.2, testee.getT0(), 0.0);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, testee.getState(), 0.0);
        assertFalse(testee.isPendingEvent());
        assertNull(testee.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, testee.getNextAction());
    }

}