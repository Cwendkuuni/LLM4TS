
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting()); // timer not started

        timer.start();
        assertTrue(timer.isCounting()); // timer started

        try {
            Thread.sleep(1500); // Wait for timer to expire
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(timer.isCounting()); // timer expired
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertNotEquals(-1, timer.startTime);
    }

}
