
import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        // Test value at position 0
        double expectedValue = 1.0;
        assertEquals(expectedValue, denseInstance.value(0), 0.001);
        
        // Test value at position 1
        expectedValue = 2.0;
        assertEquals(expectedValue, denseInstance.value(1), 0.001);
        
        // Test value at position 2
        expectedValue = 3.0;
        assertEquals(expectedValue, denseInstance.value(2), 0.001);
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        // Clear the instance
        denseInstance.clear();
        
        double expectedValue = 0.0;
        
        // Check if all values are set to 0.0
        for (int i = 0; i < att.length; i++) {
            assertEquals(expectedValue, denseInstance.value(i), 0.001);
        }
    }
    
    // Add test cases for other methods here

}

