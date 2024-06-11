import org.junit.*;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    // Test case for DenseInstance(double[] att)
    @Test
    public void testDenseInstance1() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertArrayEquals(attributes, instance.getAttributes(), 0.001);
        assertNull(instance.getClassValue());
    }

    // Test case for DenseInstance(double[] att, Object classValue)
    @Test
    public void testDenseInstance2() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(attributes, classValue);

        assertArrayEquals(attributes, instance.getAttributes(), 0.001);
        assertEquals(classValue, instance.getClassValue());
    }

    // Test case for DenseInstance(int size)
    @Test
    public void testDenseInstance3() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);

        assertEquals(size, instance.getAttributes().length);
        assertNull(instance.getClassValue());
    }

    // Test case for value(int pos)
    @Test
    public void testValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertEquals(attributes[0], instance.value(0), 0.001);
        assertEquals(attributes[1], instance.value(1), 0.001);
        assertEquals(attributes[2], instance.value(2), 0.001);
    }

    // Test case for clear()
    @Test
    public void testClear() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        instance.clear();

        assertArrayEquals(new double[attributes.length], instance.getAttributes(), 0.001);
    }

    // Test case for containsKey(Object key)
    @Test
    public void testContainsKey() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
        assertFalse(instance.containsKey("key"));
    }

    // Test case for containsValue(Object value)
    @Test
    public void testContainsValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
        assertFalse(instance.containsValue("value"));
    }

    // Test case for entrySet()
    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertEquals(attributes.length, instance.entrySet().size());
        assertTrue(instance.entrySet().containsAll(instance.keySet()));
    }

    // Test case for get(Object key)
    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertEquals(attributes[0], instance.get(0), 0.001);
        assertEquals(attributes[1], instance.get(1), 0.001);
        assertEquals(attributes[2], instance.get(2), 0.001);
        assertNull(instance.get(3));
        assertNull(instance.get("key"));
    }

    // Test case for isEmpty()
    @Test
    public void testIsEmpty() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertFalse(instance.isEmpty());
    }

    // Test case for keySet()
    @Test
    public void testKeySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertEquals(attributes.length, instance.keySet().size());
        assertTrue(instance.keySet().containsAll(instance.entrySet()));
    }

    // Test case for put(Integer key, Double value)
    @Test
    public void testPut() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        double newValue = 4.0;
        Double oldValue = instance.put(0, newValue);

        assertEquals(attributes[0], oldValue, 0.001);
        assertEquals(newValue, instance.value(0), 0.001);
    }

    // Test case for putAll(Map<? extends Integer, ? extends Double> m)
    @Test
    public void testPutAll() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        Map<Integer, Double> newAttributes = new HashMap<>();
        newAttributes.put(0, 4.0);
        newAttributes.put(1, 5.0);
        newAttributes.put(2, 6.0);

        instance.putAll(newAttributes);

        assertEquals(newAttributes.get(0), instance.value(0), 0.001);
        assertEquals(newAttributes.get(1), instance.value(1), 0.001);
        assertEquals(newAttributes.get(2), instance.value(2), 0.001);
    }

    // Test case for remove(Object key)
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        instance.remove(0);
    }

    // Test case for size()
    @Test
    public void testSize() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertEquals(attributes.length, instance.size());
    }

    // Test case for values()
    @Test
    public void testValues() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertEquals(attributes.length, instance.values().size());
        assertTrue(instance.values().containsAll(instance.entrySet()));
    }

    // Test case for noAttributes()
    @Test
    public void testNoAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertEquals(attributes.length, instance.noAttributes());
    }

    // Test case for toString()
    @Test
    public void testToString() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(attributes, classValue);

        assertEquals("{[1.0, 2.0, 3.0];class}", instance.toString());
    }

    // Test case for removeAttribute(int i)
    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        int indexToRemove = 1;
        instance.removeAttribute(indexToRemove);

        assertEquals(attributes.length - 1, instance.getAttributes().length);
        assertArrayEquals(new double[]{attributes[0], attributes[2]}, instance.getAttributes(), 0.001);
    }

    // Test case for hashCode()
    @Test
    public void testHashCode() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes);
        DenseInstance instance2 = new DenseInstance(attributes);

        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    // Test case for equals(Object obj)
    @Test
    public void testEquals() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes);
        DenseInstance instance2 = new DenseInstance(attributes);
        DenseInstance instance3 = new DenseInstance(new double[]{4.0, 5.0, 6.0});

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    // Test case for copy()
    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance1 = new DenseInstance(attributes, classValue);
        DenseInstance instance2 = instance1.copy();

        assertArrayEquals(instance1.getAttributes(), instance2.getAttributes(), 0.001);
        assertEquals(instance1.getClassValue(), instance2.getClassValue());
    }

    // Test case for removeAttributes(Set<Integer> indices)
    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        Set<Integer> indicesToRemove = new HashSet<>();
        indicesToRemove.add(0);
        indicesToRemove.add(2);
        instance.removeAttributes(indicesToRemove);

        assertEquals(attributes.length - indicesToRemove.size(), instance.getAttributes().length);
        assertArrayEquals(new double[]{attributes[1]}, instance.getAttributes(), 0.001);
    }
}