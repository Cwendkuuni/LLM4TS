package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
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
        instance.setNoAttributes(10);
        assertEquals(10, instance.noAttributes());
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(-1, instance.noAttributes());
    }

    @Test
    public void testConstructorWithNoAttributes() {
        SparseInstance instanceWithNoAttributes = new SparseInstance(5);
        assertEquals(5, instanceWithNoAttributes.noAttributes());
        assertTrue(instanceWithNoAttributes.isEmpty());
    }

    @Test
    public void testConstructorWithNoAttributesAndDefaultValue() {
        SparseInstance instanceWithNoAttributesAndDefaultValue = new SparseInstance(5, 0.0);
        assertEquals(5, instanceWithNoAttributesAndDefaultValue.noAttributes());
        assertTrue(instanceWithNoAttributesAndDefaultValue.isEmpty());
    }

    @Test
    public void testConstructorWithNoAttributesAndClassValue() {
        Object classValue = new Object();
        SparseInstance instanceWithNoAttributesAndClassValue = new SparseInstance(5, classValue);
        assertEquals(5, instanceWithNoAttributesAndClassValue.noAttributes());
        assertTrue(instanceWithNoAttributesAndClassValue.isEmpty());
        assertEquals(classValue, instanceWithNoAttributesAndClassValue.classValue());
    }

    @Test
    public void testConstructorWithNoAttributesAndDefaultValueAndClassValue() {
        Object classValue = new Object();
        SparseInstance instanceWithNoAttributesAndDefaultValueAndClassValue = new SparseInstance(5, 0.0, classValue);
        assertEquals(5, instanceWithNoAttributesAndDefaultValueAndClassValue.noAttributes());
        assertTrue(instanceWithNoAttributesAndDefaultValueAndClassValue.isEmpty());
        assertEquals(classValue, instanceWithNoAttributesAndDefaultValueAndClassValue.classValue());
    }

    @Test
    public void testConstructorWithDataVector() {
        double[] dataVector = { 1.0, 2.0, 3.0 };
        SparseInstance instanceWithDataVector = new SparseInstance(dataVector);
        assertEquals(dataVector.length, instanceWithDataVector.noAttributes());
        assertFalse(instanceWithDataVector.isEmpty());
        for (int i = 0; i < dataVector.length; i++) {
            assertEquals(dataVector[i], instanceWithDataVector.value(i), 0.0);
        }
    }

    @Test
    public void testConstructorWithDataVectorAndDefaultValue() {
        double[] dataVector = { 1.0, 2.0, 3.0 };
        SparseInstance instanceWithDataVectorAndDefaultValue = new SparseInstance(dataVector, 0.0);
        assertEquals(dataVector.length, instanceWithDataVectorAndDefaultValue.noAttributes());
        assertFalse(instanceWithDataVectorAndDefaultValue.isEmpty());
        for (int i = 0; i < dataVector.length; i++) {
            assertEquals(dataVector[i], instanceWithDataVectorAndDefaultValue.value(i), 0.0);
        }
    }

    @Test
    public void testConstructorWithDataVectorAndClassValue() {
        double[] dataVector = { 1.0, 2.0, 3.0 };
        Object classValue = new Object();
        SparseInstance instanceWithDataVectorAndClassValue = new SparseInstance(dataVector, classValue);
        assertEquals(dataVector.length, instanceWithDataVectorAndClassValue.noAttributes());
        assertFalse(instanceWithDataVectorAndClassValue.isEmpty());
        for (int i = 0; i < dataVector.length; i++) {
            assertEquals(dataVector[i], instanceWithDataVectorAndClassValue.value(i), 0.0);
        }
        assertEquals(classValue, instanceWithDataVectorAndClassValue.classValue());
    }

    @Test
    public void testConstructorWithDataVectorAndDefaultValueAndClassValue() {
        double[] dataVector = { 1.0, 2.0, 3.0 };
        Object classValue = new Object();
        SparseInstance instanceWithDataVectorAndDefaultValueAndClassValue = new SparseInstance(dataVector, 0.0, classValue);
        assertEquals(dataVector.length, instanceWithDataVectorAndDefaultValueAndClassValue.noAttributes());
        assertFalse(instanceWithDataVectorAndDefaultValueAndClassValue.isEmpty());
        for (int i = 0; i < dataVector.length; i++) {
            assertEquals(dataVector[i], instanceWithDataVectorAndDefaultValueAndClassValue.value(i), 0.0);
        }
        assertEquals(classValue, instanceWithDataVectorAndDefaultValueAndClassValue.classValue());
    }

    @Test
    public void testInitiate() {
        double[] dataVector = { 1.0, 2.0, 3.0 };
        instance.initiate(dataVector);
        assertEquals(dataVector.length, instance.noAttributes());
        assertFalse(instance.isEmpty());
        for (int i = 0; i < dataVector.length; i++) {
            assertEquals(dataVector[i], instance.value(i), 0.0);
        }
    }

    @Test
    public void testValue() {
        int pos = 0;
        double value = 1.0;
        instance.put(pos, value);
        assertEquals(value, instance.value(pos), 0.0);
    }

    @Test
    public void testClear() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        int key = 0;
        instance.put(key, 1.0);
        assertTrue(instance.containsKey(key));
    }

    @Test
    public void testContainsValue() {
        double value = 1.0;
        instance.put(0, value);
        assertTrue(instance.containsValue(value));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertTrue(entrySet.isEmpty());
    }

    @Test
    public void testGet() {
        Integer key = 0;
        Double value = 1.0;
        instance.put(key, value);
        assertEquals(value, instance.get(key));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Set<Integer> keySet = instance.keySet();
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
    }

    @Test
    public void testPut() {
        Integer key = 0;
        Double value = 1.0;
        Double previousValue = instance.put(key, value);
        assertNull(previousValue);
        assertEquals(value, instance.get(key));
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(2, instance.size());
        assertEquals(map, instance.entrySet());
    }

    @Test
    public void testRemove() {
        instance.put(0, 1.0);
        Double removedValue = instance.remove(0);
        assertEquals(1.0, removedValue, 0.0);
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSize() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.size());
    }

    @Test
    public void testValues() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.values().contains(1.0));
        assertTrue(instance.values().contains(2.0));
    }

    @Test
    public void testNoAttributes() {
        instance.setNoAttributes(10);
        assertEquals(10, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttribute(1);
        assertFalse(instance.containsKey(1));
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testToString() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals("{0=1.0;null}", instance.toString());
    }

    @Test
    public void testHashCode() {
        SparseInstance sameInstance = new SparseInstance();
        assertEquals(instance.hashCode(), sameInstance.hashCode());
        sameInstance.setNoAttributes(5);
        assertNotEquals(instance.hashCode(), sameInstance.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance sameInstance = new SparseInstance();
        assertTrue(instance.equals(sameInstance));
        sameInstance.setNoAttributes(5);
        assertFalse(instance.equals(sameInstance));
    }

    @Test
    public void testCopy() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Instance copyInstance = instance.copy();
        assertEquals(instance, copyInstance);
        assertNotSame(instance, copyInstance);
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(1);
        indices.add(2);
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttributes(indices);
        assertTrue(instance.isEmpty());
    }
}