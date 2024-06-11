import org.apache.commons.math.ode.events.EventState;
import org.apache.commons.math.ode.events.EventHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

    @Test
    public void testConstructor() {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        assertNotNull(eventState);
        assertEquals(handler, eventState.getEventHandler());
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(0.001, eventState.getConvergence(), 0.0);
        assertEquals(10, eventState.getMaxIterationCount());
        assertTrue(Double.isNaN(eventState.getT0()));
        assertTrue(Double.isNaN(eventState.getG0()));
        assertTrue(true, eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNaN(eventState.getPendingEventTime());
        assertNaN(eventState.getPreviousEventTime());
        assertTrue(eventState.isForward());
        assertTrue(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        double[] yStart = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(0.0, yStart);
        assertEquals(0.0, eventState.getT0(), 0.0);
        assertEquals(handler.g(0.0, yStart), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());
    }

    @Test(expected = EventException.class)
    public void testReinitializeBeginWithException() throws EventException {
        EventHandler handler = new DummyEventHandlerWithException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        double[] yStart = new double[]{1.0, 2.0};
        eventState.reinitializeBegin(0.0, yStart);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setForward(true);
        interpolator.setInterpolatedTime(1.0);

        // first step should not trigger an event
        assertFalse(eventState.evaluateStep(interpolator));

        interpolator.setInterpolatedTime(1.5);

        // second step should trigger an event
        assertTrue(eventState.evaluateStep(interpolator));
        assertEquals(1.25, eventState.getPendingEventTime(), 0.0);

        // third step should not trigger an event since we are already at the event time
        assertFalse(eventState.evaluateStep(interpolator));

        // fourth step should continue integration
        assertFalse(eventState.evaluateStep(interpolator));
    }

    @Test(expected = DerivativeException.class)
    public void testEvaluateStepWithDerivativeException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = new DummyEventHandlerWithDerivativeException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setForward(true);
        interpolator.setInterpolatedTime(1.0);
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = EventException.class)
    public void testEvaluateStepWithEventException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = new DummyEventHandlerWithEventException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setForward(true);
        interpolator.setInterpolatedTime(1.0);
        eventState.evaluateStep(interpolator);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateStepWithConvergenceException() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = new DummyEventHandlerWithConvergenceException();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setForward(true);
        interpolator.setInterpolatedTime(1.0);
        eventState.evaluateStep(interpolator);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        double[] y = new double[]{1.0, 2.0};
        eventState.stepAccepted(2.0, y);
        assertEquals(2.0, eventState.getT0(), 0.0);
        double g = handler.g(2.0, y);
        assertEquals(g, eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive() == (g > 0));
        assertFalse(eventState.isPendingEvent());
        assertNaN(eventState.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testStop() {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        eventState.setNextAction(EventHandler.STOP);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler handler = new DummyEventHandler();
        EventState eventState = new EventState(handler, 0.01, 0.001, 10);
        double t = 3.0;
        double[] y = new double[]{1.0, 2.0};
        eventState.reset(t, y);
        assertEquals(t, eventState.getT0(), 0.0);
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.isPendingEvent());
        assertNaN(eventState.getPendingEventTime());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

}

class DummyEventHandler implements EventHandler {

    @Override
    public double g(double t, double[] y) {
        return 0.0;
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean crossing) {
        // do nothing
    }

    @Override
    public void resetState(double t, double[] y) {
        // do nothing
    }

}

class DummyEventHandlerWithException implements EventHandler {

    @Override
    public double g(double t, double[] y) throws FunctionEvaluationException {
        throw new FunctionEvaluationException(new DerivativeException("test"), t);
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean crossing) {
        // do nothing
    }

    @Override
    public void resetState(double t, double[] y) {
        // do nothing
    }

}

class DummyEventHandlerWithDerivativeException implements EventHandler {

    @Override
    public double g(double t, double[] y) throws DerivativeException {
        throw new DerivativeException("test");
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean crossing) {
        // do nothing
    }

    @Override
    public void resetState(double t, double[] y) {
        // do nothing
    }

}

class DummyEventHandlerWithConvergenceException implements EventHandler {

    @Override
    public double g(double t, double[] y) throws ConvergenceException {
        throw new ConvergenceException("test");
    }

    @Override
    public void eventOccurred(double t, double[] y, boolean crossing) {
        // do nothing
    }

    @Override
    public void resetState(double t, double[] y) {
        // do nothing
    }

}