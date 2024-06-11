
import static org.junit.Assert.*;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
        // Testing edge case when the timer count down is exactly equal to the duration provided
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(5000);
        timer.start();
        // Testing that the startTime is greater than -1 after starting the timer
        assertTrue(timer.startTime > -1);
    }

}
