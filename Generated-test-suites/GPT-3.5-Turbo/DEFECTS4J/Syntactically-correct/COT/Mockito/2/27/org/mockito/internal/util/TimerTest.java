import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_ExpiredTime() {
        Timer timer = new Timer(1000);
        timer.start();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

}