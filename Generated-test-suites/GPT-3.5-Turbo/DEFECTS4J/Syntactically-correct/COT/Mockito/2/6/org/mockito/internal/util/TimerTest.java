import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        // Test case 1: Timer is still counting down
        Timer timer = new Timer(5000);
        timer.start();
        Assert.assertTrue(timer.isCounting());

        // Test case 2: Timer has finished counting down
        Timer timer2 = new Timer(1000);
        timer2.start();
        try {
            Thread.sleep(2000); // Wait for timer to finish counting down
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertFalse(timer2.isCounting());
    }

    @Test
    public void testStart() {
        // Test case 1: Timer starts successfully
        Timer timer = new Timer(5000);
        timer.start();
        Assert.assertTrue(timer.isCounting());

        // Test case 2: Timer is restarted
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }
}