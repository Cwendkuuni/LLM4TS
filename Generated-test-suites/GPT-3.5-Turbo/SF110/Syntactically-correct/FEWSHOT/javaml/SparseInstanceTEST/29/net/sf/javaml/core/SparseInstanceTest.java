package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {
    
    private SparseInstance instance;
    
    @Before
    public void setUp() {
        instance = new SparseInstance();
    }
    
    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }
    
    @Test
    public void testConstructorNoArguments() {
        assertEquals(-1, instance.noAttributes());
    }
    
    @Test
    public void testConstructorWithNoAttributesArgument() {
        SparseInstance instance = new SparseInstance(10);
        assertEquals(10, instance.noAttributes());
    }
    
    @Test
    public void testConstructorWithNoAttributesAndDefaultValueArguments() {
        SparseInstance instance = new SparseInstance(10, 0.0);
        assertEquals(10, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.0);
    }
    
    @Test
    public void testConstructorWithNoAttributesDefaultValueAndClassValueArguments() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(10, 0.0, classValue);
        assertEquals(10, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testConstructorWithDataVector() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testConstructorWithDataVectorAndDefaultValue() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector, 0.0);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.0);
    }
    
    @Test
    public void testConstructorWithDataVectorDefaultValueAndClassValue() {
        double[] dataVector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(dataVector, 0.0, classValue);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testValue() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }
    
    @Test
    public void testClear() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        instance.put(0, 1.0);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
    }
    
    @Test
    public void testContainsValue() {
        instance.put(0, 1.0);
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(2.0));
    }
    
    @Test
    public void testEntrySet() {
        instance.put(0, 1.0);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertFalse(entrySet.isEmpty());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(0, entry.getKey().intValue());
            assertEquals(1.0, entry.getValue(), 0.0);
        }
    }
    
    @Test
    public void testGet() {
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(instance.defaultValue, instance.get(1), 0.0);
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
        instance.put(0, 1.0);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        instance.put(0, 1.0);
        Set<Integer> keySet = instance.keySet();
        assertFalse(keySet.isEmpty());
        assertTrue(keySet.contains(0));
    }
    
    @Test
    public void testPut() {
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.0);
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(2, instance.size());
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
    }
    
    @Test
    public void testRemove() {
        instance.put(0, 1.0);
        Double removeValue = instance.remove(0);
        assertEquals(1.0, removeValue, 0.0);
        assertFalse(instance.containsKey(0));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, instance.size());
        instance.put(0, 1.0);
        assertEquals(1, instance.size());
    }
    
    @Test
    public void testValues() {
        instance.put(0, 1.0);
        Collection<Double> values = instance.values();
        assertFalse(values.isEmpty());
        assertTrue(values.contains(1.0));
    }
    
    @Test
    public void testNoAttributes() {
        instance.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttribute(1);
        assertEquals(2, instance.size());
        assertFalse(instance.containsKey(1));
    }
    
    @Test
    public void testToString() {
        instance.put(0, 1.0);
        assertEquals("{0=1.0;null}", instance.toString());
    }
    
    @Test
    public void testHashCode() {
        instance.put(0, 1.0);
        assertEquals(31 * 1 + 0, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        instance.put(0, 1.0);
        SparseInstance other = new SparseInstance();
        other.put(0, 1.0);
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testCopy() {
        instance.put(0, 1.0);
        instance.setClassValue(new Object());
        SparseInstance copy = instance.copy();
        assertEquals(instance, copy);
    }
    
    @Test
    public void testRemoveAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        assertEquals(1, instance.size());
        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsKey(2));
        assertTrue(instance.containsKey(1));
    }
}