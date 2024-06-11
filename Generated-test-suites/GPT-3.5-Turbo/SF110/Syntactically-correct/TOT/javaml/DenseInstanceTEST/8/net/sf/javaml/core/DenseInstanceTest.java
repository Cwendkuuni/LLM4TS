import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testConstructor1() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        double[] expected = {1.0, 2.0, 3.0};

        assertArrayEquals(expected, instance.attributes, 0.0);
        assertNull(instance.classValue());
    }

    @Test
    public void testConstructor2() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);

        double[] expected = {1.0, 2.0, 3.0};

        assertArrayEquals(expected, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testConstructor3() {
        DenseInstance instance = new DenseInstance();

        assertNotNull(instance);
        assertNull(instance.classValue());
    }

    @Test
    public void testConstructor4() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);

        double[] expected = {0.0, 0.0, 0.0};

        assertArrayEquals(expected, instance.attributes, 0.0);
        assertNull(instance.classValue());
    }

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        instance.clear();

        double[] expected = {0.0, 0.0, 0.0};

        assertArrayEquals(expected, instance.attributes, 0.0);
    }

    @Test
    public void testContainsKeyWithValidKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
    }

    @Test
    public void testContainsKeyWithInvalidKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValueWithValidValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
    }

    @Test
    public void testContainsValueWithInvalidValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertFalse(instance.containsValue(0.0));
    }

    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(3, instance.entrySet().size());
    }

    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
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
        
        assertEquals(3, instance.keySet().size());
    }

    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Double oldValue = instance.put(0, 4.0);

        assertEquals(1.0, oldValue, 0.0);
        assertEquals(4.0, instance.get(0), 0.0);
    }

    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);

        instance.putAll(map);

        double[] expected = {4.0, 5.0, 6.0};

        assertArrayEquals(expected, instance.attributes, 0.0);
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

        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(3, instance.values().size());
    }

    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        String expected = "{[1.0, 2.0, 3.0];null}";

        assertEquals(expected, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        instance.removeAttribute(1);

        double[] expected = {1.0, 3.0};

        assertArrayEquals(expected, instance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        double[] att1 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);

        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(att2);

        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);

        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(att2);

        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        DenseInstance copy = instance.copy();

        assertArrayEquals(att, copy.attributes, 0.0);
        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Set<Integer> indices = new HashSet<>();
        indices.add(1);

        instance.removeAttributes(indices);

        double[] expected = {1.0, 3.0};

        assertArrayEquals(expected, instance.attributes, 0.0);
    }

}