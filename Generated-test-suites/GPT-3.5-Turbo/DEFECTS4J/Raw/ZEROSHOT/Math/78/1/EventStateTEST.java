import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

    @Test
    public void testReinitializeBegin() {
        EventHandler handler = new MockEventHandler();
        double maxCheckInterval = 0.1;
        double convergence = 0.01;
        int maxIterationCount = 10;
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        double tStart = 0.0;
        double[] yStart = new double[]{1.0, 2.0, 3.0};

        try {
            eventState.reinitializeBegin(tStart, yStart);

            assertEquals(tStart, eventState.getT0(), 0.0);
            assertEquals(handler.g(tStart, yStart), eventState.getG0(), 0.0);
        } catch (EventException e) {
            fail("Unexpected Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testEvaluateStep() {
        EventHandler handler = new MockEventHandler();
        double maxCheckInterval = 0.1;
        double convergence = 0.01;
        int maxIterationCount = 10;
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        double tStart = 0.0;
        double[] yStart = new double[]{1.0, 2.0, 3.0};

        try {
            eventState.reinitializeBegin(tStart, yStart);

            StepInterpolator interpolator = new MockStepInterpolator();
            boolean result = eventState.evaluateStep(interpolator);

            assertFalse(result);
        } catch (EventException e) {
            fail("Unexpected Exception occurred: " + e.getMessage());
        } catch (DerivativeException e) {
            fail("Unexpected Exception occurred: " + e.getMessage());
        } catch (ConvergenceException e) {
            fail("Unexpected Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetEventTime() {
        EventHandler handler = new MockEventHandler();
        double maxCheckInterval = 0.1;
        double convergence = 0.01;
        int maxIterationCount = 10;
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        double tStart = 0.0;
        double[] yStart = new double[]{1.0, 2.0, 3.0};

        try {
            eventState.reinitializeBegin(tStart, yStart);

            StepInterpolator interpolator = new MockStepInterpolator();
            eventState.evaluateStep(interpolator);

            double eventTime = eventState.getEventTime();
            assertTrue(Double.isNaN(eventTime));
        } catch (EventException e) {
            fail("Unexpected Exception occurred: " + e.getMessage());
        } catch (DerivativeException e) {
            fail("Unexpected Exception occurred: " + e.getMessage());
        } catch (ConvergenceException e) {
            fail("Unexpected Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testStepAccepted() {
        EventHandler handler = new MockEventHandler();
        double maxCheckInterval = 0.1;
        double convergence = 0.01;
        int maxIterationCount = 10;
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        double t0 = 0.0;
        double g0 = 0.0;

        eventState.setT0(t0);
        eventState.setG0(g0);

        double t = 1.0;
        double[] y = new double[]{1.0, 2.0, 3.0};

        try {
            eventState.stepAccepted(t, y);

            assertEquals(t, eventState.getT0(), 0.0);
            assertEquals(handler.g(t, y), eventState.getG0(), 0.0);
        } catch (EventException e) {
            fail("Unexpected Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testStop() {
        EventHandler handler = new MockEventHandler();
        double maxCheckInterval = 0.1;
        double convergence = 0.01;
        int maxIterationCount = 10;
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        int nextAction = EventHandler.CONTINUE;
        eventState.setNextAction(nextAction);

        boolean stop = eventState.stop();

        assertFalse(stop);
    }

    @Test
    public void testReset() {
        EventHandler handler = new MockEventHandler();
        double maxCheckInterval = 0.1;
        double convergence = 0.01;
        int maxIterationCount = 10;
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);

        boolean pendingEvent = false;
        eventState.setPendingEvent(pendingEvent);

        double tStart = 0.0;
        double[] yStart = new double[]{1.0, 2.0, 3.0};

        try {
            boolean reset = eventState.reset(tStart, yStart);

            assertFalse(reset);
            assertFalse(eventState.isPendingEvent());
        } catch (EventException e) {
            fail("Unexpected Exception occurred: " + e.getMessage());
        }
    }
}