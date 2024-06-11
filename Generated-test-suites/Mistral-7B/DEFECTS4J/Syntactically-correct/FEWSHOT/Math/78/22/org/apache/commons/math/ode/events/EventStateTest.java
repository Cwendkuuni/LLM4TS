package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.MockEventHandler;

public class EventStateTest {

    @Test
    public void testEventState() {
        MockEventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);

        assertEquals(handler, eventState.getEventHandler());
        assertEquals(0.001, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler().getHandlerState());

        assertNull(eventState.getT0());
        assertNull(eventState.getG0());
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        MockEventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);

        double tStart = 1.0;
        double[] yStart = new double[] {1.0, 2.0, 3.0};

        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(handler.g(tStart, yStart), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());

        // Test that g is called correctly
        handler.assertGCalled(tStart, yStart);
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginFailure() throws EventException {
        MockEventHandler handler = new MockEventHandlerThrowingException();
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);

        double tStart = 1.0;
        double[] yStart = new double[] {1.0, 2.0, 3.0};

        eventState.reinitializeBegin(tStart, yStart);

        fail("Exception expected");
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        MockEventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator() {
            double t1 = 1.5;
            boolean forward = true;

            @Override
            public double getCurrentTime() {
                return t1;
            }

            @Override
            public boolean isForward() {
                return forward;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[] {1.1, 1.2, 1.3};
            }

            @Override
            public void setInterpolatedTime(double time) {
                t1 = time;
            }
        };

        boolean eventTriggered = eventState.evaluateStep(interpolator);

        assertFalse(eventTriggered);
        assertEquals(1.5, eventState.getT0(), 0.0);
        assertEquals(interpolator.getInterpolatedState(), eventState.getY0());
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        // Test that g is called correctly
        handler.assertGCalled(1.5, interpolator.getInterpolatedState());
    }

    @Test
    public void testEvaluateStepEventTriggered() throws DerivativeException, EventException, ConvergenceException {
        MockEventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);

        StepInterpolator interpolator = new StepInterpolator() {
            double t1 = 1.5;
            boolean forward = true;

            @Override
            public double getCurrentTime() {
                return t1;
            }

            @Override
            public boolean isForward() {
                return forward;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[] {1.2, 1.3, 1.4};
            }

            @Override
            public void setInterpolatedTime(double time) {
                t1 = time;
            }
        };

        handler.setExpectedEventTime(1.45);

        boolean eventTriggered = eventState.evaluateStep(interpolator);

        assertTrue(eventTriggered);
        assertEquals(1.5, eventState.getT0(), 0.0);
        assertEquals(interpolator.getInterpolatedState(), eventState.getY0());
        assertTrue(eventState.isG0Positive());
        assertTrue(eventState.isPendingEvent());
        assertEquals(1.45, eventState.getPendingEventTime(), 0.0);
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());

        // Test that g and eventHandler.eventOccurred are called correctly
        handler.assertGCalled(1.45, interpolator.getInterpolatedState());
        handler.assertEventOccurredCalled(1.45);
    }

    @Test
    public void testStepAccepted() throws EventException {
        MockEventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);

        double t = 1.6;
        double[] y = new double[] {1.5, 1.6, 1.7};

        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(handler.g(t, y), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(t, eventState.getPreviousEventTime(), 0.0);
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        // Test that g is called correctly
        handler.assertGCalled(t, y);
    }

    @Test
    public void testStop() {
        MockEventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);

        eventState.setNextAction(EventHandler.STOP);

        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        MockEventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);

        double t = 1.6;
        double[] y = new double[] {1.5, 1.6, 1.7};

        eventState.setNextAction(EventHandler.RESET_STATE);

        boolean reset = eventState.reset(t, y);

        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(t, eventState.getPreviousEventTime(), 0.0);

        if (reset) {
            handler.assertResetStateCalled(t, y);
        }
    }

    @Test
    public void testResetDerivatives() throws EventException {
        MockEventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.001, 0.001, 10);

        double t = 1.6;
        double[] y = new double[] {1.5, 1.6, 1.7};

        eventState.setNextAction(EventHandler.RESET_DERIVATIVES);

        boolean reset = eventState.reset(t, y);

        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertEquals(t, eventState.getPreviousEventTime(), 0.0);

        if (reset) {
            handler.assertResetDerivativesCalled(t, y);
        }
    }
}

class MockEventHandler implements EventHandler {
    private boolean eventOccurredCalled = false;
    private boolean gCalled = false;
    private boolean resetStateCalled = false;
    private boolean resetDerivativesCalled = false;
    private double expectedEventTime = Double.NaN;

    @Override
    public void eventOccurred(double time, double[] state, boolean forward) {
        eventOccurredCalled = true;
    }

    @Override
    public double g(double time, double[] state) throws EventException {
        gCalled = true;
        return 0.0;
    }

    @Override
    public void resetState(double time, double[] state) {
        resetStateCalled = true;
    }

    @Override
    public void resetDerivatives(double time, double[] state) {
        resetDerivativesCalled = true;
    }

    public void assertEventOccurredCalled(double expectedTime) {
        assertTrue(eventOccurredCalled);
        assertEquals(expectedTime, this.expectedEventTime, 0.0);
        eventOccurredCalled = false;
    }

    public void assertGCalled(double time, double[] state) {
        assertTrue(gCalled);
        gCalled = false;
    }

    public void assertResetStateCalled(double time, double[] state) {
        assertTrue(resetStateCalled);
        resetStateCalled = false;
    }

    public void assertResetDerivativesCalled(double time, double[] state) {
        assertTrue(resetDerivativesCalled);
        resetDerivativesCalled = false;
    }

    public void setExpectedEventTime(double expectedEventTime) {
        this.expectedEventTime = expectedEventTime;
    }
}

class MockEventHandlerThrowingException implements EventHandler {
    @Override
    public double g(double time, double[] state) throws EventException {
        throw new EventException("Test exception");
    }
}