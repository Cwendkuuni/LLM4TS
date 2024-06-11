
package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class DenseInstanceTest {

    private DenseInstance instance;
    private double[] attributes;

    @Before
    public void setUp() {
        attributes = new double[] {1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes);
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
        
        double[] clearedAttributes = new double[attributes.length];
        assertArrayEquals(clearedAttributes, instance.attributes, 0.0001);
    }

    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(0.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        
        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(attributes[key], value, 0.0001);
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
        
        DenseInstance emptyInstance = new DenseInstance(0);
        assertTrue(emptyInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        
        int index = 0;
        for (int key : keySet) {
            assertEquals(index++, key);
        }
    }

    @Test
    public void testPut() {
        Double oldValue = instance.put(0, 0.0);
        assertEquals(1.0, oldValue, 0.0001);
        
        assertEquals(0.0, instance.get(0), 0.0001);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 0.0);
        map.put(1, 1.0);
        map.put(2, 2.0);
        instance.putAll(map);

        assertEquals(0.0, instance.get(0), 0.0001);
        assertEquals(1.0, instance.get(1), 0.0001);
        assertEquals(2.0, instance.get(2), 0.0001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        instance.remove(0);
    }

    @Test
    public void testSize() {
        assertEquals(attributes.length, instance.size());
    }

    @Test
    public void testValues() {
        Collection<Double> values = instance.values();
        assertEquals(attributes.length, values.size());

        int index = 0;
        for (Double value : values) {
            assertEquals(attributes[index++], value, 0.0001);
        }
    }

    @Test
    public void testNoAttributes() {
        assertEquals(attributes.length, instance.noAttributes());
    }

    @Test
    public void testToString() {
        String expectedString = "{" + Arrays.toString(attributes) + ";" + instance.classValue() + "}";
        assertEquals(expectedString, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        int indexToRemove = 1;
        instance.removeAttribute(indexToRemove);

        double[] expectedAttributes = new double[] {1.0, 3.0};
        assertArrayEquals(expectedAttributes, instance.attributes, 0.0001);
    }

    @Test
    public void testHashCode() {
        double[] attributesCopy = attributes.clone();
        DenseInstance instanceCopy = new DenseInstance(attributesCopy);

        assertEquals(instance.hashCode(), instanceCopy.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributesCopy = attributes.clone();
        DenseInstance instanceCopy = new DenseInstance(attributesCopy);

        assertTrue(instance.equals(instanceCopy));
    }

    @Test
    public void testCopy() {
        DenseInstance instanceCopy = instance.copy();

        assertArrayEquals(instance.attributes, instanceCopy.attributes, 0.0001);
        assertEquals(instance.classValue(), instanceCopy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indicesToRemove = new HashSet<>();
        indicesToRemove.add(0);
        indicesToRemove.add(1);

        instance.removeAttributes(indicesToRemove);

        double[] expectedAttributes = new double[] {3.0};
        assertArrayEquals(expectedAttributes, instance.attributes, 0.0001);
    }
}
