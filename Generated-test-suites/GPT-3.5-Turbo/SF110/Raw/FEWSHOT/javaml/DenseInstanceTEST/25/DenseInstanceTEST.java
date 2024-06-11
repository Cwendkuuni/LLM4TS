
package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {
    @Test
    public void testConstructor() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertNotNull(instance);
        assertArrayEquals(att, instance.attributes, 0.0001);
        assertNull(instance.classValue());
    }

    @Test
    public void testConstructorWithClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertNotNull(instance);
        assertArrayEquals(att, instance.attributes, 0.0001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        assertArrayEquals(new double[att.length], instance.attributes, 0.0001);
    }

    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(0.0));
    }

    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(att.length, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(Double.valueOf(att[entry.getKey()]), entry.getValue());
        }
    }

    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(Double.valueOf(att[0]), instance.get(0));
        assertEquals(Double.valueOf(att[1]), instance.get(1));
        assertEquals(Double.valueOf(att[2]), instance.get(2));
        assertNull(instance.get(-1));
        assertNull(instance.get(3));
    }

    @Test
    public void testIsEmpty() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(att.length, keySet.size());
        Iterator<Integer> iterator = keySet.iterator();
        for (int i = 0; i < att.length; i++) {
            assertEquals(Integer.valueOf(i), iterator.next());
        }
    }

    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Double oldValue = instance.put(0, 4.0);
        assertEquals(Double.valueOf(att[0]), oldValue);
        assertEquals(Double.valueOf(4.0), instance.get(0));
    }

    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(2, 5.0);
        instance.putAll(map);
        assertEquals(Double.valueOf(4.0), instance.get(0));
        assertEquals(Double.valueOf(2.0), instance.get(1));
        assertEquals(Double.valueOf(5.0), instance.get(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.remove(0);
    }

    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(att.length, instance.size());
    }

    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Collection<Double> values = instance.values();
        assertEquals(att.length, values.size());
        for (double value : att) {
            assertTrue(values.contains(value));
        }
    }

    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(att.length, instance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals("{" + Arrays.toString(att) + ";null}", instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(1);
        assertEquals(2, instance.size());
        assertEquals(Double.valueOf(att[0]), instance.get(0));
        assertEquals(Double.valueOf(att[2]), instance.get(1));
    }

    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        int expectedHashCode = 1;
        expectedHashCode = 31 * expectedHashCode + Arrays.hashCode(att);
        assertEquals(expectedHashCode, instance.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(att2);
        double[] att3 = {3.0, 2.0, 1.0};
        DenseInstance instance3 = new DenseInstance(att3);

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(new Object()));
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        DenseInstance copy = (DenseInstance) instance.copy();
        assertNotSame(instance, copy);
        assertArrayEquals(instance.attributes, copy.attributes, 0.0001);
        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0, 4.0, 5.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        instance.removeAttributes(indices);
        assertEquals(3, instance.size());
        assertEquals(Double.valueOf(att[0]), instance.get(0));
        assertEquals(Double.valueOf(att[2]), instance.get(1));
        assertEquals(Double.valueOf(att[4]), instance.get(2));
    }
}
