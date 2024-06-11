
import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        // Test when the timer is counting
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertTrue(timer.isCounting());

        // Test when the timer has finished counting
        timer = new Timer(1000);
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
        Assert.assertNotEquals(-1, timer.startTime);
    }

}

