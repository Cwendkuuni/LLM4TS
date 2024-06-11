import static org.junit.Assert.*;
import org.junit.Test;

public class EventStateTest {
    
    @Test
    public void testReinitializeBegin() {
        EventState eventState = new EventState(new EventHandler(), 0.1, 0.01, 100);
        
        eventState.reinitializeBegin(0.0, new double[] {1.0});
        
        assertEquals(1.0, eventState.getEventHandler().g(0.0, new double[] {1.0}), 0.0);
        assertEquals(0.0, eventState.getEventHandler().g(1.0, new double[] {1.0}), 0.0);
    }
    
    @Test
    public void testEvaluateStep() {
        EventState eventState = new EventState(new EventHandler(), 0.1, 0.01, 100);
        
        eventState.reinitializeBegin(0.0, new double[] {1.0});
        
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setCurrentTime(0.1);
        
        try {
            assertFalse(eventState.evaluateStep(interpolator));
        } catch (DerivativeException e) {
            fail(e.getMessage());
        } catch (EventException e) {
            fail(e.getMessage());
        } catch (ConvergenceException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testGetEventTime() {
        EventState eventState = new EventState(new EventHandler(), 0.1, 0.01, 100);
        
        eventState.reinitializeBegin(0.0, new double[] {1.0});
        
        eventState.stepAccepted(0.1, new double[] {1.0});
        
        assertEquals(Double.NaN, eventState.getEventTime(), 0.0);
    }
    
    @Test
    public void testStepAccepted() {
        EventState eventState = new EventState(new EventHandler(), 0.1, 0.01, 100);
        
        eventState.reinitializeBegin(0.0, new double[] {1.0});
        
        eventState.stepAccepted(0.1, new double[] {1.0});
        
        assertEquals(1.0, eventState.getEventHandler().g(0.1, new double[] {1.0}), 0.0);
        assertEquals(0.0, eventState.getEventHandler().g(0.2, new double[] {1.0}), 0.0);
    }
    
    @Test
    public void testStop() {
        EventState eventState = new EventState(new EventHandler(), 0.1, 0.01, 100);
        
        assertFalse(eventState.stop());
        
        eventState.getEventHandler().setNextAction(EventHandler.STOP);
        
        assertTrue(eventState.stop());
    }
    
    @Test
    public void testReset() {
        EventState eventState = new EventState(new EventHandler(), 0.1, 0.01, 100);
        
        eventState.reinitializeBegin(0.0, new double[] {1.0});
        
        eventState.stepAccepted(0.1, new double[] {1.0});
        
        eventState.getEventHandler().setNextAction(EventHandler.RESET_STATE);
        
        try {
            assertTrue(eventState.reset(0.2, new double[] {2.0}));
        } catch (EventException e) {
            fail(e.getMessage());
        }
        
        assertEquals(2.0, eventState.getEventHandler().g(0.2, new double[] {2.0}), 0.0);
        assertEquals(0.0, eventState.getEventHandler().g(0.3, new double[] {2.0}), 0.0);
    }
    
}