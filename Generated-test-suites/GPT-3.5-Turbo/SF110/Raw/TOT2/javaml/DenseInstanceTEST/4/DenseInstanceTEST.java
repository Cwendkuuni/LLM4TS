import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DenseInstanceTest {
    
    private DenseInstance instance;
    
    @Test
    public void testValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes);
        
        assertEquals(2.0, instance.value(1), 0.001);
    }
    
    @Test
    public void testClear() {
        double[] attributes = {1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes);
        instance.clear();
        
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, instance.attributes, 0.001);
    }
    
    @Test
    public void testContainsKey() {
        double[] attributes = {1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes);
        
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
    }
    
    // Test cases for other methods...
    
}