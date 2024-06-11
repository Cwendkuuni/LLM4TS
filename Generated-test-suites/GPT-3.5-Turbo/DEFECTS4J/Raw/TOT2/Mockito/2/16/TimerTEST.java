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
    public void testIsNotCounting() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStartNotEqualsZero() {
        Timer timer = new Timer(1000);
        timer.start();
        assertNotEquals(0, timer.startTime);
    }

    @Test(expected = AssertionError.class)
    public void testIsCountingWithoutStart() {
        Timer timer = new Timer(1000);
        timer.isCounting();
    }

}