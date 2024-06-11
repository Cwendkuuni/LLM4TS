import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {

    private SparseInstance instance;

    @Before
    public void setUp() {
        instance = new SparseInstance();
    }

    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstance() {
        assertNotNull(instance);
        assertEquals(-1, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceInt() {
        SparseInstance instance = new SparseInstance(5);
        assertNotNull(instance);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceIntDouble() {
        SparseInstance instance = new SparseInstance(5, 0.5);
        assertNotNull(instance);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.5, instance.get(0), 0.001);
    }

    @Test
    public void testSparseInstanceIntObject() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        assertNotNull(instance);
        assertEquals(5, instance.noAttributes());
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceIntDoubleObject() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 0.5, classValue);
        assertNotNull(instance);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.5, instance.get(0), 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceDoubleArray() {
        double[] dataVector = { 1.0, 2.0, 3.0 };
        instance = new SparseInstance(dataVector);
        assertNotNull(instance);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testSparseInstanceDoubleArrayDouble() {
        double[] dataVector = { 1.0, 2.0, 3.0 };
        instance = new SparseInstance(dataVector, 0.5);
        assertNotNull(instance);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        assertEquals(0.5, instance.defaultValue, 0.001);
    }

    @Test
    public void testSparseInstanceDoubleArrayObject() {
        Object classValue = new Object();
        double[] dataVector = { 1.0, 2.0, 3.0 };
        instance = new SparseInstance(dataVector, classValue);
        assertNotNull(instance);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceDoubleArrayDoubleObject() {
        Object classValue = new Object();
        double[] dataVector = { 1.0, 2.0, 3.0 };
        instance = new SparseInstance(dataVector, 0.5, classValue);
        assertNotNull(instance);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        assertEquals(0.5, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testValue() {
        instance.put(0, 1.0);
        assertEquals(1.0, instance.value(0), 0.001);
    }

    @Test
    public void testClear() {
        instance.put(0, 1.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        assertFalse(instance.containsKey(0));
        instance.put(0, 1.0);
        assertTrue(instance.containsKey(0));
    }

    @Test
    public void testContainsValue() {
        assertFalse(instance.containsValue(1.0));
        instance.put(0, 1.0);
        assertTrue(instance.containsValue(1.0));
    }

    @Test
    public void testEntrySet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Map.Entry<Integer, Double> entry1 = new HashMap.SimpleEntry<>(0, 1.0);
        Map.Entry<Integer, Double> entry2 = new HashMap.SimpleEntry<>(1, 2.0);
        assertTrue(instance.entrySet().contains(entry1));
        assertTrue(instance.entrySet().contains(entry2));
    }

    @Test
    public void testGet() {
        assertNull(instance.get(0));
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.001);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
        instance.put(0, 1.0);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.keySet().contains(0));
        assertTrue(instance.keySet().contains(1));
    }

    @Test
    public void testPut() {
        assertNull(instance.put(0, 1.0));
        assertEquals(1.0, instance.get(0), 0.001);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
    }

    @Test
    public void testRemove() {
        instance.put(0, 1.0);
        assertEquals(1.0, instance.remove(0), 0.001);
        assertFalse(instance.containsKey(0));
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
        instance.put(1, 2.0);
        assertTrue(instance.values().contains(1.0));
        assertTrue(instance.values().contains(2.0));
    }

    @Test
    public void testNoAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.removeAttribute(0);
        assertFalse(instance.containsKey(0));
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
        assertEquals(109506694, instance.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        SparseInstance copy = (SparseInstance) instance.copy();
        assertEquals(instance, copy);
    }

    @Test
    public void testRemoveAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttributes(new HashSet<>(Arrays.asList(0, 2)));
        assertFalse(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
        assertEquals(1, instance.noAttributes());
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testNoAttributesWhenDataIsEmpty() {
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testNoAttributesWhenDataIsNull() {
        instance = new SparseInstance(5, 0.5, null);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testRemoveNonexistentAttribute() {
        instance.put(0, 1.0);
        instance.removeAttribute(5);
        assertTrue(instance.containsKey(0));
        assertEquals(1, instance.noAttributes());
    }
}

