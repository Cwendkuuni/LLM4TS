package net.sf.javaml.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;


public class SparseInstanceTest {

    private SparseInstance instance;
    private double[] dataVector;

    @Before
    public void setUp() {
        instance = new SparseInstance();
        dataVector = new double[]{0.0, 1.0, 2.0, 3.0};
        instance.initiate(dataVector);
    }

    @After
    public void tearDown() {
        instance = null;
        dataVector = null;
    }

    @Test
    public void testValue() {
        assertEquals(0.0, instance.value(0), 0.001);
        assertEquals(1.0, instance.value(1), 0.001);
        assertEquals(2.0, instance.value(2), 0.001);
        assertEquals(3.0, instance.value(3), 0.001);
    }

    @Test
    public void testClear() {
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertTrue(instance.containsKey(3));
        assertFalse(instance.containsKey(4));
    }

    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(0.0));
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        Map.Entry<Integer, Double> entry = instance.entrySet().iterator().next();
        assertEquals(0, (int)entry.getKey());
        assertEquals(0.0, entry.getValue(), 0.001);
    }

    @Test
    public void testGet() {
        assertEquals(0.0, instance.get(0), 0.001);
        assertEquals(1.0, instance.get(1), 0.001);
        assertEquals(2.0, instance.get(2), 0.001);
        assertEquals(3.0, instance.get(3), 0.001);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        assertTrue(instance.keySet().contains(0));
        assertTrue(instance.keySet().contains(1));
        assertTrue(instance.keySet().contains(2));
        assertTrue(instance.keySet().contains(3));
    }

    @Test
    public void testPut() {
        instance.put(4, 4.0);
        assertEquals(4.0, instance.get(4), 0.001);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(4, 4.0);
        map.put(5, 5.0);
        instance.putAll(map);
        assertEquals(4.0, instance.get(4), 0.001);
        assertEquals(5.0, instance.get(5), 0.001);
    }

    @Test
    public void testRemove() {
        instance.remove(2);
        assertFalse(instance.containsKey(2));
        assertNull(instance.get(2));
        assertEquals(3, instance.size());
    }

    @Test
    public void testSize() {
        assertEquals(4, instance.size());
        instance.remove(2);
        assertEquals(3, instance.size());
        instance.clear();
        assertEquals(0, instance.size());
    }

    @Test
    public void testValues() {
        assertTrue(instance.values().contains(0.0));
        assertTrue(instance.values().contains(1.0));
        assertTrue(instance.values().contains(2.0));
        assertTrue(instance.values().contains(3.0));
        assertFalse(instance.values().contains(4.0));
    }

    @Test
    public void testNoAttributes() {
        assertEquals(4, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(2);
        assertFalse(instance.containsKey(2));
        assertNull(instance.get(2));
        assertEquals(2, (int)instance.keySet().toArray()[1]);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testToString() {
        String expected = "{0=0.0; null}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 0.0);
        assertEquals(map.hashCode(), instance.hashCode());
    }

    @Test
    public void testEquals() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 0.0);
        SparseInstance other = new SparseInstance(4, 0.0, null);
        other.put(0, 0.0);
        assertEquals(instance, other);
    }

    @Test
    public void testCopy() {
        SparseInstance copy = (SparseInstance)instance.copy();
        assertEquals(instance, copy);
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> attributes = new HashSet<Integer>();
        attributes.add(2);
        attributes.add(3);
        instance.removeAttributes(attributes);
        assertFalse(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
        assertEquals(2, instance.noAttributes());
    }
}