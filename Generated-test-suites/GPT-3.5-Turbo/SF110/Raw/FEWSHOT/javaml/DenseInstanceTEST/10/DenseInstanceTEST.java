
package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class DenseInstanceTest {

    private DenseInstance instance;

    @Before
    public void setup() {
        double[] attributes = { 1.0, 2.0, 3.0 };
        instance = new DenseInstance(attributes);
    }

    @Test
    public void testValue() {
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testValueOutOfRange() {
        instance.value(3);
    }

    @Test
    public void testClear() {
        instance.clear();
        double[] attributes = { 0.0, 0.0, 0.0 };
        assertTrue(Arrays.equals(attributes, instance.getAttributes()));
    }

    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(instance.noAttributes(), entrySet.size());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertTrue(entrySet.contains(new HashMap.SimpleEntry<>(i, instance.value(i))));
        }
    }

    @Test
    public void testGet() {
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
        assertNull(instance.get(3));
    }

    @Test
    public void testIsEmpty() {
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(instance.noAttributes(), keySet.size());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertTrue(keySet.contains(i));
        }
    }

    @Test
    public void testPut() {
        assertEquals(1.0, instance.put(0, 4.0), 0.0);
        assertEquals(4.0, instance.value(0), 0.0);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        instance.putAll(map);
        assertEquals(4.0, instance.value(0), 0.0);
        assertEquals(5.0, instance.value(1), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        instance.remove(0);
    }

    @Test
    public void testSize() {
        assertEquals(instance.noAttributes(), instance.size());
    }

    @Test
    public void testValues() {
        assertEquals(Arrays.asList(1.0, 2.0, 3.0), instance.values());
    }

    @Test
    public void testNoAttributes() {
        assertEquals(instance.getAttributes().length, instance.noAttributes());
    }

    @Test
    public void testToString() {
        String expected = "{" + Arrays.toString(instance.getAttributes()) + ";" + instance.classValue() + "}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(0);
        double[] attributes = { 2.0, 3.0 };
        assertTrue(Arrays.equals(attributes, instance.getAttributes()));
    }

    @Test
    public void testHashCode() {
        int expected = 31 * 1 + Arrays.hashCode(instance.getAttributes());
        assertEquals(expected, instance.hashCode());
    }

    @Test
    public void testEquals() {
        DenseInstance sameInstance = new DenseInstance(instance.getAttributes(), instance.classValue());
        assertTrue(instance.equals(sameInstance));
        assertTrue(sameInstance.equals(instance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));
    }

    @Test
    public void testCopy() {
        DenseInstance copy = (DenseInstance) instance.copy();
        assertTrue(Arrays.equals(instance.getAttributes(), copy.getAttributes()));
        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new TreeSet<>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        double[] attributes = { 2.0 };
        assertTrue(Arrays.equals(attributes, instance.getAttributes()));
    }
}
