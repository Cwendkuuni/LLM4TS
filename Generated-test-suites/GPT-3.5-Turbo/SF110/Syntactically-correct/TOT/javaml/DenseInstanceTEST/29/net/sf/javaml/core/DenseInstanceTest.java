package net.sf.javaml.core;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {

    private DenseInstance denseInstance;

    @Before
    public void setUp() {
        denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
    }

    @Test
    public void testValue() {
        assertEquals(1.0, denseInstance.value(0), 0.01);

        try {
            denseInstance.value(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            denseInstance.value(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testClear() {
        denseInstance.clear();
        double[] expectedAttributes = new double[]{0.0, 0.0, 0.0};
        assertArrayEquals(expectedAttributes, denseInstance.getAttributes(), 0.01);
    }

    @Test
    public void testContainsKey() {
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsKey(1));
        assertTrue(denseInstance.containsKey(2));
        assertFalse(denseInstance.containsKey(-1));
        assertFalse(denseInstance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        assertTrue(denseInstance.containsValue(1.0));
        assertTrue(denseInstance.containsValue(2.0));
        assertTrue(denseInstance.containsValue(3.0));
        assertTrue(denseInstance.containsValue(1.0 + 1.0E-9)); // Testing precision
        assertFalse(denseInstance.containsValue(0.5));
        assertFalse(denseInstance.containsValue(4.0));
        assertFalse(denseInstance.containsValue("abc"));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> expectedEntrySet = new HashSet<>();
        expectedEntrySet.add(new AbstractMap.SimpleEntry<>(0, 1.0));
        expectedEntrySet.add(new AbstractMap.SimpleEntry<>(1, 2.0));
        expectedEntrySet.add(new AbstractMap.SimpleEntry<>(2, 3.0));

        Set<Map.Entry<Integer, Double>> actualEntrySet = denseInstance.entrySet();

        assertEquals(expectedEntrySet.size(), actualEntrySet.size());
        assertTrue(actualEntrySet.containsAll(expectedEntrySet));
    }

    @Test
    public void testGet() {
        assertEquals(Double.valueOf(1.0), denseInstance.get(0));
        assertEquals(Double.valueOf(2.0), denseInstance.get(1));
        assertEquals(Double.valueOf(3.0), denseInstance.get(2));

        assertNull(denseInstance.get(-1));
        assertNull(denseInstance.get(3));
    }

    @Test
    public void testIsEmpty() {
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SortedSet<Integer> expectedKeySet = new TreeSet<>();
        expectedKeySet.add(0);
        expectedKeySet.add(1);
        expectedKeySet.add(2);

        SortedSet<Integer> actualKeySet = denseInstance.keySet();

        assertEquals(expectedKeySet.size(), actualKeySet.size());
        assertTrue(actualKeySet.containsAll(expectedKeySet));
    }

    @Test
    public void testPut() {
        Double previousValue = denseInstance.put(0, 10.0);

        assertEquals(Double.valueOf(1.0), previousValue);
        assertEquals(Double.valueOf(10.0), denseInstance.get(0));
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> attributes = new HashMap<>();
        attributes.put(0, 10.0);
        attributes.put(1, 20.0);
        attributes.put(2, 30.0);

        denseInstance.putAll(attributes);

        assertEquals(Double.valueOf(10.0), denseInstance.get(0));
        assertEquals(Double.valueOf(20.0), denseInstance.get(1));
        assertEquals(Double.valueOf(30.0), denseInstance.get(2));
    }

    @Test
    public void testRemove() {
        try {
            denseInstance.remove(0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSize() {
        assertEquals(3, denseInstance.size());
    }

    @Test
    public void testValues() {
        Collection<Double> expectedValues = new ArrayList<>();
        expectedValues.add(1.0);
        expectedValues.add(2.0);
        expectedValues.add(3.0);

        Collection<Double> actualValues = denseInstance.values();

        assertEquals(expectedValues.size(), actualValues.size());
        assertTrue(actualValues.containsAll(expectedValues));
    }

    @Test
    public void testNoAttributes() {
        assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testToString() {
        String expectedString = "{[1.0, 2.0, 3.0]; null}";
        assertEquals(expectedString, denseInstance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        denseInstance.removeAttribute(1);

        assertEquals(Double.valueOf(1.0), denseInstance.get(0));
        assertEquals(Double.valueOf(3.0), denseInstance.get(1));
    }

    @Test
    public void testHashCode() {
        DenseInstance duplicate = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        assertEquals(duplicate.hashCode(), denseInstance.hashCode());
    }

    @Test
    public void testEquals() {
        DenseInstance duplicate = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        assertTrue(duplicate.equals(denseInstance));
        assertTrue(denseInstance.equals(duplicate));
    }

    @Test
    public void testCopy() {
        DenseInstance copiedInstance = denseInstance.copy();

        assertArrayEquals(denseInstance.getAttributes(), copiedInstance.getAttributes(), 0.01);
        assertEquals(denseInstance.getClassValue(), copiedInstance.getClassValue());
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indicesToRemove = new HashSet<>();
        indicesToRemove.add(1);

        denseInstance.removeAttributes(indicesToRemove);

        assertEquals(Double.valueOf(1.0), denseInstance.get(0));
        assertEquals(Double.valueOf(3.0), denseInstance.get(1));
        assertNull(denseInstance.get(2));
    }
}