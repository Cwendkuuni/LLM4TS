
import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {
    
    @Test
    public void testValue_PositiveIndex_ReturnsCorrectValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        double result = instance.value(1);
        
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testValue_NegativeIndex_ReturnsCorrectValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        double result = instance.value(-1);
        
        assertEquals(3.0, result, 0.0001);
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testValue_IndexOutOfRange_ThrowsException() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.value(3);
    }
    
    @Test
    public void testClear_SetAttributesToZero() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.clear();
        
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, instance.getAttributes(), 0.0001);
    }
    
    @Test
    public void testContainsKey_ValidIntegerKey_ReturnsTrue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        boolean result = instance.containsKey(1);
        
        assertTrue(result);
    }
    
    @Test
    public void testContainsKey_InvalidIntegerKey_ReturnsFalse() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        boolean result = instance.containsKey(3);
        
        assertFalse(result);
    }
    
    @Test
    public void testContainsValue_ValidDoubleValue_ReturnsTrue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        boolean result = instance.containsValue(2.0);
        
        assertTrue(result);
    }
    
    @Test
    public void testContainsValue_InvalidDoubleValue_ReturnsFalse() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        boolean result = instance.containsValue(4.0);
        
        assertFalse(result);
    }
    
    @Test
    public void testEntrySet_ReturnsCorrectSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        int result = instance.entrySet().size();
        
        assertEquals(3, result);
    }
    
    @Test
    public void testGet_ValidIntegerKey_ReturnsCorrectValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Double result = instance.get(2);
        
        assertEquals(3.0, result, 0.0001);
    }
    
    @Test
    public void testGet_InvalidIntegerKey_ReturnsNull() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Double result = instance.get(3);
        
        assertNull(result);
    }
    
    @Test
    public void testIsEmpty_NotEmpty_ReturnsFalse() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        boolean result = instance.isEmpty();
        
        assertFalse(result);
    }
    
    @Test
    public void testPut_ValidKeyAndValue_ReturnsPreviousValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Double result = instance.put(0, 4.0);
        
        assertEquals(1.0, result, 0.0001);
        assertEquals(4.0, instance.get(0), 0.0001);
    }
    
    @Test
    public void testPut_InvalidKey_ThrowsException() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        try {
            instance.put(3, 4.0);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Cannot unset values from a dense instance.", e.getMessage());
        }
    }
    
    @Test
    public void testPutAll_PopulateAttributesFromMap() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        
        instance.putAll(map);
        
        assertArrayEquals(new double[]{4.0, 5.0, 6.0}, instance.getAttributes(), 0.0001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove_ThrowsException() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.remove(1);
    }
    
    @Test
    public void testSize_ReturnsCorrectSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        int result = instance.size();
        
        assertEquals(3, result);
    }
    
    @Test
    public void testValues_ReturnsCorrectValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        Collection<Double> values = instance.values();
        
        assertArrayEquals(new Double[]{1.0, 2.0, 3.0}, values.toArray());
    }
    
    @Test
    public void testNoAttributes_ReturnsCorrectNumberOfAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        int result = instance.noAttributes();
        
        assertEquals(3, result);
    }
    
    @Test
    public void testToString_ReturnsCorrectStringRepresentation() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        String result = instance.toString();
        
        assertEquals("{[1.0, 2.0, 3.0];null}", result);
    }
    
    @Test
    public void testRemoveAttribute_ValidIndex_RemovesAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        instance.removeAttribute(1);
        
        assertArrayEquals(new double[]{1.0, 3.0}, instance.getAttributes(), 0.0001);
    }
    
    @Test
    public void testRemoveAttributes_RemoveMultipleAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        
        instance.removeAttributes(indices);
        
        assertArrayEquals(new double[]{2.0}, instance.getAttributes(), 0.0001);
    }
    
    @Test
    public void testHashCode_ReturnsCorrectHashCode() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        
        int result1 = instance1.hashCode();
        int result2 = instance2.hashCode();
        
        assertEquals(result1, result2);
    }
    
    @Test
    public void testEquals_EqualsAttributes_ReturnsTrue() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        
        boolean result = instance1.equals(instance2);
        
        assertTrue(result);
    }
    
    @Test
    public void testEquals_DifferentAttributes_ReturnsFalse() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {4.0, 5.0, 6.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        
        boolean result = instance1.equals(instance2);
        
        assertFalse(result);
    }
    
    @Test
    public void testCopy_CreatesDeepCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        
        DenseInstance copy = (DenseInstance) instance.copy();
        
        assertNotSame(instance, copy);
        assertArrayEquals(instance.getAttributes(), copy.getAttributes(), 0.0001);
        assertEquals(instance.classValue(), copy.classValue());
    }
    
    @Test
    public void testRemoveAttributes_RemoveAllAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(1);
        indices.add(2);
        
        instance.removeAttributes(indices);
        
        assertArrayEquals(new double[]{}, instance.getAttributes(), 0.0001);
    }
}
