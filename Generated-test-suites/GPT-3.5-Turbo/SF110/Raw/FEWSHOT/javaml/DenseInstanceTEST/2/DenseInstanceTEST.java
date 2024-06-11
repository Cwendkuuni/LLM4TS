package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertArrayEquals(attributes, instance.getAttributes(), 0.0001);
        assertNull(instance.getClassValue());
    }

    @Test
    public void testConstructorWithAttributesAndClassValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        String classValue = "class";
        DenseInstance instance = new DenseInstance(attributes, classValue);

        assertArrayEquals(attributes, instance.getAttributes(), 0.0001);
        assertEquals(classValue, instance.getClassValue());
    }

    @Test
    public void testConstructorWithSize() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);

        assertArrayEquals(new double[size], instance.getAttributes(), 0.0001);
        assertNull(instance.getClassValue());
    }

    @Test
    public void testValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);
    }

    @Test
    public void testClear() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        instance.clear();
        assertArrayEquals(new double[3], instance.getAttributes(), 0.0001);
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

        HashMap<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(0, 1.0);
        expectedMap.put(1, 2.0);
        expectedMap.put(2, 3.0);

        assertEquals(expectedMap.entrySet(), instance.entrySet());
    }

    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(3.0, instance.get(2), 0.0001);
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

        SortedSet<Integer> expectedKeys = new TreeSet<>();
        expectedKeys.add(0);
        expectedKeys.add(1);
        expectedKeys.add(2);

        assertEquals(expectedKeys, instance.keySet());
    }

    @Test
    public void testPut() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertEquals(1.0, instance.put(0, 4.0), 0.0001);
        assertEquals(2.0, instance.put(1, 5.0), 0.0001);
        assertEquals(3.0, instance.put(2, 6.0), 0.0001);

        assertArrayEquals(new double[]{4.0, 5.0, 6.0}, instance.getAttributes(), 0.0001);
    }

    @Test
    public void testPutAll() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        Map<Integer, Double> newAttributes = new HashMap<>();
        newAttributes.put(0, 4.0);
        newAttributes.put(1, 5.0);
        newAttributes.put(2, 6.0);

        instance.putAll(newAttributes);

        assertArrayEquals(new double[]{4.0, 5.0, 6.0}, instance.getAttributes(), 0.0001);
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

        Collection<Double> expectedValues = new ArrayList<>();
        expectedValues.add(1.0);
        expectedValues.add(2.0);
        expectedValues.add(3.0);

        assertEquals(expectedValues, instance.values());
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

        assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        instance.removeAttribute(1);

        assertArrayEquals(new double[]{1.0, 3.0}, instance.getAttributes(), 0.0001);
    }

    @Test
    public void testHashCode() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        int expectedHashCode = 31 * 1 + Arrays.hashCode(new double[]{1.0, 2.0, 3.0});

        assertEquals(expectedHashCode, instance.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);

        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(attributes2);

        double[] attributes3 = {4.0, 5.0, 6.0};
        DenseInstance instance3 = new DenseInstance(attributes3);

        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        DenseInstance copy = (DenseInstance) instance.copy();

        assertArrayEquals(attributes, copy.getAttributes(), 0.0001);
        assertNull(copy.getClassValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        Set<Integer> indicesToRemove = new HashSet<>();
        indicesToRemove.add(0);
        indicesToRemove.add(2);

        instance.removeAttributes(indicesToRemove);

        assertArrayEquals(new double[]{2.0}, instance.getAttributes(), 0.0001);
    }
}