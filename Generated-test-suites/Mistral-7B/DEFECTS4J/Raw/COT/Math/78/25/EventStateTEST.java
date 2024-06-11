import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;
import org.junit.Test;
import org.junit.Assert.*;

public class EventStateTest {

    @Test
    public void testConstructor() {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        Assert.assertEquals(handler, eventState.getEventHandler());
        Assert.assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        Assert.assertEquals(0.001, eventState.getConvergence(), 0.0);
        Assert.assertEquals(10, eventState.getMaxIterationCount());
        Assert.assertEquals(Double.NaN, eventState.getT0());
        Assert.assertEquals(Double.NaN, eventState.getG0());
        Assert.assertTrue(eventState.isG0Positive());
        Assert.assertFalse(eventState.hasPendingEvent());
        Assert.assertEquals(Double.NaN, eventState.getPendingEventTime());
        Assert.assertEquals(Double.NaN, eventState.getPreviousEventTime());
        Assert.assertTrue(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws Exception {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        double[] state = new double[3];
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0, 3.0};

        eventState.reinitializeBegin(tStart, yStart);

        Assert.assertEquals(tStart, eventState.getT0(), 0.0);
        Assert.assertEquals(handler.g(tStart, yStart), eventState.getG0(), 0.0);
        Assert.assertTrue(eventState.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws Exception {
        EventHandler handler = new MockEventHandlerThrowingException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        double[] state = new double[3];
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0, 3.0};

        eventState.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStep() throws DeriveiveException, EventException, ConvergenceException {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 0.1;
            }

            @Override
            public boolean isForward() {
                return true;
            }

            @Override
            public void setInterpolatedTime(double time) {
                eventState.setInterpolatedTime(time);
            }

            @Override
            public double[] getInterpolatedState() {
                return eventState.getInterpolatedState();
            }
        };

        eventState.evaluateStep(interpolator);
        Assert.assertFalse(eventState.hasPendingEvent());
    }

    @Test
    public void testEvaluateStepWithEvent() throws DeriveiveException, EventException, ConvergenceException {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 0.2;
            }

            @Override
            public boolean isForward() {
                return true;
            }

            @Override
            public void setInterpolatedTime(double time) {
                eventState.setInterpolatedTime(time);
            }

            @Override
            public double[] getInterpolatedState() {
                return eventState.getInterpolatedState();
            }
        };

        eventState.evaluateStep(interpolator);
        Assert.assertTrue(eventState.hasPendingEvent());
        Assert.assertEquals(0.2, eventState.getPendingEventTime(), 0.0);
    }

    @Test
    public void testEvaluateStepWithEventAndStepAccepted() throws DeriveiveException, EventException, ConvergenceException {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public double getCurrentTime() {
                return 0.2;
            }

            @Override
            public boolean isForward() {
                return true;
            }

            @Override
            public void setInterpolatedTime(double time) {
                eventState.setInterpolatedTime(time);
            }

            @Override
            public double[] getInterpolatedState() {
                return eventState.getInterpolatedState();
            }
        };
        double[] state = new double[3];

        eventState.evaluateStep(interpolator);
        eventState.stepAccepted(0.3, state);

        Assert.assertFalse(eventState.hasPendingEvent());
        Assert.assertEquals(0.3, eventState.getT0(), 0.0);
        Assert.assertEquals(handler.g(0.3, state), eventState.getG0(), 0.0);
    }

    @Test
    public void testStepAcceptedWithException() throws EventException {
        EventHandler handler = new MockEventHandlerThrowingException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        double[] state = new double[3];
        double t = 0.3;
        double[] y = {1.0, 2.0, 3.0};

        try {
            eventState.stepAccepted(t, y);
            fail("Expected EventException");
        } catch (EventException e) {
            // expected
        }

        Assert.assertFalse(eventState.hasPendingEvent());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler handler = new MockEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        double[] state = new double[3];
        double t = 0.3;

        eventState.setPendingEventTime(0.2);
        eventState.setNextAction(EventHandler.RESET_STATE);

        eventState.reset(t, state);

        Assert.assertFalse(eventState.hasPendingEvent());
        Assert.assertEquals(Double.NaN, eventState.getPendingEventTime());
        Assert.assertArrayEquals(new double[0], state, 0.0);
    }

    @Test
    public void testResetWithException() throws EventException {
        EventHandler handler = new MockEventHandlerThrowingException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        double[] state = new double[3];
        double t = 0.3;

        try {
            eventState.reset(t, state);
            fail("Expected EventException");
        } catch (EventException e) {
            // expected
        }

        Assert.assertFalse(eventState.hasPendingEvent());
    }

}

class MockEventHandler implements EventHandler {

    @Override
    public double g(double t, double[] y) {
        return 0.0;
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean crossing) {
        // no-op
    }

    @Override
    public void resetState(double t, double[] y) {
        // no-op
    }
}

class MockEventHandlerThrowingException implements EventHandler {

    @Override
    public double g(double t, double[] y) throws EventException {
        throw new EventException();
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean crossing) {
        // no-op
    }

    @Override
    public void resetState(double t, double[] y) {
        // no-op
    }
}