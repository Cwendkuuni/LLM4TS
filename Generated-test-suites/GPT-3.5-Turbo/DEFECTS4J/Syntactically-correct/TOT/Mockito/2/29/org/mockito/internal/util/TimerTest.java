import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {
    
    @Test
    public void testIsCounting_WhenTimerNotStarted_ReturnsFalse() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testIsCounting_WhenTimerStartedAndStillCounting_ReturnsTrue() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }
    
    @Test
    public void testIsCounting_WhenTimerStartedAndElapsed_ReturnsFalse() throws InterruptedException {
        Timer timer = new Timer(1000);
        timer.start();
        Thread.sleep(1500);
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testStart_WhenTimerNotStarted_StartsTimer() {
        Timer timer = new Timer(1000);
        timer.start();
        assertNotEquals(-1, timer.getStartTime());
    }

}