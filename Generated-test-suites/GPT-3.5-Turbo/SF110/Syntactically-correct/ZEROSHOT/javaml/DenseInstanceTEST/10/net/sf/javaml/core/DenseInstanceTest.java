package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0);
    }
    
    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(0.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(0, 1.0)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(1, 2.0)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(2, 3.0)));
    }
    
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
    }
    
    @Test
    public void testIsEmpty() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
        assertTrue(keySet.contains(2));
    }
    
    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(2.0, instance.put(1, 4.0), 0.0);
        double[] expected = {1.0, 4.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0);
    }
    
    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 4.0);
        map.put(2, 5.0);
        instance.putAll(map);
        double[] expected = {1.0, 4.0, 5.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0);
    }
    
    @Test
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertThrows(UnsupportedOperationException.class, () -> instance.remove(0));
    }
    
    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
    }
    
    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(1);
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0);
    }
    
    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        int expected = 961557972;
        assertEquals(expected, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        DenseInstance copiedInstance = (DenseInstance) instance.copy();
        double[] expected = {1.0, 2.0, 3.0};
        assertArrayEquals(expected, copiedInstance.getAttributes(), 0.0);
        assertNull(copiedInstance.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        instance.removeAttributes(indices);
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0);
    }

}