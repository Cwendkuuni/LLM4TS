package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testDenseInstance() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertNotNull(instance);
        assertEquals(1.0, instance.value(0), 0.0001);
        assertEquals(2.0, instance.value(1), 0.0001);
        assertEquals(3.0, instance.value(2), 0.0001);

        DenseInstance instance2 = new DenseInstance(att, "classValue");
        assertNotNull(instance2);
        assertEquals(1.0, instance2.value(0), 0.0001);
        assertEquals(2.0, instance2.value(1), 0.0001);
        assertEquals(3.0, instance2.value(2), 0.0001);
        assertEquals("classValue", instance2.classValue());
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        for (int i = 0; i < att.length; i++) {
            assertEquals(0.0, instance.value(i), 0.0001);
        }
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
        assertFalse(instance.containsValue(4.0));
        assertFalse(instance.containsValue(0.5));
    }

    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        assertEquals(3, entries.size());
        for (Map.Entry<Integer, Double> entry : entries) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(att[key], value, 0.0001);
        }
    }

    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(3.0, instance.get(2), 0.0001);
        assertNull(instance.get(3));
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
        SortedSet<Integer> keys = instance.keySet();
        assertEquals(3, keys.size());
        int index = 0;
        for (Integer key : keys) {
            assertEquals(index, (int) key);
            index++;
        }
    }

    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        double oldValue = instance.put(0, 10.0);
        assertEquals(1.0, oldValue, 0.0001);
        double newValue = instance.value(0);
        assertEquals(10.0, newValue, 0.0001);
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
        for (int i = 0; i < att.length; i++) {
            assertEquals(map.get(i), instance.value(i), 0.0001);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.remove(0);
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
        int index = 0;
        for (Double value : values) {
            assertEquals(att[index], value, 0.0001);
            index++;
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
        DenseInstance instance = new DenseInstance(att);
        String expectedString = "{" + Arrays.toString(att) + ";" + null + "}";
        assertEquals(expectedString, instance.toString());

        DenseInstance instance2 = new DenseInstance(att, "classValue");
        String expectedString2 = "{" + Arrays.toString(att) + ";classValue}";
        assertEquals(expectedString2, instance2.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        int attributeIndex = 1;
        instance.removeAttribute(attributeIndex);
        assertEquals(att.length - 1, instance.size());
        for (int i = 0; i < attributeIndex; i++) {
            assertEquals(att[i], instance.value(i), 0.0001);
        }
        for (int i = attributeIndex; i < att.length - 1; i++) {
            assertEquals(att[i + 1], instance.value(i), 0.0001);
        }
    }

    @Test
    public void testHashCode() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        double[] att3 = {1.1, 2.0, 3.0};

        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        DenseInstance instance3 = new DenseInstance(att3);

        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        double[] att3 = {1.1, 2.0, 3.0};

        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        DenseInstance instance3 = new DenseInstance(att3);

        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        DenseInstance copiedInstance = instance.copy();
        assertEquals(instance.toString(), copiedInstance.toString());
        assertNotSame(instance, copiedInstance);
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0, 4.0};
        DenseInstance instance = new DenseInstance(att);

        Set<Integer> removeIndices = new HashSet<>();
        removeIndices.add(1);
        removeIndices.add(3);

        instance.removeAttributes(removeIndices);

        assertEquals(att.length - removeIndices.size(), instance.size());

        int index = 0;
        for (int i = 0; i < att.length; i++) {
            if (!removeIndices.contains(i)) {
                assertEquals(att[i], instance.value(index), 0.0001);
                index++;
            }
        }
    }

}