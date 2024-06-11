import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testConstructor_1() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertArrayEquals(att, instance.attributes, 0.00001);
        assertNull(instance.classValue());
    }

    @Test
    public void testConstructor_2() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att, "class");
        assertArrayEquals(att, instance.attributes, 0.00001);
        assertEquals("class", instance.classValue());
    }

    @Test
    public void testConstructor_3() {
        DenseInstance instance = new DenseInstance(3);
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.attributes, 0.00001);
        assertNull(instance.classValue());
    }

    @Test
    public void testValue_1() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.value(0), 0.00001);
        assertEquals(2.0, instance.value(1), 0.00001);
        assertEquals(3.0, instance.value(2), 0.00001);
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.attributes, 0.00001);
    }

    @Test
    public void testContainsKey_1() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsKey_2() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
    }

    @Test
    public void testContainsValue_1() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testContainsValue_2() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsValue(1));
        assertTrue(instance.containsValue(2));
        assertTrue(instance.containsValue(3));
        assertFalse(instance.containsValue(4));
    }

    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(att[key], value, 0.00001);
        }
    }

    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(att[0], instance.get(0), 0.00001);
        assertEquals(att[1], instance.get(1), 0.00001);
        assertEquals(att[2], instance.get(2), 0.00001);
        assertNull(instance.get(3));
    }

    @Test
    public void testIsEmpty() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        for (int key : keySet) {
            assertTrue(instance.containsKey(key));
        }
    }

    @Test
    public void testPut_1() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(att[0], instance.put(0, 10.0), 0.00001);
        assertEquals(10.0, instance.value(0), 0.00001);
    }

    @Test
    public void testPut_2() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertNull(instance.put(3, 10.0));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 10.0);
        map.put(3, 20.0);
        instance.putAll(map);
        assertEquals(10.0, instance.value(0), 0.00001);
        assertEquals(20.0, instance.value(3), 0.00001);
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }

    @Test (expected = UnsupportedOperationException.class)
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
        assertEquals(3, values.size());
        for (double value : values) {
            assertTrue(instance.containsValue(value));
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
        assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.attributes, 0.00001);
    }

    @Test
    public void testHashCode() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        DenseInstance copy = (DenseInstance) instance.copy();
        assertArrayEquals(instance.attributes, copy.attributes, 0.00001);
        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        assertEquals(1, instance.noAttributes());
        double[] expected = {2.0};
        assertArrayEquals(expected, instance.attributes, 0.00001);
    }
}