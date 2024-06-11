
package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

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
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance sparseInstance = new SparseInstance();
        assertEquals(-1, sparseInstance.noAttributes());
    }

    @Test
    public void testDataVectorConstructor() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertEquals(datavector.length, sparseInstance.noAttributes());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], sparseInstance.value(i), 0.0);
        }
    }

    @Test
    public void testDataVectorConstructorWithDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        double defaultValue = 0.0;
        SparseInstance sparseInstance = new SparseInstance(datavector, defaultValue);
        assertEquals(datavector.length, sparseInstance.noAttributes());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], sparseInstance.value(i), 0.0);
        }
    }
    
    @Test
    public void testDataVectorConstructorWithClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        Object classValue = "class";
        SparseInstance sparseInstance = new SparseInstance(datavector, classValue);
        assertEquals(datavector.length, sparseInstance.noAttributes());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], sparseInstance.value(i), 0.0);
        }
        assertEquals(classValue, sparseInstance.classValue());
    }
    
    @Test
    public void testDataVectorConstructorWithDefaultValueAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        double defaultValue = 0.0;
        Object classValue = "class";
        SparseInstance sparseInstance = new SparseInstance(datavector, defaultValue, classValue);
        assertEquals(datavector.length, sparseInstance.noAttributes());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], sparseInstance.value(i), 0.0);
        }
        assertEquals(classValue, sparseInstance.classValue());
    }

    @Test
    public void testValue() {
        instance.put(0, 1.0);
        assertEquals(1.0, instance.value(0), 0.0);
    }

    @Test
    public void testClear() {
        instance.put(0, 1.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        instance.put(0, 1.0);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(1));
    }

    @Test
    public void testContainsValue() {
        instance.put(0, 1.0);
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(2.0));
    }

    @Test
    public void testEntrySet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        Map.Entry<Integer, Double> entry = instance.entrySet().iterator().next();
        assertEquals(0, entry.getKey().intValue());
        assertEquals(1.0, entry.getValue(), 0.0);
    }

    @Test
    public void testGet() {
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(instance.getDefaultValue(), instance.get(1), 0.0);
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
        TreeSet<Integer> keySet = instance.keySet();
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
    }

    @Test
    public void testPut() {
        Double oldValue = instance.put(0, 1.0);
        assertNull(oldValue);
        assertEquals(1.0, instance.value(0), 0.0);
    }

    @Test
    public void testPutAll() {
        HashMap<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(2.0, instance.value(1), 0.0);
    }

    @Test
    public void testRemove() {
        instance.put(0, 1.0);
        Double removedValue = instance.remove(0);
        assertEquals(1.0, removedValue, 0.0);
        assertNull(instance.get(0));
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
        assertTrue(instance.values().contains(1.0));
        assertTrue(instance.values().contains(2.0));
    }

    @Test
    public void testNoAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals(3, instance.noAttributes());
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertNull(instance.get(1));
    }

    @Test
    public void testToString() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals("{0=1.0;null}", instance.toString());
    }

    @Test
    public void testEquals() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        SparseInstance otherInstance = new SparseInstance();
        otherInstance.put(0, 1.0);
        otherInstance.put(1, 2.0);
        assertTrue(instance.equals(otherInstance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));
    }

    @Test
    public void testCopy() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        SparseInstance copyInstance = (SparseInstance) instance.copy();
        assertEquals(instance.noAttributes(), copyInstance.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.value(i), copyInstance.value(i), 0.0);
        }
    }

    @Test
    public void testRemoveAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.put(3, 4.0);
        instance.put(4, 5.0);
        instance.put(5, 6.0);

        TreeSet<Integer> indicesToRemove = new TreeSet<>();
        indicesToRemove.add(1);
        indicesToRemove.add(3);
        indicesToRemove.add(5);

        instance.removeAttributes(indicesToRemove);

        assertEquals(3, instance.noAttributes());
        assertNull(instance.get(1));
        assertNull(instance.get(3));
        assertNull(instance.get(5));
    }
}
