
package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {
  
    private SparseInstance instance;
    
    @Before
    public void setUp() {
        instance = new SparseInstance();
    }
    
    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstance_NoAttributesDefaultValueNull() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.0);
        assertEquals(null, instance.classValue());
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testSparseInstance_NoAttributesDefaultValueClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testSparseInstance_NoAttributesDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 1.0);
        assertEquals(5, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.0);
        assertEquals(null, instance.classValue());
        assertTrue(instance.isEmpty());
    }
    
   @Test
    public void testSparseInstance_NoAttributesDefaultValueClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 1.0, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testSparseInstance_datavectorDefaultValueNull() {
        double[] datavector = {1.0, 2.0, 3.0};
        instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.0);
        assertEquals(null, instance.classValue());
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testSparseInstance_datavectorDefaultValueClassValue() {
        Object classValue = new Object();
        double[] datavector = {1.0, 2.0, 3.0};
        instance = new SparseInstance(datavector, classValue);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testSparseInstance_datavectorDefaultValue() {
        Object classValue = new Object();
        double[] datavector = {1.0, 2.0, 3.0};
        instance = new SparseInstance(datavector, 5.0);
        assertEquals(3, instance.noAttributes());
        assertEquals(5.0, instance.defaultValue, 0.0);
        assertEquals(null, instance.classValue());
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testSparseInstance_datavectorDefaultValueClassValue() {
        Object classValue = new Object();
        double[] datavector = {1.0, 2.0, 3.0};
        instance = new SparseInstance(datavector, 5.0, classValue);
        assertEquals(3, instance.noAttributes());
        assertEquals(5.0, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testInitiate() {
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        assertEquals(2.0, instance.value(1), 0.0);
    }
    
    @Test
    public void testClear() {
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        instance.put(1, 2.0);
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }
    
    @Test
    public void testContainsValue() {
        instance.put(1, 2.0);
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(3.0));
    }
    
    @Test
    public void testEntrySet() {
        instance.put(1, 2.0);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(1, entrySet.size());
    }
    
    @Test
    public void testGet() {
        instance.put(1, 2.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(0.0, instance.get(2), 0.0);
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
        instance.put(1, 2.0);
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        instance.put(1, 2.0);
        Set<Integer> keySet = instance.keySet();
        assertEquals(1, keySet.size());
    }
    
    @Test
    public void testPut() {
        instance.put(1, 2.0);
        assertEquals(2.0, instance.value(1), 0.0);
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(2.0, instance.value(1), 0.0);
    }
    
    @Test
    public void testRemove() {
        instance.put(1, 2.0);
        instance.remove(1);
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testSize() {
        instance.put(1, 2.0);
        assertEquals(1, instance.size());
    }
    
    @Test
    public void testValues() {
        instance.put(1, 2.0);
        assertEquals(1, instance.values().size());
    }
    
    @Test
    public void testNoAttributes() {
        instance.put(1, 2.0);
        assertEquals(2, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        instance.put(1, 2.0);
        instance.removeAttribute(1);
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testToString() {
        instance.put(1, 2.0);
        assertEquals("{1=2.0;null}", instance.toString());
    }
    
    @Test
    public void testHashCode() {
        instance.put(1, 2.0);
        int hashCode = instance.hashCode();
        assertEquals(109603812, hashCode);
    }
    
    @Test
    public void testEquals() {
        instance.put(1, 2.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(1, 2.0);
        assertTrue(instance.equals(instance2));
    }
    
    @Test
    public void testCopy() {
        instance.put(1, 2.0);
        SparseInstance copy = (SparseInstance) instance.copy();
        assertTrue(instance.equals(copy));
    }
    
    @Test
    public void testRemoveAttributes() {
        instance.put(1, 2.0);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        instance.removeAttributes(indices);
        assertTrue(instance.isEmpty());
    }
}
  