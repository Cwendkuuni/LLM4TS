
package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithAttAndClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);

        assertArrayEquals(att, instance.getAttributes(), 0.0001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testConstructorWithSize() {
        int size = 5;
        DenseInstance instance = new DenseInstance(size);

        assertEquals(size, instance.noAttributes());
        assertArrayEquals(new double[size], instance.getAttributes(), 0.0001);
    }

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        for (int i = 0; i < att.length; i++) {
            assertEquals(att[i], instance.value(i), 0.0001);
        }
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        instance.clear();

        double[] expected = new double[att.length];
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
    }

    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        for (int i = 0; i < att.length; i++) {
            assertTrue(instance.containsKey(i));
        }

        assertFalse(instance.containsKey(att.length));
        assertFalse(instance.containsKey("key"));
    }

    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        for (double value : att) {
            assertTrue(instance.containsValue(value));
        }

        assertFalse(instance.containsValue(0.0));
        assertFalse(instance.containsValue("value"));
    }

    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();

        assertEquals(att.length, entrySet.size());
        for (int i = 0; i < att.length; i++) {
            assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(i, att[i])));
        }
    }

    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        for (int i = 0; i < att.length; i++) {
            assertEquals(att[i], instance.get(i), 0.0001);
        }
    }

    @Test
    public void testIsEmpty() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Set<Integer> keySet = instance.keySet();

        assertEquals(att.length, keySet.size());
        for (int i = 0; i < att.length; i++) {
            assertTrue(keySet.contains(i));
        }
    }

    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        int key = 1;
        double value = 5.0;

        Double previousValue = instance.put(key, value);

        assertEquals(att[key], previousValue, 0.0001);
        assertEquals(value, instance.get(key), 0.0001);
    }

    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 10.0);
        map.put(1, 20.0);

        instance.putAll(map);

        assertEquals(map.get(0), instance.get(0), 0.0001);
        assertEquals(map.get(1), instance.get(1), 0.0001);
        assertEquals(att[2], instance.get(2), 0.0001);
    }

    @Test
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertThrows(UnsupportedOperationException.class, () -> instance.remove(0));
    }

    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(att.length, instance.size());
    }

    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Collection<Double> values = instance.values();

        assertEquals(att.length, values.size());
        for (double value : att) {
            assertTrue(values.contains(value));
        }
    }

    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(att.length, instance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);

        String expected = "{" + Arrays.toString(att) + ";" + classValue + "}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        int index = 1;
        instance.removeAttribute(index);

        assertEquals(att.length - 1, instance.noAttributes());
        for (int i = 0; i < index; i++) {
            assertEquals(att[i], instance.value(i), 0.0001);
        }
        for (int i = index; i < att.length - 1; i++) {
            assertEquals(att[i + 1], instance.value(i), 0.0001);
        }
    }

    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(att);

        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(att);

        assertTrue(instance1.equals(instance2));
        assertTrue(instance2.equals(instance1));
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);

        DenseInstance copy = instance.copy();

        assertArrayEquals(att, copy.getAttributes(), 0.0001);
        assertEquals(classValue, copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);

        instance.removeAttributes(indices);

        double[] expected = {2.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
    }

}

