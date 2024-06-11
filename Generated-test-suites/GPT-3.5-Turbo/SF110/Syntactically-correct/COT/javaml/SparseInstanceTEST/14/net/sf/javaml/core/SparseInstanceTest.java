import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SparseInstanceTest {
    private SparseInstance sparseInstance;

    @Before
    public void setUp() {
        sparseInstance = new SparseInstance();
    }

    @Test
    public void testSetNoAttributes() {
        sparseInstance.setNoAttributes(5);
        assertEquals(5, sparseInstance.noAttributes());
    }

    @Test
    public void testSparseInstanceDefaultConstructor() {
        assertEquals(-1, sparseInstance.noAttributes());
    }

    @Test
    public void testSparseInstanceConstructorWithNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceConstructorWithNoAttributesAndDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.get(0), 0.001);
    }

    @Test
    public void testSparseInstanceConstructorWithNoAttributesAndClassValue() {
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceConstructorWithNoAttributesDefaultValueAndClassValue() {
        double defaultValue = 0.0;
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(5, defaultValue, classValue);
        assertEquals(classValue, instance.classValue());
        assertEquals(defaultValue, instance.get(0), 0.001);
    }

    @Test
    public void testSparseInstanceConstructorWithDatavector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testSparseInstanceConstructorWithDatavectorAndDefaultValue() {
        double[] datavector = {1.0, 0.0, 3.0};
        double defaultValue = -1.0;
        SparseInstance instance = new SparseInstance(datavector, defaultValue);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(-1.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testSparseInstanceConstructorWithDatavectorDefaultValueAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        double defaultValue = -1.0;
        Object classValue = new Object();
        SparseInstance instance = new SparseInstance(datavector, defaultValue, classValue);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testValue() {
        sparseInstance.put(0, 1.0);
        assertEquals(1.0, sparseInstance.value(0), 0.001);
    }

    @Test
    public void testClear() {
        sparseInstance.put(0, 1.0);
        sparseInstance.clear();
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        sparseInstance.put(0, 1.0);
        assertTrue(sparseInstance.containsKey(0));
        assertFalse(sparseInstance.containsKey(1));
    }

    @Test
    public void testContainsValue() {
        sparseInstance.put(0, 1.0);
        assertTrue(sparseInstance.containsValue(1.0));
        assertFalse(sparseInstance.containsValue(2.0));
    }

    @Test
    public void testEntrySet() {
        sparseInstance.put(0, 1.0);
        Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
        assertNotNull(entrySet);
        assertEquals(1, entrySet.size());
    }

    @Test
    public void testGet() {
        sparseInstance.put(0, 1.0);
        assertEquals(1.0, sparseInstance.get(0), 0.001);
        assertEquals(0.0, sparseInstance.get(1), 0.001);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(sparseInstance.isEmpty());
        sparseInstance.put(0, 1.0);
        assertFalse(sparseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        sparseInstance.put(0, 1.0);
        TreeSet<Integer> keySet = sparseInstance.keySet();
        assertNotNull(keySet);
        assertTrue(keySet.contains(0));
        assertFalse(keySet.contains(1));
    }

    @Test
    public void testPut() {
        assertNull(sparseInstance.put(0, 1.0));
        assertEquals(1.0, sparseInstance.get(0), 0.001);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        sparseInstance.putAll(map);
        assertEquals(1.0, sparseInstance.get(0), 0.001);
        assertEquals(2.0, sparseInstance.get(1), 0.001);
    }

    @Test
    public void testRemove() {
        sparseInstance.put(0, 1.0);
        assertEquals(1.0, sparseInstance.remove(0), 0.001);
        assertNull(sparseInstance.remove(0));
    }

    @Test
    public void testSize() {
        assertEquals(0, sparseInstance.size());
        sparseInstance.put(0, 1.0);
        assertEquals(1, sparseInstance.size());
    }

    @Test
    public void testValues() {
        sparseInstance.put(0, 1.0);
        Collection<Double> values = sparseInstance.values();
        assertNotNull(values);
        assertTrue(values.contains(1.0));
        assertFalse(values.contains(2.0));
    }

    @Test
    public void testNoAttributes() {
        assertEquals(-1, sparseInstance.noAttributes());
        sparseInstance.put(0, 1.0);
        assertEquals(1, sparseInstance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        sparseInstance.removeAttribute(0);
        assertEquals(1, sparseInstance.noAttributes());
        assertEquals(2.0, sparseInstance.get(0), 0.001);
    }

    @Test
    public void testToString() {
        sparseInstance.put(0, 1.0);
        assertEquals("{0=1.0;null}", sparseInstance.toString());
    }

    @Test
    public void testHashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((sparseInstance.data == null) ? 0 : sparseInstance.data.hashCode());
        long temp = Double.doubleToLongBits(sparseInstance.defaultValue);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        assertEquals(result, sparseInstance.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(sparseInstance.equals(sparseInstance));
        assertFalse(sparseInstance.equals(null));
        assertFalse(sparseInstance.equals(new Object()));
        assertFalse(sparseInstance.equals(new SparseInstance()));
        assertFalse(sparseInstance.equals(new SparseInstance(5, 0.0)));
        assertTrue(sparseInstance.equals(new SparseInstance(sparseInstance.noAttributes())));
    }

    @Test
    public void testCopy() {
        sparseInstance.put(0, 1.0);
        SparseInstance copy = (SparseInstance) sparseInstance.copy();
        assertEquals(sparseInstance, copy);
    }

    @Test
    public void testRemoveAttributes() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        sparseInstance.removeAttributes(indices);
        assertEquals(1, sparseInstance.noAttributes());
        assertEquals(2.0, sparseInstance.get(0), 0.001);
    }
}