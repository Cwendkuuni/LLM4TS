import org.junit.Before;
import org.junit.Test;

public class EventStateTest {

    private EventState eventState;
    private EventHandlerMock eventHandler;
    private StepInterpolatorMock interpolator;

    @Before
    public void setUp() {
        eventHandler = new EventHandlerMock();
        interpolator = new StepInterpolatorMock();
        eventState = new EventState(eventHandler, 0.01, 0.01, 100);
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0, 3.0};

        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(tStart, eventState.t0, 0.0);
        assertEquals(eventHandler.g(tStart, yStart), eventState.g0, 0.0);
        assertTrue(eventState.g0Positive);
    }

    @Test
    public void testEvaluateStep_noEvent() throws DerivativeException, EventException, ConvergenceException {
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0, 3.0};
        double tEnd = 1.0;
        interpolator.setCurrentTime(tEnd);

        eventState.reinitializeBegin(tStart, yStart);
        boolean result = eventState.evaluateStep(interpolator);

        assertFalse(result);
        assertFalse(eventState.pendingEvent);
        assertTrue(Double.isNaN(eventState.pendingEventTime));
    }

    @Test
    public void testEvaluateStep_event() throws DerivativeException, EventException, ConvergenceException {
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0, 3.0};
        double tEnd = 1.0;
        interpolator.setCurrentTime(tEnd);

        eventState.reinitializeBegin(tStart, yStart);
        eventHandler.setEventOccurrence(true);
        boolean result = eventState.evaluateStep(interpolator);

        assertTrue(result);
        assertTrue(eventState.pendingEvent);
        assertEquals(eventHandler.getEventTime(), eventState.pendingEventTime, 0.0);
    }

    @Test
    public void testStepAccepted_noEvent() throws EventException {
        double t = 1.0;
        double[] y = {2.0, 3.0, 4.0};

        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.t0, 0.0);
        assertEquals(eventHandler.g(t, y), eventState.g0, 0.0);
        assertFalse(eventState.pendingEvent);
        assertTrue(Double.isNaN(eventState.pendingEventTime));
        assertEquals(EventHandler.CONTINUE, eventState.nextAction);
    }


    @Test
    public void testStepAccepted_pendingEvent() throws EventException {
        double t = 1.0;
        double[] y = {2.0, 3.0, 4.0};

        eventState.pendingEvent = true;
        eventState.increasing = true;
        eventState.previousEventTime = 0.0;
        eventState.pendingEventTime = 0.5;
        eventState.forward = true;

        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.t0, 0.0);
        assertEquals(eventHandler.g(t, y), eventState.g0, 0.0);
        assertFalse(eventState.pendingEvent);
        assertTrue(Double.isNaN(eventState.pendingEventTime));
        assertTrue(eventState.g0Positive);
        assertEquals(EventHandler.CONTINUE, eventState.nextAction);
    }

    @Test
    public void testStop() {
        assertFalse(eventState.stop());

        eventState.nextAction = EventHandler.STOP;

        assertTrue(eventState.stop());
    }

    @Test
    public void testReset_notPendingEvent() throws EventException {
        double t = 1.0;
        double[] y = {2.0, 3.0, 4.0};

        assertFalse(eventState.reset(t, y));
    }

    @Test
    public void testReset_pendingEvent() throws EventException {
        double t = 1.0;
        double[] y = {2.0, 3.0, 4.0};

        eventState.nextAction = EventHandler.RESET_STATE;

        assertTrue(eventState.reset(t, y));
    }

}

class EventHandlerMock implements EventHandler {

    private boolean eventOccurrence;
    private double eventTime;

    public double g(double t, double[] y) {
        return 0;
    }

    public int eventOccurred(double t, double[] y, boolean increasing) {
        return 0;
    }

    public void resetState(double t, double[] y) {

    }

    public boolean isEventOccurred() {
        return eventOccurrence;
    }

    public double getEventTime() {
        return eventTime;
    }

    public void setEventOccurrence(boolean eventOccurrence) {
        this.eventOccurrence = eventOccurrence;
    }

    public void setEventTime(double eventTime) {
        this.eventTime = eventTime;
    }
}

class StepInterpolatorMock implements StepInterpolator {

    private double currentTime;

    public double getCurrentTime() {
        return currentTime;
    }

    public void setInterpolatedTime(double time) {

    }

    public double getInterpolatedTime() {
        return 0;
    }

    public double[] getInterpolatedState() {
        return new double[0];
    }

    public void finalizeStep() {

    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }
}