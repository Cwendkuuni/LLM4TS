import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();

        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingAfterDuration() throws InterruptedException {
        Timer timer = new Timer(1000);
        timer.start();
        Thread.sleep(1100);

        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();

        assertTrue(timer.isCounting());
    }

}