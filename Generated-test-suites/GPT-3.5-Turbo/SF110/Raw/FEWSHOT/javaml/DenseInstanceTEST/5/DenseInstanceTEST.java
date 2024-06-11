package net.sf.javaml.core;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {
    
     @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        assertEquals(1.0, di.value(0), 0.001);
        assertEquals(2.0, di.value(1), 0.001);
        assertEquals(3.0, di.value(2), 0.001);
    }
    
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        di.clear();
        double[] expected = {0.0, 0.0, 0.0};
    
        assertArrayEquals(expected, di.attributes, 0.001);
    }
    
    
    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        assertTrue(di.containsKey(0));
        assertTrue(di.containsKey(1));
        assertTrue(di.containsKey(2));
        assertFalse(di.containsKey(3));
    }
    
    
    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);

        assertTrue(di.containsValue(1.0));
        assertTrue(di.containsValue(2.0));
        assertTrue(di.containsValue(3.0));
        assertFalse(di.containsValue(4.0));
    }
    
    
    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        Set<Map.Entry<Integer, Double>> entrySet = di.entrySet();
        
        assertEquals(3, entrySet.size());
        
        for(Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(att[key], value, 0.001);
        }
    }
    
    
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        assertEquals(att[0], di.get(0), 0.001);
        assertEquals(att[1], di.get(1), 0.001);
        assertEquals(att[2], di.get(2), 0.001);
        assertNull(di.get(3));
    }
    
    
    @Test
    public void testIsEmpty() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        assertFalse(di.isEmpty());
    }
    
    
    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        SortedSet<Integer> keySet = di.keySet();
        
        assertEquals(3, keySet.size());
        
        int index = 0;
        for(Integer key : keySet) {
            assertEquals(index++, key, 0.001);
        }
    }
    
    
    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        di.put(0, 10.0);
        di.put(1, 20.0);
        di.put(2, 30.0);
        
        assertEquals(10.0, di.attributes[0], 0.001);
        assertEquals(20.0, di.attributes[1], 0.001);
        assertEquals(30.0, di.attributes[2], 0.001);
    }
    
    
    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        HashMap<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 10.0);
        map.put(1, 20.0);
        map.put(2, 30.0);
        
        di.putAll(map);
        
        assertEquals(10.0, di.attributes[0], 0.001);
        assertEquals(20.0, di.attributes[1], 0.001);
        assertEquals(30.0, di.attributes[2], 0.001);
    }
    
    
    @Test (expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        di.remove(0);
    }
    
    
    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        int expected = 3;
        int actual = di.size();
        
        assertEquals(expected, actual);
    }
    
    
    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        Collection<Double> values = di.values();
        
        assertEquals(3, values.size());
        
        for(Double value : values) {
            assertTrue(di.containsValue(value));
        }
    }
    
    
    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        int expected = 3;
        int actual = di.noAttributes();
        
        assertEquals(expected, actual);
    }
    
    
    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        String expected = "{[1.0, 2.0, 3.0];null}";
        String actual = di.toString();
        
        assertEquals(expected, actual);
    }
    
    
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        
        di.removeAttribute(1);
        
        double[] expected = {1.0, 3.0};
    
        assertArrayEquals(expected, di.attributes, 0.001);
    }
    
    
    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di1 = new DenseInstance(att);
        DenseInstance di2 = new DenseInstance(att);
        
        int hashCode1 = di1.hashCode();
        int hashCode2 = di2.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }
    
    
    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance di1 = new DenseInstance(att1);
        DenseInstance di2 = new DenseInstance(att2);
        
        assertTrue(di1.equals(di2));
    }
    
    
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di1 = new DenseInstance(att);
        DenseInstance di2 = di1.copy();
        
        assertNotSame(di1, di2);
        assertArrayEquals(att, di2.attributes, 0.001);
        assertEquals(di1.classValue(), di2.classValue());
    }
    
    
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        
        di.removeAttributes(indices);
        
        double[] expected = {1.0, 3.0};
    
        assertArrayEquals(expected, di.attributes, 0.001);
    }
    
    
}