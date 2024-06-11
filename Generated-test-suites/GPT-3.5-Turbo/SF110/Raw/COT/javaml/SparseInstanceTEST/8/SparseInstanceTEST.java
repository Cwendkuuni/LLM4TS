import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes);
    }

    @Test
    public void testSparseInstance() {
        SparseInstance instance = new SparseInstance();
        assertNotNull(instance.data);
        assertEquals(-1, instance.noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.0);
        assertNull(instance.classValue());
    }

    @Test
    public void testSparseInstanceInt() {
        SparseInstance instance = new SparseInstance(5);
        assertNotNull(instance.data);
        assertEquals(5, instance.noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.0);
        assertNull(instance.classValue());
    }

    @Test
    public void testSparseInstanceIntDouble() {
        SparseInstance instance = new SparseInstance(5, 1.0);
        assertNotNull(instance.data);
        assertEquals(5, instance.noAttributes);
        assertEquals(1.0, instance.defaultValue, 0.0);
        assertNull(instance.classValue());
    }

    @Test
    public void testSparseInstanceIntObject() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        assertNotNull(instance.data);
        assertEquals(5, instance.noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceIntDoubleObject() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 1.0, classValue);
        assertNotNull(instance.data);
        assertEquals(5, instance.noAttributes);
        assertEquals(1.0, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceDoubleArray() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertNotNull(instance.data);
        assertEquals(3, instance.noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.0);
        assertNull(instance.classValue());
    }

    @Test
    public void testSparseInstanceDoubleArrayDouble() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 1.0);
        assertNotNull(instance.data);
        assertEquals(3, instance.noAttributes);
        assertEquals(1.0, instance.defaultValue, 0.0);
        assertNull(instance.classValue());
    }

    @Test
    public void testSparseInstanceDoubleArrayObject() {
        Object classValue = new Object();
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, classValue);
        assertNotNull(instance.data);
        assertEquals(3, instance.noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceDoubleArrayDoubleObject() {
        Object classValue = new Object();
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 1.0, classValue);
        assertNotNull(instance.data);
        assertEquals(3, instance.noAttributes);
        assertEquals(1.0, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testInitiate() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance();
        instance.initiate(datavector);
        assertEquals(3, instance.noAttributes);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(3.0));
    }

    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.entrySet().size());
        assertTrue(instance.entrySet().contains(new AbstractMap.SimpleEntry<>(0, 1.0)));
        assertTrue(instance.entrySet().contains(new AbstractMap.SimpleEntry<>(1, 2.0)));
    }

    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(0.0, instance.get(1), 0.0);
    }

    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
        instance.put(0, 1.0);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(0, 2.0);
        TreeSet<Integer> set = new TreeSet<>();
        set.add(0);
        set.add(1);
        assertEquals(set, instance.keySet());
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        assertNull(instance.put(0, 1.0));
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(1, instance.size());
        assertEquals(1.0, instance.put(0, 2.0), 0.0);
        assertEquals(2.0, instance.get(0), 0.0);
    }

    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.remove(0), 0.0);
        assertFalse(instance.containsKey(0));
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.size());
        instance.put(0, 1.0);
        assertEquals(1, instance.size());
    }

    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.values().size());
        assertTrue(instance.values().contains(1.0));
        assertTrue(instance.values().contains(2.0));
    }

    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(0, 2.0);
        assertEquals(2, instance.noAttributes());
        instance.removeAttribute(1);
        assertEquals(1, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(0, 2.0);
        instance.removeAttribute(1);
        assertFalse(instance.containsKey(1));
        assertEquals(1, instance.size());
        assertEquals(1.0, instance.get(0), 0.0);
    }

    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(0, 2.0);
        assertEquals("{0=2.0;null}", instance.toString());
    }

    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(1, 1.0);
        instance1.put(0, 2.0);

        SparseInstance instance2 = new SparseInstance();
        instance2.put(1, 1.0);
        instance2.put(0, 2.0);

        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(1, 1.0);
        instance1.put(0, 2.0);

        SparseInstance instance2 = new SparseInstance();
        instance2.put(1, 1.0);
        instance2.put(0, 2.0);

        assertEquals(instance1, instance2);
    }

    @Test
    public void testCopy() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(1, 1.0);
        instance1.put(0, 2.0);
        Object classValue = new Object();
        instance1.setClassValue(classValue);

        SparseInstance instance2 = (SparseInstance) instance1.copy();

        assertEquals(instance1, instance2);
        assertEquals(classValue, instance2.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(0, 2.0);
        instance.put(2, 3.0);

        instance.removeAttributes(new HashSet<>(Arrays.asList(0, 2)));
        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsKey(2));
        assertTrue(instance.containsKey(1));
        assertEquals(1, instance.size());
    }
}