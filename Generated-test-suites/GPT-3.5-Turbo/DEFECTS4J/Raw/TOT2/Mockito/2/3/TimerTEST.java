import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue("Timer should be counting", timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertNotEquals("Start time should be set", -1, timer.startTime);
    }

    @Test
    public void testIsCountingEdgeCase() {
        Timer timer = new Timer(1000);
        timer.startTime = System.currentTimeMillis();
        assertTrue("Timer should be counting", timer.isCounting());
    }

    @Test
    public void testIsCountingFalse() {
        Timer timer = new Timer(1000);
        timer.startTime = 0;
        assertFalse("Timer should not be counting", timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCountingException() {
        Timer timer = new Timer(1000);
        timer.isCounting();
    }

}