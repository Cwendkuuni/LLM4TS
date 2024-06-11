import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceDefaultConstructor() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
        assertEquals(0, instance.size());
        assertEquals(-1, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
    }

    @Test
    public void testSparseInstanceConstructorWithNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assertTrue(instance.isEmpty());
        assertEquals(0, instance.size());
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
    }

    @Test
    public void testSparseInstanceConstructorWithNoAttributesAndDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 1.0);
        assertTrue(instance.isEmpty());
        assertEquals(0, instance.size());
        assertEquals(5, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.001);
    }

    @Test
    public void testSparseInstanceConstructorWithNoAttributesDefaultValueAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 1.0, classValue);
        assertTrue(instance.isEmpty());
        assertEquals(0, instance.size());
        assertEquals(5, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceDoubleArrayConstructor() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertFalse(instance.isEmpty());
        assertEquals(3, instance.size());
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
    }

    @Test
    public void testSparseInstanceDoubleArrayConstructorWithDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 1.0);
        assertFalse(instance.isEmpty());
        assertEquals(3, instance.size());
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.001);
    }

    @Test
    public void testSparseInstanceDoubleArrayConstructorWithDefaultValueAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, 1.0, classValue);
        assertFalse(instance.isEmpty());
        assertEquals(3, instance.size());
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testInitiate() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance();
        instance.initiate(datavector);
        assertFalse(instance.isEmpty());
        assertEquals(3, instance.size());
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.clear();
        assertTrue(instance.isEmpty());
        assertEquals(0, instance.size());
        assertEquals(-1, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
    }

    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
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
        SparseInstance instance = new SparseInstance();
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
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
    }

    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        assertEquals(0.0, instance.get(3), 0.001);
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
        instance.put(2, 3.0);
        TreeSet<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        assertEquals(null, instance.put(0, 1.0));
        assertEquals(1.0, instance.put(0, 2.0), 0.001);
        assertEquals(1.0, instance.get(0), 0.001);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        map.put(2, 3.0);
        SparseInstance instance = new SparseInstance();
        instance.putAll(map);
        assertEquals(3, instance.size());
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals(1.0, instance.remove(0), 0.001);
        assertFalse(instance.containsKey(0));
        assertEquals(null, instance.remove(3));
        assertFalse(instance.containsKey(3));
    }

    @Deprecated
    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.size());
        instance.put(0, 1.0);
        assertEquals(1, instance.size());
        instance.remove(0);
        assertEquals(0, instance.size());
    }

    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
    }

    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        instance.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
        instance.put(2, 3.0);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttribute(1);
        assertFalse(instance.containsKey(1));
        assertEquals(2, instance.size());
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        assertEquals("{;null}", instance.toString());
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals("{0=1.0, 1=2.0, 2=3.0;null}", instance.toString());
    }

    @Test
    public void testHashCode() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance1 = new SparseInstance(datavector);
        SparseInstance instance2 = new SparseInstance(datavector);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] datavector1 = {1.0, 2.0, 3.0};
        double[] datavector2 = {1.0, 2.0, 3.0};
        SparseInstance instance1 = new SparseInstance(datavector1);
        SparseInstance instance2 = new SparseInstance(datavector2);
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance1 = new SparseInstance(datavector, 1.0, classValue);
        SparseInstance instance2 = instance1.copy();
        assertEquals(instance1, instance2);
        assertNotSame(instance1, instance2);
    }

    @Test
    public void testRemoveAttributes() {
        double[] datavector = {1.0, 2.0, 3.0};
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(0);
        indices.add(1);
        SparseInstance instance = new SparseInstance(datavector);
        instance.removeAttributes(indices);
        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
        assertEquals(1, instance.size());
        assertEquals(1, instance.noAttributes());
    }

}