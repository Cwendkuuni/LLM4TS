import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testConstructor() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(instance.size(), 3);
        assertEquals(instance.value(1), 2.0, 0.0001);
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        
        assertEquals(instance.value(0), 0.0, 0.0001);
    }
    
    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        
        assertEquals(entrySet.size(), 3);
    }
    
    // Add more test cases for other methods
}