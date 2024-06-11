import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenTimerNotStarted() {
        Timer timer = new Timer(5000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingAfterTimerExpiration() {
        Timer timer = new Timer(1000);
        timer.start();
        while(timer.isCounting()) {
            // Wait for timer to expire
        }
        assertFalse(timer.isCounting());
    }

}