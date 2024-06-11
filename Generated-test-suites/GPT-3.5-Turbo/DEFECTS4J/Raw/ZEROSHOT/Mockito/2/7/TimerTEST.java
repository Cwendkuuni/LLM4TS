import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(5000);

        timer.start();
        Assert.assertTrue(timer.isCounting());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(timer.isCounting());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(5000);

        timer.start();
        Assert.assertNotEquals(-1, timer.getStartTime());
    }

}