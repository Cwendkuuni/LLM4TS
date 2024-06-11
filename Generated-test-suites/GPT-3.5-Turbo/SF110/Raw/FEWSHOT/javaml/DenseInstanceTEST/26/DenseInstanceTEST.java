
package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
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
    public void setUp() {
        double[] attributes = {1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes);
    }

    @Test
    public void testConstructor() {
        double[] attributes = instance.attributes;
        assertArrayEquals(new double[] {1.0, 2.0, 3.0}, attributes, 0.001);
        assertNull(instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        instance.setClassValue("class");
        assertEquals("class", instance.classValue());
    }

    @Test
    public void testValue() {
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
    }

    @Test
    public void testClear() {
        instance.clear();
        assertArrayEquals(new double[] {0.0, 0.0, 0.0}, instance.attributes, 0.001);
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
        Map<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(0, 1.0);
        expectedMap.put(1, 2.0);
        expectedMap.put(2, 3.0);
        assertEquals(expectedMap.entrySet(), entrySet);
    }

    @Test
    public void testGet() {
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(new TreeSet<>(Arrays.asList(0, 1, 2)), keySet);
    }

    @Test
    public void testPut() {
        instance.put(0, 9.0);
        assertEquals(9.0, instance.attributes[0], 0.001);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 9.0);
        map.put(1, 8.0);
        instance.putAll(map);
        assertEquals(9.0, instance.attributes[0], 0.001);
        assertEquals(8.0, instance.attributes[1], 0.001);
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
        assertEquals(Arrays.asList(1.0, 2.0, 3.0), values);
    }

    @Test
    public void testNoAttributes() {
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testToString() {
        assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(1);
        assertArrayEquals(new double[] {1.0, 3.0}, instance.attributes, 0.001);
    }

    @Test
    public void testHashCode() {
        int hashCode = instance.hashCode();
        assertEquals(1290250842, hashCode);
    }

    @Test
    public void testEquals() {
        DenseInstance instance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertTrue(instance.equals(instance2));
    }

    @Test
    public void testCopy() {
        DenseInstance copy = instance.copy();
        assertArrayEquals(new double[] {1.0, 2.0, 3.0}, copy.attributes, 0.001);
        assertEquals(null, copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new TreeSet<>(Arrays.asList(0, 2));
        instance.removeAttributes(indices);
        assertArrayEquals(new double[] {2.0}, instance.attributes, 0.001);
    }
}
