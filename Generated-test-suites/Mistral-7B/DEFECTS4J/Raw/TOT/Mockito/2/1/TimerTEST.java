import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.Timer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCounting_withinDuration() {
        timer.start();
        long startTime = System.currentTimeMillis();

        // Wait a little less than the duration millis
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertTrue(timer.isCounting());
        assertTrue(elapsedTime < timer.durationMillis);
    }

    @Test
    public void testIsCounting_afterDuration() {
        timer.start();
        long startTime = System.currentTimeMillis();

        // Wait a little more than the duration millis
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertFalse(timer.isCounting());
        assertTrue(elapsedTime > timer.durationMillis);
    }
}