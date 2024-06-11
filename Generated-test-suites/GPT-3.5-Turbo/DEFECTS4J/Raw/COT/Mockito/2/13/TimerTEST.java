
import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        
        // Test when timer has just started
        timer.start();
        Assert.assertTrue(timer.isCounting());
        
        // Test when timer has completed
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        
        // Test when timer starts
        timer.start();
        Assert.assertNotEquals(-1, timer.getStartTime());
    }

}
