package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.sampling.StepInterpolator;

public class EventStateTest {

    @Test
    public void testEventState() {
        EventHandler handlerMock = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return 0;
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean increasingDirection) {
                // no action needed for this test
            }

            @Override
            public void resetState(double t, double[] y) {
                // no action needed for this test
            }
        };

        double maxCheckInterval = 0.001;
        double convergence = 1e-6;
        int maxIterationCount = 10;

        EventState eventState = new EventState(handlerMock, maxCheckInterval, convergence, maxIterationCount);

        // test initialization
        assertNull(eventState.getEventHandler());
        assertEquals(maxCheckInterval, eventState.getMaxCheckInterval(), 0);
        assertEquals(convergence, eventState.getConvergence(), 0);
        assertEquals(maxIterationCount, eventState.getMaxIterationCount());
        assertNull(eventState.getEventHandler());
        assertFalse(eventState.isPendingEvent());
        assertNull(eventState.getPendingEventTime());
        assertNull(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        // test reinitialization
        double tStart = 0.0;
        double[] yStart = new double[1];
        try {
            eventState.reinitializeBegin(tStart, yStart);
            assertEquals(tStart, eventState.getT0(), 0);
            assertEquals(0.0, eventState.getG0(), 0);
            assertTrue(eventState.isG0Positive());
        } catch (EventException e) {
            fail("Exception should not be thrown when reinitializing");
        }

        // test evaluateStep
        StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public boolean isForward() {
                return true;
            }

            @Override
            public double getCurrentTime() {
                return 0.005;
            }
        };

        try {
            eventState.evaluateStep(interpolator);
            assertFalse(eventState.isPendingEvent());
            assertNull(eventState.getPendingEventTime());
        } catch (ConvergenceException e) {
            fail("Unexpected exception when no event occurred");
        } catch (DerivativeException e) {
            fail("Unexpected exception when no event occurred");
        } catch (EventException e) {
            fail("Unexpected exception when no event occurred");
        }

        interpolator.setCurrentTime(0.006);

        try {
            eventState.evaluateStep(interpolator);
            assertTrue(eventState.isPendingEvent());
            assertEquals(0.006, eventState.getPendingEventTime(), 0);
        } catch (ConvergenceException e) {
            fail("Unexpected exception when an event occurred");
        } catch (DerivativeException e) {
            fail("Unexpected exception when an event occurred");
        } catch (EventException e) {
            fail("Unexpected exception when an event occurred");
        }

        // test stepAccepted
        double tEnd = 0.0061;
        double[] yEnd = new double[1];
        try {
            eventState.stepAccepted(tEnd, yEnd);
            assertEquals(tEnd, eventState.getT0(), 0);
            assertEquals(0.0, eventState.getG0(), 0);
            assertTrue(eventState.isG0Positive());
            assertFalse(eventState.isPendingEvent());
            assertNull(eventState.getPendingEventTime());
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        } catch (EventException e) {
            fail("Exception should not be thrown when accepting a step");
        }

        // test stop
        assertFalse(eventState.stop());

        // test reset
        double tReset = 0.0062;
        double[] yReset = new double[1];
        try {
            eventState.reset(tReset, yReset);
            assertFalse(eventState.isPendingEvent());
            assertNull(eventState.getPendingEventTime());
            assertFalse(eventState.isForward());
            assertFalse(eventState.isIncreasing());
            assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
        } catch (EventException e) {
            fail("Exception should not be thrown when resetting the state");
        }

    }

}