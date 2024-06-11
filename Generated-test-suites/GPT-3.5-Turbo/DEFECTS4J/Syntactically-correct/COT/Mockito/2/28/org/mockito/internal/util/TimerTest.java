
import org.junit.Assert;
import org.junit.Test;


public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(5000);
        timer.start();
        
        Assert.assertTrue(timer.isCounting());
    }
    
    @Test
    public void testStart() {
        Timer timer = new Timer(5000);
        timer.start();
        
        Assert.assertNotEquals(-1, timer.startTime);
    }
    
    @Test
    public void testIsCountingWhenNotStarted() {
        Timer timer = new Timer(5000);
        
        Assert.assertFalse(timer.isCounting());
    }
    
    @Test
    public void testIsCountingAfterDuration() throws InterruptedException {
        Timer timer = new Timer(2000);
        timer.start();
        
        Thread.sleep(3000);
        
        Assert.assertFalse(timer.isCounting());
    }

}
