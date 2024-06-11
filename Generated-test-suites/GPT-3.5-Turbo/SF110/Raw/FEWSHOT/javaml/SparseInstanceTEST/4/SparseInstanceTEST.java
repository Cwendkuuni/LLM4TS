package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SparseInstanceTest {

    private SparseInstance instance;

    @Before
    public void setup() {
        instance = new SparseInstance(3, 1.0, null);
    }

    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testConstructor() {
        SparseInstance defaultInstance = new SparseInstance();
        assertEquals(-1, defaultInstance.noAttributes());
        assertNull(defaultInstance.classValue());

        SparseInstance instanceWithNoAttributes = new SparseInstance(5);
        assertEquals(5, instanceWithNoAttributes.noAttributes());
        assertNull(instanceWithNoAttributes.classValue());

        SparseInstance instanceWithDefaultValue = new SparseInstance(5, 2.0);
        assertEquals(5, instanceWithDefaultValue.noAttributes());
        assertNull(instanceWithDefaultValue.classValue());

        SparseInstance instanceWithClassValue = new SparseInstance(5, 2.0, "class");
        assertEquals(5, instanceWithClassValue.noAttributes());
        assertEquals("class", instanceWithClassValue.classValue());
    }

    @Test
    public void testDataEmptyConstructor() {
        SparseInstance defaultInstance = new SparseInstance();
        assertTrue(defaultInstance.isEmpty());
        assertEquals(0, defaultInstance.size());
    }

    @Test
    public void testDataConstructor() {
        double[] data = {1.0, 2.0, 3.0};
        SparseInstance instanceWithData = new SparseInstance(data);
        assertEquals(3, instanceWithData.noAttributes());
        assertFalse(instanceWithData.isEmpty());
        assertEquals(3, instanceWithData.size());
    }

    @Test
    public void testValue() {
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(0.0, instance.value(1), 0.0);
    }

    @Test
    public void testClear() {
        instance.clear();
        assertTrue(instance.isEmpty());
        assertEquals(0, instance.size());
    }

    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
    }

    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(2.0));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(1, entrySet.size());
    }

    @Test
    public void testGet() {
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(1.0, instance.get(1), 0.0);
        assertEquals(1.0, instance.get(2), 0.0);

        instance.remove(1);
        assertEquals(1.0, instance.get(0), 0.0);
        assertNull(instance.get(1));
        assertEquals(1.0, instance.get(2), 0.0);

        instance.put(1, 2.0);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(1.0, instance.get(2), 0.0);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        Set<Integer> keySet = instance.keySet();
        assertEquals(1, keySet.size());
    }

    @Test
    public void testPut() {
        instance.put(1, 2.0);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(0.0, instance.value(2), 0.0);
        assertEquals(2, instance.size());
    }
    
    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 2.0);
        map.put(2, 3.0);
        instance.putAll(map);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2, instance.value(1), 0.0);
        assertEquals(3, instance.value(2), 0.0);
        assertEquals(3, instance.size());
    }

    @Test
    public void testRemove() {
        assertEquals(1.0, instance.remove(0), 0.0);
        assertEquals(0.0, instance.remove(0), 0.0);
        assertNull(instance.remove(1));
        assertEquals(0, instance.size());
    }
    
    @Test
    public void testSize() {
        assertEquals(1, instance.size());
        instance.put(1, 2.0);
        assertEquals(2, instance.size());
    }

    @Test
    public void testValues() {
        instance.put(1, 2.0);
        assertEquals(1.0, instance.values().iterator().next(), 0.0);
        assertEquals(2.0, instance.values().iterator().next(), 0.0);
    }

    @Test
    public void testNoAttributes() {
        assertEquals(3, instance.noAttributes());
        instance.clear();
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(0);
        assertEquals(0.0, instance.value(0), 0.0);
        assertNull(instance.get(0));
        assertEquals(1, instance.size());
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testToString() {
        assertEquals("{0=1.0;null}", instance.toString());
        instance.put(1, 2.0);
        assertEquals("{0=1.0;1=2.0;null}", instance.toString());
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance(3, 1.0, null);
        assertTrue(instance.equals(instance1));

        instance.put(1, 2.0);
        assertFalse(instance.equals(instance1));

        SparseInstance instance2 = new SparseInstance(3, 1.0, null);
        instance2.put(1, 2.0);
        assertTrue(instance.equals(instance2));

        SparseInstance instance3 = new SparseInstance(3, 2.0, null);
        assertFalse(instance.equals(instance3));
    }

    @Test
    public void testCopy() {
        SparseInstance copyInstance = (SparseInstance) instance.copy();
        assertTrue(instance.equals(copyInstance));
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        instance.removeAttributes(indices);
        assertEquals(0.0, instance.value(0), 0.0);
        assertNull(instance.get(0));
        assertEquals(1, instance.size());
        assertEquals(0, instance.noAttributes());
    }
}