import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        assertEquals(-1, instance.noAttributes);
    }

    @Test
    public void testSparseInstanceInt() {
        SparseInstance instance = new SparseInstance(10);
        assertEquals(10, instance.noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.0);
    }

    @Test
    public void testSparseInstanceIntDouble() {
        SparseInstance instance = new SparseInstance(10, 1.0);
        assertEquals(10, instance.noAttributes);
        assertEquals(1.0, instance.defaultValue, 0.0);
    }

    @Test
    public void testSparseInstanceIntObject() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(10, classValue);
        assertEquals(10, instance.noAttributes);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceIntDoubleObject() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(10, 1.0, classValue);
        assertEquals(10, instance.noAttributes);
        assertEquals(1.0, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceDoubleArray() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.0);
    }

    @Test
    public void testSparseInstanceDoubleArrayDouble() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0);
        assertEquals(3, instance.noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.0);
    }

    @Test
    public void testSparseInstanceDoubleArrayObject() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, classValue);
        assertEquals(3, instance.noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceDoubleArrayDoubleObject() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, 0.0, classValue);
        assertEquals(3, instance.noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testInitiate() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance();
        instance.initiate(datavector);
        assertEquals(3, instance.noAttributes);
        assertEquals(1.0, instance.defaultValue, 0.0);
        assertTrue(instance.data.containsKey(0));
        assertTrue(instance.data.containsKey(1));
        assertTrue(instance.data.containsKey(2));
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
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new HashMap.SimpleEntry<Integer, Double>(0, 1.0)));
        assertTrue(entrySet.contains(new HashMap.SimpleEntry<Integer, Double>(1, 2.0)));
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
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Set<Integer> keySet = instance.keySet();
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        assertNull(instance.put(0, 1.0));
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(1, instance.size());
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        SparseInstance instance = new SparseInstance();
        instance.putAll(map);
        assertEquals(2, instance.size());
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        assertEquals(1.0, instance.remove(0), 0.0);
        assertFalse(instance.containsKey(0));
        assertEquals(0, instance.size());
    }

    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
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
        assertEquals(0, instance.noAttributes());
        instance.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.removeAttribute(0);
        assertFalse(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertEquals(1, instance.noAttributes());
    }

    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        String expected = "{0=1.0;null}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        int expectedHashCode = instance.data.hashCode() * 31 + Double.valueOf(instance.defaultValue).hashCode();
        assertEquals(expectedHashCode, instance.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);
        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);
        assertTrue(instance1.equals(instance1));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(new Object()));
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        SparseInstance copy = (SparseInstance) instance.copy();
        assertTrue(instance != copy);
        assertTrue(instance.data != copy.data);
        assertEquals(instance.toString(), copy.toString());
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Set<Integer> indices = Set.of(0, 1);
        instance.removeAttributes(indices);
        assertTrue(instance.isEmpty());
    }
}