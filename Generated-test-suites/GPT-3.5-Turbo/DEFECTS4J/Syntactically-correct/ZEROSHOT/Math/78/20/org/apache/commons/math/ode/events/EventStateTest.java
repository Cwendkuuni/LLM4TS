import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) {
                return t + y[0];
            }

            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException {
                return EventHandler.CONTINUE;
            }

            public void resetState(double t, double[] y) throws EventException {
                y[0] = 0;
            }
        };

        EventState eventState = new EventState(handler, 0.1, 1e-10, 100);
        double tStart = 0.0;
        double[] yStart = {1.0};
        eventState.reinitializeBegin(tStart, yStart);

        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(handler.g(tStart, yStart), eventState.getG0(), 0.0);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) {
                return t + y[0];
            }

            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException {
                return EventHandler.CONTINUE;
            }

            public void resetState(double t, double[] y) throws EventException {
                y[0] = 0;
            }
        };

        EventState eventState = new EventState(handler, 0.1, 1e-10, 100);
        double tStart = 0.0;
        double[] yStart = {1.0};
        eventState.reinitializeBegin(tStart, yStart);

        StepInterpolator interpolator = new StepInterpolator() {
            public boolean isForward() {
                return true;
            }

            public double getCurrentTime() {
                return 10.0;
            }

            public int getDimension() {
                return 1;
            }

            public double[] getInterpolatedState() throws DerivativeException {
                return new double[]{10.0};
            }

            public double[] getInterpolatedDerivatives() throws DerivativeException {
                return null;
            }

            public double getInterpolatedTime() {
                return 10.0;
            }

            public void setInterpolatedTime(double time) {
            }

            public void setInterpolatedState(double[] state) throws DerivativeException {
            }

            public void setInterpolatedTimeAndState(double time, double[] state) throws DerivativeException {
            }
        };

        assertTrue(eventState.evaluateStep(interpolator));
        assertTrue(eventState.getEventTime() < 10.0);
    }

    @Test
    public void testStepAccepted() throws EventException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) {
                return t + y[0];
            }

            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException {
                return EventHandler.CONTINUE;
            }

            public void resetState(double t, double[] y) throws EventException {
                y[0] = 0;
            }
        };

        EventState eventState = new EventState(handler, 0.1, 1e-10, 100);
        double t = 10.0;
        double[] y = {10.0};
        eventState.stepAccepted(t, y);

        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(handler.g(t, y), eventState.getG0(), 0.0);
        assertFalse(eventState.isPendingEvent());
        assertTrue(Double.isNaN(eventState.getPendingEventTime()));
    }

    @Test
    public void testStop() throws EventException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) {
                return t + y[0];
            }

            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException {
                return EventHandler.STOP;
            }

            public void resetState(double t, double[] y) throws EventException {
                y[0] = 0;
            }
        };

        EventState eventState = new EventState(handler, 0.1, 1e-10, 100);
        assertTrue(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) {
                return t + y[0];
            }

            public int eventOccurred(double t, double[] y, boolean increasing) throws EventException {
                return EventHandler.RESET_STATE;
            }

            public void resetState(double t, double[] y) throws EventException {
                y[0] = 0;
            }
        };

        EventState eventState = new EventState(handler, 0.1, 1e-10, 100);
        double t = 10.0;
        double[] y = {10.0};
        assertTrue(eventState.reset(t, y));
    }
}