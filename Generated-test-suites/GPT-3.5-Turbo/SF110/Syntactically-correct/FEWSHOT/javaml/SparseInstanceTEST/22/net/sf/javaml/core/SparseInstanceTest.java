package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class SparseInstanceTest {
    
    private SparseInstance instance;
    
    @Before
    public void setUp() {
        instance = new SparseInstance();
    }
    
    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(10);
        assertEquals(10, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstanceNoAttributes() {
        SparseInstance sparseInstance = new SparseInstance(5);
        assertEquals(5, sparseInstance.noAttributes());
    }
    
    @Test
    public void testSparseInstanceNoAttributesDefaultValue() {
        SparseInstance sparseInstance = new SparseInstance(5, 0.5);
        assertEquals(5, sparseInstance.noAttributes());
        assertEquals(0.5, sparseInstance.DEFAULT_VALUE, 0.0);
    }
    
    @Test
    public void testSparseInstanceNoAttributesClassValue() {
        Object classValue = new Object();
        SparseInstance sparseInstance = new SparseInstance(5, classValue);
        assertEquals(5, sparseInstance.noAttributes());
        assertEquals(classValue, sparseInstance.classValue());
    }
    
    @Test
    public void testSparseInstanceNoAttributesDefaultValueClassValue() {
        Object classValue = new Object();
        SparseInstance sparseInstance = new SparseInstance(5, 0.5, classValue);
        assertEquals(5, sparseInstance.noAttributes());
        assertEquals(0.5, sparseInstance.DEFAULT_VALUE, 0.0);
        assertEquals(classValue, sparseInstance.classValue());
    }
    
    @Test
    public void testSparseInstanceDataVector() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertEquals(3, sparseInstance.noAttributes());
        assertEquals(0.0, sparseInstance.DEFAULT_VALUE, 0.0);
    }
    
    @Test
    public void testSparseInstanceDataVectorDefaultValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance sparseInstance = new SparseInstance(datavector, 0.5);
        assertEquals(3, sparseInstance.noAttributes());
        assertEquals(0.5, sparseInstance.DEFAULT_VALUE, 0.0);
    }
    
    @Test
    public void testSparseInstanceDataVectorClassValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        Object classValue = new Object();
        SparseInstance sparseInstance = new SparseInstance(datavector, classValue);
        assertEquals(3, sparseInstance.noAttributes());
        assertEquals(0.0, sparseInstance.DEFAULT_VALUE, 0.0);
        assertEquals(classValue, sparseInstance.classValue());
    }
    
    @Test
    public void testSparseInstanceDataVectorDefaultValueClassValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        Object classValue = new Object();
        SparseInstance sparseInstance = new SparseInstance(datavector, 0.5, classValue);
        assertEquals(3, sparseInstance.noAttributes());
        assertEquals(0.5, sparseInstance.DEFAULT_VALUE, 0.0);
        assertEquals(classValue, sparseInstance.classValue());
    }
    
    @Test
    public void testValue() {
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        instance.put(2, 0.3);
        assertEquals(0.1, instance.value(0), 0.0);
        assertEquals(0.2, instance.value(1), 0.0);
        assertEquals(0.3, instance.value(2), 0.0);
    }
    
    @Test
    public void testClear() {
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        instance.put(2, 0.3);
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        instance.put(1, 0.1);
        instance.put(2, 0.2);
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }
    
    @Test
    public void testContainsValue() {
        instance.put(1, 0.1);
        instance.put(2, 0.2);
        assertTrue(instance.containsValue(0.1));
        assertTrue(instance.containsValue(0.2));
        assertFalse(instance.containsValue(0.3));
    }
    
    @Test
    public void testEntrySet() {
        instance.put(1, 0.1);
        instance.put(2, 0.2);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(2, entrySet.size());
    }
    
    @Test
    public void testGet() {
        instance.put(1, 0.1);
        instance.put(2, 0.2);
        assertEquals(0.1, instance.get(1), 0.0);
        assertEquals(0.2, instance.get(2), 0.0);
        assertEquals(instance.DEFAULT_VALUE, instance.get(3), 0.0);
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
        instance.put(1, 0.1);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        instance.put(1, 0.1);
        instance.put(2, 0.2);
        TreeSet<Integer> keySet = instance.keySet();
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains(1));
        assertTrue(keySet.contains(2));
    }
    
    @Test
    public void testPut() {
        assertNull(instance.put(1, 0.1));
        assertNull(instance.put(2, 0.2));
        assertEquals(0.1, instance.get(1), 0.0);
        assertEquals(0.2, instance.get(2), 0.0);
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(1, 0.1);
        map.put(2, 0.2);
        instance.putAll(map);
        assertEquals(0.1, instance.get(1), 0.0);
        assertEquals(0.2, instance.get(2), 0.0);
    }
    
    @Test
    public void testRemove() {
        instance.put(1, 0.1);
        instance.put(2, 0.2);
        assertEquals(0.1, instance.remove(1), 0.0);
        assertNull(instance.get(1));
    }
    
    @Test
    public void testSize() {
        instance.put(1, 0.1);
        instance.put(2, 0.2);
        assertEquals(2, instance.size());
        instance.remove(1);
        assertEquals(1, instance.size());
        instance.remove(2);
        assertEquals(0, instance.size());
    }
    
    @Test
    public void testValues() {
        instance.put(1, 0.1);
        instance.put(2, 0.2);
        Collection<Double> values = instance.values();
        assertEquals(2, values.size());
        assertTrue(values.contains(0.1));
        assertTrue(values.contains(0.2));
    }
    
    @Test
    public void testNoAttributes() {
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
        instance.put(0, 0.1);
        assertEquals(1, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        instance.put(2, 0.3);
        instance.removeAttribute(1);
        assertFalse(instance.containsKey(1));
        assertEquals(2, instance.noAttributes());
        assertEquals(0.1, instance.get(0), 0.0);
        assertEquals(0.3, instance.get(1), 0.0);
    }
    
    @Test
    public void testToString() {
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        instance.put(2, 0.3);
        assertEquals("{0=0.1;0.2}", instance.toString());
    }
    
    @Test
    public void testHashCode() {
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        instance.put(2, 0.3);
        int hashCode = instance.hashCode();
        assertEquals(hashCode, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        instance.put(2, 0.3);
        SparseInstance other = new SparseInstance();
        other.put(0, 0.1);
        other.put(1, 0.2);
        other.put(2, 0.3);
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testCopy() {
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        instance.put(2, 0.3);
        SparseInstance copy = (SparseInstance) instance.copy();
        assertTrue(instance.equals(copy));
    }
    
    @Test
    public void testRemoveAttributes() {
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        instance.put(2, 0.3);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(0);
        indices.add(1);
        instance.removeAttributes(indices);
        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
    }
}