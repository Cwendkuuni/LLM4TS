package net.sf.javaml.core;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;

public class SparseInstanceTest {
    
    @Test
    public void testSetNoAttributes() {
        // Test setNoAttributes method
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstance() {
        // Test SparseInstance constructor
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testSparseInstanceWithNoAttributes() {
        // Test SparseInstance constructor with noAttributes parameter
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testSparseInstanceWithDefaultValue() {
        // Test SparseInstance constructor with defaultValue parameter
        SparseInstance instance = new SparseInstance(5, 0.0);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.get(0), 0.0);
    }
    
    @Test
    public void testSparseInstanceWithClassValue() {
        // Test SparseInstance constructor with classValue parameter
        SparseInstance instance = new SparseInstance(5, "class");
        assertEquals(5, instance.noAttributes());
        assertEquals("class", instance.classValue());
    }
    
    @Test
    public void testSparseInstanceWithAllParameters() {
        // Test SparseInstance constructor with all parameters
        SparseInstance instance = new SparseInstance(5, 0.0, "class");
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.get(0), 0.0);
        assertEquals("class", instance.classValue());
    }
    
    @Test
    public void testInitiate() {
        // Test private initiate method
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 0.0, 2.0};
        instance.initiate(datavector);
        assertEquals(3, instance.size());
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(2), 0.0);
    }
    
    @Test
    public void testValue() {
        // Test value method
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        double val = instance.value(0);
        assertEquals(1.0, val, 0.0);
    }
    
    @Test
    public void testClear() {
        // Test clear method
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        // Test containsKey method
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
    }
    
    @Test
    public void testContainsValue() {
        // Test containsValue method
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(2.0));
    }
    
    @Test
    public void testEntrySet() {
        // Test entrySet method
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        assertEquals(1, entries.size());
        Map.Entry<Integer, Double> entry = entries.iterator().next();
        assertEquals(0, entry.getKey().intValue());
        assertEquals(1.0, entry.getValue(), 0.0);
    }
    
    @Test
    public void testGet() {
        // Test get method
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        double val = instance.get(0);
        assertEquals(1.0, val, 0.0);
        val = instance.get(1);
        assertEquals(0.0, val, 0.0);
    }
    
    @Test
    public void testIsEmpty() {
        // Test isEmpty method
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
        instance.put(0, 1.0);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        // Test keySet method
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        TreeSet<Integer> keys = instance.keySet();
        assertEquals(1, keys.size());
        assertEquals(0, keys.first().intValue());
    }
    
    @Test
    public void testPut() {
        // Test put method
        SparseInstance instance = new SparseInstance();
        Double oldVal = instance.put(0, 1.0);
        assertNull(oldVal);
        oldVal = instance.put(0, 2.0);
        assertEquals(1.0, oldVal, 0.0);
        assertEquals(2.0, instance.get(0), 0.0);
    }
    
    @Test
    public void testPutAll() {
        // Test putAll method
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> values = new HashMap<>();
        values.put(0, 1.0);
        values.put(1, 2.0);
        instance.putAll(values);
        assertEquals(2, instance.size());
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
    }
    
    @Test
    public void testRemove() {
        // Test remove method
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        Double removedVal = instance.remove(0);
        assertEquals(1.0, removedVal, 0.0);
        assertFalse(instance.containsKey(0));
    }
    
    @Test
    public void testSize() {
        // Test size method
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.size());
        instance.put(0, 1.0);
        assertEquals(1, instance.size());
    }
    
    @Test
    public void testValues() {
        // Test values method
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        Collection<Double> values = instance.values();
        assertEquals(1, values.size());
        assertEquals(1.0, values.iterator().next(), 0.0);
    }
    
    @Test
    public void testNoAttributes() {
        // Test noAttributes method
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
        instance.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        // Test removeAttribute method
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttribute(1);
        assertEquals(2, instance.size());
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(3.0, instance.get(1), 0.0);
    }
    
    @Test
    public void testToString() {
        // Test toString method
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        String str = instance.toString();
        assertEquals("{0=1.0, 1=2.0;null}", str);
    }
    
    @Test
    public void testHashCode() {
        // Test hashCode method
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        int hashCode = instance.hashCode();
        assertEquals(961, hashCode);
    }
    
    @Test
    public void testEquals() {
        // Test equals method
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        
        SparseInstance instance3 = new SparseInstance();
        instance3.put(0, 2.0);
        
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    @Test
    public void testCopy() {
        // Test copy method
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.setClassValue("class");
        Instance copy = instance.copy();
        
        assertEquals(instance.noAttributes(), copy.noAttributes());
        assertEquals(instance.get(0), copy.get(0));
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        // Test removeAttributes method
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(1);
        instance.removeAttributes(indices);
        
        assertEquals(1, instance.size());
        assertEquals(3.0, instance.get(0), 0.0);
    }
    
}