import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithAttAndClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "Class A";

        DenseInstance instance = new DenseInstance(att, classValue);

        assertArrayEquals(att, instance.getAttributes(), 0.0001);
        assertEquals(classValue, instance.getClassValue());
    }

    @Test
    public void testConstructorWithSize() {
        int size = 5;

        DenseInstance instance = new DenseInstance(size);

        double[] expectedAtt = new double[size];
        assertArrayEquals(expectedAtt, instance.getAttributes(), 0.0001);
        assertNull(instance.getClassValue());
    }

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(2.0, instance.value(1), 0.0001);
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        instance.clear();

        double[] expectedAtt = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedAtt, instance.getAttributes(), 0.0001);
    }

    @Test
    public void testContainsKeyWithValidKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertTrue(instance.containsKey(1));
    }

    @Test
    public void testContainsKeyWithInvalidKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsKeyWithNonIntegerKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertFalse(instance.containsKey("key"));
    }

    @Test
    public void testContainsValueWithValidValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertTrue(instance.containsValue(2.0));
    }

    @Test
    public void testContainsValueWithInvalidValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testContainsValueWithNonNumberValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertFalse(instance.containsValue("value"));
    }

    @Test
    public void testEntrySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(3, instance.entrySet().size());
    }

    @Test
    public void testGetWithValidKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertEquals(2.0, instance.get(1), 0.0001);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetWithInvalidKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        instance.get(3);
    }

    @Test
    public void testIsEmptyWithNonEmptyInstance() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        assertFalse(instance.isEmpty());
    }

    @Test
    public void testIsEmptyWithEmptyInstance() {
        double[] att = {0.0, 0.0, 0.0};
        DenseInstance instance = new DenseInstance(att);

        assertTrue(instance.isEmpty());
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

        instance.put(1, 4.0);

        assertEquals(4.0, instance.get(1), 0.0001);
    }

    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(2, 5.0);

        instance.putAll(map);

        assertEquals(4.0, instance.get(0), 0.0001);
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

        assertEquals("{[1.0, 2.0, 3.0];null}", instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        instance.removeAttribute(1);

        double[] expectedAtt = {1.0, 3.0};
        assertArrayEquals(expectedAtt, instance.getAttributes(), 0.0001);
    }

    @Test
    public void testHashCode() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(att);

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
        Object classValue = "Class A";
        DenseInstance instance1 = new DenseInstance(att, classValue);

        DenseInstance instance2 = instance1.copy();

        assertArrayEquals(att, instance2.getAttributes(), 0.0001);
        assertEquals(classValue, instance2.getClassValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);

        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);

        instance.removeAttributes(indices);

        double[] expectedAtt = {2.0};
        assertArrayEquals(expectedAtt, instance.getAttributes(), 0.0001);
    }
}