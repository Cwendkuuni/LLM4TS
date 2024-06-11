import org.junit.Assert;
import org.junit.Test;

public class EventStateTest {

    @Test
    public void testReinitializeBegin() {
        EventHandler handler = new EventHandler();
        EventState eventState = new EventState(handler, 0.1, 1e-6, 100);
        
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0, 3.0};
        
        try {
            eventState.reinitializeBegin(tStart, yStart);
        } catch (EventException e) {
            Assert.fail("Exception thrown during reinitializeBegin()");
        }
        
        Assert.assertEquals(tStart, eventState.t0, 0.0);
        Assert.assertEquals(handler.g(tStart, yStart), eventState.g0, 0.0);
    }
    
    @Test
    public void testEvaluateStep() {
        EventHandler handler = new EventHandler();
        EventState eventState = new EventState(handler, 0.1, 1e-6, 100);
        
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0, 3.0};
        StepInterpolator interpolator = new StepInterpolator();
        
        boolean result = false;
        
        try {
            eventState.reinitializeBegin(tStart, yStart);
            result = eventState.evaluateStep(interpolator);
        } catch (DerivativeException | EventException | ConvergenceException e) {
            Assert.fail("Exception thrown during evaluateStep()");
        }
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testGetEventTime() {
        EventHandler handler = new EventHandler();
        EventState eventState = new EventState(handler, 0.1, 1e-6, 100);
        
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0, 3.0};
        StepInterpolator interpolator = new StepInterpolator();
        
        try {
            eventState.reinitializeBegin(tStart, yStart);
            eventState.evaluateStep(interpolator);
        } catch (DerivativeException | EventException | ConvergenceException e) {
            Assert.fail("Exception thrown during evaluateStep()");
        }
        
        Assert.assertEquals(handler.getEventTime(), eventState.getEventTime(), 0.0);
    }
    
    @Test
    public void testStepAccepted() {
        EventHandler handler = new EventHandler();
        EventState eventState = new EventState(handler, 0.1, 1e-6, 100);
        
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0, 3.0};
        
        double t = 1.0;
        double[] y = {4.0, 5.0, 6.0};
        
        try {
            eventState.reinitializeBegin(tStart, yStart);
            eventState.stepAccepted(t, y);
        } catch (EventException e) {
            Assert.fail("Exception thrown during stepAccepted()");
        }
        
        Assert.assertEquals(t, eventState.t0, 0.0);
        Assert.assertEquals(handler.g(t, y), eventState.g0, 0.0);
    }
    
    @Test
    public void testStop() {
        EventHandler handler = new EventHandler();
        EventState eventState = new EventState(handler, 0.1, 1e-6, 100);
        
        Assert.assertFalse(eventState.stop());
        
        handler.setAction(EventHandler.STOP);
        
        Assert.assertTrue(eventState.stop());
    }
    
    @Test
    public void testReset() {
        EventHandler handler = new EventHandler();
        EventState eventState = new EventState(handler, 0.1, 1e-6, 100);
        
        double t = 1.0;
        double[] y = {4.0, 5.0, 6.0};
        
        eventState.pendingEvent = true;
        eventState.nextAction = EventHandler.RESET_STATE;
        
        try {
            Assert.assertTrue(eventState.reset(t, y));
        } catch (EventException e) {
            Assert.fail("Exception thrown during reset()");
        }
        
        eventState.pendingEvent = true;
        eventState.nextAction = EventHandler.RESET_DERIVATIVES;
        
        try {
            Assert.assertTrue(eventState.reset(t, y));
        } catch (EventException e) {
            Assert.fail("Exception thrown during reset()");
        }
        
        eventState.pendingEvent = false;
        eventState.nextAction = EventHandler.CONTINUE;
        
        try {
            Assert.assertFalse(eventState.reset(t, y));
        } catch (EventException e) {
            Assert.fail("Exception thrown during reset()");
        }
    }

    class EventHandler {
        public static final int STOP = 0;
        public static final int CONTINUE = 1;
        public static final int RESET_STATE = 2;
        public static final int RESET_DERIVATIVES = 3;
        
        private int action;
        private double eventTime;
        
        public int eventOccurred(double t, double[] y, boolean increasing) {
            // Set the event action based on some condition
            if (increasing) {
                action = EventHandler.STOP;
            } else {
                action = EventHandler.RESET_STATE;
            }
            
            // Set the event time
            eventTime = t;
            
            return action;
        }
        
        public double g(double t, double[] y) {
            // Calculate the value of the event function at time t and state y
            return y[0] - t;
        }
        
        public void resetState(double t, double[] y) {
            // Reset the state of the event handler
            y[0] = 0.0;
        }
        
        public int getAction() {
            return action;
        }
        
        public double getEventTime() {
            return eventTime;
        }
    }
}