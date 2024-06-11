import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingAfterDuration() throws InterruptedException {
        Timer timer = new Timer(1000);
        timer.start();
        Thread.sleep(1001);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }
    
    // Additional test cases for potential edge cases and exceptions
    
    @Test
    public void testIsCountingEdgeCase() {
        Timer timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testIsCountingWithNegativeDuration() {
        Timer timer = new Timer(-1000);
        timer.start();
        assertFalse(timer.isCounting());
    }
    
    @Test(expected = AssertionError.class)
    public void testIsCountingWithoutStart() {
        Timer timer = new Timer(1000);
        timer.isCounting();
    }
    
    // Complete JUnit 4 test file
    
    // 
    
    @Test
    public void testIsCounting() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingAfterDuration() throws InterruptedException {
        Timer timer = new Timer(1000);
        timer.start();
        Thread.sleep(1001);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }
    
    @Test
    public void testIsCountingEdgeCase() {
        Timer timer = new Timer(0);
        timer.start();
        assertFalse(timer.isCounting());
    }
    
    @Test
    public void testIsCountingWithNegativeDuration() {
        Timer timer = new Timer(-1000);
        timer.start();
        assertFalse(timer.isCounting());
    }
    
    @Test(expected = AssertionError.class)
    public void testIsCountingWithoutStart() {
        Timer timer = new Timer(1000);
        timer.isCounting();
    }
    
    // 
}