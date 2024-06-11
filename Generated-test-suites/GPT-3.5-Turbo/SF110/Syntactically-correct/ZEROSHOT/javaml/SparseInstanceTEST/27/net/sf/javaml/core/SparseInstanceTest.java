package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstance() {
        SparseInstance instance = new SparseInstance();
        assertNotNull(instance);
    }

    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance instance = new SparseInstance(10);
        assertEquals(10, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndDefaultValue() {
        SparseInstance instance = new SparseInstance(10, 0.5);
        assertEquals(10, instance.noAttributes());
        assertEquals(0.5, instance.defaultValue, 0.0);
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(10, classValue);
        assertEquals(10, instance.noAttributes());
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndDefaultValueAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(10, 0.5, classValue);
        assertEquals(10, instance.noAttributes());
        assertEquals(0.5, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithDataVector() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(datavector.length, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithDataVectorAndDefaultValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector, 0.5);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(0.5, instance.defaultValue, 0.0);
    }

    @Test
    public void testSparseInstanceWithDataVectorAndClassValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, classValue);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithDataVectorAndDefaultValueAndClassValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, 0.5, classValue);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(0.5, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testInitiate() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {0.1, 0.2, 0.0, 0.4};
        instance.initiate(datavector);
        assertEquals(datavector.length - 1, instance.noAttributes());
        assertFalse(instance.containsKey(2));
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 0.1);
        assertEquals(0.1, instance.value(1), 0.0);
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 0.1);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 0.1);
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 0.1);
        assertTrue(instance.containsValue(0.1));
        assertFalse(instance.containsValue(0.2));
    }

    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 0.1);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(1, entrySet.size());
    }

    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 0.1);
        assertEquals(0.1, instance.get(1), 0.0);
        assertEquals(instance.defaultValue, instance.get(2), 0.0);
    }

    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
        instance.put(1, 0.1);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 0.1);
        TreeSet<Integer> keySet = instance.keySet();
        assertEquals(1, keySet.size());
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        assertNull(instance.put(1, 0.1));
        assertEquals(0.1, instance.get(1), 0.0);
    }

    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 0.1);
        map.put(2, 0.2);
        instance.putAll(map);
        assertEquals(2, instance.size());
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 0.1);
        assertEquals(0.1, instance.remove(1), 0.0);
        assertNull(instance.remove(2));
    }

    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 0.1);
        assertEquals(1, instance.size());
    }

    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 0.1);
        Collection<Double> values = instance.values();
        assertEquals(1, values.size());
    }

    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 0.1);
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 0.1);
        instance.put(2, 0.2);
        instance.put(3, 0.3);
        instance.put(4, 0.4);
        instance.removeAttribute(2);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.containsKey(2));
        assertEquals(0.3, instance.get(1), 0.0);
        assertEquals(0.4, instance.get(3), 0.0);
    }

    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 0.1);
        String expected = "{" + instance.data.toString() + ";" + instance.classValue() + "}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(1, 0.1);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(1, 0.1);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(1, 0.1);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(1, 0.1);
        assertTrue(instance1.equals(instance2));

        SparseInstance instance3 = new SparseInstance();
        instance3.put(2, 0.2);
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testCopy() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(1, 0.1);
        SparseInstance instance2 = (SparseInstance) instance1.copy();
        assertEquals(instance1, instance2);
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 0.1);
        instance.put(2, 0.2);
        instance.put(3, 0.3);
        instance.put(4, 0.4);
        Set<Integer> indices = new HashSet<>();
        indices.add(2);
        indices.add(3);
        instance.removeAttributes(indices);
        assertEquals(2, instance.noAttributes());
        assertFalse(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
        assertEquals(0.1, instance.get(1), 0.0);
        assertEquals(0.4, instance.get(4), 0.0);
    }

}