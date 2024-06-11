import static org.junit.Assert.*;
import org.junit.Test;

public class TimerTest {

    private Timer timer;

    @Test
    public void testIsCountingWhenNotStarted() {
        timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenStarted() {
        timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingAfterDuration() {
        timer = new Timer(1000);
        timer.start();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStartSetsStartTime() {
        timer = new Timer(1000);
        timer.start();
        assertNotEquals(-1, timer.startTime);
    }
}