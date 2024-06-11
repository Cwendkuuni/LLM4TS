import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @Test
    public void testIsNotCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertNotEquals(-1, timer.getStartTime());
    }

}