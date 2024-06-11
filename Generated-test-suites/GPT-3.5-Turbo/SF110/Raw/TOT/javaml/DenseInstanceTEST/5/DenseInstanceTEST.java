import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testConstructor1() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertArrayEquals(att, instance.getAttributes(), 0.0001);
        assertNull(instance.getClassValue());
    }

    @Test
    public void testConstructor2() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertArrayEquals(att, instance.getAttributes(), 0.0001);
        assertEquals(classValue, instance.getClassValue());
    }

    @Test
    public void testConstructor3() {
        DenseInstance instance = new DenseInstance(3);
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
        assertNull(instance.getClassValue());
    }

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(2.0, instance.value(1), 0.0001);
    }

    @Test
    public void testContainsKey_Positive() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsKey(2));
    }

    @Test
    public void testContainsKey_Negative() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.containsKey(5));
    }

    @Test
    public void testContainsValue_Positive() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsValue(2.0));
    }

    @Test
    public void testContainsValue_Negative() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(3, instance.entrySet().size());
        for (int i = 0; i < att.length; i++) {
            assertTrue(instance.entrySet().contains(i, att[i]));
        }
    }

    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(2.0, instance.get(1), 0.0001);
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        double[] expected = {0.0, 0.0, 0.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
    }

    @Test
    public void testIsEmpty_Positive() {
        DenseInstance instance = new DenseInstance(3);
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testIsEmpty_Negative() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        DenseInstance instance = new DenseInstance(3);
        assertEquals(3, instance.keySet().size());
        for (int i = 0; i < 3; i++) {
            assertTrue(instance.keySet().contains(i));
        }
    }

    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Double oldValue = instance.put(1, 4.0);
        assertEquals(oldValue, Double.valueOf(2.0));
        assertEquals(4.0, instance.get(1), 0.0001);
    }

    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 4.0);
        map.put(2, 5.0);
        instance.putAll(map);
        assertEquals(4.0, instance.get(1), 0.0001);
        assertEquals(5.0, instance.get(2), 0.0001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.remove(1);
    }

    @Test
    public void testSize() {
        DenseInstance instance = new DenseInstance(3);
        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        List<Double> values = new ArrayList<>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);
        assertEquals(values, instance.values());
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
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        String expected = "{[1.0, 2.0, 3.0];class}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(1);
        double[] expected = {1.0, 3.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
    }

    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        int expected = Objects.hash(att);
        assertEquals(expected, instance.hashCode());
    }

    @Test
    public void testEquals_SameObject() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.equals(instance));
    }

    @Test
    public void testEquals_NullObject() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.equals("string"));
    }

    @Test
    public void testEquals_EqualsInstance() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue1 = "class";
        DenseInstance instance1 = new DenseInstance(att, classValue1);
        double[] att2 = {1.0, 2.0, 3.0};
        Object classValue2 = "class";
        DenseInstance instance2 = new DenseInstance(att2, classValue2);
        assertTrue(instance1.equals(instance2));
        assertTrue(instance2.equals(instance1));
    }

    @Test
    public void testEquals_DifferentAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue1 = "class";
        DenseInstance instance1 = new DenseInstance(att, classValue1);
        double[] att2 = {1.0, 2.0, 4.0};
        Object classValue2 = "class";
        DenseInstance instance2 = new DenseInstance(att2, classValue2);
        assertFalse(instance1.equals(instance2));
        assertFalse(instance2.equals(instance1));
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        DenseInstance copy = (DenseInstance) instance.copy();
        assertArrayEquals(att, copy.getAttributes(), 0.0001);
        assertEquals(classValue, copy.getClassValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        double[] expected = {2.0};
        assertArrayEquals(expected, instance.getAttributes(), 0.0001);
    }

}