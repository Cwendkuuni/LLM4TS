import static org.junit.Assert.*;
import org.junit.Test;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.addPoint();
        
        // Assert that points vector has increased in size
        assertEquals(1, foxHuntFrame.points.size());
        
        // Assert that display.addPoint() has been called
        // ...
        
        // Add additional assertions as needed
        // ...
    }

    // Add additional test methods as needed

}