import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {
    
    // Test case for the constructor that takes double array as input
    @Test
    public void testConstructorWithDoubleArray() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertNotNull(instance);
        assertArrayEquals(att, instance.attributes, 0.0);
    }
    
    // Test case for the constructor that takes double array and classValue as input
    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "test";
        DenseInstance instance = new DenseInstance(att, classValue);
        
        assertNotNull(instance);
        assertArrayEquals(att, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }
    
    // Test case for the value() method
    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }
    
    // Test case for the clear() method
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.clear();
        assertArrayEquals(new double[3], instance.attributes, 0.0);
    }
    
    // Test case for the containsKey() method with valid key
    @Test
    public void testContainsKeyWithValidKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
    }
    
    // Test case for the containsKey() method with invalid key
    @Test
    public void testContainsKeyWithInvalidKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
        assertFalse(instance.containsKey("test"));
    }
    
    // Test case for the containsValue() method with valid value
    @Test
    public void testContainsValueWithValidValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
    }
    
    // Test case for the containsValue() method with invalid value
    @Test
    public void testContainsValueWithInvalidValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertFalse(instance.containsValue(0.0));
        assertFalse(instance.containsValue(4.0));
        assertFalse(instance.containsValue("test"));
    }
    
    // Test case for the entrySet() method
    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Map.Entry<Integer, Double>> expectedSet = new HashSet<>();
        expectedSet.add(Map.entry(0, 1.0));
        expectedSet.add(Map.entry(1, 2.0));
        expectedSet.add(Map.entry(2, 3.0));
        
        assertEquals(expectedSet, instance.entrySet());
    }
    
    // Test case for the get() method
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
    }
    
    // Test case for the isEmpty() method
    @Test
    public void testIsEmpty() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertFalse(instance.isEmpty());
    }
    
    // Test case for the keySet() method
    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        SortedSet<Integer> expectedSet = new TreeSet<>();
        expectedSet.add(0);
        expectedSet.add(1);
        expectedSet.add(2);
        
        assertEquals(expectedSet, instance.keySet());
    }
    
    // Test case for the put() method
    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(1.0, instance.put(0, 10.0), 0.0);
        assertEquals(10.0, instance.value(0), 0.0);
    }
    
    // Test case for the putAll() method
    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 10.0);
        map.put(1, 20.0);
        map.put(2, 30.0);
        
        instance.putAll(map);
        
        assertEquals(10.0, instance.value(0), 0.0);
        assertEquals(20.0, instance.value(1), 0.0);
        assertEquals(30.0, instance.value(2), 0.0);
    }
    
    // Test case for the remove() method
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.remove(0);
    }
    
    // Test case for the size() method
    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(3, instance.size());
    }
    
    // Test case for the values() method
    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Collection<Double> expectedValues = new ArrayList<>();
        expectedValues.add(1.0);
        expectedValues.add(2.0);
        expectedValues.add(3.0);
        
        assertEquals(expectedValues, instance.values());
    }
    
    // Test case for the noAttributes() method
    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(3, instance.noAttributes());
    }
    
    // Test case for the toString() method
    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
    }
    
    // Test case for the removeAttribute() method
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(1);
        
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.0);
    }
    
    // Test case for the hashCode() method
    @Test
    public void testHashCode() {
        double[] att1 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(att2);
        
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    // Test case for the equals() method
    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(att2);
        
        assertEquals(instance1, instance2);
    }
    
    // Test case for the copy() method
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        DenseInstance copy = (DenseInstance) instance.copy();
        
        assertArrayEquals(att, copy.attributes, 0.0);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    // Test case for the removeAttributes() method
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        
        instance.removeAttributes(indices);
        
        assertArrayEquals(new double[]{2.0}, instance.attributes, 0.0);
    }
}