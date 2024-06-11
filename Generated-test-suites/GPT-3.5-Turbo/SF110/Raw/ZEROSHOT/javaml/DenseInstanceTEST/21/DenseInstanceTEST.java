package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class DenseInstanceTest {
    private DenseInstance instance;

    @Before
    public void setup() {
        instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
    }

    @Test
    public void testValue() {
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
    }

    @Test
    public void testClear() {
        instance.clear();
        double[] attributes = instance.getAttributes();
        for (double attribute : attributes) {
            assertEquals(0.0, attribute, 0.0001);
        }
    }

    @Test
    public void testContainsKey() {
        assertFalse(instance.containsKey(-1));
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        assertFalse(instance.containsValue(-1.0));
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(instance.value(key), value, 0.0001);
        }
    }

    @Test
    public void testGet() {
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(3.0, instance.get(2), 0.0001);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        int index = 0;
        for (Integer key : keySet) {
            assertEquals(index, key.intValue());
            index++;
        }
    }

    @Test
    public void testPut() {
        assertEquals(1.0, instance.put(0, 10.0), 0.0001);
        assertEquals(10.0, instance.value(0), 0.0001);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 10.0);
        map.put(1, 20.0);
        map.put(2, 30.0);
        instance.putAll(map);
        assertEquals(10.0, instance.value(0), 0.0001);
        assertEquals(20.0, instance.value(1), 0.0001);
        assertEquals(30.0, instance.value(2), 0.0001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        instance.remove(0);
    }

    @Test
    public void testSize() {
        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        int index = 0;
        for (Double value : values) {
            assertEquals(instance.value(index), value, 0.0001);
            index++;
        }
    }

    @Test
    public void testNoAttributes() {
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testToString() {
        String expected = "{[1.0, 2.0, 3.0];null}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(1);
        assertEquals(2, instance.getAttributes().length);
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(3.0, instance.value(1), 0.0001);
    }

    @Test
    public void testHashCode() {
        DenseInstance other = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(instance.hashCode(), other.hashCode());
    }

    @Test
    public void testEquals() {
        DenseInstance other = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertTrue(instance.equals(other));
    }

    @Test
    public void testCopy() {
        DenseInstance copy = instance.copy();
        assertEquals(instance.toString(), copy.toString());
        assertNotSame(instance, copy);
        assertNotSame(instance.getAttributes(), copy.getAttributes());
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        instance.removeAttributes(indices);
        assertEquals(2, instance.getAttributes().length);
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(3.0, instance.value(1), 0.0001);
    }
}