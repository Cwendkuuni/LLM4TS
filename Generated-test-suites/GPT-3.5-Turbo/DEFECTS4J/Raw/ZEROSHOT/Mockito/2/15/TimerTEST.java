import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000);
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
        long startTime = timer.getStartTime();

        assertTrue(startTime != -1);
        assertTrue(startTime <= System.currentTimeMillis());
    }

}