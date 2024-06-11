package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructor1() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertNotNull(instance);
        assertArrayEquals(att, instance.getAttributes(), 0.1);
        assertNull(instance.getClassValue());
    }

    @Test
    public void testConstructor2() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);

        assertNotNull(instance);
        assertArrayEquals(att, instance.getAttributes(), 0.1);
        assertEquals(classValue, instance.getClassValue());
    }

    @Test
    public void testConstructor3() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);

        assertNotNull(instance);
        assertEquals(size, instance.getAttributes().length);
        assertNull(instance.getClassValue());
    }

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(att[0], instance.value(0), 0.1);
        assertEquals(att[1], instance.value(1), 0.1);
        assertEquals(att[2], instance.value(2), 0.1);
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        instance.clear();
        assertArrayEquals(new double[att.length], instance.getAttributes(), 0.1);
    }

    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
        assertFalse(instance.containsKey(-1));
    }

    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(0.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(att.length, entrySet.size());

        for (Map.Entry<Integer, Double> entry : entrySet) {
            int index = entry.getKey();
            double value = entry.getValue();
            assertEquals(att[index], value, 0.1);
        }
    }

    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(att[0], instance.get(0), 0.1);
        assertEquals(att[1], instance.get(1), 0.1);
        assertEquals(att[2], instance.get(2), 0.1);
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

        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(att.length, keySet.size());

        for (int key : keySet) {
            assertTrue(instance.containsKey(key));
        }
    }

    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        int index = 0;
        double value = 10.0;
        Double oldValue = instance.put(index, value);

        assertEquals(oldValue, Double.valueOf(att[index]));
        assertEquals(value, instance.value(index), 0.1);
    }

    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 10.0);
        map.put(1, 20.0);
        map.put(2, 30.0);

        instance.putAll(map);

        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            int index = entry.getKey();
            double value = entry.getValue();
            assertEquals(value, instance.value(index), 0.1);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        int index = 0;
        instance.remove(index);
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

        for (double value : values) {
            assertTrue(instance.containsValue(value));
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

        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.1);
    }

    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        int expectedHash = 31 * Arrays.hashCode(att);
        assertEquals(expectedHash, instance.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        double[] att3 = {1.0, 2.0, 4.0};
        Object classValue1 = "class";
        Object classValue2 = "class";

        DenseInstance instance1 = new DenseInstance(att1, classValue1);
        DenseInstance instance2 = new DenseInstance(att2, classValue1);
        DenseInstance instance3 = new DenseInstance(att3, classValue2);

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(new Object()));
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);

        DenseInstance copy = (DenseInstance) instance.copy();

        assertNotSame(instance, copy);
        assertArrayEquals(att, copy.getAttributes(), 0.1);
        assertEquals(classValue, copy.getClassValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Set<Integer> indices = new HashSet<>();
        indices.add(1);

        instance.removeAttributes(indices);

        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.1);
    }
}