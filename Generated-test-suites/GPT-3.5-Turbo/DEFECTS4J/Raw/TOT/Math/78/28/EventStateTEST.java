import static org.junit.Assert.*;
import org.junit.Test;

public class EventStateTest {

    // Helper method to create a dummy event handler
    public EventHandler createDummyEventHandler() {
        return new EventHandler() {
            public double g(double t, double[] y) {
                return 0.0;
            }
            
            public int eventOccurred(double t, double[] y, boolean increasing) {
                return EventHandler.CONTINUE;
            }
            
            public void resetState(double t, double[] y) {
                
            }
        };
    }
    
    @Test
    public void testReinitializeBegin() throws EventException {
        EventHandler handler = createDummyEventHandler();
        EventState eventState = new EventState(handler, 0.1, 0.001, 100);
        double tStart = 0.0;
        double[] yStart = new double[]{1.0, 2.0, 3.0};
        
        eventState.reinitializeBegin(tStart, yStart);
        
        assertEquals(tStart, eventState.t0, 0.0);
        assertEquals(0.0, eventState.g0, 0.0);
        assertTrue(eventState.g0Positive);
    }
    
    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        EventHandler handler = createDummyEventHandler();
        EventState eventState = new EventState(handler, 0.1, 0.001, 100);
        
        StepInterpolator interpolator = new StepInterpolator() {
            public void setInterpolatedTime(double t) {
                
            }
            
            public double getInterpolatedTime() {
                return 0.0;
            }
            
            public void getInterpolatedState(double[] y) {
                
            }
            
            public void setInterpolatedState(double[] y) {
                
            }
            
            public boolean isForward() {
                return true;
            }
            
            public double getCurrentTime() {
                return 0.0;
            }
        };
        
        assertFalse(eventState.evaluateStep(interpolator));
        
        interpolator = new StepInterpolator() {
            public void setInterpolatedTime(double t) {
                
            }
            
            public double getInterpolatedTime() {
                return 0.0;
            }
            
            public void getInterpolatedState(double[] y) {
                
            }
            
            public void setInterpolatedState(double[] y) {
                
            }
            
            public boolean isForward() {
                return true;
            }
            
            public double getCurrentTime() {
                return 0.1;
            }
        };
        
        assertFalse(eventState.evaluateStep(interpolator));
        
        interpolator = new StepInterpolator() {
            public void setInterpolatedTime(double t) {
                
            }
            
            public double getInterpolatedTime() {
                return 0.0;
            }
            
            public void getInterpolatedState(double[] y) {
                
            }
            
            public void setInterpolatedState(double[] y) {
                
            }
            
            public boolean isForward() {
                return true;
            }
            
            public double getCurrentTime() {
                return 0.11;
            }
        };
        
        assertTrue(eventState.evaluateStep(interpolator));
    }
    
    @Test
    public void testStepAccepted() throws EventException {
        EventHandler handler = createDummyEventHandler();
        EventState eventState = new EventState(handler, 0.1, 0.001, 100);
        
        double t = 0.0;
        double[] y = new double[]{1.0, 2.0, 3.0};
        
        eventState.stepAccepted(t, y);
        
        assertEquals(t, eventState.t0, 0.0);
        assertEquals(0.0, eventState.g0, 0.0);
        assertTrue(eventState.g0Positive);
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
            
            public void resetState(double t, double[] y) {
                
            }
        };
        EventState eventState = new EventState(handler, 0.1, 0.001, 100);
        
        assertTrue(eventState.stop());
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
            
            public void resetState(double t, double[] y) {
                
            }
        };
        EventState eventState = new EventState(handler, 0.1, 0.001, 100);
        
        double t = 0.0;
        double[] y = new double[]{1.0, 2.0, 3.0};
        
        assertTrue(eventState.reset(t, y));
        
        handler = new EventHandler() {
            public double g(double t, double[] y) {
                return 0.0;
            }
            
            public int eventOccurred(double t, double[] y, boolean increasing) {
                return EventHandler.RESET_DERIVATIVES;
            }
            
            public void resetState(double t, double[] y) {
                
            }
        };
        eventState = new EventState(handler, 0.1, 0.001, 100);
        
        assertFalse(eventState.reset(t, y));
    }
}