import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

    private EventState eventState;
    private EventHandlerStub eventHandler;

    @Before
    public void setUp() {
        eventHandler = new EventHandlerStub();
        eventState = new EventState(eventHandler, 1, 0.001, 100);
    }

    @Test
    public void testGetEventHandler() {
        assertEquals(eventHandler, eventState.getEventHandler());
    }

    @Test
    public void testGetMaxCheckInterval() {
        assertEquals(1, eventState.getMaxCheckInterval(), 0);
    }

    @Test
    public void testGetConvergence() {
        assertEquals(0.001, eventState.getConvergence(), 0);
    }

    @Test
    public void testGetMaxIterationCount() {
        assertEquals(100, eventState.getMaxIterationCount());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 0;
        double[] yStart = { 1, 2, 3 };
        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(tStart, eventState.t0, 0);
        assertEquals(eventHandler.g(tStart, yStart), eventState.g0, 0);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        double tStart = 0;
        double[] yStart = { 1, 2, 3 };
        eventState.reinitializeBegin(tStart, yStart);

        StepInterpolatorStub interpolator = new StepInterpolatorStub();
        interpolator.setForward(true);
        interpolator.setCurrentTime(1);

        boolean result = eventState.evaluateStep(interpolator);
        assertFalse(result);
    }

    @Test
    public void testStepAccepted() throws EventException {
        double t = 0;
        double[] y = { 1, 2, 3 };
        eventState.reinitializeBegin(t, y);

        eventHandler.setNextAction(EventHandler.STOP);
        eventState.stepAccepted(t, y);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        double t = 0;
        double[] y = { 1, 2, 3 };
        eventState.reinitializeBegin(t, y);

        eventHandler.setNextAction(EventHandler.RESET_STATE);
        boolean resetDerivatives = eventState.reset(t, y);
        assertTrue(resetDerivatives);
    }
}

class EventHandlerStub implements EventHandler {

    private int nextAction;

    public void setNextAction(int nextAction) {
        this.nextAction = nextAction;
    }

    @Override
    public double g(double t, double[] y) throws EventException {
        return 0;
    }

    @Override
    public int eventOccurred(double t, double[] y, boolean increasing) throws EventException {
        return nextAction;
    }

    @Override
    public void resetState(double t, double[] y) throws EventException {}

}

class StepInterpolatorStub implements StepInterpolator {

    private boolean forward;
    private double currentTime;

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public void setInterpolatedTime(double time) {}

    @Override
    public double getInterpolatedTime() {
        return 0;
    }

    @Override
    public double[] getInterpolatedState() {
        return new double[0];
    }

    @Override
    public double[] getInterpolatedDerivatives() {
        return new double[0];
    }

    @Override
    public int getCurrentStepStart() {
        return 0;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {}

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {}

    @Override
    public double[] getInterpolatedSecondaryState(int index) {
        return new double[0];
    }

    @Override
    public double[] getInterpolatedSecondaryDerivatives(int index) {
        return new double[0];
    }

    @Override
    public int getDimension() {
        return 0;
    }

    @Override
    public boolean isForward() {
        return forward;
    }

    @Override
    public double getCurrentTime() {
        return currentTime;
    }
}