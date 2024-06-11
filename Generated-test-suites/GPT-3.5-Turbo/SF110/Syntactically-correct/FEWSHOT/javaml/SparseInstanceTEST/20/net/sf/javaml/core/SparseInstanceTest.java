package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {
    
    private SparseInstance sparseInstance;
    
    @Before
    public void setUp() {
        sparseInstance = new SparseInstance(5, -1.0);
    }
    
    @Test
    public void testSetNoAttributes() {
        sparseInstance.setNoAttributes(10);
        assertEquals(10, sparseInstance.noAttributes());
    }
    
    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstanceWithNoAttributesAndDefaultValue() {
        SparseInstance instance = new SparseInstance(-1, -1.0);
        assertEquals(-1, instance.noAttributes());
        assertEquals(-1.0, instance.defaultValue, 0);
    }
    
    @Test
    public void testSparseInstanceWithNoAttributesAndClassValue() {
        SparseInstance instance = new SparseInstance(-1, "classValue");
        assertEquals(-1, instance.noAttributes());
        assertEquals("classValue", instance.classValue());
    }
    
    @Test
    public void testSparseInstanceWithNoAttributesAndDefaultValueAndClassValue() {
        SparseInstance instance = new SparseInstance(-1, -1.0, "classValue");
        assertEquals(-1, instance.noAttributes());
        assertEquals(-1.0, instance.defaultValue, 0);
        assertEquals("classValue", instance.classValue());
    }
    
    @Test
    public void testSparseInstanceWithDataVector() {
        double[] datavector = {1.0, -1.5, 3.2};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0);
        assertEquals(-1.5, instance.get(1), 0);
        assertEquals(3.2, instance.get(2), 0);
    }
    
    @Test
    public void testSparseInstanceWithDataVectorAndDefaultValue() {
        double[] datavector = {1.0, -1.5, 3.2};
        SparseInstance instance = new SparseInstance(datavector, -1.0);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0);
        assertEquals(-1.5, instance.get(1), 0);
        assertEquals(3.2, instance.get(2), 0);
        assertEquals(-1.0, instance.defaultValue, 0);
    }
    
    @Test
    public void testSparseInstanceWithDataVectorAndClassValue() {
        double[] datavector = {1.0, -1.5, 3.2};
        SparseInstance instance = new SparseInstance(datavector, "classValue");
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0);
        assertEquals(-1.5, instance.get(1), 0);
        assertEquals(3.2, instance.get(2), 0);
        assertEquals("classValue", instance.classValue());
    }
    
    @Test
    public void testSparseInstanceWithDataVectorAndDefaultValueAndClassValue() {
        double[] datavector = {1.0, -1.5, 3.2};
        SparseInstance instance = new SparseInstance(datavector, -1.0, "classValue");
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0);
        assertEquals(-1.5, instance.get(1), 0);
        assertEquals(3.2, instance.get(2), 0);
        assertEquals(-1.0, instance.defaultValue, 0);
        assertEquals("classValue", instance.classValue());
    }
    
    @Test
    public void testInitiate() {
        double[] datavector = {1.0, -1.5, 0.0, 3.2};
        sparseInstance.initiate(datavector);
        assertEquals(3, sparseInstance.noAttributes());
        assertEquals(1.0, sparseInstance.get(0), 0);
        assertEquals(-1.5, sparseInstance.get(1), 0);
        assertEquals(3.2, sparseInstance.get(3), 0);
        assertNull(sparseInstance.get(2));
    }
    
    @Test
    public void testValue() {
        sparseInstance.put(1, 1.0);
        assertEquals(1.0, sparseInstance.value(1), 0);
    }
    
    @Test
    public void testClear() {
        sparseInstance.put(1, 1.0);
        sparseInstance.put(2, -1.0);
        sparseInstance.clear();
        assertTrue(sparseInstance.isEmpty());
    }
    
    @Test
    public void testContainsKey() {
        sparseInstance.put(1, 1.0);
        assertTrue(sparseInstance.containsKey(1));
    }
    
    @Test
    public void testContainsValue() {
        sparseInstance.put(1, 1.0);
        assertTrue(sparseInstance.containsValue(1.0));
    }
    
    @Test
    public void testEntrySet() {
        sparseInstance.put(1, 1.0);
        Map.Entry<Integer, Double> entry = new HashMap.SimpleEntry<Integer, Double>(1, 1.0);
        assertTrue(sparseInstance.entrySet().contains(entry));
    }
    
    @Test
    public void testGet() {
        sparseInstance.put(1, 1.0);
        assertEquals(1.0, sparseInstance.get(1), 0);
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(sparseInstance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        sparseInstance.put(1, 1.0);
        assertTrue(sparseInstance.keySet().contains(1));
    }
    
    @Test
    public void testPut() {
        assertNull(sparseInstance.put(1, 1.0));
        assertEquals(1.0, sparseInstance.get(1), 0);
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(1, 1.0);
        map.put(2, -1.0);
        sparseInstance.putAll(map);
        assertEquals(2, sparseInstance.size());
        assertEquals(1.0, sparseInstance.get(1), 0);
        assertEquals(-1.0, sparseInstance.get(2), 0);
    }
    
    @Test
    public void testRemove() {
        sparseInstance.put(1, 1.0);
        assertEquals(1.0, sparseInstance.remove(1), 0);
        assertTrue(sparseInstance.isEmpty());
    }
    
    @Test
    public void testSize() {
        sparseInstance.put(1, 1.0);
        assertEquals(1, sparseInstance.size());
    }
    
    @Test
    public void testValues() {
        sparseInstance.put(1, 1.0);
        assertTrue(sparseInstance.values().contains(1.0));
    }
    
    @Test
    public void testNoAttributes() {
        sparseInstance.put(1, 1.0);
        assertEquals(2, sparseInstance.noAttributes());
    }
    
    @Test
    public void testRemoveAttribute() {
        sparseInstance.put(1, 1.0);
        sparseInstance.put(2, -1.0);
        sparseInstance.removeAttribute(1);
        assertEquals(1, sparseInstance.size());
        assertTrue(sparseInstance.containsKey(1));
    }
    
    @Test
    public void testToString() {
        sparseInstance.put(1, 1.0);
        assertNotNull(sparseInstance.toString());
    }
    
    @Test
    public void testHashCode() {
        sparseInstance.put(1, 1.0);
        assertNotNull(sparseInstance.hashCode());
    }
    
    @Test
    public void testEquals() {
        sparseInstance.put(1, 1.0);
        assertTrue(sparseInstance.equals(sparseInstance));
    }
    
    @Test
    public void testCopy() {
        sparseInstance.put(1, 1.0);
        SparseInstance copy = (SparseInstance) sparseInstance.copy();
        assertEquals(sparseInstance.noAttributes(), copy.noAttributes());
        assertEquals(sparseInstance.get(1), copy.get(1), 0);
        assertEquals(sparseInstance.defaultValue, copy.defaultValue, 0);
        assertEquals(sparseInstance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes() {
        sparseInstance.put(1, 1.0);
        sparseInstance.put(2, -1.0);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        sparseInstance.removeAttributes(indices);
        assertEquals(1, sparseInstance.size());
        assertTrue(sparseInstance.containsKey(1));
    }
}