import org.junit.Test;
import org.junit.Assert;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertTrue(timer.isCounting()); // normal case
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertFalse(timer.isCounting()); // edge case: timer has elapsed
    }

}