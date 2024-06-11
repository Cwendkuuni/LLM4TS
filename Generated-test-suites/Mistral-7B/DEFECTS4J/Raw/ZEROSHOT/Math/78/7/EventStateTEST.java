import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class EventStateTest {

    @Test
    public void testEventStateConstruction() {
        EventHandler handlerMock = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return 0.0;
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean crossingForward) {
                // no-op
            }

            @Override
            public void resetState(double t, double[] y) {
                // no-op
            }
        };

        EventState eventState = new EventState(handlerMock, 1.0, 1.0e-6, 100);
        assertNotNull(eventState);
        assertEquals(handlerMock, eventState.getEventHandler());
        assertEquals(1.0, eventState.getMaxCheckInterval(), 0.0);
        assertEquals(1.0e-6, eventState.getConvergence(), 0.0);
        assertEquals(100, eventState.getMaxIterationCount());
        assertTrue(Double.isNaN(eventState.getT0()));
        assertTrue(Double.isNaN(eventState.getG0()));
        assertTrue(eventState.isG0Positive());
        assertFalse(eventState.hasPendingEvent());
        assertFalse(eventState.hasPendingEventTime());
        assertFalse(eventState.hasPreviousEventTime());
        assertFalse(eventState.isForward());
        assertFalse(eventState.isIncreasing());
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testEventStateReinitializeBegin() throws Exception {
        EventHandler handlerMock = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return 1.0 * Math.sin(t);
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean crossingForward) {
                // no-op
            }

            @Override
            public void resetState(double t, double[] y) {
                // no-op
            }
        };

        StepInterpolator interpolatorMock = new StepInterpolator() {
            @Override
            public double getInterpolatedState(int step) {
                return new double[]{step};
            }

            @Override
            public void setInterpolatedState(double[] state) {
                // no-op
            }

            @Override
            public double getInterpolatedTime() {
                return 0.0;
            }

            @Override
            public void setInterpolatedTime(double time) {
                // no-op
            }

            @Override
            public boolean isForward() {
                return true;
            }
        };

        EventState eventState = new EventState(handlerMock, 1.0, 1.0e-6, 100);
        double t0 = 0.0;
        double[] yStart = new double[]{0.0};
        eventState.reinitializeBegin(t0, yStart);
        assertEquals(t0, eventState.getT0(), 0.0);
        assertEquals(Math.sin(t0), eventState.getG0(), 0.0);
        assertTrue(eventState.isG0Positive());

        double g0 = eventState.getG0();

        eventState.evaluateStep(interpolatorMock);

        // This test depends on the specific implementation of the handlerMock.
        // In this case, it is expected that there's a change of sign in g0.
        assertTrue(g0 < 0.0 || (g0 >= 0.0 && eventState.hasPendingEvent()));
    }

    @Test
    public void testEventStateEvaluateStep() throws Exception {
        EventHandler handlerMock = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return Math.sin(t);
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean crossingForward) {
                // no-op
            }

            @Override
            public void resetState(double t, double[] y) {
                // no-op
            }
        };

        StepInterpolator interpolatorMock = new StepInterpolator() {
            @Override
            public double getInterpolatedState(int step) {
                return new double[]{step};
            }

            @Override
            public void setInterpolatedState(double[] state) {
                // no-op
            }

            @Override
            public double getInterpolatedTime() {
                return 0.0;
            }

            @Override
            public void setInterpolatedTime(double time) {
                // no-op
            }

            @Override
            public boolean isForward() {
                return true;
            }
        };

        EventState eventState = new EventState(handlerMock, 1.0, 1.0e-6, 100);
        double t0 = 0.0;
        double[] yStart = new double[]{0.0};
        eventState.reinitializeBegin(t0, yStart);

        eventState.evaluateStep(interpolatorMock);

        assertTrue(eventState.hasPendingEvent());
        assertEquals(eventState.getPendingEventTime(), interpolatorMock.getInterpolatedTime(), 0.0);

        eventState.stepAccepted(interpolatorMock.getInterpolatedTime(), new double[]{interpolatorMock.getInterpolatedState()[0] + 1.0});

        assertFalse(eventState.hasPendingEvent());
    }

    @Test
    public void testEventStateStepAccepted() throws Exception {
        EventHandler handlerMock = new EventHandler() {
            @Override
            public double g(double t, double[] y) {
                return 0.0;
            }

            @Override
            public void eventOccurred(double t, double[] y, boolean crossingForward) {
                throw new RuntimeException("Should not reach here.");
            }

            @Override
            public void resetState(double t, double[] y) {
                // no-op
            }
        };

        StepInterpolator interpolatorMock = new StepInterpolator() {
            @Override
            public double getInterpolatedState(int step) {
                return new double[]{step};
            }

            @Override
            public void setInterpolatedState(double[] state) {
                // no-op
            }

            @Override
            public double getInterpolatedTime() {
                return 1.0;
            }

            @Override
            public void setInterpolatedTime(double time) {
                // no-op
            }

            @Override
            public boolean isForward() {
                return true;
            }
        };

        EventState eventState = new EventState(handlerMock, 1.0, 1.0e-6, 100);
        double t0 = 0.0;
        double[] yStart = new double[]{0.0};
        eventState.reinitializeBegin(t0, yStart);

        eventState.evaluateStep(interpolatorMock);

        assertTrue(eventState.hasPendingEvent());
        try {
            eventState.stepAccepted(interpolatorMock.getInterpolatedTime(), new double[]{interpolatorMock.getInterpolatedState()[0]});
            fail("Should throw an EventException.");
        } catch (EventException e) {
            // Expected
        }
    }

}