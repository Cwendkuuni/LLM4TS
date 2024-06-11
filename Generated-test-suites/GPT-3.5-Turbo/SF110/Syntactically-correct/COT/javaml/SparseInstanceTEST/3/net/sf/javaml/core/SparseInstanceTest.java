
package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SparseInstanceTest {

    SparseInstance instance;

    @Before
    public void setUp() {
        instance = new SparseInstance(3, 0.0, null);
    }

    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes);
    }

    @Test
    public void testConstructor1() {
        SparseInstance instance1 = new SparseInstance();
        assertEquals(-1, instance1.noAttributes);
        assertEquals(0.0, instance1.defaultValue, 0.0);
        assertNull(instance1.classValue());
    }

    @Test
    public void testConstructor2() {
        SparseInstance instance2 = new SparseInstance(3);
        assertEquals(3, instance2.noAttributes);
        assertEquals(0.0, instance2.defaultValue, 0.0);
        assertNull(instance2.classValue());
    }

    @Test
    public void testConstructor3() {
        SparseInstance instance3 = new SparseInstance(3, 1.0, "Class A");
        assertEquals(3, instance3.noAttributes);
        assertEquals(1.0, instance3.defaultValue, 0.0);
        assertEquals("Class A", instance3.classValue());
    }

    @Test
    public void testConstructor4() {
        SparseInstance instance4 = new SparseInstance(3, "Class B");
        assertEquals(3, instance4.noAttributes);
        assertEquals(0.0, instance4.defaultValue, 0.0);
        assertEquals("Class B", instance4.classValue());
    }

    @Test
    public void testConstructor5() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance5 = new SparseInstance(datavector);
        assertEquals(3, instance5.noAttributes);
        assertEquals(0.0, instance5.defaultValue, 0.0);
        assertNull(instance5.classValue());
        assertEquals(1.0, instance5.get(0), 0.0);
        assertEquals(2.0, instance5.get(1), 0.0);
        assertEquals(3.0, instance5.get(2), 0.0);
    }

    @Test
    public void testConstructor6() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance6 = new SparseInstance(datavector, 1.0);
        assertEquals(3, instance6.noAttributes);
        assertEquals(1.0, instance6.defaultValue, 0.0);
        assertNull(instance6.classValue());
        assertEquals(1.0, instance6.get(0), 0.0);
        assertEquals(2.0, instance6.get(1), 0.0);
        assertEquals(3.0, instance6.get(2), 0.0);
    }

    @Test
    public void testConstructor7() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance7 = new SparseInstance(datavector, "Class C");
        assertEquals(3, instance7.noAttributes);
        assertEquals(0.0, instance7.defaultValue, 0.0);
        assertEquals("Class C", instance7.classValue());
        assertEquals(1.0, instance7.get(0), 0.0);
        assertEquals(2.0, instance7.get(1), 0.0);
        assertEquals(3.0, instance7.get(2), 0.0);
    }

    @Test
    public void testConstructor8() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance8 = new SparseInstance(datavector, 1.0, "Class D");
        assertEquals(3, instance8.noAttributes);
        assertEquals(1.0, instance8.defaultValue, 0.0);
        assertEquals("Class D", instance8.classValue());
        assertEquals(1.0, instance8.get(0), 0.0);
        assertEquals(2.0, instance8.get(1), 0.0);
        assertEquals(3.0, instance8.get(2), 0.0);
    }

    @Test
    public void testValue() {
        instance.put(0, 1.0);
        assertEquals(1.0, instance.value(0), 0.0);
    }

    @Test
    public void testClear() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
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
        assertEquals(2, instance.entrySet().size());
    }

    @Test
    public void testGet() {
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(0.0, instance.get(1), 0.0);
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
        assertTrue(instance.keySet().contains(0));
        assertTrue(instance.keySet().contains(1));
    }

    @Test
    public void testPut() {
        instance.put(0, 1.0);
        assertEquals(1.0, instance.get(0), 0.0);
    }

    @Test
    public void testPutAll() {
        SparseInstance instance1 = new SparseInstance(3, 1.0, null);
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);
        instance.putAll(instance1);
        assertEquals(2, instance.size());
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
    }

    @Test
    public void testRemove() {
        instance.put(0, 1.0);
        instance.remove(0);
        assertEquals(0, instance.size());
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
        assertEquals(2, instance.values().size());
        assertTrue(instance.values().contains(1.0));
        assertTrue(instance.values().contains(2.0));
    }

    @Test
    public void testNoAttributes1() {
        instance.put(0, 1.0);
        assertEquals(1, instance.noAttributes());
    }

    @Test
    public void testNoAttributes2() {
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testNoAttributes3() {
        SparseInstance instance2 = new SparseInstance(0, 0.0, null);
        assertEquals(0, instance2.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.removeAttribute(0);
        assertEquals(1, instance.size());
        assertEquals(2.0, instance.get(0), 0.0);
    }

    @Test
    public void testToString() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals("{0=1.0;null}", instance.toString());
    }

    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance(3, 0.0, null);
        assertTrue(instance.hashCode() == instance1.hashCode());
    }

    @Test
    public void testEquals1() {
        SparseInstance instance1 = new SparseInstance(3, 0.0, null);
        assertEquals(instance, instance1);
        assertEquals(instance, instance);
        assertNotEquals(instance, null);
        assertNotEquals(instance, new Object());
    }

    @Test
    public void testEquals2() {
        SparseInstance instance1 = new SparseInstance(3, 0.0, null);
        instance.put(0, 1.0);
        assertNotEquals(instance, instance1);
        instance1.put(0, 1.0);
        assertEquals(instance, instance1);
    }

    @Test
    public void testCopy() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        SparseInstance copy = (SparseInstance) instance.copy();
        assertEquals(instance, copy);
        assertNotSame(instance, copy);
    }

    @Test
    public void testRemoveAttributes() {
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttributes(Set.of(0, 2));
        assertEquals(1, instance.size());
        assertEquals(2.0, instance.get(0), 0.0);
    }

}
