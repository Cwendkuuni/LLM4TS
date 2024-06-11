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

import org.junit.Test;

public class DenseInstanceTest {
    
    @Test
    public void testConstructorWithSingleAttribute() {
        double[] att = { 1.0 };
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0001);
    }
    
    @Test
    public void testConstructorWithMultipleAttributes() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
    }
    
    @Test
    public void testConstructorWithClassValue() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att, "class");
        assertEquals("class", instance.classValue());
    }
    
    @Test
    public void testValue() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
    }
    
    @Test
    public void testClear() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        assertEquals(0.0, instance.value(0), 0.0001);
        assertEquals(0.0, instance.value(1), 0.0001);
        assertEquals(0.0, instance.value(2), 0.0001);
    }
    
    @Test
    public void testContainsKeyWithValidKey() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
    }
    
    @Test
    public void testContainsKeyWithInvalidKey() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValueWithValidValue() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
    }
    
    @Test
    public void testContainsValueWithInvalidValue() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.containsValue(0.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        assertTrue(entrySet.contains(new HashMap.SimpleEntry<Integer, Double>(0, 1.0)));
        assertTrue(entrySet.contains(new HashMap.SimpleEntry<Integer, Double>(1, 2.0)));
        assertTrue(entrySet.contains(new HashMap.SimpleEntry<Integer, Double>(2, 3.0)));
    }
    
    @Test
    public void testGet() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(3.0, instance.get(2), 0.0001);
    }
    
    @Test
    public void testIsEmpty() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        SortedSet<Integer> keys = instance.keySet();
        assertEquals(3, keys.size());
        assertTrue(keys.contains(0));
        assertTrue(keys.contains(1));
        assertTrue(keys.contains(2));
    }
    
    @Test
    public void testPut() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertNull(instance.put(0, 0.0));
        assertEquals(0.0, instance.value(0), 0.0001);
    }
    
    @Test
    public void testPutAll() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 0.0);
        map.put(1, 1.0);
        map.put(2, 2.0);
        
        instance.putAll(map);
        
        assertEquals(0.0, instance.value(0), 0.0001);
        assertEquals(1.0, instance.value(1), 0.0001);
        assertEquals(2.0, instance.value(2), 0.0001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertEquals(3, instance.size());
    }
    
    @Test
    public void testValues() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
    }
    
    @Test
    public void testNoAttributes() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(3.0, instance.value(1), 0.0001);
    }
    
    @Test
    public void testHashCode() {
        double[] att1 = { 1.0, 2.0, 3.0 };
        double[] att2 = { 1.0, 2.0, 3.0 };
        double[] att3 = { 1.0, 2.0, 4.0 };
        
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        DenseInstance instance3 = new DenseInstance(att3);
        
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] att1 = { 1.0, 2.0, 3.0 };
        double[] att2 = { 1.0, 2.0, 3.0 };
        double[] att3 = { 1.0, 2.0, 4.0 };
        
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        DenseInstance instance3 = new DenseInstance(att3);
        
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    @Test
    public void testCopy() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        
        DenseInstance copy = instance.copy();
        
        assertTrue(Arrays.equals(instance.attributes, copy.attributes));
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(0);
        indices.add(2);
        
        instance.removeAttributes(indices);
        
        assertEquals(1, instance.noAttributes());
        assertEquals(2.0, instance.value(0), 0.0001);
    }
}