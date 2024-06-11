import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingEdgeCases() {
        Timer timer = new Timer(Long.MAX_VALUE); // Test for extremely long duration
        assertFalse(timer.isCounting());
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStartExceptionHandling() {
        Timer timer = new Timer(-100); // Test for negative duration
        try {
            timer.start();
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}