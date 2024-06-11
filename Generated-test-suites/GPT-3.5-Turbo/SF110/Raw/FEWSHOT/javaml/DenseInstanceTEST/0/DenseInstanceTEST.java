package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class DenseInstanceTest {
    
    @Test
    public void testConstructor_withAttributeArray() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertArrayEquals(attributes, instance.attributes, 0.0);
    }
    
    @Test
    public void testConstructor_withAttributeArrayAndClassValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "Class A";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertArrayEquals(attributes, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }
    
    @Test
    public void testConstructor_withSize() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);
        assertEquals(size, instance.attributes.length);
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
        assertArrayEquals(new double[attributes.length], instance.attributes, 0.0);
    }
    
    @Test
    public void testContainsKey() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(0.0));
        assertFalse(instance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertNotNull(instance.entrySet());
        assertEquals(attributes.length, instance.entrySet().size());
    }
    
    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(Double.valueOf(1.0), instance.get(0));
        assertEquals(Double.valueOf(2.0), instance.get(1));
        assertEquals(Double.valueOf(3.0), instance.get(2));
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
        assertNotNull(instance.keySet());
        assertEquals(attributes.length, instance.keySet().size());
    }
    
    @Test
    public void testPut() {
        double[] attributes = {1.0, 2.0, 3.0};
        Double newValue = 4.0;
        DenseInstance instance = new DenseInstance(attributes);
        Double oldValue = instance.put(0, newValue);
        assertEquals(Double.valueOf(1.0), oldValue);
        assertEquals(Double.valueOf(4.0), instance.get(0));
    }
    
    @Test
    public void testPutAll() {
        double[] attributes = {1.0, 2.0, 3.0};
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 0.0);
        map.put(1, 1.0);
        map.put(2, 2.0);
        
        DenseInstance instance = new DenseInstance(attributes);
        instance.putAll(map);
        assertEquals(Double.valueOf(0.0), instance.get(0));
        assertEquals(Double.valueOf(1.0), instance.get(1));
        assertEquals(Double.valueOf(2.0), instance.get(2));
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
        assertNotNull(instance.values());
        assertEquals(attributes.length, instance.values().size());
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
        Object classValue = "Class A";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        String expected = "{" + Arrays.toString(attributes) + ";" + classValue + "}";
        assertEquals(expected, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(1);
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.attributes, 0.0);
    }
    
    @Test
    public void testHashCode() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        int expectedHashCode = 31 * 1 + Arrays.hashCode(attributes);
        assertEquals(expectedHashCode, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        double[] attributes3 = {4.0, 5.0, 6.0};
        
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        DenseInstance instance3 = new DenseInstance(attributes3);
        
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }
    
    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "Class A";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        DenseInstance copy = (DenseInstance) instance.copy();
        assertArrayEquals(attributes, copy.attributes, 0.0);
        assertEquals(classValue, copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        
        instance.removeAttributes(indices);
        
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.attributes, 0.0);
    }
    
}