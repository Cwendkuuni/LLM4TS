import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class DenseInstanceTest {

    private DenseInstance instance;

    @Before
    public void setUp() {
        // Initialize a DenseInstance object for testing
        double[] attributes = { 1.0, 2.0, 3.0 };
        instance = new DenseInstance(attributes);
    }

    @Test
    public void testValue() {
        // Test the value() method
        double expectedValue = 1.0;
        assertEquals(expectedValue, instance.value(0), 0.001);
    }

    @Test
    public void testClear() {
        // Test the clear() method
        instance.clear();
        double[] expectedAttributes = { 0.0, 0.0, 0.0 };
        assertArrayEquals(expectedAttributes, instance.getAttributes(), 0.001);
    }

    @Test
    public void testContainsKey() {
        // Test the containsKey() method
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        // Test the containsValue() method
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(0.0));
    }

    @Test
    public void testEntrySet() {
        // Test the entrySet() method
        Set<Map.Entry<Integer, Double>> expectedSet = new HashSet<>();
        expectedSet.add(new AbstractMap.SimpleEntry<>(0, 1.0));
        expectedSet.add(new AbstractMap.SimpleEntry<>(1, 2.0));
        expectedSet.add(new AbstractMap.SimpleEntry<>(2, 3.0));
        assertEquals(expectedSet, instance.entrySet());
    }

    @Test
    public void testGet() {
        // Test the get() method
        assertEquals(1.0, instance.get(0), 0.001);
        assertNull(instance.get(3));
    }

    @Test
    public void testIsEmpty() {
        // Test the isEmpty() method
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        // Test the keySet() method
        Set<Integer> expectedSet = new TreeSet<>();
        expectedSet.add(0);
        expectedSet.add(1);
        expectedSet.add(2);
        assertEquals(expectedSet, instance.keySet());
    }

    @Test
    public void testPut() {
        // Test the put() method
        assertEquals(1.0, instance.put(0, 10.0), 0.001);
        assertEquals(10.0, instance.get(0), 0.001);
    }

    @Test
    public void testPutAll() {
        // Test the putAll() method
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 10.0);
        map.put(2, 20.0);
        instance.putAll(map);
        assertEquals(10.0, instance.get(1), 0.001);
        assertEquals(20.0, instance.get(2), 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        // Test the remove() method
        instance.remove(0);
    }

    @Test
    public void testSize() {
        // Test the size() method
        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        // Test the values() method
        Collection<Double> expectedValues = new ArrayList<>();
        expectedValues.add(1.0);
        expectedValues.add(2.0);
        expectedValues.add(3.0);
        assertEquals(expectedValues, instance.values());
    }

    @Test
    public void testNoAttributes() {
        // Test the noAttributes() method
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testToString() {
        // Test the toString() method
        String expectedString = "{[1.0, 2.0, 3.0];null}";
        assertEquals(expectedString, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        // Test the removeAttribute() method
        instance.removeAttribute(1);
        double[] expectedAttributes = { 1.0, 3.0 };
        assertArrayEquals(expectedAttributes, instance.getAttributes(), 0.001);
    }

    @Test
    public void testHashCode() {
        // Test the hashCode() method
        int expectedHashCode = Objects.hash(1.0, 2.0, 3.0);
        assertEquals(expectedHashCode, instance.hashCode());
    }

    @Test
    public void testEquals() {
        // Test the equals() method
        DenseInstance anotherInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
        assertEquals(instance, anotherInstance);
    }

    @Test
    public void testCopy() {
        // Test the copy() method
        DenseInstance copyInstance = (DenseInstance) instance.copy();
        assertArrayEquals(instance.getAttributes(), copyInstance.getAttributes(), 0.001);
        assertEquals(instance.classValue(), copyInstance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        // Test the removeAttributes() method
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        double[] expectedAttributes = { 2.0 };
        assertArrayEquals(expectedAttributes, instance.getAttributes(), 0.001);
    }
}