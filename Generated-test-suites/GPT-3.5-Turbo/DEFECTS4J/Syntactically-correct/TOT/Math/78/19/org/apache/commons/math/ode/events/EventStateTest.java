import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

    private EventState eventState;
    private EventHandler handler;
    private double maxCheckInterval;
    private double convergence;
    private int maxIterationCount;
    private double tStart;
    private double[] yStart;

    @Before
    public void setUp() {
        handler = new EventHandler() {
            public double g(double t, double[] y) {
                // Implement the dummy event handler for testing purposes
                return t + y[0];
            }

            public int eventOccurred(double t, double[] y, boolean increasing) {
                return EventHandler.RESET_STATE;
            }

            public void resetState(double t, double[] y) {

            }
        };

        maxCheckInterval = 0.1;
        convergence = 0.01;
        maxIterationCount = 100;
        tStart = 0.0;
        yStart = new double[]{1.0};

        eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
    }

    @Test
    public void reinitializeBegin_correctly_set_initial_values() throws EventException {
        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(handler.g(tStart, yStart), eventState.getG0(), 0.0);
    }

    @Test
    public void evaluateStep_no_event_triggered() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolatorMock interpolator = new StepInterpolatorMock(0.0, false);

        boolean result = eventState.evaluateStep(interpolator);

        assertFalse(result);
        assertFalse(eventState.hasPendingEvent());
    }

    @Test
    public void evaluateStep_event_triggered() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolatorMock interpolator = new StepInterpolatorMock(0.0, true);

        boolean result = eventState.evaluateStep(interpolator);

        assertTrue(result);
        assertTrue(eventState.hasPendingEvent());
        assertEquals(0.0, eventState.getPendingEventTime(), 0.0);
    }

    @Test
    public void getEventTime_return_pendingEventTime() throws EventException {
        eventState.setPendingEvent(true);
        double pendingEventTime = 1.0;
        eventState.setPendingEventTime(pendingEventTime);

        assertEquals(pendingEventTime, eventState.getEventTime(), 0.0);
    }

    @Test
    public void stepAccepted_pendingEvent_accepted() throws EventException {
        eventState.setPendingEvent(true);
        eventState.setIncreasing(false);
        double pendingEventTime = 1.0;
        eventState.setPendingEventTime(pendingEventTime);
        double t = 2.0;
        double[] y = new double[]{1.0};

        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(handler.g(t, y), eventState.getG0(), 0.0);
    }

    @Test
    public void stepAccepted_pendingEvent_not_accepted() throws EventException {
        eventState.setPendingEvent(true);
        eventState.setIncreasing(true);
        double pendingEventTime = eventState.getConvergence();
        eventState.setPendingEventTime(pendingEventTime);
        double t = pendingEventTime + 0.01;
        double[] y = new double[]{1.0};

        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(handler.g(t, y), eventState.getG0(), 0.0);
    }

    @Test
    public void stop_nextAction_stop() {
        eventState.setNextAction(EventHandler.STOP);

        assertTrue(eventState.stop());
    }

    @Test
    public void reset_no_pendingEvent() throws EventException {
        eventState.setPendingEvent(false);
        double t = 1.0;
        double[] y = new double[]{1.0};

        boolean resetDerivatives = eventState.reset(t, y);

        assertFalse(resetDerivatives);
        assertFalse(eventState.hasPendingEvent());
        assertTrue(Double.isNaN(eventState.getPendingEventTime()));
    }

    @Test
    public void reset_pendingEvent_resetState() throws EventException {
        eventState.setPendingEvent(true);
        eventState.setNextAction(EventHandler.RESET_STATE);
        double t = 1.0;
        double[] y = new double[]{1.0};

        boolean resetDerivatives = eventState.reset(t, y);

        assertTrue(resetDerivatives);
        assertFalse(eventState.hasPendingEvent());
        assertTrue(Double.isNaN(eventState.getPendingEventTime()));
    }
}

class StepInterpolatorMock implements StepInterpolator {

    private double currentTime;
    private boolean forward;

    public StepInterpolatorMock(double currentTime, boolean forward) {
        this.currentTime = currentTime;
        this.forward = forward;
    }

    @Override
    public void setInterpolatedTime(double time) {
        currentTime = time;
    }

    @Override
    public double getInterpolatedTime() {
        return currentTime;
    }

    @Override
    public double[] getInterpolatedState() {
        return new double[0];
    }

    @Override
    public double getCurrentTime() {
        return currentTime;
    }

    @Override
    public boolean isForward() {
        return forward;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }
}