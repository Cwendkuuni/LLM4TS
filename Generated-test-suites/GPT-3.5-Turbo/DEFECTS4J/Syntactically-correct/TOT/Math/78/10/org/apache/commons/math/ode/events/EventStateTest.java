import static org.junit.Assert.*;
import org.junit.Test;

public class EventStateTest {

    @Test
    public void testReinitializeBegin() {
        // Creating object of EventState class
        EventState eventState = new EventState();

        // Initializing the values
        double tStart = 0;
        double[] yStart = {2, 3, 4, 5};

        // Calling the reinitializeBegin() method
        eventState.reinitializeBegin(tStart, yStart);

        // Testing the values after initialization
        assertEquals(tStart, eventState.getT0(), 0);
        assertArrayEquals(yStart, eventState.getYStart(), 0);
    }

    @Test
    public void testEvaluateStep() {
        // Creating object of EventState class
        EventState eventState = new EventState();

        // Initializing the values
        StepInterpolator interpolator = new StepInterpolator();
        interpolator.setForward(true);
        interpolator.setCurrentTime(0);

        // Calling the evaluateStep() method
        boolean result = eventState.evaluateStep(interpolator);

        // Testing the result
        assertFalse(result);
    }

    @Test
    public void testGetEventTime() {
        // Creating object of EventState class
        EventState eventState = new EventState();

        // Initializing the values
        double expectedEventTime = 2.5;
        eventState.setPendingEventTime(expectedEventTime);

        // Calling the getEventTime() method
        double result = eventState.getEventTime();

        // Testing the result
        assertEquals(expectedEventTime, result, 0);
    }

    @Test
    public void testStepAccepted() {
        // Creating object of EventState class
        EventState eventState = new EventState();

        // Initializing the values
        double t = 2.5;
        double[] y = {1, 2, 3, 4};

        // Calling the stepAccepted() method
        eventState.stepAccepted(t, y);

        // Testing the values after step acceptance
        assertEquals(t, eventState.getT0(), 0);
        assertArrayEquals(y, eventState.getY(), 0);
    }

    @Test
    public void testStop() {
        // Creating object of EventState class
        EventState eventState = new EventState();

        // Initializing the value of nextAction
        eventState.setNextAction(EventHandler.STOP);

        // Calling the stop() method
        boolean result = eventState.stop();

        // Testing the result
        assertTrue(result);
    }

    @Test
    public void testReset() {
        // Creating object of EventState class
        EventState eventState = new EventState();

        // Initializing the values
        double t = 2.5;
        double[] y = {1, 2, 3, 4};
        eventState.setNextAction(EventHandler.RESET_STATE);

        // Calling the reset() method
        boolean result = eventState.reset(t, y);

        // Testing the result
        assertTrue(result);
    }

}