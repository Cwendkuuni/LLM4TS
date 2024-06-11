package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class SparseInstanceTest {
    
    private SparseInstance instance;
    
    @Before
    public void setUp() {
        instance = new SparseInstance();
    }
    
    @Test
    public void testSetNoAttributes() {
        int noAttributes = 5;
        instance.setNoAttributes(noAttributes);
        assertEquals(noAttributes, instance.noAttributes);
    }
    
    @Test
    public void testSparseInstance() {
        assertEquals(-1, instance.noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals(null, instance.classValue());
    }
    
    @Test
    public void testSparseInstanceWithArgs() {
        int noAttributes = 5;
        double defaultValue = 2.0;
        Object classValue = "abc";
        
        instance = new SparseInstance(noAttributes, defaultValue, classValue);
        
        assertEquals(noAttributes, instance.noAttributes);
        assertEquals(defaultValue, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
        assertTrue(instance.data.isEmpty());
    }
    
    @Test
    public void testInitiate() {
        double[] datavector = {1.0, 2.0, 0.0, 3.0};
        double defaultValue = 0.0;
        
        instance.initiate(datavector);
        
        assertEquals(datavector.length, instance.noAttributes);
        assertFalse(instance.data.containsKey(0));
        assertTrue(instance.data.containsKey(1));
        assertFalse(instance.data.containsKey(2));
        assertTrue(instance.data.containsKey(3));
    }
    
    @Test
    public void testValue() {
        int pos = 2;
        double value = 3.0;
        instance.data.put(pos, value);
        
        assertEquals(value, instance.value(pos), 0.001);
    }
    
    @Test
    public void testClear() {
        instance.data.put(1, 2.0);
        instance.data.put(3, 4.0);
        
        instance.clear();
        
        assertTrue(instance.data.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        instance.data.put(1, 2.0);
        
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }
    
    @Test
    public void testContainsValue() {
        instance.data.put(1, 2.0);
        
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(3.0));
    }
    
    @Test
    public void testEntrySet() {
        instance.data.put(1, 2.0);
        instance.data.put(3, 4.0);
        
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        
        assertEquals(2, entrySet.size());
    }
    
    @Test
    public void testGet() {
        int key = 1;
        double value = 2.0;
        instance.data.put(key, value);
        
        assertEquals(value, instance.get(key), 0.001);
        assertEquals(instance.defaultValue, instance.get(2), 0.001);
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
        
        instance.data.put(1, 2.0);
        
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        instance.data.put(1, 2.0);
        instance.data.put(3, 4.0);
        
        TreeSet<Integer> keySet = instance.keySet();
        
        assertEquals(2, keySet.size());
    }
    
    @Test
    public void testPut() {
        int key = 1;
        double value = 2.0;
        
        Double result = instance.put(key, value);
        
        assertEquals(value, instance.get(key), 0.001);
        assertNull(result);
    }
    
    @Test
    public void testPutAll() {
        HashMap<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(1, 2.0);
        map.put(3, 4.0);
        
        instance.putAll(map);
        
        assertEquals(map.size(), instance.data.size());
    }
    
    @Test
    public void testRemove() {
        int key = 1;
        double value = 2.0;
        instance.data.put(key, value);
        
        Double result = instance.remove(key);
        
        assertEquals(value, result, 0.001);
        assertFalse(instance.data.containsKey(key));
    }
    
    @Test
    public void testSize() {
        instance.data.put(1, 2.0);
        instance.data.put(3, 4.0);
        
        assertEquals(instance.data.size(), instance.size());
    }
    
    @Test
    public void testValues() {
        instance.data.put(1, 2.0);
        instance.data.put(3, 4.0);
        
        Collection<Double> values = instance.values();
        
        assertEquals(2, values.size());
    }
    
    @Test
    public void testNoAttributes() {
        int noAttributes = 5;
        instance.noAttributes = noAttributes;
        
        assertEquals(noAttributes, instance.noAttributes());
        
        instance.data.put(1, 2.0);
        assertEquals(noAttributes, instance.noAttributes());
        
        instance.data.clear();
        assertEquals(0, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        int remove = 2;
        
        instance.data.put(1, 2.0);
        instance.data.put(3, 4.0);
        
        instance.removeAttribute(remove);
        
        assertFalse(instance.data.containsKey(remove));
        assertFalse(instance.data.containsKey(remove+1));
        assertTrue(instance.data.containsKey(3));
        
        assertEquals(instance.noAttributes-1, instance.noAttributes);
    }
    
    @Test
    public void testToString() {
        instance.data.put(1, 2.0);
        instance.data.put(3, 4.0);
        
        String expected = "{" + instance.data.toString() + ";" + instance.classValue() + "}";
        
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testHashCode() {
        HashMap<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(1, 2.0);
        map.put(3, 4.0);
        
        instance.data = map;
        instance.defaultValue = 0.0;
        
        int expectedHashCode = (31 * map.hashCode()) + Double.valueOf(0.0).hashCode();
        
        assertEquals(expectedHashCode, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        HashMap<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(1, 2.0);
        map.put(3, 4.0);
        
        instance.data = map;
        instance.defaultValue = 0.0;
        
        SparseInstance other = new SparseInstance();
        other.data = map;
        other.defaultValue = 0.0;
        
        assertTrue(instance.equals(other));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));
        assertFalse(instance.equals(new SparseInstance()));
        
        map.put(5, 6.0);
        other.data = map;
        assertFalse(instance.equals(other));
    }
    
    @Test
    public void testCopy() {
        HashMap<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(1, 2.0);
        map.put(3, 4.0);
        
        instance.data = map;
        instance.defaultValue = 0.0;
        instance.noAttributes = 5;
        
        SparseInstance copy = (SparseInstance) instance.copy();
        
        assertEquals(instance.data, copy.data);
        assertEquals(instance.defaultValue, copy.defaultValue, 0.001);
        assertEquals(instance.noAttributes, copy.noAttributes);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        HashSet<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        indices.add(3);
        
        instance.data.put(1, 2.0);
        instance.data.put(3, 4.0);
        instance.data.put(5, 6.0);
        
        instance.removeAttributes(indices);
        
        assertFalse(instance.data.containsKey(1));
        assertFalse(instance.data.containsKey(3));
        assertTrue(instance.data.containsKey(5));
        assertEquals(1, instance.noAttributes);
    }
}