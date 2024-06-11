import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.Timer;

import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setup() {
        timer = new Timer(1000); // Typical use case: setting a duration of 1000 milliseconds
    }

    @Test
    public void testIsCounting_WhenTimerNotStarted_ReturnsFalse() {
        assertFalse(timer.isCounting()); // Typical use case: timer not started yet, expected false
    }

    @Test
    public void testIsCounting_WhenTimerStartedButNotExpired_ReturnsTrue() {
        timer.start(); // Typical use case: timer started
        assertTrue(timer.isCounting()); // Typical use case: timer started but not expired, expected true
    }

    @Test
    public void testIsCounting_WhenTimerExpired_ReturnsFalse() throws InterruptedException {
        timer.start(); // Typical use case: timer started
        Thread.sleep(1200); // Edge case: wait longer than the timer duration (1000 milliseconds)
        assertFalse(timer.isCounting()); // Edge case: timer expired, expected false
    }

    @Test
    public void testStart_WhenTimerNotStarted_StartTimeUpdated() {
        assertEquals(-1, timer.startTime); // Typical use case: timer not started yet, initial value is -1

        timer.start(); // Typical use case: timer started

        assertNotEquals(-1, timer.startTime); // Typical use case: timer started, startTime updated
    }

}