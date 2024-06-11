
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        
        // Test when timer is not started yet
        assertFalse(timer.isCounting());
        
        // Test when timer is still counting down
        timer.start();
        assertTrue(timer.isCounting());
        
        // Test when timer has finished counting down
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        
        // Test if start time is set correctly
        timer.start();
        long startTime = timer.getStartTime();
        assertTrue(startTime >= System.currentTimeMillis() - 10 && startTime <= System.currentTimeMillis());
    }

}
