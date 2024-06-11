import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Collection;

public class SparseInstanceTest {

    SparseInstance instance;

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
    public void testSparseInstanceNoArgConstructor() {
        assertEquals(-1, instance.noAttributes());
        assertEquals(0.0, instance.value(0), 0);
        assertNull(instance.classValue());
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        instance = new SparseInstance(5, 1.0);
        assertEquals(5, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0);
        assertNull(instance.classValue());
    }

    @Test
    public void testSparseInstanceWithClassValue() {
        Object classValue = new Object();
        instance = new SparseInstance(5, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.value(0), 0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithDataVector() {
        double[] datavector = {0.5, 1.5, 2.5, 3.5, 4.5};
        instance = new SparseInstance(datavector);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(datavector[0], instance.value(0), 0);
        assertNull(instance.classValue());
    }

    @Test
    public void testInitiate() {
        double[] datavector = {0.5, 0.0, 2.5, 0.0, 4.5};
        instance.initiate(datavector);
        assertFalse(instance.containsKey(1));
        assertEquals(datavector[0], instance.value(0), 0);
        assertEquals(datavector[1], instance.value(1), 0);
    }

    @Test
    public void testValue() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        assertEquals(0.5, instance.value(0), 0);
    }

    @Test
    public void testClear() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
    }

    @Test
    public void testContainsValue() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        assertTrue(instance.containsValue(0.5));
        assertFalse(instance.containsValue(1.0));
    }

    @Test
    public void testEntrySet() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(1, entrySet.size());
        Map.Entry<Integer, Double> entry = entrySet.iterator().next();
        assertEquals(0, entry.getKey().intValue());
        assertEquals(0.5, entry.getValue(), 0);
    }

    @Test
    public void testGet() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        assertEquals(0.5, instance.get(0), 0);
        assertEquals(0.0, instance.get(1), 0);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        instance.put(Integer.valueOf(1), Double.valueOf(1.5));
        TreeSet<Integer> keySet = instance.keySet();
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
    }

    @Test
    public void testPut() {
        Double previousValue = instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        assertNull(previousValue);
        assertEquals(0.5, instance.get(0), 0);
        previousValue = instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        assertEquals(0.5, previousValue, 0);
        assertEquals(1.0, instance.get(0), 0);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 0.5);
        map.put(1, 1.5);
        instance.putAll(map);
        assertEquals(0.5, instance.get(0), 0);
        assertEquals(1.5, instance.get(1), 0);
    }

    @Test
    public void testRemove() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        Double removedValue = instance.remove(0);
        assertEquals(0.5, removedValue, 0);
        assertFalse(instance.containsKey(0));
    }

    @Test
    public void testSize() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        instance.put(Integer.valueOf(1), Double.valueOf(1.5));
        assertEquals(2, instance.size());
    }

    @Test
    public void testValues() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        instance.put(Integer.valueOf(1), Double.valueOf(1.5));
        Collection<Double> values = instance.values();
        assertEquals(2, values.size());
        assertTrue(values.contains(0.5));
        assertTrue(values.contains(1.5));
    }

    @Test
    public void testNoAttributes() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        instance.put(Integer.valueOf(2), Double.valueOf(2.5));
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        instance.put(Integer.valueOf(2), Double.valueOf(2.5));
        instance.removeAttribute(0);
        assertEquals(1, instance.noAttributes());
        assertFalse(instance.containsKey(0));
        assertEquals(2.5, instance.get(1), 0);
        assertEquals(0.0, instance.get(2), 0);
    }

    @Test
    public void testToString() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        instance.put(Integer.valueOf(2), Double.valueOf(2.5));
        assertEquals("{0=0.5;null}", instance.toString());
    }

    @Test
    public void testHashCode() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        assertEquals(instance.hashCode(), instance.hashCode());
    }

    @Test
    public void testEquals() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        SparseInstance other = new SparseInstance();
        other.put(Integer.valueOf(0), Double.valueOf(0.5));
        assertTrue(instance.equals(other));
    }

    @Test
    public void testCopy() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        SparseInstance copy = instance.copy();
        assertEquals(instance, copy);
    }

    @Test
    public void testRemoveAttributes() {
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        instance.put(Integer.valueOf(1), Double.valueOf(1.5));
        instance.removeAttributes(new TreeSet<>());
        assertTrue(instance.isEmpty());
    }

}