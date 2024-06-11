
package net.sf.javaml.core;

import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {
    @Test
    public void testConstructor1() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertArrayEquals(att, instance.getAttributes(), 0.0001);
        assertNull(instance.getClassValue());
    }
    
    @Test
    public void testConstructor2() {
        double[] att = {4.0, 5.0, 6.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        
        assertArrayEquals(att, instance.getAttributes(), 0.0001);
        assertEquals(classValue, instance.getClassValue());
    }
    
    @Test
    public void testConstructor3() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);
        double[] expected = new double[size];
        
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
        assertNull(instance.getClassValue());
    }
    
    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(att[0], instance.value(0), 0.0001);
        assertEquals(att[1], instance.value(1), 0.0001);
        assertEquals(att[2], instance.value(2), 0.0001);
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.clear();
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
    }
    
    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Map<Integer, Double> expected = new HashMap<>();
        expected.put(0, 1.0);
        expected.put(1, 2.0);
        expected.put(2, 3.0);
        
        assertEquals(expected.entrySet(), instance.entrySet());
    }
    
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(3.0, instance.get(2), 0.0001);
        assertNull(instance.get(3));
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
        
        Set<Integer> expected = new TreeSet<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        
        assertEquals(expected, instance.keySet());
    }
    
    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        double oldValue = instance.put(0, 4.0);
        double[] expected = {4.0, 2.0, 3.0};
        assertEquals(oldValue, 1.0, 0.0001);
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
    }
    
    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        
        instance.putAll(map);
        double[] expected = {4.0, 5.0, 6.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(att.length, instance.size());
    }
    
    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        List<Double> expected = new ArrayList<>();
        expected.add(1.0);
        expected.add(2.0);
        expected.add(3.0);
        
        assertEquals(expected, instance.values());
    }
    
    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(att.length, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        String expected = "{" + Arrays.toString(att) + ";" + instance.getClassValue() + "}";
        
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.removeAttribute(1);
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
    }
    
    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        assertEquals(Arrays.hashCode(att), instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(att2);
        
        double[] att3 = {4.0, 5.0, 6.0};
        DenseInstance instance3 = new DenseInstance(att3);
        
        assertEquals(instance2, instance1);
        assertNotEquals(instance3, instance1);
    }
    
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        DenseInstance copy = (DenseInstance) instance.copy();
        
        assertArrayEquals(att, copy.getAttributes(), 0.0001);
        assertEquals(instance.getClassValue(), copy.getClassValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        
        instance.removeAttributes(indices);
        
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
    }
}
