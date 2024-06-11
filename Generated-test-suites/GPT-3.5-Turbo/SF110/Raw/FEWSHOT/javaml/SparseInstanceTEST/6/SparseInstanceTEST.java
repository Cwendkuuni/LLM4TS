package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {
    
    private SparseInstance instance;
    
    @Before
    public void setUp() {
        instance = new SparseInstance();
        instance.setNoAttributes(3);
    }
    
    @Test
    public void testConstructor() {
        SparseInstance newInstance = new SparseInstance();
        
        assertEquals(-1, newInstance.noAttributes());
    }
    
    @Test
    public void testConstructorWithNoAttributes() {
        SparseInstance newInstance = new SparseInstance(3);
        
        assertEquals(3, newInstance.noAttributes());
    }
    
    @Test
    public void testConstructorWithDefaultValue() {
        SparseInstance newInstance = new SparseInstance(3, 0.5);
        
        assertEquals(0.5, newInstance.get(1), 0.0001);
    }
    
    @Test
    public void testConstructorWithClassValue() {
        Object classValue = new Object();
        SparseInstance newInstance = new SparseInstance(3, classValue);
        
        assertEquals(classValue, newInstance.classValue());
    }
    
    @Test
    public void testConstructorWithAllParameters() {
        Object classValue = new Object();
        SparseInstance newInstance = new SparseInstance(3, 0.5, classValue);
        
        assertEquals(0.5, newInstance.get(1), 0.0001);
        assertEquals(classValue, newInstance.classValue());
    }
    
    @Test
    public void testConstructorWithDoubleArray() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance newInstance = new SparseInstance(datavector);
        
        assertEquals(0.1, newInstance.get(0), 0.0001);
        assertEquals(0.2, newInstance.get(1), 0.0001);
        assertEquals(0.3, newInstance.get(2), 0.0001);
    }
    
    @Test
    public void testConstructorWithDoubleArrayAndDefaultValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance newInstance = new SparseInstance(datavector, 0.5);
        
        assertEquals(0.1, newInstance.get(0), 0.0001);
        assertEquals(0.2, newInstance.get(1), 0.0001);
        assertEquals(0.3, newInstance.get(2), 0.0001);
    }
    
    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        Object classValue = new Object();
        SparseInstance newInstance = new SparseInstance(datavector, classValue);
        
        assertEquals(0.1, newInstance.get(0), 0.0001);
        assertEquals(0.2, newInstance.get(1), 0.0001);
        assertEquals(0.3, newInstance.get(2), 0.0001);
        assertEquals(classValue, newInstance.classValue());
    }
    
    @Test
    public void testConstructorWithAllDoubleArrayParameters() {
        double[] datavector = {0.1, 0.2, 0.3};
        Object classValue = new Object();
        SparseInstance newInstance = new SparseInstance(datavector, 0.5, classValue);
        
        assertEquals(0.1, newInstance.get(0), 0.0001);
        assertEquals(0.2, newInstance.get(1), 0.0001);
        assertEquals(0.3, newInstance.get(2), 0.0001);
        assertEquals(classValue, newInstance.classValue());
    }
    
    @Test
    public void testInitiate() {
        double[] datavector = {0.1, 0.0, 0.3};
        instance.initiate(datavector);
        
        assertEquals(0.1, instance.get(0), 0.0001);
        assertFalse(instance.containsKey(1));
        assertEquals(0.3, instance.get(2), 0.0001);
    }
    
    @Test
    public void testValue() {
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        
        assertEquals(0.1, instance.value(0), 0.0001);
        assertEquals(0.2, instance.value(1), 0.0001);
    }
    
    @Test
    public void testClear() {
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        
        instance.clear();
        
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        instance.put(0, 0.1);
        
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
    }
    
    @Test
    public void testContainsValue() {
        instance.put(0, 0.1);
        
        assertTrue(instance.containsValue(0.1));
        assertFalse(instance.containsValue(0.2));
    }
    
    @Test
    public void testEntrySet() {
        instance.put(0, 0.1);
        
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        
        assertEquals(1, entrySet.size());
        Map.Entry<Integer, Double> entry = entrySet.iterator().next();
        assertEquals(0, entry.getKey().intValue());
        assertEquals(0.1, entry.getValue(), 0.0001);
    }
    
    @Test
    public void testGet() {
        instance.put(0, 0.1);
        
        assertEquals(0.1, instance.get(0), 0.0001);
        assertEquals(instance.defaultValue, instance.get(1), 0.0001);
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
        
        instance.put(0, 0.1);
        
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        instance.put(0, 0.1);
        
        TreeSet<Integer> keySet = instance.keySet();
        
        assertEquals(1, keySet.size());
        assertEquals(0, keySet.iterator().next().intValue());
    }
    
    @Test
    public void testPut() {
        instance.put(0, 0.1);
        
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
    }
    
    @Test
    public void testPutAll() {
        HashMap<Integer, Double> map = new HashMap<>();
        map.put(0, 0.1);
        map.put(1, 0.2);
        
        instance.putAll(map);
        
        assertEquals(0.1, instance.get(0), 0.0001);
        assertEquals(0.2, instance.get(1), 0.0001);
    }
    
    @Test
    public void testRemove() {
        instance.put(0, 0.1);
        
        assertEquals(0.1, instance.remove(0), 0.0001);
        assertFalse(instance.containsKey(0));
    }
    
    @Test
    public void testSize() {
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        
        assertEquals(2, instance.size());
    }
    
    @Test
    public void testValues() {
        instance.put(0, 0.1);
        
        assertEquals(0.1, instance.values().iterator().next(), 0.0001);
    }
    
    @Test
    public void testNoAttributes() {
        assertEquals(3, instance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        
        instance.removeAttribute(0);
        
        assertFalse(instance.containsKey(0));
        assertEquals(0.2, instance.get(0), 0.0001);
    }
    
    @Test
    public void testToString() {
        instance.put(0, 0.1);
        instance.setClassValue(new Object());
        
        assertEquals("{0=0.1;classValue}", instance.toString());
    }
    
    @Test
    public void testHashCode() {
        instance.put(0, 0.1);
        
        int hashCode = instance.hashCode();
        
        assertEquals(hashCode, instance.hashCode());
    }
    
    @Test
    public void testEquals() {
        instance.put(0, 0.1);
        
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));
        
        SparseInstance other = new SparseInstance();
        other.put(0, 0.1);
        
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testCopy() {
        instance.put(0, 0.1);
        instance.setClassValue(new Object());
        
        SparseInstance copy = (SparseInstance) instance.copy();
        
        assertEquals(instance, copy);
    }
    
    @Test
    public void testRemoveAttributes() {
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        instance.put(2, 0.3);
        
        Set<Integer> indices = new TreeSet<>();
        indices.add(0);
        indices.add(2);
        
        instance.removeAttributes(indices);
        
        assertFalse(instance.containsKey(0));
        assertEquals(0.2, instance.get(0), 0.0001);
        assertFalse(instance.containsKey(2));
    }

}