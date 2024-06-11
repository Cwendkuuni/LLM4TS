import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EventStateTest {
  
    private EventState eventState;
    private EventHandler eventHandler;
    
    @Before
    public void setUp() {
        eventHandler = new EventHandler();
        eventState = new EventState(eventHandler, 0.01, 0.001, 100);
    }
    
    @Test
    public void testGetEventHandler() {
        assertEquals(eventHandler, eventState.getEventHandler());
    }
    
    @Test
    public void testGetMaxCheckInterval() {
        assertEquals(0.01, eventState.getMaxCheckInterval(), 0.001);
    }
    
    @Test
    public void testGetConvergence() {
        assertEquals(0.001, eventState.getConvergence(), 0.0001);
    }
    
    @Test
    public void testGetMaxIterationCount() {
        assertEquals(100, eventState.getMaxIterationCount());
    }
    
    @Test
    public void testReinitializeBegin() throws EventException {
        eventState.reinitializeBegin(0.0, new double[] {0.0, 0.0});
        // No assertion added as the method does not return anything
    }
    
    @Test
    public void testEvaluateStep() throws DerivativeException, EventException, ConvergenceException {
        StepInterpolator interpolator = new StepInterpolator() {
            @Override
            public void setInterpolatedTime(double time) throws IllegalStateException {
                // Do nothing
            }
            
            @Override
            public void setInterpolatedTime(double time, boolean forward) throws IllegalStateException {
                // Do nothing
            }
            
            @Override
            public boolean isForward() {
                return true;
            }
            
            @Override
            public double getPreviousTime() {
                return 0.0;
            }
            
            @Override
            public double[] getPreviousState() {
                return new double[] {0.0, 0.0};
            }
            
            @Override
            public double getPreviousState(int i) {
                return 0.0;
            }
            
            @Override
            public int getInterpolatedStateSize() {
                return 2;
            }
            
            @Override
            public double getCurrentTime() {
                return 1.0;
            }
            
            @Override
            public double[] getCurrentState() {
                return new double[] {0.0, 0.0};
            }
            
            @Override
            public double getCurrentState(int i) {
                return 0.0;
            }
            
            @Override
            public double[] getInterpolatedState() throws IllegalStateException {
                return new double[] {0.0, 0.0};
            }
            
            @Override
            public double getInterpolatedState(int i) throws IllegalStateException {
                return 0.0;
            }
        };
        
        assertFalse(eventState.evaluateStep(interpolator));
    }
    
    @Test
    public void testGetEventTime() {
        assertEquals(Double.NaN, eventState.getEventTime(), 0.0001);
    }
    
    @Test
    public void testStepAccepted() throws EventException {
        eventState.stepAccepted(1.0, new double[] {1.0, 1.0});
        assertEquals(1.0, eventState.getEventTime(), 0.001);
    }
    
    @Test
    public void testStop() {
        assertFalse(eventState.stop());
    }
    
    @Test
    public void testReset() throws EventException {
        assertFalse(eventState.reset(2.0, new double[] {2.0, 2.0}));
    }

    private class EventHandler implements EventHandler {
        
        @Override
        public double g(double t, double[] y) throws DerivativeException, EventException {
            return 0.0;
        }

        @Override
        public int eventOccurred(double t, double[] y, boolean increasing) throws EventHandlerException {
            return EventHandler.CONTINUE;
        }

        @Override
        public void resetState(double t, double[] y) throws EventHandlerException {
            // Do nothing
        }
        
    }
}