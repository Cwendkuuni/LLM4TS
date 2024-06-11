import org.junit.*;
import static org.junit.Assert.*;

public class MathUtilsTest {
    
    @Test
    public void testHashForDouble() {
        double value = 10.5;
        int expectedHash = new Double(value).hashCode();
        int actualHash = MathUtils.hash(value);
        
        assertEquals(expectedHash, actualHash);
    }
    
    @Test
    public void testHashForDoubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expectedHash = Arrays.hashCode(value);
        int actualHash = MathUtils.hash(value);
        
        assertEquals(expectedHash, actualHash);
    }

    // Add test cases for other public methods
    
    // Add test cases for edge cases and exception handling scenarios
    
}