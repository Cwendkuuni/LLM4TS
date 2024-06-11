import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {
    
    // Test setNoAttributes method
    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }
    
    // Test default constructor
    @Test
    public void testDefaultConstructor() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
    }
    
    // Test constructor with noAttributes parameter
    @Test
    public void testConstructorWithNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
    }
    
    // Test constructor with noAttributes and defaultValue parameters
    @Test
    public void testConstructorWithNoAttributesAndDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.0001);
    }
    
    // Test constructor with noAttributes, defaultValue, and classValue parameters
    @Test
    public void testConstructorWithAllParameters() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 0.0, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.0001);
        assertEquals(classValue, instance.classValue());
    }
    
    // Test constructor with data vector
    @Test
    public void testConstructorWithDataVector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(datavector.length, instance.noAttributes());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], instance.value(i), 0.0001);
        }
    }
    
    // Test constructor with data vector, defaultValue, and classValue parameters
    @Test
    public void testConstructorWithAllParametersWithDataVector() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, 0.0, classValue);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.0001);
        assertEquals(classValue, instance.classValue());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], instance.value(i), 0.0001);
        }
    }
    
    // Test initiate method
    @Test
    public void testInitiate() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance();
        instance.initiate(datavector);
        assertEquals(datavector.length, instance.noAttributes());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], instance.value(i), 0.0001);
        }
    }
    
    // Test value method
    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
    }
    
    // Test clear method
    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.clear();
        assertEquals(0, instance.size());
        assertTrue(instance.isEmpty());
    }
    
    // Test containsKey method
    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }
    
    // Test containsValue method
    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(3.0));
    }
    
    // Test entrySet method
    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        assertEquals(2, entries.size());
        for (Map.Entry<Integer, Double> entry : entries) {
            assertTrue(instance.containsKey(entry.getKey()));
            assertTrue(instance.containsValue(entry.getValue()));
        }
    }
    
    // Test get method
    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(instance.defaultValue, instance.get(2), 0.0001);
    }
    
    // Test isEmpty method
    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
        instance.put(0, 1.0);
        assertFalse(instance.isEmpty());
    }
    
    // Test keySet method
    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        TreeSet<Integer> keys = instance.keySet();
        for (int key : keys) {
            assertTrue(instance.containsKey(key));
        }
    }
    
    // Test put method
    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
    }
    
    // Test putAll method
    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
    }
    
    // Test remove method
    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(1.0, instance.remove(0), 0.0001);
        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsValue(1.0));
    }
    
    // Test size method
    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.size());
        instance.put(0, 1.0);
        assertEquals(1, instance.size());
    }
    
    // Test values method
    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Collection<Double> values = instance.values();
        assertEquals(2, values.size());
        for (double value : values) {
            assertTrue(instance.containsValue(value));
        }
    }
    
    // Test noAttributes method
    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        instance.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
    }
    
    // Test removeAttribute method
    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.removeAttribute(0);
        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsValue(1.0));
        assertEquals(1, instance.noAttributes());
    }
    
    // Test toString method
    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        String expected = "{" + instance.data.toString() + ";" + instance.classValue() + "}";
        assertEquals(expected, instance.toString());
    }
    
    // Test hashCode method
    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    // Test equals method
    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);
        assertTrue(instance1.equals(instance2));
    }
    
    // Test copy method
    @Test
    public void testCopy() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        SparseInstance copy = (SparseInstance) instance.copy();
        assertEquals(instance, copy);
    }
    
    // Test removeAttributes method
    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        instance.removeAttributes(indices);
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsValue(2.0));
        assertEquals(1, instance.noAttributes());
    }
}