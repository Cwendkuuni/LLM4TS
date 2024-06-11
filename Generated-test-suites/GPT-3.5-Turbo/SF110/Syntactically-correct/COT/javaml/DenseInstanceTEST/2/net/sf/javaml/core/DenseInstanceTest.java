import static org.junit.Assert.*;
import org.junit.*;

import java.util.*;

public class DenseInstanceTest {
    private DenseInstance instance;

    @Before
    public void setUp() {
        // Create a DenseInstance object with size 3
        instance = new DenseInstance(3);
    }

    @Test
    public void testConstructor() {
        // Test the constructor with attributes and classValue
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "A";
        DenseInstance newInstance = new DenseInstance(att, classValue);
        assertArrayEquals(att, newInstance.getAttributes(), 0.0001);
        assertEquals(classValue, newInstance.classValue());

        // Test the constructor with attributes only
        DenseInstance newInstance2 = new DenseInstance(att);
        assertArrayEquals(att, newInstance2.getAttributes(), 0.0001);
        assertNull(newInstance2.classValue());
    }

    @Test
    public void testValue() {
        // Test value() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        assertEquals(1.0, newInstance.value(0), 0.0001);
        assertEquals(2.0, newInstance.value(1), 0.0001);
        assertEquals(3.0, newInstance.value(2), 0.0001);
        try {
            newInstance.value(3); // Index out of bounds
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Exception handled successfully
        }
    }

    @Test
    public void testClear() {
        // Test clear() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        newInstance.clear();
        assertArrayEquals(new double[3], newInstance.getAttributes(), 0.0001);
    }

    @Test
    public void testContainsKey() {
        // Test containsKey() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        assertTrue(newInstance.containsKey(0));
        assertTrue(newInstance.containsKey(1));
        assertTrue(newInstance.containsKey(2));
        assertFalse(newInstance.containsKey(-1));
        assertFalse(newInstance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        // Test containsValue() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        assertTrue(newInstance.containsValue(1.0));
        assertTrue(newInstance.containsValue(2.0));
        assertTrue(newInstance.containsValue(3.0));
        assertFalse(newInstance.containsValue(0.0));
        assertFalse(newInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        // Test entrySet() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entrySet = newInstance.entrySet();
        HashMap<Integer, Double> expectedMap = new HashMap<Integer, Double>();
        expectedMap.put(0, 1.0);
        expectedMap.put(1, 2.0);
        expectedMap.put(2, 3.0);
        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(expectedMap.get(key), value, 0.0001);
        }
    }

    @Test
    public void testGet() {
        // Test get() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        assertEquals(1.0, newInstance.get(0), 0.0001);
        assertEquals(2.0, newInstance.get(1), 0.0001);
        assertEquals(3.0, newInstance.get(2), 0.0001);
        assertNull(newInstance.get(3));
    }

    @Test
    public void testIsEmpty() {
        // Test isEmpty() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        assertFalse(newInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        // Test keySet() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        SortedSet<Integer> keySet = newInstance.keySet();
        SortedSet<Integer> expectedSet = new TreeSet<Integer>();
        expectedSet.add(0);
        expectedSet.add(1);
        expectedSet.add(2);
        assertEquals(expectedSet, keySet);
    }

    @Test
    public void testPut() {
        // Test put() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        newInstance.put(0, 10.0);
        newInstance.put(1, 20.0);
        newInstance.put(2, 30.0);
        assertEquals(10.0, newInstance.get(0), 0.0001);
        assertEquals(20.0, newInstance.get(1), 0.0001);
        assertEquals(30.0, newInstance.get(2), 0.0001);
    }

    @Test
    public void testPutAll() {
        // Test putAll() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 10.0);
        map.put(1, 20.0);
        map.put(2, 30.0);
        newInstance.putAll(map);
        assertEquals(10.0, newInstance.get(0), 0.0001);
        assertEquals(20.0, newInstance.get(1), 0.0001);
        assertEquals(30.0, newInstance.get(2), 0.0001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        // Test remove() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        newInstance.remove(0);
    }

    @Test
    public void testSize() {
        // Test size() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        assertEquals(3, newInstance.size());
    }

    @Test
    public void testValues() {
        // Test values() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        Collection<Double> values = newInstance.values();
        List<Double> expectedList = Arrays.asList(1.0, 2.0, 3.0);
        assertEquals(expectedList, values);
    }

    @Test
    public void testNoAttributes() {
        // Test noAttributes() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        assertEquals(3, newInstance.noAttributes());
    }

    @Test
    public void testToString() {
        // Test toString() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance newInstance = new DenseInstance(att);
        String expectedString = "{[1.0, 2.0, 3.0];null}";
        assertEquals(expectedString, newInstance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        // Test removeAttribute() method
        double[] att = {1.0, 2.0, 3.0, 4.0};
        DenseInstance newInstance = new DenseInstance(att);
        newInstance.removeAttribute(1);
        double[] expectedAttributes = {1.0, 3.0, 4.0};
        assertArrayEquals(expectedAttributes, newInstance.getAttributes(), 0.0001);
    }

    @Test
    public void testHashCode() {
        // Test hashCode() method
        double[] att = {1.0, 2.0, 3.0, 4.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = new DenseInstance(att);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        // Test equals() method
        double[] att1 = {1.0, 2.0, 3.0, 4.0};
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att1);
        DenseInstance instance3 = new DenseInstance(att2);
        assertEquals(instance1, instance2);
        assertNotEquals(instance1, instance3);
    }

    @Test
    public void testCopy() {
        // Test copy() method
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att);
        DenseInstance instance2 = (DenseInstance)instance1.copy();
        assertArrayEquals(instance1.getAttributes(), instance2.getAttributes(), 0.0001);
        assertEquals(instance1.classValue(), instance2.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        // Test removeAttributes() method
        double[] att = {1.0, 2.0, 3.0, 4.0};
        DenseInstance newInstance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(0);
        indices.add(2);
        newInstance.removeAttributes(indices);
        double[] expectedAttributes = {2.0, 4.0};
        assertArrayEquals(expectedAttributes, newInstance.getAttributes(), 0.0001);
    }

    // TODO: Add additional test cases for potential edge cases and exception handling scenarios

    // Add more test methods here
    
    
}