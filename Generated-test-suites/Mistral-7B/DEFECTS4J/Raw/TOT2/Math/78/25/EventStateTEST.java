import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EventStateTest {

    private EventHandler mockEventHandler;
    private StepInterpolator mockInterpolator;
    private EventState eventState;

    @Before
    public void setUp() {
        mockEventHandler = new MockEventHandler();
        mockInterpolator = new MockStepInterpolator();
        eventState = new EventState(mockEventHandler, 0.1, 1e-6, 10);
    }

    @Test
    public void testInitializeBegin() throws EventException {
        double[] initialState = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(0.0, initialState);
        Assert.assertEquals(0.0, eventState.getT0(), 1e-6);
        Assert.assertArrayEquals(initialState, eventState.getY0(), 1e-6);
    }

    @Test
    public void testEvaluateStep_noEvent() throws DerivativeException, EventException, ConvergenceException {
        mockInterpolator.setForward(true);
        mockInterpolator.setCurrentTime(0.1);

        Assert.assertFalse(eventState.evaluateStep(mockInterpolator));
        Assert.assertFalse(eventState.hasPendingEvent());
    }

    @Test
    public void testEvaluateStep_eventInStep() throws DerivativeException, EventException, ConvergenceException {
        mockInterpolator.setForward(true);
        mockInterpolator.setCurrentTime(0.1);

        double[] changePoint = new double[]{0.05};
        double initialState = 0.0;
        double[] finalState = new double[]{1.0};
        mockEventHandler.setChangePoint(changePoint, initialState, finalState);

        Assert.assertTrue(eventState.evaluateStep(mockInterpolator));
        Assert.assertTrue(eventState.hasPendingEvent());
        Assert.assertEquals(changePoint[0], eventState.getPendingEventTime(), 1e-6);
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStep_derivativeException() throws DerivativeException, EventException, ConvergenceException {
        mockInterpolator.setForward(true);
        mockInterpolator.setCurrentTime(0.1);
        mockEventHandler.setDerivativeExceptionThrower(true);

        Assert.assertTrue(eventState.evaluateStep(mockInterpolator));
    }

    @Test(expected = EventException.class)
    public void testEvaluateStep_eventException() throws DerivativeException, EventException, ConvergenceException {
        mockInterpolator.setForward(true);
        mockInterpolator.setCurrentTime(0.1);
        mockEventHandler.setEventExceptionThrower(true);

        Assert.assertTrue(eventState.evaluateStep(mockInterpolator));
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStep_convergenceException() throws DerivativeException, EventException, ConvergenceException {
        mockInterpolator.setForward(true);
        mockInterpolator.setCurrentTime(0.1);
        mockEventHandler.setConvergenceExceptionThrower(true);

        Assert.assertTrue(eventState.evaluateStep(mockInterpolator));
    }

    @Test
    public void testGetEventTime() {
        double[] changePoint = new double[]{0.05};
        double initialState = 0.0;
        double[] finalState = new double[]{1.0};
        mockEventHandler.setChangePoint(changePoint, initialState, finalState);
        eventState.evaluateStep(mockInterpolator);

        Assert.assertEquals(changePoint[0], eventState.getEventTime(), 1e-6);
    }

    @Test
    public void testStepAccepted() throws EventException {
        double[] finalState = new double[]{1.0};
        eventState.stepAccepted(0.1, finalState);
        Assert.assertEquals(0.1, eventState.getT0(), 1e-6);
        Assert.assertArrayEquals(finalState, eventState.getY0(), 1e-6);
    }

    @Test
    public void testHasPendingEvent() {
        mockInterpolator.setForward(true);
        mockInterpolator.setCurrentTime(0.1);

        double[] changePoint = new double[]{0.05};
        double initialState = 0.0;
        double[] finalState = new double[]{1.0};
        mockEventHandler.setChangePoint(changePoint, initialState, finalState);

        Assert.assertTrue(eventState.evaluateStep(mockInterpolator));
        Assert.assertTrue(eventState.hasPendingEvent());
        Assert.assertEquals(changePoint[0], eventState.getPendingEventTime(), 1e-6);
        Assert.assertFalse(eventState.hasPendingEvent());
    }

    @Test
    public void testStop() {
        mockInterpolator.setForward(true);
        mockInterpolator.setCurrentTime(0.1);

        double[] changePoint = new double[]{0.05};
        double initialState = 0.0;
        double[] finalState = new double[]{1.0};
        mockEventHandler.setChangePoint(changePoint, initialState, finalState);
        mockEventHandler.setStopEvent(true);

        Assert.assertTrue(eventState.evaluateStep(mockInterpolator));
        Assert.assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        mockInterpolator.setForward(true);
        mockInterpolator.setCurrentTime(0.1);

        double[] changePoint = new double[]{0.05};
        double initialState = 0.0;
        double[] finalState = new double[]{1.0};
        mockEventHandler.setChangePoint(changePoint, initialState, finalState);
        mockEventHandler.setResetState(true);

        Assert.assertTrue(eventState.evaluateStep(mockInterpolator));
        Assert.assertTrue(eventState.reset(0.2, new double[]{2.0}));
        Assert.assertArrayEquals(new double[]{0.0}, eventState.getY0(), 1e-6);
    }

}

class MockEventHandler implements EventHandler {
    private boolean hasChangePoint = false;
    private double[] changePoint;
    private double initialState;
    private double[] finalState;
    private boolean derivativeExceptionThrower = false;
    private boolean eventExceptionThrower = false;
    private boolean convergenceExceptionThrower = false;
    private boolean resetState = false;
    private boolean resetDerivatives = false;
    private boolean stopEvent = false;

    public void setChangePoint(double[] changePoint, double initialState, double[] finalState) {
        this.hasChangePoint = true;
        this.changePoint = changePoint;
        this.initialState = initialState;
        this.finalState = finalState;
    }

    public void setDerivativeExceptionThrower(boolean derivativeExceptionThrower) {
        this.derivativeExceptionThrower = derivativeExceptionThrower;
    }

    public void setEventExceptionThrower(boolean eventExceptionThrower) {
        this.eventExceptionThrower = eventExceptionThrower;
    }

    public void setConvergenceExceptionThrower(boolean convergenceExceptionThrower) {
        this.convergenceExceptionThrower = convergenceExceptionThrower;
    }

    public void setResetState(boolean resetState) {
        this.resetState = resetState;
    }

    public void setResetDerivatives(boolean resetDerivatives) {
        this.resetDerivatives = resetDerivatives;
    }

    public void setStopEvent(boolean stopEvent) {
        this.stopEvent = stopEvent;
    }

    @Override
    public double g(double t, double[] y) throws EventException {
        if (derivativeExceptionThrower) {
            throw new DerivativeException("Mock Derivative Exception");
        } else if (eventExceptionThrower) {
            throw new EventException("Mock Event Exception");
        } else if (convergenceExceptionThrower) {
            throw new ConvergenceException("Mock Convergence Exception");
        }

        if (hasChangePoint && Math.abs(t - previousEventTime) <= convergence) {
            return getNextEventValue(t);
        }
        return initialState;
    }

    private double getNextEventValue(double t) {
        if (resetState) {
            return initialState;
        } else {
            return finalState[0];
        }
    }

    private double previousEventTime = Double.NaN;

    @Override
    public void eventOccurred(double t, double[] y, boolean triggered) {
        previousEventTime = t;
    }

    @Override
    public int eventOccurred(double t, double[] y, int nextAction) {
        this.nextAction = nextAction;
        return EventHandler.CONTINUE;
    }

    @Override
    public void resetState(double t, double[] y) throws EventException {
        if (resetState) {
            initialState = y[0];
        }
    }
}

class MockStepInterpolator implements StepInterpolator {
    private boolean forward = true;
    private double currentTime;

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public void setInterpolatedTime(double t) {
        this.currentTime = t;
    }

    public boolean isForward() {
        return forward;
    }

    public double getInterpolatedState() {
        return new double[]{currentTime};
    }

    public double getInterpolatedTime() {
        return currentTime;
    }
}