import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000); // Setting the duration to 1 second for testing purposes
    }

    @Test
    public void testIsCounting() {
        timer.start(); // Start the timer
        Assert.assertTrue(timer.isCounting()); // Timer should be counting down
    }

    @Test
    public void testStart() {
        timer.start(); // Start the timer
        Assert.assertNotEquals(-1, timer.startTime); // Start time should have been initialized
    }

    @Test
    public void testIsCountingEdgeCase() {
        timer.start(); // Start the timer
        try {
            Thread.sleep(1000); // Sleep for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertFalse(timer.isCounting()); // Timer should have finished counting down
    }

    @Test
    public void testStartEdgeCase() {
        timer.start(); // Start the timer
        long previousStartTime = timer.startTime;
        try {
            Thread.sleep(1000); // Sleep for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.start(); // Restart the timer
        Assert.assertNotEquals(previousStartTime, timer.startTime); // Start time should have been updated
    }
}