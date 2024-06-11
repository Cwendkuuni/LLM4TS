

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {
    
    @Test
    public void testValue() {
        // Test the value() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
        assertEquals(4.0, instance.value(3), 0.0001);
    }
    
    @Test
    public void testClear() {
        // Test the clear() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        double[] clearedAtt = instance.attributes;
        for (double value : clearedAtt) {
            assertEquals(0.0, value, 0.0001);
        }
    }
    
    @Test
    public void testContainsKey() {
        // Test the containsKey() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(3));
        assertFalse(instance.containsKey(4));
        assertFalse(instance.containsKey(-1));
    }
    
    @Test
    public void testContainsValue() {
        // Test the containsValue() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsValue(1));
        assertTrue(instance.containsValue(3));
        assertTrue(instance.containsValue(2.99999));
        assertFalse(instance.containsValue(5));
    }
    
    @Test
    public void testEntrySet() {
        // Test the entrySet() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(4, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(att[key], value, 0.0001);
        }
    }
    
    @Test
    public void testGet() {
        // Test the get() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(4.0, instance.get(3), 0.0001);
    }
    
    @Test
    public void testIsEmpty() {
        // Test the isEmpty() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        // Test the keySet() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(4, keySet.size());
        int index = 0;
        for (int key : keySet) {
            assertEquals(index++, key);
        }
    }
    
    @Test
    public void testPut() {
        // Test the put() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        instance.put(0, 5.0);
        assertEquals(5.0, instance.attributes[0], 0.0001);
    }
    
    @Test
    public void testPutAll() {
        // Test the putAll() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 5.0);
        map.put(2, 8.0);
        
        instance.putAll(map);
        assertEquals(5.0, instance.attributes[0], 0.0001);
        assertEquals(8.0, instance.attributes[2], 0.0001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        // Test the remove() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        // Test the size() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(4, instance.size());
    }
    
    @Test
    public void testValues() {
        // Test the values() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        Collection<Double> values = instance.values();
        assertEquals(4, values.size());
        int index = 0;
        for (double value : values) {
            assertEquals(att[index++], value, 0.0001);
        }
    }
    
    @Test
    public void testNoAttributes() {
        // Test the noAttributes() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(4, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        // Test the toString() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        String expected = "{[1.0, 2.0, 3.0, 4.0];null}";
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        // Test the removeAttribute() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(0);
        double[] expected = {2, 3, 4};
        assertArrayEquals(expected, instance.attributes, 0.0001);
    }
    
    @Test
    public void testHashCode() {
        // Test the hashCode() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(att);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        // Test the equals() method
        double[] att1 = {1, 2, 3, 4};
        double[] att2 = {1, 2, 3, 4};
        double[] att3 = {1, 2, 3, 5};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        DenseInstance instance3 = new DenseInstance(att3);
        
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    @Test
    public void testCopy() {
        // Test the copy() method
        double[] att = {1, 2, 3, 4};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = instance1.copy();
        
        assertNotSame(instance1, instance2);
        assertArrayEquals(att, instance2.attributes, 0.0001);
        assertEquals(instance1.classValue(), instance2.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        // Test the removeAttributes() method
        double[] att = {1, 2, 3, 4};
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(3);
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttributes(indices);
        
        double[] expected = {2, 3};
        assertArrayEquals(expected, instance.attributes, 0.0001);
    }
}

