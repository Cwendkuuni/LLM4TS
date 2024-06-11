package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class EventStateTest {

    @Test
    public void testConstructor() {
        EventHandler mockHandler = new MockEventHandler();
        EventState eventState = new EventState(mockHandler, 0.01, 0.001, 10);
        assertEquals(mockHandler, eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler().getState());
        assertNull(eventState.getEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler mockHandler = new MockEventHandler();
        EventState eventState = new EventState(mockHandler, 0.01, 0.001, 10);

        double tStart = 1.0;
        double[] yStart = new double[] {1.0, 2.0};
        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(mockHandler.g(tStart, yStart), eventState.getG0(), 0.0);
        assertEquals(true, eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        EventHandler mockHandler = new MockEventHandlerThrowingException();
        EventState eventState = new EventState(mockHandler, 0.01, 0.001, 10);

        double tStart = 1.0;
        double[] yStart = new double[] {1.0, 2.0};
        eventState.reinitializeBegin(tStart, yStart);

        fail("Should have thrown an EventException");
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockHandler = new MockEventHandler();
        EventState eventState = new EventState(mockHandler, 0.01, 0.001, 10);
        StepInterpolator mockInterpolator = new MockStepInterpolator();

        double t1 = 2.0;
        mockInterpolator.setForward(true);
        mockInterpolator.setCurrentTime(t1);

        eventState.evaluateStep(mockInterpolator);

        assertEquals(true, eventState.isPendingEvent());
        assertEquals(t1, eventState.getPendingEventTime(), 0.0);
    }

    @Test
    public void testEvaluateStepNoEvent() throws DerivativeException, EventException, ConvergenceException {
        EventHandler mockHandler = new MockEventHandler();
        EventState eventState = new EventState(mockHandler, 0.01, 0.001, 10);
        StepInterpolator mockInterpolator = new MockStepInterpolator();

        double t1 = 2.0;
        mockInterpolator.setForward(true);
        mockInterpolator.setCurrentTime(t1);

        eventState.evaluateStep(mockInterpolator);

        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler mockHandler = new MockEventHandler();
        EventState eventState = new EventState(mockHandler, 0.01, 0.001, 10);

        double t = 3.0;
        double[] y = new double[] {3.0, 4.0};
        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(mockHandler.g(t, y), eventState.getG0(), 0.0);
        assertEquals(true, eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testStepAcceptedWithEvent() throws EventException {
        EventHandler mockHandler = new MockEventHandler();
        EventState eventState = new EventState(mockHandler, 0.01, 0.001, 10);

        double t = 3.0;
        double[] y = new double[] {3.0, 4.0};
        eventState.stepAccepted(t, y);

        eventState.pendingEvent = true;
        double pendingEventTime = 3.1;

        eventState.evaluateStep(new MockStepInterpolator());

        assertEquals(pendingEventTime, eventState.getPendingEventTime(), 0.0);
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());
        assertEquals(true, eventState.isPendingEvent());
    }

    @Test
    public void testStop() {
        EventHandler mockHandler = new MockEventHandler();
        EventState eventState = new EventState(mockHandler, 0.01, 0.001, 10);

        eventState.nextAction = EventHandler.STOP;

        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler mockHandler = new MockEventHandler();
        EventState eventState = new EventState(mockHandler, 0.01, 0.001, 10);

        eventState.pendingEvent = true;
        double t = 4.0;
        double[] y = new double[] {4.0, 5.0};
        eventState.reset(t, y);

        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
    }
}

class MockEventHandler implements EventHandler {
    private double state;

    @Override
    public double g(double t, double[] y) throws EventException {
        return state;
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean forward) throws EventException {
        state = Double.NaN;
    }

    @Override
    public void resetState(double t, double[] y) throws EventException {
        state = 0.0;
    }
}

class MockEventHandlerThrowingException implements EventHandler {
    @Override
    public double g(double t, double[] y) throws EventException {
        throw new EventException("Exception");
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean forward) throws EventException {
        throw new EventException("Exception");
    }

    @Override
    public void resetState(double t, double[] y) throws EventException {
        throw new EventException("Exception");
    }
}

class MockStepInterpolator implements StepInterpolator {

    private boolean forward;
    private double interpolatedTime;

    @Override
    public void setForward(boolean forward) {
        this.forward = forward;
    }

    @Override
    public boolean isForward() {
        return forward;
    }

    @Override
    public void setInterpolatedTime(double interpolatedTime) {
        this.interpolatedTime = interpolatedTime;
    }

    @Override
    public double getInterpolatedTime() {
        return interpolatedTime;
    }

    @Override
    public double[] getInterpolatedState() {
        throw new UnsupportedOperationException();
    }
}