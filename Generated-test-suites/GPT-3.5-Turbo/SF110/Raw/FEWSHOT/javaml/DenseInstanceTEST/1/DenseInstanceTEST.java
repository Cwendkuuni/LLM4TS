
package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;

public class DenseInstanceTest {
    
    @Test
    public void testConstructorWithAttributeArray() {
        double[] att = {1.0, 2.0, 3.0};
        
        DenseInstance denseInstance = new DenseInstance(att);
        
        assertNotNull(denseInstance);
        assertEquals(3, denseInstance.noAttributes());
        assertTrue(Arrays.equals(att, denseInstance.attributes));
    }
    
    @Test
    public void testConstructorWithAttributeArrayAndClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class1";
        
        DenseInstance denseInstance = new DenseInstance(att, classValue);
        
        assertNotNull(denseInstance);
        assertEquals(3, denseInstance.noAttributes());
        assertTrue(Arrays.equals(att, denseInstance.attributes));
        assertEquals(classValue, denseInstance.classValue());
    }
    
    @Test
    public void testConstructorWithSize() {
        int size = 3;
        
        DenseInstance denseInstance = new DenseInstance(size);
        
        assertNotNull(denseInstance);
        assertEquals(size, denseInstance.noAttributes());
        assertArrayEquals(new double[size], denseInstance.attributes, 0.0);
    }
    
    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        assertEquals(2.0, denseInstance.value(1), 0.0);
    }
    
    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        denseInstance.clear();
        
        assertArrayEquals(new double[3], denseInstance.attributes, 0.0);
    }
    
    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        assertTrue(denseInstance.containsKey(1));
        assertFalse(denseInstance.containsKey(3));
        assertFalse(denseInstance.containsKey("key"));
    }
    
    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        assertTrue(denseInstance.containsValue(2.0));
        assertFalse(denseInstance.containsValue(4.0));
        assertFalse(denseInstance.containsValue("value"));
    }
    
    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        
        assertEquals(3, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Integer, Double>(0, 1.0)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Integer, Double>(1, 2.0)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Integer, Double>(2, 3.0)));
    }
    
    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        assertEquals(2.0, denseInstance.get(1), 0.0);
    }
    
    @Test
    public void testIsEmpty() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        assertFalse(denseInstance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        SortedSet<Integer> keySet = denseInstance.keySet();
        
        assertEquals(3, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
        assertTrue(keySet.contains(2));
    }
    
    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        Double oldValue = denseInstance.put(1, 4.0);
        
        assertEquals(2.0, oldValue, 0.0);
        assertEquals(4.0, denseInstance.get(1), 0.0);
    }
    
    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        
        denseInstance.putAll(map);
        
        assertEquals(4.0, denseInstance.get(0), 0.0);
        assertEquals(5.0, denseInstance.get(1), 0.0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        denseInstance.remove(1);
    }
    
    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        assertEquals(3, denseInstance.size());
    }
    
    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        Collection<Double> values = denseInstance.values();
        
        assertEquals(3, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
    }
    
    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        assertEquals(3, denseInstance.noAttributes());
    }
    
    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class1";
        DenseInstance denseInstance = new DenseInstance(att, classValue);
        
        assertEquals("{[1.0, 2.0, 3.0];class1}", denseInstance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        denseInstance.removeAttribute(1);
        
        assertEquals(2, denseInstance.noAttributes());
        assertEquals(1.0, denseInstance.get(0), 0.0);
        assertEquals(3.0, denseInstance.get(1), 0.0);
    }
    
    @Test
    public void testHashCode() {
        double[] att1 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(att1);
        
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance2 = new DenseInstance(att2);
        
        assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
    }
    
    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(att1);
        
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance2 = new DenseInstance(att2);
        
        assertTrue(denseInstance1.equals(denseInstance2));
        
        double[] att3 = {1.0, 2.0, 4.0};
        DenseInstance denseInstance3 = new DenseInstance(att3);
        
        assertFalse(denseInstance1.equals(denseInstance3));
        
        assertFalse(denseInstance1.equals(null));
        assertFalse(denseInstance1.equals(new Object()));
    }
    
    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class1";
        DenseInstance denseInstance = new DenseInstance(att, classValue);
        
        Instance copy = denseInstance.copy();
        
        assertNotNull(copy);
        assertNotSame(denseInstance, copy);
        assertEquals(denseInstance.toString(), copy.toString());
    }
    
    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(att);
        
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        
        denseInstance.removeAttributes(indices);
        
        assertEquals(2, denseInstance.noAttributes());
        assertEquals(1.0, denseInstance.get(0), 0.0);
        assertEquals(3.0, denseInstance.get(1), 0.0);
    }
}
