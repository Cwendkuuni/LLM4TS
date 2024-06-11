
package net.sf.javaml.core;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.clear();
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0);
    }

    @Test
    public void testContainsKey() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> expected = new HashSet<>();
        expected.add(new AbstractMap.SimpleEntry<>(0, 1.0));
        expected.add(new AbstractMap.SimpleEntry<>(1, 2.0));
        expected.add(new AbstractMap.SimpleEntry<>(2, 3.0));
        assertEquals(expected, instance.entrySet());
    }

    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
        assertNull(instance.get(3));
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> expected = new HashSet<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        assertEquals(expected, instance.keySet());
    }

    @Test
    public void testPut() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.put(0, 4.0), 0.0);
        assertEquals(4.0, instance.value(0), 0.0);
    }

    @Test
    public void testPutAll() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        instance.putAll(map);
        assertEquals(4.0, instance.value(0), 0.0);
        assertEquals(5.0, instance.value(1), 0.0);
        assertEquals(6.0, instance.value(2), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.remove(0);
    }

    @Test
    public void testSize() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Collection<Double> expected = new ArrayList<>();
        expected.add(1.0);
        expected.add(2.0);
        expected.add(3.0);
        assertEquals(expected, instance.values());
    }

    @Test
    public void testNoAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        String expected = "{[1.0, 2.0, 3.0];null}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(1);
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0);
    }

    @Test
    public void testHashCode() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes);
        Double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(Arrays.stream(attributes2).mapToDouble(Double::doubleValue).toArray());
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes);
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes);
        DenseInstance instance2 = instance1.copy();
        assertNotSame(instance1, instance2);
        assertArrayEquals(instance1.getAttributes(), instance2.getAttributes(), 0.0);
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indicesToRemove = new HashSet<>();
        indicesToRemove.add(1);
        instance.removeAttributes(indicesToRemove);
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0);
    }

}
