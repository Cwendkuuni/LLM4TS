package net.sf.javaml.core;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

    DenseInstance testInstance;

    @Before
    public void setUp() {
        double[] attributes = {1.0, 2.0, 3.0};
        testInstance = new DenseInstance(attributes, "ClassLabel");
    }

    @Test
    public void testValue() {
        assertEquals(2.0, testInstance.value(1), 0.001);
    }

    @Test
    public void testClear() {
        testInstance.clear();
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, testInstance.attributes, 0.001);
    }

    @Test
    public void testContainsKey() {
        assertTrue(testInstance.containsKey(1));
        assertFalse(testInstance.containsKey(5));
    }

    @Test
    public void testContainsValue() {
        assertTrue(testInstance.containsValue(1.0));
        assertFalse(testInstance.containsValue(5.0));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = testInstance.entrySet();
        assertEquals(3, entrySet.size());
    }

    @Test
    public void testGet() {
        assertEquals(2.0, testInstance.get(1), 0.001);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(testInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = testInstance.keySet();
        assertEquals(3, keySet.size());
    }

    @Test
    public void testPut() {
        assertEquals(2.0, testInstance.put(1, 5.0), 0.001);
        assertEquals(5.0, testInstance.attributes[1], 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        testInstance.remove(1);
    }

    @Test
    public void testSize() {
        assertEquals(3, testInstance.size());
    }

    @Test
    public void testValues() {
        Collection<Double> values = testInstance.values();
        assertEquals(3, values.size());
    }

    @Test
    public void testNoAttributes() {
        assertEquals(3, testInstance.noAttributes());
    }

    @Test
    public void testToString() {
        assertEquals("{[1.0, 2.0, 3.0];ClassLabel}", testInstance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        testInstance.removeAttribute(1);
        assertArrayEquals(new double[]{1.0, 3.0}, testInstance.attributes, 0.001);
    }

    @Test
    public void testHashCode() {
        assertEquals(1686653479, testInstance.hashCode());
    }

    @Test
    public void testEquals() {
        DenseInstance testInstance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassLabel");
        assertTrue(testInstance.equals(testInstance2));
    }

    @Test
    public void testCopy() {
        DenseInstance copyInstance = (DenseInstance) testInstance.copy();
        assertArrayEquals(testInstance.attributes, copyInstance.attributes, 0.001);
        assertEquals(testInstance.classValue(), copyInstance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> toRemove = new HashSet<>();
        toRemove.add(0);
        testInstance.removeAttributes(toRemove);
        assertArrayEquals(new double[]{2.0, 3.0}, testInstance.attributes, 0.001);
    }

}