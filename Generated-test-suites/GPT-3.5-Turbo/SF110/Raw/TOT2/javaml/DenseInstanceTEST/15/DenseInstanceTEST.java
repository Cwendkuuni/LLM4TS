import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

    private DenseInstance instance;
    
    @Before
    public void setUp() {
        double[] attributes = {1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes);
    }
    
    @Test
    public void testValue() {
        assertEquals(2.0, instance.value(1), 0);
    }
    
    @Test
    public void testClear() {
        instance.clear();
        assertEquals(0.0, instance.value(0), 0);
    }
    
    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        assertEquals(3, entries.size());
    }
    
    @Test
    public void testGet() {
        assertEquals(2.0, instance.get(1), 0);
    }
    
    @Test
    public void testKeySet() {
        SortedSet<Integer> keys = instance.keySet();
        assertEquals(3, keys.size());
    }
    
    @Test
    public void testPut() {
        double value = instance.put(1, 5.0);
        assertEquals(2.0, value, 0);
        assertEquals(5.0, instance.value(1), 0);
    }
    
    @Test
    public void testRemove() {
        try {
            instance.remove(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSize() {
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
    }
    
    @Test
    public void testNoAttributes() {
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(1);
        assertEquals(2.0, instance.value(1), 0);
    }
    
    @Test
    public void testHashCode() {
        assertEquals(1571227820, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance other = new DenseInstance(attributes);
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testCopy() {
        DenseInstance copy = (DenseInstance) instance.copy();
        assertEquals(instance.toString(), copy.toString());
    }
    
    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        instance.removeAttributes(indices);
        assertEquals(2.0, instance.value(1), 0);
    }

}