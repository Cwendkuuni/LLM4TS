import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setup() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCountingWhenTimerNotStarted() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenTimerStarted() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenTimerCompleted() throws InterruptedException {
        timer.start();
        Thread.sleep(2000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        timer.start();
        assertNotNull(timer.startTime);
    }

}