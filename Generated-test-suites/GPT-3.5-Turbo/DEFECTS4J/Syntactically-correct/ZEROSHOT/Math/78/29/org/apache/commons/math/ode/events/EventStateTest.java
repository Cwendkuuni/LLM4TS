import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EventStateTest {

    private EventState eventState;
    private EventHandler eventHandler;

    @Before
    public void setUp() throws Exception {
        eventHandler = new EventHandler();
        eventState = new EventState(eventHandler, 0.1, 0.001, 1000);
    }

    @Test
    public void testGetEventHandler() {
        assertSame(eventHandler, eventState.getEventHandler());
    }

    @Test
    public void testGetMaxCheckInterval() {
        assertEquals(0.1, eventState.getMaxCheckInterval(), 0.000001);
    }

    @Test
    public void testGetConvergence() {
        assertEquals(0.001, eventState.getConvergence(), 0.000001);
    }

    @Test
    public void testGetMaxIterationCount() {
        assertEquals(1000, eventState.getMaxIterationCount());
    }

    @Test
    public void testReinitializeBegin() throws Exception {
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0, 3.0};
        
        eventState.reinitializeBegin(tStart, yStart);
        
        assertEquals(tStart, eventState.t0, 0.0);
        assertEquals(eventHandler.g(tStart, yStart), eventState.g0, 0.0);
    }

    @Test
    public void testEvaluateStep() throws Exception {
        StepInterpolator interpolator = new StepInterpolator();
        boolean result = eventState.evaluateStep(interpolator);
        assertFalse(result);
    }

    @Test
    public void testGetEventTime() {
        double expected = Double.NaN;
        assertEquals(expected, eventState.getEventTime(), 0.0);
    }

    @Test
    public void testStepAccepted() throws Exception {
        double t = 1.0;
        double[] y = {4.0, 5.0, 6.0};
        
        eventState.stepAccepted(t, y);
        
        assertEquals(t, eventState.t0, 0.0);
        assertEquals(eventHandler.g(t, y), eventState.g0, 0.0);
        assertFalse(eventState.pendingEvent);
    }

    @Test
    public void testStop() {
        boolean expected = false;
        assertEquals(expected, eventState.stop());
    }

    @Test
    public void testReset() throws Exception {
        double t = 2.0;
        double[] y = {7.0, 8.0, 9.0};

        eventState.pendingEvent = true;
        eventState.nextAction = EventHandler.RESET_DERIVATIVES;
        
        boolean result = eventState.reset(t, y);
        
        assertTrue(result);
        assertFalse(eventState.pendingEvent);
        assertEquals(Double.NaN, eventState.pendingEventTime, 0.0);
    }
}

class EventHandler {
    
    public static final int CONTINUE = 0;
    public static final int STOP = 1;
    public static final int RESET_STATE = 2;
    public static final int RESET_DERIVATIVES = 3;
    
    public double g(double t, double[] y) {
        return 0.0;
    }
    
    public int eventOccurred(double t, double[] y, boolean increasing) {
        return CONTINUE;
    }
    
    public void resetState(double t, double[] y) {
        
    }
}

class StepInterpolator {
    
    private double currentTime;
    private boolean forward;
    private double[] interpolatedState;
    
    public void setInterpolatedTime(double time) {
        currentTime = time;
    }
    
    public double getCurrentTime() {
        return currentTime;
    }
    
    public boolean isForward() {
        return forward;
    }
    
    public void setForward(boolean forward) {
        this.forward = forward;
    }
    
    public double[] getInterpolatedState() {
        return interpolatedState;
    }
    
    public void setInterpolatedState(double[] state) {
        interpolatedState = state;
    }
}