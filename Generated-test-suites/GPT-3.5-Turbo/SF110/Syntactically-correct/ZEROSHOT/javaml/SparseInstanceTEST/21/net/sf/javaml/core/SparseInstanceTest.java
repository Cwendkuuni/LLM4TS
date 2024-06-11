import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SparseInstanceTest {

    private SparseInstance sparseInstance;

    @Before
    public void setUp() {
        sparseInstance = new SparseInstance();
    }

    @Test
    public void testSetNoAttributes() {
        sparseInstance.setNoAttributes(5);
        assertEquals(5, sparseInstance.noAttributes);
    }

    @Test
    public void testSparseInstanceDefaultConstructor() {
        assertEquals(-1, sparseInstance.noAttributes);
    }

    @Test
    public void testSparseInstanceConstructorWithNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes);
    }

    @Test
    public void testSparseInstanceConstructorWithNoAttributesAndDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        assertEquals(0.0, instance.defaultValue, 0.0001);
    }

    @Test
    public void testSparseInstanceConstructorWithNoAttributesAndClassValue() {
        SparseInstance instance = new SparseInstance(5, "class");
        assertEquals("class", instance.classValue());
    }

    @Test
    public void testSparseInstanceConstructorWithAllParameters() {
        SparseInstance instance = new SparseInstance(5, 0.0, "class");
        assertEquals(5, instance.noAttributes);
        assertEquals(0.0, instance.defaultValue, 0.0001);
        assertEquals("class", instance.classValue());
    }

    @Test
    public void testInitiateMethod() {
        double[] datavector = {1.0, 2.0, 0.0, 3.0};
        sparseInstance.initiate(datavector);
        assertEquals(4, sparseInstance.noAttributes);
        assertTrue(sparseInstance.containsKey(0));
        assertTrue(sparseInstance.containsKey(1));
        assertFalse(sparseInstance.containsKey(2));
        assertTrue(sparseInstance.containsKey(3));
        assertEquals(1.0, sparseInstance.get(0), 0.0001);
        assertEquals(2.0, sparseInstance.get(1), 0.0001);
        assertEquals(3.0, sparseInstance.get(3), 0.0001);
    }

    @Test
    public void testValueMethod() {
        sparseInstance.put(0, 1.0);
        assertEquals(1.0, sparseInstance.value(0), 0.0001);
    }

    @Test
    public void testClearMethod() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        sparseInstance.clear();
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testContainsKeyMethod() {
        sparseInstance.put(0, 1.0);
        assertTrue(sparseInstance.containsKey(0));
        assertFalse(sparseInstance.containsKey(1));
    }

    @Test
    public void testContainsValueMethod() {
        sparseInstance.put(0, 1.0);
        assertTrue(sparseInstance.containsValue(1.0));
        assertFalse(sparseInstance.containsValue(2.0));
    }

    @Test
    public void testEntrySetMethod() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        Map.Entry<Integer, Double> entry1 = new HashMap.SimpleEntry<>(0, 1.0);
        Map.Entry<Integer, Double> entry2 = new HashMap.SimpleEntry<>(1, 2.0);
        assertTrue(sparseInstance.entrySet().contains(entry1));
        assertTrue(sparseInstance.entrySet().contains(entry2));
    }

    @Test
    public void testGetMethod() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        assertEquals(1.0, sparseInstance.get(0), 0.0001);
        assertEquals(2.0, sparseInstance.get(1), 0.0001);
        assertEquals(0.0, sparseInstance.get(2), 0.0001);
    }

    @Test
    public void testIsEmptyMethod() {
        assertTrue(sparseInstance.isEmpty());
        sparseInstance.put(0, 1.0);
        assertFalse(sparseInstance.isEmpty());
    }

    @Test
    public void testKeySetMethod() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        assertTrue(sparseInstance.keySet().contains(0));
        assertTrue(sparseInstance.keySet().contains(1));
        assertFalse(sparseInstance.keySet().contains(2));
    }

    @Test
    public void testPutMethod() {
        assertNull(sparseInstance.put(0, 1.0));
        assertEquals(1.0, sparseInstance.get(0), 0.0001);
        assertEquals(1.0, sparseInstance.put(0, 2.0), 0.0001);
        assertEquals(2.0, sparseInstance.get(0), 0.0001);
    }

    @Test
    public void testPutAllMethod() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        sparseInstance.putAll(map);
        assertTrue(sparseInstance.containsKey(0));
        assertTrue(sparseInstance.containsKey(1));
        assertEquals(1.0, sparseInstance.get(0), 0.0001);
        assertEquals(2.0, sparseInstance.get(1), 0.0001);
    }

    @Test
    public void testRemoveMethod() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        assertEquals(1.0, sparseInstance.remove(0), 0.0001);
        assertFalse(sparseInstance.containsKey(0));
    }

    @Test
    public void testSizeMethod() {
        assertEquals(0, sparseInstance.size());
        sparseInstance.put(0, 1.0);
        assertEquals(1, sparseInstance.size());
    }

    @Test
    public void testValuesMethod() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        assertTrue(sparseInstance.values().contains(1.0));
        assertTrue(sparseInstance.values().contains(2.0));
    }

    @Test
    public void testNoAttributesMethod() {
        assertEquals(0, sparseInstance.noAttributes());
        sparseInstance.put(0, 1.0);
        assertEquals(1, sparseInstance.noAttributes());
        sparseInstance.put(1, 2.0);
        assertEquals(2, sparseInstance.noAttributes());
    }

    @Test
    public void testRemoveAttributeMethod() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        sparseInstance.removeAttribute(0);
        assertFalse(sparseInstance.containsKey(0));
        assertFalse(sparseInstance.containsKey(1));
        assertEquals(0, sparseInstance.noAttributes());
    }

    @Test
    public void testToStringMethod() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        assertEquals("{0=1.0;null}", sparseInstance.toString());
    }

    @Test
    public void testHashCodeMethod() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        int hashCode = 31;
        hashCode = 31 * hashCode + sparseInstance.data.hashCode();
        long temp = Double.doubleToLongBits(sparseInstance.defaultValue);
        hashCode = 31 * hashCode + (int) (temp ^ (temp >>> 32));
        assertEquals(hashCode, sparseInstance.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        SparseInstance otherSparseInstance = new SparseInstance();
        otherSparseInstance.put(0, 1.0);
        otherSparseInstance.put(1, 2.0);
        assertEquals(sparseInstance, otherSparseInstance);
        otherSparseInstance.put(1, 3.0);
        assertNotEquals(sparseInstance, otherSparseInstance);
    }

    @Test
    public void testCopyMethod() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        SparseInstance copy = (SparseInstance) sparseInstance.copy();
        assertEquals(sparseInstance, copy);
        assertNotSame(sparseInstance, copy);
    }

    @Test
    public void testRemoveAttributesMethod() {
        sparseInstance.put(0, 1.0);
        sparseInstance.put(1, 2.0);
        sparseInstance.put(2, 3.0);
        sparseInstance.put(3, 4.0);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        sparseInstance.removeAttributes(indices);
        assertFalse(sparseInstance.containsKey(0));
        assertTrue(sparseInstance.containsKey(1));
        assertFalse(sparseInstance.containsKey(2));
        assertTrue(sparseInstance.containsKey(3));
        assertEquals(2, sparseInstance.noAttributes());
    }
}