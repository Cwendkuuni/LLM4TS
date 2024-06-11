
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
    public void testSparseInstance() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 1.0);
        assertEquals(5, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDefaultValueAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, 1.0, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(1.0, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDataVector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDataVectorAndDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.5);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.5, instance.defaultValue, 0.001);
        assertNull(instance.classValue());
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDataVectorAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, classValue);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDataVectorAndDefaultValueAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, 0.5, classValue);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.5, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
    }

    @Test
    public void testClear() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertFalse(instance.isEmpty());
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
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Integer, Double>(0, 1.0)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Integer, Double>(1, 2.0)));
    }

    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(0.0, instance.get(2), 0.001);
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

        TreeSet<Integer> keySet = instance.keySet();
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        assertNull(instance.put(0, 1.0));
        assertNull(instance.put(1, 2.0));

        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
    }

    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);

        instance.putAll(map);
        assertEquals(2, instance.size());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);

        assertEquals(1.0, instance.remove(0), 0.001);
        assertNull(instance.remove(1));
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

        Collection<Double> values = instance.values();
        assertEquals(2, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
    }

    @Test
    public void testNoAttributesWithNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testNoAttributesWithData() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(2, 2.0);
        instance.put(4, 3.0);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals(3, instance.noAttributes());

        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(3.0, instance.get(1), 0.001);
    }

    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.setClassValue("Class");

        assertEquals("{0=1.0;1=2.0;Class}", instance.toString());
    }

    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);

        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);

        assertTrue(instance1.hashCode() == instance2.hashCode());
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
        assertTrue(instance2.equals(instance1));
    }

    @Test
    public void testCopy() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);
        instance1.setClassValue("Class");

        SparseInstance instance2 = (SparseInstance) instance1.copy();

        assertTrue(instance1.equals(instance2));
        assertTrue(instance2.equals(instance1));
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals(3, instance.noAttributes());

        Set<Integer> indices = new HashSet<Integer>();
        indices.add(0);
        indices.add(2);

        instance.removeAttributes(indices);
        assertEquals(1, instance.noAttributes());
        assertFalse(instance.containsKey(0));
        assertEquals(2.0, instance.get(1), 0.001);
    }

}

