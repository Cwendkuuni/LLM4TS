import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EventStateTest {

    private EventState eventState;
    private EventHandler mockEventHandler;

    @Before
    public void setUp() {
        mockEventHandler = new MockEventHandler();
        eventState = new EventState(mockEventHandler, 0.01, 0.001, 10);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(eventState);
        Assert.assertNotNull(eventState.getEventHandler());
        Assert.assertEquals(0.01, eventState.getMaxCheckInterval(), 0.001);
        Assert.assertEquals(0.001, eventState.getConvergence(), 0.001);
        Assert.assertEquals(10, eventState.getMaxIterationCount());
        Assert.assertTrue(Double.isNaN(eventState.getT0()));
        Assert.assertTrue(Double.isNaN(eventState.getG0()));
        Assert.assertTrue(eventState.isG0Positive());
        Assert.assertFalse(eventState.hasPendingEvent());
        Assert.assertTrue(Double.isNaN(eventState.getPendingEventTime()));
        Assert.assertTrue(Double.isNaN(eventState.getPreviousEventTime()));
        Assert.assertTrue(eventState.isForward());
        Assert.assertTrue(eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 0.0;
        double[] yStart = new double[]{1.0, 1.0};
        eventState.reinitializeBegin(tStart, yStart);
        Assert.assertEquals(tStart, eventState.getT0(), 0.001);
        Assert.assertEquals(mockEventHandler.g(tStart, yStart), eventState.getG0(), 0.001);
        Assert.assertTrue(eventState.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        double tStart = 0.0;
        double[] yStart = new double[]{1.0, 1.0};
        mockEventHandler.setThrowExceptionOnEvaluate(true);
        eventState.reinitializeBegin(tStart, yStart);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolator(new PolynomialInterpolator(1));
        interpolator.setForward(true);
        interpolator.setInterpolatedTime(0.0);

        double t1 = 0.1;
        eventState.evaluateStep(interpolator);
        Assert.assertFalse(eventState.hasPendingEvent());

        interpolator.setInterpolatedTime(0.2);
        eventState.evaluateStep(interpolator);
        Assert.assertTrue(eventState.hasPendingEvent());
        Assert.assertEquals(0.198, eventState.getPendingEventTime(), 0.001);
    }

    @Test
    public void testEvaluateStepWithException() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setInterpolator(new PolynomialInterpolator(1));
        interpolator.setForward(true);
        interpolator.setInterpolatedTime(0.0);

        double t1 = 0.1;
        mockEventHandler.setThrowExceptionOnEvaluateAtTime(0.1, true);
        eventState.evaluateStep(interpolator);
        Assert.assertTrue(eventState.hasPendingEvent());
        Assert.assertEquals(0.1, eventState.getPendingEventTime(), 0.001);
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 0.2;
        double[] y = new double[]{1.0, 1.0};
        eventState.stepAccepted(t, y);
        Assert.assertEquals(t, eventState.getT0(), 0.001);
        Assert.assertEquals(mockEventHandler.g(t, y), eventState.getG0(), 0.001);
        Assert.assertEquals(eventState.isG0Positive(), eventState.isIncreasing());
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testStepAcceptedWithEvent() throws EventException {
        double t = 0.2;
        double[] y = new double[]{1.0, 1.0};
        eventState.pendingEvent = true;
        eventState.pendingEventTime = 0.198;
        eventState.stepAccepted(t, y);
        Assert.assertFalse(eventState.hasPendingEvent());
        Assert.assertEquals(0.2, eventState.getPendingEventTime(), 0.001);
        Assert.assertEquals(EventHandler.EVENT_OCCURRED, eventState.getNextAction());
    }

    @Test
    public void testStop() {
        Assert.assertFalse(eventState.stop());

        eventState.nextAction = EventHandler.STOP;
        Assert.assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        double t = 0.3;
        double[] y = new double[]{1.0, 1.0};
        eventState.pendingEvent = true;
        eventState.pendingEventTime = 0.2;
        eventState.previousEventTime = 0.1;
        eventState.nextAction = EventHandler.RESET_STATE;
        eventState.reset(t, y);
        Assert.assertFalse(eventState.hasPendingEvent());
        Assert.assertEquals(0.3, eventState.getPendingEventTime(), 0.001);
        Assert.assertEquals(EventHandler.CONTINUE, eventState.getNextAction());

        eventState.nextAction = EventHandler.RESET_DERIVATIVES;
        eventState.reset(t, y);
        Assert.assertEquals(0.3, eventState.getT0(), 0.001);
        Assert.assertEquals(Double.NaN, eventState.getG0(), 0.001);
    }

}

class MockEventHandler implements EventHandler {

    private boolean throwExceptionOnEvaluate = false;
    private boolean throwExceptionOnEvaluateAtTime = false;

    @Override
    public double g(double t, double[] y) throws EventException {
        if (throwExceptionOnEvaluate) {
            throw new EventException("Mock Exception");
        }
        if (throwExceptionOnEvaluateAtTime && (Math.abs(t - 0.1) < 0.001)) {
            throw new EventException("Mock Exception");
        }
        return Math.sin(t) + Math.sin(y[0]);
    }

    public void setThrowExceptionOnEvaluate(boolean throwExceptionOnEvaluate) {
        this.throwExceptionOnEvaluate = throwExceptionOnEvaluate;
    }

    public void setThrowExceptionOnEvaluateAtTime(double t, boolean throwExceptionOnEvaluateAtTime) {
        this.throwExceptionOnEvaluateAtTime = throwExceptionOnEvaluateAtTime;
    }

    public void resetState(double t, double[] y) throws EventException {
        // empty implementation
    }

    public int eventOccurred(double t, double[] y, boolean forward) {
        return EventHandler.EVENT_OCCURRED;
    }
}