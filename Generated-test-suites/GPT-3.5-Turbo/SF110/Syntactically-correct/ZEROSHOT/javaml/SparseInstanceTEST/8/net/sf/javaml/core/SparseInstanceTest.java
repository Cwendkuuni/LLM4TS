import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SparseInstanceTest {

    private SparseInstance instance;

    @Before
    public void setUp() {
        instance = new SparseInstance();
    }

    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(10);
        assertEquals(10, instance.noAttributes());
    }

    @Test
    public void testSparseInstance_noAttributes() {
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testSparseInstance_noAttributes_defaultValue() {
        SparseInstance instance = new SparseInstance(10, 1.0);
        assertEquals(10, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.0);
    }

    @Test
    public void testSparseInstance_noAttributes_classValue() {
        SparseInstance instance = new SparseInstance(10, "class1");
        assertEquals(10, instance.noAttributes());
        assertEquals("class1", instance.classValue());
    }

    @Test
    public void testSparseInstance_noAttributes_defaultValue_classValue() {
        SparseInstance instance = new SparseInstance(10, 1.0, "class1");
        assertEquals(10, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.0);
        assertEquals("class1", instance.classValue());
    }

    @Test
    public void testSparseInstance_datavector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testSparseInstance_datavector_defaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.0);
    }

    @Test
    public void testSparseInstance_datavector_classValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, "class1");
        assertEquals(3, instance.noAttributes());
        assertEquals("class1", instance.classValue());
    }

    @Test
    public void testSparseInstance_datavector_defaultValue_classValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "class1");
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.0);
        assertEquals("class1", instance.classValue());
    }

    @Test
    public void testValue() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);

        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);

        instance.clear();

        assertEquals(0, instance.size());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);

        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);

        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);

        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();

        assertEquals(3, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(0, 1.0)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(1, 2.0)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(2, 3.0)));
    }

    @Test
    public void testGet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);

        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
        assertEquals(0.0, instance.get(3), 0.0);
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
        instance.put(2, 3.0);

        TreeSet<Integer> keySet = instance.keySet();

        assertEquals(3, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
        assertTrue(keySet.contains(2));
    }

    @Test
    public void testPut() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
    }

    @Test
    public void testPutAll() {
        HashMap<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);

        instance.putAll(map);

        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
    }

    @Test
    public void testRemove() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        instance.remove(0);

        assertFalse(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
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
        instance.put(2, 3.0);

        Collection<Double> values = instance.values();

        assertEquals(3, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
    }

    @Test
    public void testNoAttributes() {
        instance.put(0, 1.0);
        instance.put(3, 2.0);

        assertEquals(4, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);

        instance.removeAttribute(0);

        assertFalse(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertEquals(2, instance.noAttributes());
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

        int hashCode = instance.hashCode();

        assertEquals(hashCode, instance.hashCode());
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(instance.equals(instance));
    }

    @Test
    public void testEquals_nullObject() {
        assertFalse(instance.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        assertFalse(instance.equals("test"));
    }

    @Test
    public void testEquals_differentData() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);

        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 3.0);
        instance2.put(1, 4.0);

        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testEquals_sameData() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);

        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);

        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testEquals_sameData_differentDefaultValue() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);

        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);
        instance2.defaultValue = 0.0;

        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        SparseInstance copy = (SparseInstance) instance.copy();

        assertEquals(instance, copy);
        assertNotSame(instance, copy);
    }

    @Test
    public void testRemoveAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);

        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(1);

        instance.removeAttributes(indices);

        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
    }
}