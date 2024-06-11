
import static org.junit.Assert.*;
import org.junit.Test;

public class EventStateTest {

    @Test
    public void testReinitializeBegin() {
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        double tStart = 0.0;
        double[] yStart = {1.0, 2.0, 3.0};
        
        eventState.reinitializeBegin(tStart, yStart);
        
        // Check if the values are initialized correctly
        assertEquals(tStart, eventState.getT0(), 0.0);
        assertEquals(g(tStart, yStart), eventState.getG0(), 0.0);
        assertEquals(true, eventState.isG0Positive());
    }

    @Test
    public void testEvaluateStep() {
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        StepInterpolator interpolator = new StepInterpolator();
        
        boolean result = eventState.evaluateStep(interpolator);
        
        // The result should be false since there are no events triggered
        // by the given step
        assertEquals(false, result);
    }

    @Test
    public void testGetEventTime() {
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        
        double eventTime = eventState.getEventTime();
        
        // The event time should be Double.NaN since there are no events triggered
        // by the current step
        assertEquals(Double.NaN, eventTime, 0.0);
    }

    @Test
    public void testStepAccepted() {
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        double t = 1.0;
        double[] y = {2.0, 3.0, 4.0};
        
        eventState.stepAccepted(t, y);
        
        // Check if the values are updated correctly after accepting the step
        assertEquals(t, eventState.getT0(), 0.0);
        assertEquals(g(t, y), eventState.getG0(), 0.0);
        assertEquals(true, eventState.isG0Positive());
        // Check if the next action is correct after accepting the step
        assertEquals(EventHandler.CONTINUE, eventState.getNextAction());
    }

    @Test
    public void testStop() {
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        
        boolean stop = eventState.stop();
        
        // The stop value should be false by default
        assertEquals(false, stop);
    }

    @Test
    public void testReset() {
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount);
        double t = 1.0;
        double[] y = {2.0, 3.0, 4.0};
        
        eventState.reset(t, y);
        
        // Check if the pending event is reset correctly
        assertEquals(false, eventState.isPendingEvent());
        assertEquals(Double.NaN, eventState.getPendingEventTime(), 0.0);
        
        // Check if the reset actions are correct
        boolean resetDerivatives = (EventHandler.RESET_DERIVATIVES == eventState.getNextAction());
        boolean resetState = (EventHandler.RESET_STATE == eventState.getNextAction());
        assertEquals(true, resetDerivatives || resetState);
    }

}
