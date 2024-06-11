package net.sf.javaml.core;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstance1() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
    }

    @Test
    public void testSparseInstance2() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
    }

    @Test
    public void testSparseInstance3() {
        SparseInstance instance = new SparseInstance(5, 1.0);
        assertEquals(5, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
    }

    @Test
    public void testSparseInstance4() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstance5() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 1.0, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstance6() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
    }

    @Test
    public void testSparseInstance7() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 1.0);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
    }

    @Test
    public void testSparseInstance8() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, classValue);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstance9() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, 1.0, classValue);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.value(0), 0.001);
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
    }

    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(2.0));
    }

    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(1, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(0, (int)entry.getKey());
            assertEquals(1.0, entry.getValue(), 0.001);
        }
    }

    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(0.0, instance.get(1), 0.001);
    }

    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
        instance.put(0, 1.0);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        TreeSet<Integer> keySet = instance.keySet();
        assertEquals(1, keySet.size());
        for (int key : keySet) {
            assertEquals(0, key);
        }
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        assertNull(instance.put(0, 1.0));
        assertEquals(1.0, instance.get(0), 0.001);
    }

    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.remove(0), 0.001);
        assertEquals(0.0, instance.get(0), 0.001);
    }

    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.size());
        instance.put(0, 1.0);
        assertEquals(1, instance.size());
    }

    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        Collection<Double> values = instance.values();
        assertEquals(1, values.size());
        for (double value : values) {
            assertEquals(1.0, value, 0.001);
        }
    }

    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
        instance.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(3.0, instance.get(1), 0.001);
    }

    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.setClassValue(2.0);
        assertEquals("{0=1.0;2.0}", instance.toString());
    }

    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        assertEquals(instance1.hashCode(), instance2.hashCode());
        instance1.put(0, 1.0);
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
        instance2.put(0, 1.0);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        assertTrue(instance1.equals(instance2));
        instance1.put(0, 1.0);
        assertFalse(instance1.equals(instance2));
        instance2.put(0, 1.0);
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        SparseInstance copy = (SparseInstance)instance.copy();
        assertEquals(instance, copy);
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        instance.removeAttributes(indices);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertEquals(2, instance.noAttributes());
    }

}