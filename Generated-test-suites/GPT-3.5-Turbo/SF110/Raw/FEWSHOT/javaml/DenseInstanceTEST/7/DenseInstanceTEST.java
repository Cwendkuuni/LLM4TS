package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import static org.junit.Assert.*;

public class DenseInstanceTest {

    DenseInstance denseInstance;

    @Before
    public void setup() {
        double[] att = {1.2, 3.4, 5.6};
        denseInstance = new DenseInstance(att);
    }

    @Test
    public void testValue() {
        assertEquals(1.2, denseInstance.value(0), 0.00001);
        assertEquals(3.4, denseInstance.value(1), 0.00001);
        assertEquals(5.6, denseInstance.value(2), 0.00001);
    }

    @Test
    public void testClear() {
        denseInstance.clear();
        double[] expectedAtt = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedAtt, denseInstance.getAttributes(), 0.00001);
    }

    @Test
    public void testContainsKey() {
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsKey(1));
        assertTrue(denseInstance.containsKey(2));

        assertFalse(denseInstance.containsKey(-1));
        assertFalse(denseInstance.containsKey(3));
        assertFalse(denseInstance.containsKey("key"));
    }

    @Test
    public void testContainsValue() {
        assertTrue(denseInstance.containsValue(1.2));
        assertTrue(denseInstance.containsValue(3.4));
        assertTrue(denseInstance.containsValue(5.6));

        assertFalse(denseInstance.containsValue(0.0));
        assertFalse(denseInstance.containsValue(1.0));
        assertFalse(denseInstance.containsValue("value"));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        assertEquals(3, entrySet.size());

        Map<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(0, 1.2);
        expectedMap.put(1, 3.4);
        expectedMap.put(2, 5.6);

        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertTrue(expectedMap.containsKey(entry.getKey()));
            assertEquals(expectedMap.get(entry.getKey()), entry.getValue(), 0.00001);
        }
    }

    @Test
    public void testGet() {
        assertEquals(1.2, denseInstance.get(0), 0.00001);
        assertEquals(3.4, denseInstance.get(1), 0.00001);
        assertEquals(5.6, denseInstance.get(2), 0.00001);

        assertNull(denseInstance.get(-1));
        assertNull(denseInstance.get(3));
        assertNull(denseInstance.get("key"));
    }

    @Test
    public void testIsEmpty() {
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SortedSet<Integer> keySet = denseInstance.keySet();
        assertEquals(3, keySet.size());

        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
        assertTrue(keySet.contains(2));

        assertFalse(keySet.contains(-1));
        assertFalse(keySet.contains(3));
        assertFalse(keySet.contains("key"));
    }

    @Test
    public void testPut() {
        assertEquals(1.2, denseInstance.put(0, 0.0), 0.00001);
        assertEquals(0.0, denseInstance.get(0), 0.00001);

        assertEquals(3.4, denseInstance.put(1, 2.2), 0.00001);
        assertEquals(2.2, denseInstance.get(1), 0.00001);

        assertEquals(5.6, denseInstance.put(2, 4.4), 0.00001);
        assertEquals(4.4, denseInstance.get(2), 0.00001);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> attributeMap = new HashMap<>();
        attributeMap.put(0, 0.0);
        attributeMap.put(1, 2.2);
        attributeMap.put(2, 4.4);

        denseInstance.putAll(attributeMap);

        assertEquals(3, denseInstance.size());
        assertEquals(0.0, denseInstance.get(0), 0.00001);
        assertEquals(2.2, denseInstance.get(1), 0.00001);
        assertEquals(4.4, denseInstance.get(2), 0.00001);
    }

    @Test
    public void testRemove() {
        assertThrows(UnsupportedOperationException.class, () -> {
            denseInstance.remove(0);
        });
    }

    @Test
    public void testSize() {
        assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testValues() {
        Collection<Double> values = denseInstance.values();
        assertEquals(3, values.size());

        assertTrue(values.contains(1.2));
        assertTrue(values.contains(3.4));
        assertTrue(values.contains(5.6));

        assertFalse(values.contains(0.0));
        assertFalse(values.contains(1.0));
        assertFalse(values.contains("value"));
    }

    @Test
    public void testCopy() {
        DenseInstance copyInstance = denseInstance.copy();
        assertArrayEquals(denseInstance.getAttributes(), copyInstance.getAttributes(), 0.00001);
        assertEquals(denseInstance.classValue(), copyInstance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(1);

        denseInstance.removeAttributes(indices);

        assertEquals(2, denseInstance.noAttributes());
        assertEquals(1.2, denseInstance.get(0), 0.00001);
        assertEquals(5.6, denseInstance.get(1), 0.00001);
    }


}