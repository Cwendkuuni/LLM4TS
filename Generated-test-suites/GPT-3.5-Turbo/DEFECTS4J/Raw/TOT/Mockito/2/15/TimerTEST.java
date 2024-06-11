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
    public void testIsCounting_StartTimeNotSet_ReturnsFalse() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_StartTimeSetWithDurationElapsed_ReturnsFalse() {
        timer.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_StartTimeSetWithDurationNotElapsed_ReturnsTrue() {
        timer.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_StartTimeSetToCurrentTime() {
        timer.start();
        assertTrue(System.currentTimeMillis() - timer.startTime < 10);
    }
}