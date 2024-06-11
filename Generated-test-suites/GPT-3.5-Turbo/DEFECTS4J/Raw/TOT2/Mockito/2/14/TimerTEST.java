import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(5000);
    }

    @Test
    public void testIsCounting() {
        assertFalse(timer.isCounting());
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart() {
        timer.start();
        assertNotEquals(-1, timer.startTime);
    }

    @Test(expected = AssertionError.class)
    public void testIsCountingWithoutStart() {
        timer.isCounting();
    }

}