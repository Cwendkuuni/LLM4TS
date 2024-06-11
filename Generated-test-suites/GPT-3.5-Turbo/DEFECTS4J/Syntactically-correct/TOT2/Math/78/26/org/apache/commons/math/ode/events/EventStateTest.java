import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EventStateTest {

    @Test
    public void testReinitializeBegin() {
        EventState eventState = new EventState(new TestEventHandler(), 0.1, 0.01, 100);
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0, 3.0};
        assertDoesNotThrow(() -> eventState.reinitializeBegin(tStart, yStart));
    }

    @Test
    public void testEvaluateStep() {
        EventState eventState = new EventState(new TestEventHandler(), 0.1, 0.01, 100);
        StepInterpolator interpolator = new TestStepInterpolator();
        assertDoesNotThrow(() -> eventState.evaluateStep(interpolator));
    }

    @Test
    public void testGetEventTime() {
        EventState eventState = new EventState(new TestEventHandler(), 0.1, 0.01, 100);
        assertEquals(Double.NaN, eventState.getEventTime(), 0.01);
    }

    @Test
    public void testStepAccepted() {
        EventState eventState = new EventState(new TestEventHandler(), 0.1, 0.01, 100);
        double t = 1.0;
        double[] y = {2.0, 3.0, 4.0};
        assertDoesNotThrow(() -> eventState.stepAccepted(t, y));
    }

    @Test
    public void testStop() {
        EventState eventState = new EventState(new TestEventHandler(), 0.1, 0.01, 100);
        assertFalse(eventState.stop());
    }

    @Test
    public void testReset() {
        EventState eventState = new EventState(new TestEventHandler(), 0.1, 0.01, 100);
        double t = 2.0;
        double[] y = {3.0, 4.0, 5.0};
        assertFalse(eventState.reset(t, y));
    }

    private class TestEventHandler implements EventHandler {

        @Override
        public double g(double t, double[] y) {
            return 0.0;
        }

        @Override
        public int eventOccurred(double t, double[] y, boolean increasing) {
            return EventHandler.CONTINUE;
        }

        @Override
        public void resetState(double t, double[] y) {
            
        }
    }

    private class TestStepInterpolator implements StepInterpolator {

        @Override
        public void setInterpolatedTime(double time) {
            
        }

        @Override
        public void setInterpolatedState(double[] state) {
            
        }

        @Override
        public void setInterpolatedDerivatives(double[] derivatives) {
            
        }

        @Override
        public double getCurrentTime() {
            return 0.0;
        }

        @Override
        public double[] getInterpolatedState() {
            return new double[]{1.0, 2.0, 3.0};
        }

        @Override
        public double[] getInterpolatedDerivatives() {
            return new double[]{4.0, 5.0, 6.0};
        }

        @Override
        public boolean isForward() {
            return true;
        }
        
    }

}