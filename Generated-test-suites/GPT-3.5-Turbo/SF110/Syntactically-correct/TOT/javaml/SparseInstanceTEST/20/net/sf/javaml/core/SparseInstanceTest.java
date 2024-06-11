import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SparseInstanceTest {

    private SparseInstance instance;

    @Before
    public void setUp() {
        instance = new SparseInstance(5, 0.0);
    }

    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(10);
        assertEquals(10, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceNoAttributes() {
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceDefaultValue() {
        assertEquals(0.0, instance.get(0), 0.0);
    }

    @Test
    public void testSparseInstanceDataVector() {
        double[] dataVector = {0.0, 1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(dataVector, 0.0);
        for (int i = 0; i < dataVector.length; i++) {
            assertEquals(dataVector[i], instance.get(i), 0.0);
        }
    }

    @Test
    public void testValue() {
        instance.put(2, 2.0);
        assertEquals(2.0, instance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        instance.put(0, 1.0);
        assertTrue(instance.containsKey(0));
    }

    @Test
    public void testContainsValue() {
        instance.put(0, 1.0);
        assertTrue(instance.containsValue(1.0));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertTrue(entrySet.isEmpty());
    }

    @Test
    public void testGet() {
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.0);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        instance.put(0, 1.0);
        TreeSet<Integer> keySet = instance.keySet();
        assertEquals(1, keySet.size());
        assertTrue(keySet.contains(0));
    }

    @Test
    public void testPut() {
        assertNull(instance.put(0, 1.0));
        assertEquals(1.0, instance.get(0), 0.0);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
    }

    @Test
    public void testRemove() {
        instance.put(0, 1.0);
        assertEquals(1.0, instance.remove(0), 0.0);
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, instance.size());
        instance.put(0, 1.0);
        assertEquals(1, instance.size());
    }

    @Test
    public void testValues() {
        instance.put(0, 1.0);
        Collection<Double> values = instance.values();
        assertEquals(1, values.size());
        assertTrue(values.contains(1.0));
    }

    @Test
    public void testNoAttributes() {
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.removeAttribute(1);
        assertEquals(1.0, instance.get(0), 0.0);
        assertFalse(instance.containsKey(1));
        assertEquals(1, instance.noAttributes());
    }

    @Test
    public void testToString() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals("{0=1.0;null}", instance.toString());
    }

    @Test
    public void testHashCode() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(-1146865970, instance.hashCode());
    }

    @Test
    public void testEquals() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        SparseInstance anotherInstance = new SparseInstance(5, 0.0);
        anotherInstance.put(0, 1.0);
        anotherInstance.put(1, 2.0);

        assertTrue(instance.equals(anotherInstance));
    }

    @Test
    public void testCopy() {
        SparseInstance newInstance = (SparseInstance) instance.copy();
        assertEquals(instance.noAttributes(), newInstance.noAttributes());
        assertEquals(instance.get(0), newInstance.get(0), 0.0);
        assertEquals(instance.get(1), newInstance.get(1), 0.0);
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(1);
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        instance.removeAttributes(indices);
        assertEquals(0, instance.size());
    }

}