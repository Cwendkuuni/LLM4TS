package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstance1() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
    }

    @Test
    public void testSparseInstance2() {
        SparseInstance instance = new SparseInstance(3);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.0, instance.value(0), 0.0);
        assertEquals(0.0, instance.value(1), 0.0);
        assertEquals(0.0, instance.value(2), 0.0);
    }

    @Test
    public void testSparseInstance3() {
        SparseInstance instance = new SparseInstance(3, 1.0);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(1.0, instance.value(1), 0.0);
        assertEquals(1.0, instance.value(2), 0.0);
    }

    @Test
    public void testSparseInstance4() {
        SparseInstance instance = new SparseInstance(3, "class");
        assertEquals(3, instance.noAttributes());
        assertEquals("class", instance.classValue());
    }

    @Test
    public void testSparseInstance5() {
        SparseInstance instance = new SparseInstance(3, 1.0, "class");
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(1.0, instance.value(1), 0.0);
        assertEquals(1.0, instance.value(2), 0.0);
        assertEquals("class", instance.classValue());
    }

    @Test
    public void testSparseInstance6() {
        double[] datavector = { 1.0, 0.0, 2.0 };
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(0.0, instance.value(1), 0.0);
        assertEquals(2.0, instance.value(2), 0.0);
    }

    @Test
    public void testSparseInstance7() {
        double[] datavector = { 1.0, 0.0, 2.0 };
        SparseInstance instance = new SparseInstance(datavector, 1.0);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(1.0, instance.value(1), 0.0);
        assertEquals(1.0, instance.value(2), 0.0);
    }

    @Test
    public void testSparseInstance8() {
        double[] datavector = { 1.0, 0.0, 2.0 };
        SparseInstance instance = new SparseInstance(datavector, "class");
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(0.0, instance.value(1), 0.0);
        assertEquals(2.0, instance.value(2), 0.0);
        assertEquals("class", instance.classValue());
    }

    @Test
    public void testSparseInstance9() {
        double[] datavector = { 1.0, 0.0, 2.0 };
        SparseInstance instance = new SparseInstance(datavector, 1.0, "class");
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(1.0, instance.value(1), 0.0);
        assertEquals(1.0, instance.value(2), 0.0);
        assertEquals("class", instance.classValue());
    }

    @Test
    public void testInitiate() {
        double[] datavector = { 1.0, 0.0, 2.0 };
        SparseInstance instance = new SparseInstance();
        instance.initiate(datavector);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(0.0, instance.value(1), 0.0);
        assertEquals(2.0, instance.value(2), 0.0);
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance(3, 1.0);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(1.0, instance.value(1), 0.0);
        assertEquals(1.0, instance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance(3, 1.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance(3, 1.0);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance(3, 1.0);
        assertTrue(instance.containsValue(1.0));
    }

    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance(3, 1.0);
        assertEquals(3, instance.entrySet().size());
    }

    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance(3, 1.0);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(1.0, instance.get(1), 0.0);
        assertEquals(1.0, instance.get(2), 0.0);
        assertEquals(0.0, instance.get(3), 0.0);
    }

    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance(3, 1.0);
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance(3, 1.0);
        assertEquals(3, instance.keySet().size());
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        assertNull(instance.put(0, 1.0));
        assertEquals(1.0, instance.value(0), 0.0);
    }

    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        HashMap<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        instance.putAll(map);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance(3, 1.0);
        assertEquals(1.0, instance.remove(0), 0.0);
        assertFalse(instance.containsKey(0));
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance(3, 1.0);
        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance(3, 1.0);
        assertEquals(3, instance.values().size());
    }

    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals(3, instance.noAttributes());
        instance.clear();
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttribute(1);
        assertFalse(instance.containsKey(1));
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals("{0=1.0, 1=2.0, 2=3.0;null}", instance.toString());
    }

    @Test
    public void testHashCode() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals(52150445, instance.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);
        instance1.put(2, 3.0);
        instance2.put(0, 1.0);
        instance2.put(1, 2.0);
        instance2.put(2, 3.0);
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(0, 1.0);
        instance1.put(1, 2.0);
        instance1.put(2, 3.0);
        SparseInstance instance2 = (SparseInstance) instance1.copy();
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.put(3, 4.0);
        HashSet<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        indices.add(3);
        instance.removeAttributes(indices);
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
        assertEquals(2, instance.noAttributes());
    }

}