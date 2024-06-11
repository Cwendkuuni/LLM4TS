import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(5000); // Test with 5 seconds duration
        timer.start(); // Start the timer
        assertTrue(timer.isCounting()); // Check if timer is counting

        try {
            Thread.sleep(1000); // Sleep for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(timer.isCounting()); // Check if timer is still counting

        try {
            Thread.sleep(5000); // Sleep for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(timer.isCounting()); // Check if timer has stopped counting
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(5000); // Test with 5 seconds duration
        timer.start(); // Start the timer
        assertTrue(timer.isCounting()); // Check if timer is counting
        assertTrue(timer.startTime != -1); // Check if startTime is updated
    }

}