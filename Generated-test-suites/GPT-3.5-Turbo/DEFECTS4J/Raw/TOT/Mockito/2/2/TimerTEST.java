import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        // Test when the timer has just started
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
        
        // Test when the timer has been counting for less than the specified duration
        Timer timer2 = new Timer(5000);
        timer2.start();
        assertTrue(timer2.isCounting());
        
        // Test when the timer has reached the specified duration
        Timer timer3 = new Timer(10000);
        timer3.start();
        assertTrue(timer3.isCounting());
        
        // Test when the timer has exceeded the specified duration
        Timer timer4 = new Timer(500);
        timer4.start();
        assertFalse(timer4.isCounting());
    }

    @Test
    public void testStart() {
        // Test if the start time is properly set when starting the timer
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = timer.getStartTime();
        assertTrue(startTime >= 0);
        assertTrue(startTime <= System.currentTimeMillis());
    }
}