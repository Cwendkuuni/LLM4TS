import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

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
    public void testSparseInstance1() {
        SparseInstance newInstance = new SparseInstance(5);
        assertEquals(5, newInstance.noAttributes());
        assertEquals(0.0, newInstance.defaultValue, 0.001);
        assertNull(newInstance.classValue());
        assertTrue(newInstance.isEmpty());
    }

    @Test
    public void testSparseInstance2() {
        SparseInstance newInstance = new SparseInstance(5, 0.5);
        assertEquals(5, newInstance.noAttributes());
        assertEquals(0.5, newInstance.defaultValue, 0.001);
        assertNull(newInstance.classValue());
        assertTrue(newInstance.isEmpty());
    }

    @Test
    public void testSparseInstance3() {
        Object classValue = new Object();
        SparseInstance newInstance = new SparseInstance(5, classValue);
        assertEquals(5, newInstance.noAttributes());
        assertEquals(0.0, newInstance.defaultValue, 0.001);
        assertEquals(classValue, newInstance.classValue());
        assertTrue(newInstance.isEmpty());
    }

    @Test
    public void testSparseInstance4() {
        Object classValue = new Object();
        SparseInstance newInstance = new SparseInstance(5, 0.5, classValue);
        assertEquals(5, newInstance.noAttributes());
        assertEquals(0.5, newInstance.defaultValue, 0.001);
        assertEquals(classValue, newInstance.classValue());
        assertTrue(newInstance.isEmpty());
    }

    @Test
    public void testSparseInstance5() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance newInstance = new SparseInstance(datavector);
        assertEquals(3, newInstance.noAttributes());
        assertEquals(0.0, newInstance.defaultValue, 0.001);
        assertNull(newInstance.classValue());
        assertEquals(3.0, newInstance.get(2), 0.001);
    }

    @Test
    public void testSparseInstance6() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance newInstance = new SparseInstance(datavector, 0.5);
        assertEquals(3, newInstance.noAttributes());
        assertEquals(0.5, newInstance.defaultValue, 0.001);
        assertNull(newInstance.classValue());
        assertEquals(3.0, newInstance.get(2), 0.001);
    }

    @Test
    public void testSparseInstance7() {
        Object classValue = new Object();
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance newInstance = new SparseInstance(datavector, classValue);
        assertEquals(3, newInstance.noAttributes());
        assertEquals(0.0, newInstance.defaultValue, 0.001);
        assertEquals(classValue, newInstance.classValue());
        assertEquals(3.0, newInstance.get(2), 0.001);
    }

    @Test
    public void testSparseInstance8() {
        Object classValue = new Object();
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance newInstance = new SparseInstance(datavector, 0.5, classValue);
        assertEquals(3, newInstance.noAttributes());
        assertEquals(0.5, newInstance.defaultValue, 0.001);
        assertEquals(classValue, newInstance.classValue());
        assertEquals(3.0, newInstance.get(2), 0.001);
    }

    @Test
    public void testInitiate() {
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        assertFalse(instance.isEmpty());
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
    }

    @Test
    public void testValue() {
        instance.put(1, 2.0);
        assertEquals(2.0, instance.value(1), 0.001);
    }

    @Test
    public void testClear() {
        instance.put(1, 2.0);
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        instance.put(1, 2.0);
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        instance.put(1, 2.0);
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(3.0));
    }

    @Test
    public void testEntrySet() {
        instance.put(1, 2.0);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(1, entrySet.size());
        Map.Entry<Integer, Double> entry = entrySet.iterator().next();
        assertEquals(1, (int) entry.getKey());
        assertEquals(2.0, entry.getValue(), 0.001);
    }

    @Test
    public void testGet() {
        instance.put(1, 2.0);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(0.0, instance.get(2), 0.001);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
        instance.put(1, 2.0);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        TreeSet<Integer> keySet = instance.keySet();
        assertEquals(2, keySet.size());
        Iterator<Integer> iterator = keySet.iterator();
        assertEquals(1, (int) iterator.next());
        assertEquals(2, (int) iterator.next());
    }

    @Test
    public void testPut() {
        assertEquals(0.0, instance.put(1, 2.0), 0.001);
        assertEquals(2.0, instance.put(1, 3.0), 0.001);
        assertEquals(3.0, instance.get(1), 0.001);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 2.0);
        map.put(2, 3.0);
        instance.putAll(map);
        assertEquals(2, instance.size());
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testRemove() {
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals(2.0, instance.remove(1), 0.001);
        assertFalse(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
    }

    @Test
    public void testSize() {
        assertEquals(0, instance.size());
        instance.put(1, 2.0);
        assertEquals(1, instance.size());
    }

    @Test
    public void testValues() {
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Collection<Double> values = instance.values();
        assertEquals(2, values.size());
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
    }

    @Test
    public void testNoAttributes1() {
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testNoAttributes2() {
        instance.put(1, 2.0);
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttribute(1);
        assertFalse(instance.containsKey(1));
        assertEquals(1, (int) instance.keySet().iterator().next());
    }

    @Test
    public void testToString() {
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals("{1=2.0;null}", instance.toString());
    }

    @Test
    public void testHashCode() {
        instance.put(1, 2.0);
        int expectedHashCode = Objects.hash(instance.data, instance.defaultValue);
        assertEquals(expectedHashCode, instance.hashCode());
    }

    @Test
    public void testEquals1() {
        SparseInstance newInstance = new SparseInstance();
        assertTrue(instance.equals(newInstance));
    }

    @Test
    public void testEquals2() {
        SparseInstance other = new SparseInstance();
        assertFalse(instance.equals(other));
    }

    @Test
    public void testEquals3() {
        Object object = new Object();
        assertFalse(instance.equals(object));
    }

    @Test
    public void testEquals4() {
        SparseInstance newInstance = new SparseInstance();
        newInstance.put(1, 2.0);
        assertFalse(instance.equals(newInstance));
    }

    @Test
    public void testEquals5() {
        SparseInstance newInstance = new SparseInstance();
        newInstance.put(1, 2.0);
        instance.put(1, 2.0);
        assertTrue(instance.equals(newInstance));
    }

    @Test
    public void testCopy() {
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        SparseInstance copyInstance = (SparseInstance) instance.copy();
        assertEquals(instance.noAttributes(), copyInstance.noAttributes());
        assertEquals(instance.defaultValue, copyInstance.defaultValue, 0.001);
        assertEquals(instance.classValue(), copyInstance.classValue());
        assertEquals(instance.get(1), copyInstance.get(1), 0.001);
        assertEquals(instance.get(2), copyInstance.get(2), 0.001);
    }

    @Test
    public void testRemoveAttributes() {
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        instance.removeAttributes(indices);
        assertFalse(instance.containsKey(1));
    }
}