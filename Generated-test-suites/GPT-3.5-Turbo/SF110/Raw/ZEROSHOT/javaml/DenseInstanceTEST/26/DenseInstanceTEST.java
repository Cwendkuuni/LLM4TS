import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.001);
    }

    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        HashMap<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(0, 1.0);
        expectedMap.put(1, 2.0);
        expectedMap.put(2, 3.0);
        Set<Map.Entry<Integer, Double>> expected = expectedMap.entrySet();
        assertEquals(expected, instance.entrySet());
    }

    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
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
        TreeSet<Integer> expectedSet = new TreeSet<>();
        expectedSet.add(0);
        expectedSet.add(1);
        expectedSet.add(2);
        assertEquals(expectedSet, instance.keySet());
    }

    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.put(0, 4.0), 0.001);
        assertEquals(2.0, instance.put(1, 5.0), 0.001);
        assertEquals(3.0, instance.put(2, 6.0), 0.001);
        double[] expected = {4.0, 5.0, 6.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.001);
    }

    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        HashMap<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        instance.putAll(map);
        double[] expected = {4.0, 5.0, 6.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.001);
    }

    @Test
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        try {
            instance.remove(0);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Passed
        }
    }

    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Collection<Double> expected = new ArrayList<>();
        expected.add(1.0);
        expected.add(2.0);
        expected.add(3.0);
        assertEquals(expected, instance.values());
    }

    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        String expected = "{[1.0, 2.0, 3.0];null}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(1);
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.001);
    }

    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        int expected = Objects.hash(1.0, 2.0, 3.0);
        assertEquals(expected, instance.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(att);
        DenseInstance instance3 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = (DenseInstance) instance1.copy();
        assertTrue(instance1.equals(instance2));
        instance1.put(0, 4.0);
        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        instance.removeAttributes(indices);
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.001);
    }

}