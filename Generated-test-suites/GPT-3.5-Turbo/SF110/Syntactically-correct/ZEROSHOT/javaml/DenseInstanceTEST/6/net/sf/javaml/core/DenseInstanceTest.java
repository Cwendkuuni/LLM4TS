package net.sf.javaml.core;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {

    DenseInstance denseInstance;

    @Before
    public void setUp() {
        double[] att = {1.0, 2.0, 3.0};
        denseInstance = new DenseInstance(att);
    }

    @Test
    public void testValue() {
        assertEquals(1.0, denseInstance.value(0), 0.0001);
        assertEquals(2.0, denseInstance.value(1), 0.0001);
        assertEquals(3.0, denseInstance.value(2), 0.0001);
    }

    @Test
    public void testClear() {
        denseInstance.clear();
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, denseInstance.getAttributes(), 0.0001);
    }

    @Test
    public void testContainsKey() {
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsKey(1));
        assertTrue(denseInstance.containsKey(2));
        assertFalse(denseInstance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        assertTrue(denseInstance.containsValue(1.0));
        assertTrue(denseInstance.containsValue(2.0));
        assertTrue(denseInstance.containsValue(3.0));
        assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        assertEquals(3, entrySet.size());
        for(Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(denseInstance.value(key), value, 0.0001);
        }
    }

    @Test
    public void testGet() {
        assertEquals(1.0, denseInstance.get(0), 0.0001);
        assertEquals(2.0, denseInstance.get(1), 0.0001);
        assertEquals(3.0, denseInstance.get(2), 0.0001);
        assertNull(denseInstance.get(3));
    }

    @Test
    public void testIsEmpty() {
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = denseInstance.keySet();
        assertEquals(3, keySet.size());
        int i = 0;
        for(Integer key : keySet) {
            assertEquals(i, key.intValue());
            i++;
        }
    }

    @Test
    public void testPut() {
        assertNull(denseInstance.put(0, 0.0));
        assertEquals(0.0, denseInstance.value(0), 0.0001);
    }

    @Test
    public void testPutAll() {
        HashMap<Integer, Double> map = new HashMap<>();
        map.put(0, 0.0);
        map.put(1, 0.0);
        map.put(2, 0.0);
        denseInstance.putAll(map);
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, denseInstance.getAttributes(), 0.0001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        denseInstance.remove(0);
    }

    @Test
    public void testSize() {
        assertEquals(3, denseInstance.size());
    }

    @Test
    public void testValues() {
        Collection<Double> values = denseInstance.values();
        assertEquals(3, values.size());
        int i = 0;
        for(Double value : values) {
            assertEquals(denseInstance.value(i), value, 0.0001);
            i++;
        }
    }

    @Test
    public void testNoAttributes() {
        assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testToString() {
        String expected = "{[1.0, 2.0, 3.0];null}";
        assertEquals(expected, denseInstance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        denseInstance.removeAttribute(1);
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, denseInstance.getAttributes(), 0.0001);
    }

    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance other = new DenseInstance(att);
        assertEquals(denseInstance.hashCode(), other.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance other = new DenseInstance(att);
        assertTrue(denseInstance.equals(other));
    }

    @Test
    public void testCopy() {
        DenseInstance copy = (DenseInstance)denseInstance.copy();
        assertArrayEquals(denseInstance.getAttributes(), copy.getAttributes(), 0.0001);
        assertEquals(denseInstance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        denseInstance.removeAttributes(indices);
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, denseInstance.getAttributes(), 0.0001);
    }

    @After
    public void tearDown() {
        denseInstance = null;
    }
}

