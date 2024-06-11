
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
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertNotEquals(-1, timer.getStartTime());
    }

    @Test
    public void testIsCountingEdgeCase() {
        Timer timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testStartException() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

}
