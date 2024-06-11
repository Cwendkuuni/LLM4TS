package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class DenseInstanceTest {
    private DenseInstance denseInstance;
    private final double[] attributes = {1.0, 2.0, 3.0};
    
    @Before
    public void setUp() {
        denseInstance = new DenseInstance(attributes, "classValue");
    }
    
    @Test
    public void testConstructorWithAttributesAndClassValue() {
        assertEquals(1.0, denseInstance.get(0), 1e-8);
        assertEquals(2.0, denseInstance.get(1), 1e-8);
        assertEquals(3.0, denseInstance.get(2), 1e-8);
        assertEquals("classValue", denseInstance.classValue());
    }
    
    @Test
    public void testConstructorWithSize() {
        DenseInstance denseInstance = new DenseInstance(3);
        assertEquals(0.0, denseInstance.get(0), 1e-8);
        assertEquals(0.0, denseInstance.get(1), 1e-8);
        assertEquals(0.0, denseInstance.get(2), 1e-8);
        assertNull(denseInstance.classValue());
    }
    
    @Test
    public void testValue() {
        assertEquals(1.0, denseInstance.value(0), 1e-8);
        assertEquals(2.0, denseInstance.value(1), 1e-8);
        assertEquals(3.0, denseInstance.value(2), 1e-8);
    }
    
    @Test
    public void testClear() {
        denseInstance.clear();
        assertEquals(0.0, denseInstance.get(0), 1e-8);
        assertEquals(0.0, denseInstance.get(1), 1e-8);
        assertEquals(0.0, denseInstance.get(2), 1e-8);
    }
    
    @Test
    public void testContainsKey() {
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsKey(1));
        assertTrue(denseInstance.containsKey(2));
        assertFalse(denseInstance.containsKey(-1));
        assertFalse(denseInstance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        assertTrue(denseInstance.containsValue(1.0));
        assertTrue(denseInstance.containsValue(2.0));
        assertTrue(denseInstance.containsValue(3.0));
        assertFalse(denseInstance.containsValue(0.0));
        assertFalse(denseInstance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        assertEquals(3, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertTrue(Arrays.asList(0, 1, 2).contains(entry.getKey()));
        }
    }
    
    @Test
    public void testGet() {
        assertEquals(new Double(1.0), denseInstance.get(0));
        assertEquals(new Double(2.0), denseInstance.get(1));
        assertEquals(new Double(3.0), denseInstance.get(2));
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(denseInstance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = denseInstance.keySet();
        assertEquals(3, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
        assertTrue(keySet.contains(2));
    }
    
    @Test
    public void testPut() {
        Double oldValue = denseInstance.put(0, 0.0);
        assertEquals(1.0, oldValue, 1e-8);
        assertEquals(0.0, denseInstance.get(0), 1e-8);
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 0.0);
        map.put(1, 0.0);
        map.put(2, 0.0);
        denseInstance.putAll(map);
        
        assertEquals(0.0, denseInstance.get(0), 1e-8);
        assertEquals(0.0, denseInstance.get(1), 1e-8);
        assertEquals(0.0, denseInstance.get(2), 1e-8);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        denseInstance.remove(0);
    }
    
    @Test
    public void testSize() {
        assertEquals(3, denseInstance.size());
    }
    
    @Test
    public void testValues() {
        Collection<Double> values = denseInstance.values();
        assertEquals(3, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
    }
    
    @Test
    public void testNoAttributes() {
        assertEquals(3, denseInstance.noAttributes());
    }
    
    @Test
    public void testToString() {
        assertEquals("{[1.0, 2.0, 3.0];classValue}", denseInstance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        denseInstance.removeAttribute(1);
        assertEquals(2.0, denseInstance.get(1), 1e-8);
        assertEquals(3, denseInstance.size());
    }
    
    @Test
    public void testHashCode() {
        DenseInstance duplicate = new DenseInstance(attributes, "classValue");
        assertEquals(duplicate.hashCode(), denseInstance.hashCode());
    }
    
    @Test
    public void testEquals() {
        DenseInstance duplicate = new DenseInstance(attributes, "classValue");
        assertTrue(denseInstance.equals(duplicate));
    }
    
    @Test
    public void testCopy() {
        DenseInstance copy = denseInstance.copy();
        assertTrue(denseInstance.equals(copy));
    }
    
    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new TreeSet<>();
        indices.add(1);
        denseInstance.removeAttributes(indices);
        assertEquals(1.0, denseInstance.get(0), 1e-8);
        assertEquals(0, denseInstance.size());
    }
}