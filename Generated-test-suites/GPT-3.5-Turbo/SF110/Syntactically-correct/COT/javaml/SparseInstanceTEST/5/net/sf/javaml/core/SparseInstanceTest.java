
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.Collections;

public class SparseInstanceTest {

    private SparseInstance instance;
    private double[] datavector;
    private Object classValue;

    @Before
    public void setUp() {
        instance = new SparseInstance();
        datavector = new double[]{1.0, 2.0, 3.0};
        classValue = "classValue";
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
    public void testSparseInstanceWithNoAttributes() {
        instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndDefaultValue() {
        instance = new SparseInstance(5, 0.0);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.get(0), 0.001);
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndClassValue() {
        instance = new SparseInstance(5, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithAllParameters() {
        instance = new SparseInstance(5, 0.0, classValue);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.0, instance.get(0), 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithDatavector() {
        instance = new SparseInstance(datavector);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(datavector[0], instance.get(0), 0.001);
        assertEquals(datavector[1], instance.get(1), 0.001);
        assertEquals(datavector[2], instance.get(2), 0.001);
    }

    @Test
    public void testSparseInstanceWithDatavectorAndDefaultValue() {
        instance = new SparseInstance(datavector, 0.0);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(datavector[0], instance.get(0), 0.001);
        assertEquals(datavector[1], instance.get(1), 0.001);
        assertEquals(datavector[2], instance.get(2), 0.001);
        assertEquals(0.0, instance.get(3), 0.001);
    }

    @Test
    public void testSparseInstanceWithDatavectorAndClassValue() {
        instance = new SparseInstance(datavector, classValue);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(datavector[0], instance.get(0), 0.001);
        assertEquals(datavector[1], instance.get(1), 0.001);
        assertEquals(datavector[2], instance.get(2), 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testSparseInstanceWithAllParametersAndDatavector() {
        instance = new SparseInstance(datavector, 0.0, classValue);
        assertEquals(datavector.length, instance.noAttributes());
        assertEquals(datavector[0], instance.get(0), 0.001);
        assertEquals(datavector[1], instance.get(1), 0.001);
        assertEquals(datavector[2], instance.get(2), 0.001);
        assertEquals(0.0, instance.get(3), 0.001);
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
        instance.put(1, 2.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKeyWithExistingKey() {
        instance.put(0, 1.0);
        assertTrue(instance.containsKey(0));
    }

    @Test
    public void testContainsKeyWithNonExistingKey() {
        assertFalse(instance.containsKey(0));
    }

    @Test
    public void testContainsValueWithExistingValue() {
        instance.put(0, 1.0);
        assertTrue(instance.containsValue(1.0));
    }

    @Test
    public void testContainsValueWithNonExistingValue() {
        assertFalse(instance.containsValue(1.0));
    }

    @Test
    public void testEntrySet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(2, entrySet.size());
    }

    @Test
    public void testGetWithExistingKey() {
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.001);
    }

    @Test
    public void testGetWithNonExistingKey() {
        assertEquals(instance.defaultValue, instance.get(0), 0.001);
    }

    @Test
    public void testIsEmptyWithEmptyInstance() {
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testIsEmptyWithNonEmptyInstance() {
        instance.put(0, 1.0);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        TreeSet<Integer> keySet = instance.keySet();
        assertEquals(2, keySet.size());
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
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.size());
    }

    @Test
    public void testValues() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Collection<Double> values = instance.values();
        assertEquals(2, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
    }

    @Test
    public void testNoAttributes() {
        assertEquals(0, instance.noAttributes());
        instance.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.removeAttribute(0);
        assertFalse(instance.containsKey(0));
        assertEquals(1.0, instance.get(1), 0.001);
        assertEquals(1, instance.noAttributes());
    }

    @Test
    public void testToString() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        String expected = "{0=1.0; null}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        int expected = 961;
        assertEquals(expected, instance.hashCode());
    }

    @Test
    public void testEqualsWithSameInstance() {
        assertTrue(instance.equals(instance));
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(instance.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(instance.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentData() {
        SparseInstance other = new SparseInstance();
        assertFalse(instance.equals(other));
    }

    @Test
    public void testEqualsWithEqualData() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        SparseInstance other = new SparseInstance();
        other.put(0, 1.0);
        other.put(1, 2.0);
        assertTrue(instance.equals(other));
    }

    @Test
    public void testCopy() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        SparseInstance copy = (SparseInstance) instance.copy();
        assertTrue(instance.equals(copy));
    }

    @Test
    public void testRemoveAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        instance.removeAttributes(indices);
        assertFalse(instance.containsKey(0));
        assertEquals(1.0, instance.get(1), 0.001);
    }

}

