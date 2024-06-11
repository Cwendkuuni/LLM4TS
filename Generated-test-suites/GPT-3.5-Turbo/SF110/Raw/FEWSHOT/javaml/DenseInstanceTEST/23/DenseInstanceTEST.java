package net.sf.javaml.core;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DenseInstanceTest {
    
    @Test
    public void testConstructorWithAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        assertArrayEquals(attributes, instance.getAttributes(), 0.0);
        assertNull(instance.getClassValue());
    }
    
    @Test
    public void testConstructorWithAttributesAndClassValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        
        assertArrayEquals(attributes, instance.getAttributes(), 0.0);
        assertEquals(classValue, instance.getClassValue());
    }
    
    @Test
    public void testConstructorWithSize() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);
        
        assertArrayEquals(new double[size], instance.getAttributes(), 0.0);
        assertNull(instance.getClassValue());
    }
    
    @Test
    public void testValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }
    
    @Test
    public void testClear() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        instance.clear();
        
        assertArrayEquals(new double[attributes.length], instance.getAttributes(), 0.0);
    }
    
    @Test
    public void testContainsKey() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        
        for (int i = 0; i < attributes.length; i++) {
            assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(i, attributes[i])));
        }
    }
    
    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        assertEquals(attributes[0], instance.get(0), 0.0);
        assertEquals(attributes[1], instance.get(1), 0.0);
        assertEquals(attributes[2], instance.get(2), 0.0);
    }
    
    @Test
    public void testIsEmpty() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        SortedSet<Integer> keySet = instance.keySet();
        
        for (int i = 0; i < attributes.length; i++) {
            assertTrue(keySet.contains(i));
        }
    }
    
    @Test
    public void testPut() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        assertEquals(attributes[0], instance.put(0, 4.0), 0.0);
        assertEquals(4.0, instance.get(0), 0.0);
    }
    
    @Test
    public void testPutAll() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        
        instance.putAll(map);
        
        for (int i = 0; i < attributes.length; i++) {
            assertEquals(map.get(i), instance.get(i), 0.0);
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        instance.remove(0);
    }
    
    @Test
    public void testSize() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        assertEquals(attributes.length, instance.size());
    }
    
    @Test
    public void testValues() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        Collection<Double> values = instance.values();
        
        for (double attribute : attributes) {
            assertTrue(values.contains(attribute));
        }
    }
    
    @Test
    public void testNoAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        assertEquals(attributes.length, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        String expected = "{" + Arrays.toString(attributes) + ";" + instance.getClassValue() + "}";
        
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes.clone());
        
        int indexToRemove = 1;
        
        instance.removeAttribute(indexToRemove);
        
        double[] expectedAttributes = {1.0, 3.0};
        assertArrayEquals(expectedAttributes, instance.getAttributes(), 0.0);
    }
    
    @Test
    public void testHashCode() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes);
        DenseInstance instance2 = new DenseInstance(attributes.clone());
        
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes);
        DenseInstance instance2 = new DenseInstance(attributes.clone());
        DenseInstance instance3 = new DenseInstance(attributes, "class");
        
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        
        DenseInstance copy = (DenseInstance) instance.copy();
        
        assertArrayEquals(instance.getAttributes(), copy.getAttributes(), 0.0);
        assertEquals(instance.getClassValue(), copy.getClassValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes.clone());
        
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        
        instance.removeAttributes(indices);
        
        double[] expectedAttributes = {1.0, 3.0};
        assertArrayEquals(expectedAttributes, instance.getAttributes(), 0.0);
    }
    
}