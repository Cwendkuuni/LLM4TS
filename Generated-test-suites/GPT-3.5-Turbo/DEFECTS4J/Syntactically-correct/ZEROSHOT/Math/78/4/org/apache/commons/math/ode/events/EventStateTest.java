import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EventStateTest {

    private EventState eventState;
    private EventHandler eventHandler;

    @Before
    public void setUp() {
        eventHandler = new EventHandler() {

            public double g(double t, double[] y) {
                return t + y[0];
            }

            public int eventOccurred(double t, double[] y, boolean increasing) {
                return EventHandler.RESET_STATE;
            }

            public void resetState(double t, double[] y) {
                y[0] = 0;
            }

        };
        eventState = new EventState(eventHandler, 0.1, 1e-6, 100);
    }

    @Test
    public void testGetEventHandler() {
        assertEquals(eventHandler, eventState.getEventHandler());
    }

    @Test
    public void testGetMaxCheckInterval() {
        assertEquals(0.1, eventState.getMaxCheckInterval(), 0.0);
    }

    @Test
    public void testGetConvergence() {
        assertEquals(1e-6, eventState.getConvergence(), 0.0);
    }

    @Test
    public void testGetMaxIterationCount() {
        assertEquals(100, eventState.getMaxIterationCount());
    }

    @Test
    public void testReinitializeBegin() throws EventException {
        eventState.reinitializeBegin(0.0, new double[] {1.0});
        assertEquals(0.0, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        MockStepInterpolator interpolator = new MockStepInterpolator();
        interpolator.setCurrentTime(0.0);
        interpolator.setForward(true);
        interpolator.setInterpolatedState(new double[] {1.0});
        boolean result = eventState.evaluateStep(interpolator);
        assertFalse(result);
    }

    @Test
    public void testStepAccepted() throws EventException {
        eventState.stepAccepted(0.0, new double[] {1.0});
        assertEquals(0.0, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testStop() {
        assertFalse(eventState.stop());
    }

    @Test
    public void testReset() throws EventException {
        boolean result = eventState.reset(0.0, new double[] {1.0});
        assertTrue(result);
    }

    public class MockStepInterpolator implements StepInterpolator {

        private double currentTime;
        private boolean forward;
        private double[] interpolatedState;

        public void setInterpolatedTime(double time) {
            this.currentTime = time;
        }

        public double getCurrentTime() {
            return this.currentTime;
        }

        public double getPreviousTime() {
            return 0.0;
        }

        public boolean isForward() {
            return this.forward;
        }

        public void setForward(boolean forward) {
            this.forward = forward;
        }

        public void setInterpolatedState(double[] state) {
            this.interpolatedState = state;
        }

        public double[] getInterpolatedState() {
            return this.interpolatedState;
        }

        public double[] getInterpolatedDerivatives() {
            return null;
        }

        public double[] getInterpolatedSecondaryState(int index) {
            return null;
        }

        public double[][] getInterpolatedSecondaryDerivatives(int index) {
            return null;
        }

    }

}