package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {
    private SparseInstance instance;
    
    @Before
    public void setup() {
        instance = new SparseInstance();
    }

    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(10);
        assertEquals(10, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstanceNoAttributes() {
        SparseInstance newInstance = new SparseInstance(5);
        assertEquals(5, newInstance.noAttributes());
        assertEquals(0.0, newInstance.defaultValue, 0.001);
        assertEquals(new HashMap<Integer, Double>(), newInstance.data);
    }
    
    @Test
    public void testSparseInstance() {
        SparseInstance newInstance = new SparseInstance(5, 2.0);
        assertEquals(5, newInstance.noAttributes());
        assertEquals(2.0, newInstance.defaultValue, 0.001);
        assertEquals(new HashMap<Integer, Double>(), newInstance.data);
    }
    
    @Test
    public void testSparseInstanceWithClassValue() {
        Object classValue = new Object();
        SparseInstance newInstance = new SparseInstance(5, 2.0, classValue);
        assertEquals(classValue, newInstance.classValue());
    }
    
    @Test
    public void testSparseInstanceWithDataVector() {
        double[] datavector = {1.0, 0.0, 3.5};
        SparseInstance newInstance = new SparseInstance(datavector);
        assertEquals(3, newInstance.noAttributes());
        assertEquals(0.0, newInstance.defaultValue, 0.001);
        assertEquals(new HashMap<Integer, Double>(), newInstance.data);
    }
    
    @Test
    public void testSparseInstanceWithDataVectorAndDefaultValue() {
        double[] datavector = {1.0, 0.0, 3.5};
        SparseInstance newInstance = new SparseInstance(datavector, -1.0);
        assertEquals(3, newInstance.noAttributes());
        assertEquals(-1.0, newInstance.defaultValue, 0.001);
        assertEquals(new HashMap<Integer, Double>(), newInstance.data);
    }
    
    @Test
    public void testSparseInstanceWithDataVectorAndClassValue() {
        double[] datavector = {1.0, 0.0, 3.5};
        Object classValue = new Object();
        SparseInstance newInstance = new SparseInstance(datavector, classValue);
        assertEquals(classValue, newInstance.classValue());
    }
    
    @Test
    public void testSparseInstanceWithDataVectorAndDefaultValueAndClassValue() {
        double[] datavector = {1.0, 0.0, 3.5};
        Object classValue = new Object();
        SparseInstance newInstance = new SparseInstance(datavector, -1.0, classValue);
        assertEquals(classValue, newInstance.classValue());
    }
    
    @Test
    public void testValue() {
        instance.put(0, 3.0);
        assertEquals(3.0, instance.value(0), 0.001);
    }
    
    @Test
    public void testClear() {
        instance.put(0, 3.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        instance.put(0, 3.0);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
    }
    
    @Test
    public void testContainsValue() {
        instance.put(0, 3.0);
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(2.0));
    }
    
    @Test
    public void testEntrySet() {
        instance.put(0, 3.0);
        assertEquals(instance.entrySet(), instance.data.entrySet());
    }
    
    @Test
    public void testGet() {
        instance.put(0, 3.0);
        assertEquals(3.0, instance.get(0), 0.001);
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        instance.put(0, 3.0);
        assertEquals(instance.keySet(), instance.data.keySet());
    }
    
    @Test
    public void testPut() {
        instance.put(0, 3.0);
        assertTrue(instance.containsKey(0));
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 3.0);
        map.put(1, 4.0);
        
        instance.putAll(map);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
    }
    
    @Test
    public void testRemove() {
        instance.put(0, 3.0);
        instance.remove(0);
        assertFalse(instance.containsKey(0));
    }
    
    @Test
    public void testSize() {
        instance.put(0, 3.0);
        assertEquals(1, instance.size());
    }
    
    @Test
    public void testValues() {
        instance.put(0, 3.0);
        assertEquals(instance.values(), instance.data.values());
    }
    
    @Test
    public void testNoAttributes() {
        instance.setNoAttributes(3);
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        instance.put(0, 3.0);
        instance.removeAttribute(0);
        assertFalse(instance.containsKey(0));
        assertEquals(0, instance.noAttributes());
    }
    
    @Test
    public void testToString() {
        instance.put(0, 3.0);
        assertEquals("{0=3.0;null}", instance.toString());
    }
    
    @Test
    public void testHashCode() {
        SparseInstance newInstance = new SparseInstance();
        assertEquals(newInstance.hashCode(), instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        SparseInstance newInstance = new SparseInstance();
        assertEquals(newInstance, instance);
    }
    
    @Test
    public void testCopy() {
        instance.put(0, 3.0);
        instance.setClassValue(new Object());
        
        SparseInstance copyInstance = (SparseInstance) instance.copy();
        assertEquals(instance, copyInstance);
    }
    
    @Test
    public void testRemoveAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttributes(Collections.singleton(1));
        
        assertFalse(instance.containsKey(1));
        assertEquals(2, instance.noAttributes());
    }
}