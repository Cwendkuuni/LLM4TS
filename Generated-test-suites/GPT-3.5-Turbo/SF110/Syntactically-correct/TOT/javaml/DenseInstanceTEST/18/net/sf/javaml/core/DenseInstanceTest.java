import static org.junit.Assert.*;
import org.junit.Test;

public class DenseInstanceTest {
    
    @Test
    public void testValue_PositiveIndex() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        double expectedResult = 2.0;
        double actualResult = instance.value(1);
        assertEquals(expectedResult, actualResult, 0.00001);
    }
    
    @Test
    public void testValue_NegativeIndex() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        try {
            double result = instance.value(-1);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected, test passes
        }
    }
    
    @Test
    public void testValue_IndexOutOfBounds() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        try {
            double result = instance.value(3);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected, test passes
        }
    }
    
    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        instance.clear();
        double[] expectedAttributes = new double[] {0.0, 0.0, 0.0};
        assertArrayEquals(expectedAttributes, instance.getAttributes(), 0.00001);
    }
    
    @Test
    public void testContainsKey_ValidInteger() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        boolean containsKey = instance.containsKey(1);
        assertTrue(containsKey);
    }
    
    @Test
    public void testConainsKey_InvalidObject() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        boolean containsKey = instance.containsKey("abc");
        assertFalse(containsKey);
    }
    
    @Test
    public void testContainsValue_NumberValuePresent() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        boolean containsValue = instance.containsValue(2.0);
        assertTrue(containsValue);
    }
    
    @Test
    public void testContainsValue_NumberValueNotPresent() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        boolean containsValue = instance.containsValue(4.0);
        assertFalse(containsValue);
    }
    
    @Test
    public void testContainsValue_InvalidObject() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        boolean containsValue = instance.containsValue("abc");
        assertFalse(containsValue);
    }
    
    @Test
    public void testEntrySet() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(instance.value(key), value, 0.00001);
        }
    }
    
    @Test
    public void testGet_ValidKey() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Double value = instance.get(1);
        assertNotNull(value);
        assertEquals(2.0, value, 0.00001);
    }
    
    @Test
    public void testGet_InvalidKey() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Double value = instance.get("abc");
        assertNull(value);
    }
    
    @Test
    public void testIsEmpty_False() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertFalse(instance.isEmpty());
    }
    
    @Test
    public void testIsEmpty_True() {
        DenseInstance instance = new DenseInstance(new double[] {});
        assertTrue(instance.isEmpty());
    }
    
    @Test
    public void testKeySet() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        for (int key : keySet) {
            double value = instance.get(key);
            assertEquals(instance.value(key), value, 0.00001);
        }
    }
    
    @Test
    public void testPut_ValidKey() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Double oldValue = instance.put(1, 4.0);
        assertNotNull(oldValue);
        assertEquals(2.0, oldValue, 0.00001);
        assertEquals(4.0, instance.get(1), 0.00001);
    }
    
    @Test
    public void testPut_InvalidKey() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        try {
            instance.put("abc", 4.0);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            // Exception expected, test passes
        }
    }
    
    @Test
    public void testPutAll_ValidMap() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(2, 5.0);
        instance.putAll(map);
        assertEquals(4.0, instance.get(0), 0.00001);
        assertEquals(2.0, instance.get(1), 0.00001);
        assertEquals(5.0, instance.get(2), 0.00001);
    }
    
    @Test
    public void testPutAll_InvalidMap() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Map<String, Double> map = new HashMap<>();
        map.put("abc", 4.0);
        try {
            instance.putAll(map);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            // Exception expected, test passes
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove_ValidKey() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        instance.remove(1);
        instance.get(1); // should throw UnsupportedOperationException
    }
    
    @Test
    public void testSize() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        int size = instance.size();
        assertEquals(3, size);
    }
    
    @Test
    public void testValues() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        for (double value : values) {
            assertTrue(instance.containsValue(value));
        }
    }
    
    @Test
    public void testNoAttributes() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        int noAttributes = instance.noAttributes();
        assertEquals(3, noAttributes);
    }
    
    @Test
    public void testToString() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        String expectedResult = "{[1.0, 2.0, 3.0];null}";
        assertEquals(expectedResult, instance.toString());
    }
    
    @Test
    public void testRemoveAttribute() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        instance.removeAttribute(1);
        double[] expectedAttributes = new double[] {1.0, 3.0};
        assertArrayEquals(expectedAttributes, instance.getAttributes(), 0.00001);
    }
    
    @Test
    public void testHashCode_SameAttributes() {
        DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentAttributes() {
        DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[] {1.0, 4.0, 3.0});
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
    }
    
    @Test
    public void testEquals_SameInstance() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(instance, instance);
    }
    
    @Test
    public void testEquals_NullObject() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertNotEquals(instance, null);
    }
    
    @Test
    public void testEquals_DifferentClass() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertNotEquals(instance, "abc");
    }
    
    @Test
    public void testEquals_EqualAttributes() {
        DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(instance1, instance2);
        assertEquals(instance2, instance1);
    }
    
    @Test
    public void testEquals_DifferentAttributes() {
        DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[] {1.0, 4.0, 3.0});
        assertNotEquals(instance1, instance2);
        assertNotEquals(instance2, instance1);
    }
    
    @Test
    public void testCopy() {
        DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance instance2 = instance1.copy();
        assertNotSame(instance1, instance2);
        assertEquals(instance1, instance2);
    }
    
    @Test
    public void testRemoveAttributes() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        instance.removeAttributes(indices);
        double[] expectedAttributes = new double[] {1.0, 3.0};
        assertArrayEquals(expectedAttributes, instance.getAttributes(), 0.00001);
    }
}