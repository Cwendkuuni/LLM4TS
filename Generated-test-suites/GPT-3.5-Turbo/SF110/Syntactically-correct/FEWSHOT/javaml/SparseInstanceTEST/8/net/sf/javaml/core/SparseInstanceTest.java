
package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class SparseInstanceTest {

    private SparseInstance instance;
    private double[] dataVector;

    @Before
    public void setUp() {
        dataVector = new double[] {1.0, 2.0, 3.0};
        instance = new SparseInstance(dataVector);
    }

    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(3);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testConstructorWithNoAttributes() {
        SparseInstance newInstance = new SparseInstance(3);
        assertEquals(3, newInstance.noAttributes());
    }

    @Test
    public void testConstructorWithDefaultValue() {
        double defaultValue = 0.5;
        instance = new SparseInstance(dataVector, defaultValue);
        for (double val : instance.values()) {
            assertEquals(defaultValue, val, 0.001);
        }
    }

    @Test
    public void testConstructorWithClassValue() {
        Object classValue = "class";
        instance = new SparseInstance(dataVector, classValue);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testConstructorWithDefaultAndClassValue() {
        double defaultValue = 0.5;
        Object classValue = "class";
        instance = new SparseInstance(dataVector, defaultValue, classValue);
        assertEquals(defaultValue, instance.defaultValue, 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testInitiate() {
        instance.initiate(dataVector);
        for (double val : instance.values()) {
            assertFalse(val == instance.defaultValue);
        }
    }

    @Test
    public void testValue() {
        assertEquals(dataVector[0], instance.value(0), 0.001);
        assertEquals(dataVector[1], instance.value(1), 0.001);
        assertEquals(dataVector[2], instance.value(2), 0.001);
    }

    @Test
    public void testClear() {
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(dataVector[0]));
        assertTrue(instance.containsValue(dataVector[1]));
        assertTrue(instance.containsValue(dataVector[2]));
        assertFalse(instance.containsValue(dataVector[0] + 1));
    }

    @Test
    public void testEntrySet() {
        assertEquals(3, instance.entrySet().size());
    }

    @Test
    public void testGet() {
        assertEquals(dataVector[0], instance.get(0), 0.001);
        assertEquals(dataVector[1], instance.get(1), 0.001);
        assertEquals(dataVector[2], instance.get(2), 0.001);
        assertEquals(instance.defaultValue, instance.get(3), 0.001);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        assertEquals(3, instance.keySet().size());
    }

    @Test
    public void testPut() {
        instance.put(3, 4.0);
        assertEquals(4.0, instance.get(3), 0.001);
    }

    @Test
    public void testPutAll() {
        HashMap<Integer, Double> map = new HashMap<>();
        map.put(3, 4.0);
        map.put(4, 5.0);
        instance.putAll(map);
        assertEquals(4.0, instance.get(3), 0.001);
        assertEquals(5.0, instance.get(4), 0.001);
    }

    @Test
    public void testRemove() {
        instance.remove(2);
        assertFalse(instance.containsKey(2));
    }

    @Test
    public void testSize() {
        assertEquals(3, instance.size());
        instance.clear();
        assertEquals(0, instance.size());
    }

    @Test
    public void testValues() {
        assertEquals(3, instance.values().size());
    }

    @Test
    public void testNoAttributes() {
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(2);
        assertFalse(instance.containsKey(2));
        assertEquals(2, instance.keySet().size());
    }

    @Test
    public void testToString() {
        String expected = "{" + instance.data.toString() + ";" + instance.classValue() + "}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        int expected = 31 * instance.data.hashCode() + Double.doubleToLongBits(instance.defaultValue);
        assertEquals(expected, instance.hashCode());
    }

    @Test
    public void testEquals() {
        HashMap<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        data.put(1, 2.0);
        data.put(2, 3.0);
        SparseInstance other = new SparseInstance();
        other.data = data;
        other.defaultValue = 0.5;
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));
        assertFalse(instance.equals(other));
        assertFalse(other.equals(instance));
        other = new SparseInstance();
        other.data = new HashMap<>();
        assertTrue(instance.equals(other));
    }

    @Test
    public void testCopy() {
        SparseInstance copy = instance.copy();
        assertEquals(instance.data, copy.data);
        assertEquals(instance.defaultValue, copy.defaultValue, 0.001);
        assertEquals(instance.noAttributes, copy.noAttributes);
        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        instance.removeAttributes(new HashSet<>(Arrays.asList(0, 2)));
        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsKey(2));
        assertEquals(1, instance.size());
    }
}
