import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(5000);
    }

    @Test
    public void testIsCountingTrue() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingFalseAfterDuration() throws InterruptedException {
        timer.start();
        Thread.sleep(6000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingFalseWithoutStart() {
        assertFalse(timer.isCounting());
    }

}