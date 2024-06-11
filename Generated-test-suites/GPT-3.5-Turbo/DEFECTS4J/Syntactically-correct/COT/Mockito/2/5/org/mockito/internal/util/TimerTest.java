
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.Timer;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(5000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(5000);
        timer.start();
        long startTime = timer.getStartTime();
        assertEquals(System.currentTimeMillis(), startTime, 100); 
    }
}
