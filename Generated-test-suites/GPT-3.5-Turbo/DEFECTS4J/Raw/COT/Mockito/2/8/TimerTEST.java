import static org.junit.Assert.*;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        
        // Test when timer is not started
        assertFalse(timer.isCounting());
        
        // Test when timer is started
        timer.start();
        assertTrue(timer.isCounting());
    }
    
    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        
        // Test starting timer
        timer.start();
        assertTrue(timer.isCounting());
    }

}