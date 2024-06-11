
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {
    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        
        assertFalse(timer.isCounting());
        
        timer.start();
        
        assertTrue(timer.isCounting());
        
        try {
            // Sleep for 2 seconds to allow timer to finish counting
            Thread.sleep(2000);
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
        
        try {
            // Sleep for 1 second to allow timer to finish counting
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertFalse(timer.isCounting());
    }
}
