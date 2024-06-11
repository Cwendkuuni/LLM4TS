import org.junit.Test;
import static org.junit.Assert.*;

public class EventStateTest {
    
    private class DummyEventHandler implements EventHandler {
        
        public int count = 0;

        public double g(double t, double[] y) {
            return t - y[0];
        }

        public int eventOccurred(double t, double[] y, boolean increasing) {
            count++;
            return EventHandler.STOP;
        }

        public void resetState(double t, double[] y) {
            
        }
        
    }
    
    @Test
    public void testReinitializeBegin() throws EventException {
        EventState state = new EventState(new DummyEventHandler(), 0.01, 1e-6, 100);
        state.reinitializeBegin(0.0, new double[] {0.0});
        
        assertTrue(Double.isNaN(state.getEventTime()));
    }
    
    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventState state = new EventState(new DummyEventHandler(), 0.01, 1e-6, 100);
        state.reinitializeBegin(0.0, new double[] {0.0});
        
        assertFalse(state.evaluateStep(new DummyStepInterpolator(0.0, new double[] {0.0})));
    }
    
    @Test
    public void testStepAccepted() throws EventException {
        EventState state = new EventState(new DummyEventHandler(), 0.01, 1e-6, 100);
        state.reinitializeBegin(0.0, new double[] {0.0});
        state.stepAccepted(0.01, new double[] {0.0});
        
        assertFalse(state.stop());
        assertEquals(1, ((DummyEventHandler) state.getEventHandler()).count);
    }
    
    @Test
    public void testReset() throws EventException {
        EventState state = new EventState(new DummyEventHandler(), 0.01, 1e-6, 100);
        state.reinitializeBegin(0.0, new double[] {0.0});
        state.stepAccepted(0.01, new double[] {0.0});
        
        assertFalse(state.reset(0.01, new double[] {0.0}));
    }
}

class DummyStepInterpolator implements StepInterpolator {

    private double t;
    private double[] y;

    public DummyStepInterpolator(double t, double[] y) {
        this.t = t;
        this.y = y;
    }

    @Override
    public void setInterpolatedTime(double time) throws IllegalArgumentException {
        if (time < t) {
            throw new IllegalArgumentException();
        }
        t = time;
    }

    @Override
    public double getInterpolatedTime() {
        return t;
    }

    @Override
    public double[] getInterpolatedState() throws IllegalStateException {
        return y;
    }

    @Override
    public double[] getInterpolatedDerivatives() throws IllegalStateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getCurrentTime() {
        return t;
    }

    @Override
    public boolean isForward() {
        return true;
    }

    @Override
    public StepInterpolator copy() {
        throw new UnsupportedOperationException();
    }
}