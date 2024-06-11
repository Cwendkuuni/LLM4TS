import org.junit.Assert;
import org.junit.Test;

public class EventStateTest {

    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) {
                return 0.0;
            }
            public int eventOccurred(double t, double[] y, boolean increasing) {
                return EventHandler.STOP;
            }
            public void resetState(double t, double[] y) throws EventException {
            }
        };
        
        EventState eventState = new EventState(handler, 0.1, 0.01, 100);
        
        eventState.reinitializeBegin(0.0, new double[] {1.0, 2.0});
        
        Assert.assertEquals(0.0, eventState.getEventHandler().g(0.0, new double[] {1.0, 2.0}), 0.001);
    }
    
    @Test
    public void testEvaluateStepWithEvent() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) {
                return t - 1.0;
            }
            public int eventOccurred(double t, double[] y, boolean increasing) {
                return EventHandler.STOP;
            }
            public void resetState(double t, double[] y) throws EventException {
            }
        };
        
        EventState eventState = new EventState(handler, 0.1, 0.01, 100);
        
        StepInterpolator interpolator = new StepInterpolator() {
            public void setInterpolatedTime(double time) throws DerivativeException {
            }
            public double getCurrentTime() {
                return 0.0;
            }
            public void interpolate(double t, double[] y) throws DerivativeException {
            }
            public void setInterpolatedDerivatives(double[] derivatives) {
            }
            public double[] getInterpolatedState() throws DerivativeException {
                return new double[] {1.0, 2.0};
            }
            public double[] getInterpolatedDerivatives() throws DerivativeException {
                return new double[] {3.0, 4.0};
            }
            public boolean isForward() {
                return true;
            }
        };
        
        Assert.assertTrue(eventState.evaluateStep(interpolator));
    }
    
    @Test
    public void testEvaluateStepWithoutEvent() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) {
                return t - 1.0;
            }
            public int eventOccurred(double t, double[] y, boolean increasing) {
                return EventHandler.STOP;
            }
            public void resetState(double t, double[] y) throws EventException {
            }
        };
        
        EventState eventState = new EventState(handler, 0.1, 0.01, 100);
        
        StepInterpolator interpolator = new StepInterpolator() {
            public void setInterpolatedTime(double time) throws DerivativeException {
            }
            public double getCurrentTime() {
                return 0.0;
            }
            public void interpolate(double t, double[] y) throws DerivativeException {
            }
            public void setInterpolatedDerivatives(double[] derivatives) {
            }
            public double[] getInterpolatedState() throws DerivativeException {
                return new double[] {1.0, 2.0};
            }
            public double[] getInterpolatedDerivatives() throws DerivativeException {
                return new double[] {3.0, 4.0};
            }
            public boolean isForward() {
                return true;
            }
        };
        
        Assert.assertFalse(eventState.evaluateStep(interpolator));
    }
    
    @Test
    public void testStop() {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) {
                return 0.0;
            }
            public int eventOccurred(double t, double[] y, boolean increasing) {
                return EventHandler.STOP;
            }
            public void resetState(double t, double[] y) throws EventException {
            }
        };
        
        EventState eventState = new EventState(handler, 0.1, 0.01, 100);
        
        Assert.assertTrue(eventState.stop());
    }
    
    @Test
    public void testReset() throws EventException {
        EventHandler handler = new EventHandler() {
            public double g(double t, double[] y) {
                return 0.0;
            }
            public int eventOccurred(double t, double[] y, boolean increasing) {
                return EventHandler.RESET_STATE;
            }
            public void resetState(double t, double[] y) throws EventException {
            }
        };
        
        EventState eventState = new EventState(handler, 0.1, 0.01, 100);
        
        Assert.assertTrue(eventState.reset(0.0, new double[] {1.0, 2.0}));
    }
}