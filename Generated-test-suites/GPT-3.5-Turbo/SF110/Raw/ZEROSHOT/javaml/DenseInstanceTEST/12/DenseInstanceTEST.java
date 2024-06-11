import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

    private DenseInstance instance;
    private double[] attributes;
    
    @Before
    public void setUp() {
        attributes = new double[]{1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(instance);
    }
    
    @Test
    public void testValue() {
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
    }
    
    @Test
    public void testClear() {
        instance.clear();
        
        for (double attribute : attributes) {
            assertEquals(0.0, attribute, 0.0001);
        }
    }
    
    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        
        assertFalse(instance.containsKey(3));
        assertFalse(instance.containsKey(-1));
    }
    
    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        
        assertFalse(instance.containsValue(0.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        
        assertEquals(attributes.length, entries.size());
        
        for (Map.Entry<Integer, Double> entry : entries) {
            int key = entry.getKey();
            double value = entry.getValue();
            
            assertEquals(attributes[key], value, 0.0001);
        }
    }
    
    @Test
    public void testGet() {
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(3.0, instance.get(2), 0.0001);
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SortedSet<Integer> keys = instance.keySet();
        
        assertEquals(attributes.length, keys.size());
        
        int index = 0;
        for (int key : keys) {
            assertEquals(index++, key);
        }
    }
    
    @Test
    public void testPut() {
        double oldValue = instance.put(0, 4.0);
        
        assertEquals(1.0, oldValue, 0.0001);
        assertEquals(4.0, instance.value(0), 0.0001);
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        instance.putAll(map);
        
        assertEquals(4.0, instance.value(0), 0.0001);
        assertEquals(5.0, instance.value(1), 0.0001);
        assertEquals(6.0, instance.value(2), 0.0001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        assertEquals(attributes.length, instance.size());
    }
    
    @Test
    public void testValues() {
        Collection<Double> values = instance.values();
        
        assertEquals(attributes.length, values.size());
        
        for (double value : values) {
            assertTrue(instance.containsValue(value));
        }
    }
    
    @Test
    public void testNoAttributes() {
        assertEquals(attributes.length, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        String expected = "{" + Arrays.toString(attributes) + ";" + instance.classValue() + "}";
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(0);
        
        assertEquals(2.0, instance.value(0), 0.0001);
        assertEquals(3.0, instance.value(1), 0.0001);
    }
    
    @Test
    public void testHashCode() {
        int expected = 31 * 1 + Arrays.hashCode(attributes);
        assertEquals(expected, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        DenseInstance other = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testCopy() {
        DenseInstance copy = (DenseInstance) instance.copy();
        
        assertEquals(instance.noAttributes(), copy.noAttributes());
        assertEquals(instance.classValue(), copy.classValue());
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.value(i), copy.value(i), 0.0001);
        }
    }
    
    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        
        instance.removeAttributes(indices);
        
        assertEquals(2, instance.noAttributes());
        assertEquals(2.0, instance.value(0), 0.0001);
        assertEquals(0.0, instance.value(1), 0.0001);
    }
}