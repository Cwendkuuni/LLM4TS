import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    /**
     * Test for isCounting() method.
     * Should return true if timer is still counting down, false if timer has expired.
     */
    @Test
    public void testIsCounting() {
        Timer timer = new Timer(5000);
        timer.start();
        
        assertTrue(timer.isCounting());

        try {
            Thread.sleep(6000); // Sleep for more than the timer duration
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(timer.isCounting());
    }

    /**
     * Test for start() method.
     * Should update the start time of the timer.
     */
    @Test
    public void testStart() {
        Timer timer = new Timer(5000);

        timer.start();
        long startTime = timer.getStartTime();
        assertTrue(startTime > 0);
    }
    
    /**
     * Test for start() method with negative duration.
     * Should throw an IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStartWithNegativeDuration() {
        Timer timer = new Timer(-5000);
        timer.start();
    }
}