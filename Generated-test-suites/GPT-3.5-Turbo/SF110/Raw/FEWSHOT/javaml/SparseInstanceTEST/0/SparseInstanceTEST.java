package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SparseInstanceTest {

    private SparseInstance instance;

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
    public void testConstructors() {
        SparseInstance instance1 = new SparseInstance();
        assertNull(instance1.classValue());

        SparseInstance instance2 = new SparseInstance(5);
        assertNull(instance2.classValue());
        assertEquals(5, instance2.noAttributes());

        Object classValue = new Object();
        SparseInstance instance3 = new SparseInstance(5, classValue);
        assertSame(classValue, instance3.classValue());
        assertEquals(5, instance3.noAttributes());

        SparseInstance instance4 = new SparseInstance(5, 0.5);
        assertNull(instance4.classValue());
        assertEquals(5, instance4.noAttributes());
    }

    @Test
    public void testInitiate() {
        double[] datavector = {1.0, 2.0, 3.0};
        instance.initiate(datavector);
        assertEquals(3, instance.noAttributes());
        assertNotNull(instance.get(0));
        assertNotNull(instance.get(1));
        assertNotNull(instance.get(2));
    }

    @Test
    public void testValue() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(3.0));
    }

    @Test
    public void testEntrySet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.entrySet().size());
    }

    @Test
    public void testGet() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(0.0, instance.get(2), 0.0);
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
        assertEquals(2, instance.keySet().size());
    }

    @Test
    public void testPut() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
    }

    @Test
    public void testPutAll() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        SparseInstance newInstance = new SparseInstance();
        newInstance.putAll(instance);

        assertEquals(instance.noAttributes(), newInstance.noAttributes());
        assertEquals(instance.get(0), newInstance.get(0));
        assertEquals(instance.get(1), newInstance.get(1));
    }

    @Test
    public void testRemove() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.remove(0);
        assertFalse(instance.containsKey(0));
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
        assertEquals(2, instance.values().size());
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
        instance.removeAttribute(0);
        assertEquals(1, instance.noAttributes());
        assertFalse(instance.containsKey(0));
        assertEquals(2.0, instance.get(1), 0.0);
    }

    @Test
    public void testToString() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        String expected = "{" + instance.data.toString() + ";" + instance.classValue() + "}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(instance.data.hashCode(), instance.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);

        SparseInstance instance2 = new SparseInstance();
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);

        SparseInstance instance3 = new SparseInstance();
        instance3.put(0, 1.0);
        instance3.put(1, 3.0);

        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(new Object()));
    }

    @Test
    public void testCopy() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        SparseInstance copy = (SparseInstance) instance.copy();
        assertEquals(copy.noAttributes(), instance.noAttributes());
        assertEquals(copy.get(0), instance.get(0));
        assertEquals(copy.get(1), instance.get(1));
        assertNotSame(copy, instance);
    }

    @Test
    public void testRemoveAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttributes(Set.of(0, 2));
        assertFalse(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
        assertNull(instance.get(0));
        assertEquals(1.0, instance.get(1), 0.0);
        assertNull(instance.get(2));
    }
}