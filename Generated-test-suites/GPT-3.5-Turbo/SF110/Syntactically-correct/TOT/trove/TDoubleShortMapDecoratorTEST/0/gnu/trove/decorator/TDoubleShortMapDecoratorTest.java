import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator mapDecorator;
    
    @Before
    public void setUp() {
        mapDecorator = new TDoubleShortMapDecorator();
    }
    
    @Test
    public void testPut_NullKeyAndNullValue_ReturnNull() {
        // Arrange
        Double key = null;
        Short value = null;
        
        // Act
        Short result = mapDecorator.put(key, value);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testPut_NullKeyAndNonNullValue_ReturnWrappedValue() {
        // Arrange
        Double key = null;
        Short value = 10;
        
        // Act
        Short result = mapDecorator.put(key, value);
        
        // Assert
        assertEquals(value, result);
    }
    
    @Test
    public void testPut_NonNullKeyAndNullValue_ReturnNull() {
        // Arrange
        Double key = 5.0;
        Short value = null;
        
        // Act
        Short result = mapDecorator.put(key, value);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testPut_NonNullKeyAndNonNullValue_ReturnWrappedValue() {
        // Arrange
        Double key = 5.0;
        Short value = 10;
        
        // Act
        Short result = mapDecorator.put(key, value);
        
        // Assert
        assertEquals(value, result);
    }
    
    @Test
    public void testGet_NullKey_ReturnNull() {
        // Arrange
        Object key = null;
        
        // Act
        Short result = mapDecorator.get(key);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testGet_NonNullKey_ReturnWrappedValue() {
        // Arrange
        Object key = 5.0;
        mapDecorator.put((Double)key, (short)10);
        
        // Act
        Short result = mapDecorator.get(key);
        
        // Assert
        assertEquals((short)10, (short)result);
    }
    
    @Test
    public void testClear_EmptyMap_ReturnSizeZero() {
        // Arrange
        // Nothing to arrange as map is already empty
        
        // Act
        mapDecorator.clear();
        
        // Assert
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testClear_NonEmptyMap_ReturnSizeZero() {
        // Arrange
        mapDecorator.put(5.0, (short)10);
        
        // Act
        mapDecorator.clear();
        
        // Assert
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testRemove_NullKey_ReturnNull() {
        // Arrange
        Object key = null;
        
        // Act
        Short result = mapDecorator.remove(key);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testRemove_NonNullKey_ReturnWrappedValue() {
        // Arrange
        Object key = 5.0;
        mapDecorator.put((Double)key, (short)10);
        
        // Act
        Short result = mapDecorator.remove(key);
        
        // Assert
        assertEquals((short)10, (short)result);
    }
    
    @Test
    public void testEntrySet_EmptyMap_ReturnSizeZero() {
        // Arrange
        // Nothing to arrange as map is already empty
        
        // Act
        int size = mapDecorator.entrySet().size();
        
        // Assert
        assertEquals(0, size);
    }
    
    @Test
    public void testEntrySet_NonEmptyMap_ReturnSizeGreaterThanZero() {
        // Arrange
        mapDecorator.put(5.0, (short)10);
        
        // Act
        int size = mapDecorator.entrySet().size();
        
        // Assert
        assertTrue(size > 0);
    }
    
    @Test
    public void testContainsValue_ExistingValue_ReturnTrue() {
        // Arrange
        mapDecorator.put(5.0, (short)10);
        Short value = 10;
        
        // Act
        boolean result = mapDecorator.containsValue(value);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testContainsValue_NonExistingValue_ReturnFalse() {
        // Arrange
        Short value = 20;
        
        // Act
        boolean result = mapDecorator.containsValue(value);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testContainsKey_ExistingKey_ReturnTrue() {
        // Arrange
        Double key = 5.0;
        mapDecorator.put(key, (short)10);
        
        // Act
        boolean result = mapDecorator.containsKey(key);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testContainsKey_NonExistingKey_ReturnFalse() {
        // Arrange
        Double key = 10.0;
        
        // Act
        boolean result = mapDecorator.containsKey(key);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testSize_EmptyMap_ReturnZero() {
        // Arrange
        // Nothing to arrange as map is already empty
        
        // Act
        int size = mapDecorator.size();
        
        // Assert
        assertEquals(0, size);
    }
    
    @Test
    public void testSize_NonEmptyMap_ReturnSizeGreaterThanZero() {
        // Arrange
        mapDecorator.put(5.0, (short)10);
        
        // Act
        int size = mapDecorator.size();
        
        // Assert
        assertTrue(size > 0);
    }
    
    @Test
    public void testIsEmpty_EmptyMap_ReturnTrue() {
        // Arrange
        // Nothing to arrange as map is already empty
        
        // Act
        boolean isEmpty = mapDecorator.isEmpty();
        
        // Assert
        assertTrue(isEmpty);
    }
    
    @Test
    public void testIsEmpty_NonEmptyMap_ReturnFalse() {
        // Arrange
        mapDecorator.put(5.0, (short)10);
        
        // Act
        boolean isEmpty = mapDecorator.isEmpty();
        
        // Assert
        assertFalse(isEmpty);
    }
    
    @Test
    public void testPutAll_EmptyMap_ReturnSameSize() {
        // Arrange
        Map<Double, Short> inputMap = new HashMap<Double, Short>();
        
        // Act
        mapDecorator.putAll(inputMap);
        
        // Assert
        assertEquals(inputMap.size(), mapDecorator.size());
    }
    
    @Test
    public void testPutAll_NonEmptyMap_ReturnModifiedSize() {
        // Arrange
        Map<Double, Short> inputMap = new HashMap<Double, Short>();
        inputMap.put(5.0, (short)10);
        
        // Act
        mapDecorator.putAll(inputMap);
        
        // Assert
        assertTrue(mapDecorator.size() > 0);
    }
    
    @Test
    public void testWrapKey_ValidDouble_ReturnSameKey() {
        // Arrange
        double key = 5.0;
        
        // Act
        Double result = mapDecorator.wrapKey(key);
        
        // Assert
        assertEquals(key, result, 0.0);
    }
    
    @Test
    public void testUnwrapKey_ValidDouble_ReturnSameKey() {
        // Arrange
        Object key = 5.0;
        
        // Act
        double result = mapDecorator.unwrapKey(key);
        
        // Assert
        assertEquals(key, result, 0.0);
    }
    
    @Test
    public void testWrapValue_ValidShort_ReturnSameValue() {
        // Arrange
        short value = 10;
        
        // Act
        Short result = mapDecorator.wrapValue(value);
        
        // Assert
        assertEquals(value, (short)result);
    }
    
    @Test
    public void testUnwrapValue_ValidShort_ReturnSameValue() {
        // Arrange
        Object value = (short)10;
        
        // Act
        short result = mapDecorator.unwrapValue(value);
        
        // Assert
        assertEquals(value, (short)result);
    }
    
    @Test
    public void testReadExternal_ValidInput_ThrowNoException() {
        // Arrange
        ObjectInput in = null;
        
        // Act & Assert
        assertDoesNotThrow(() -> mapDecorator.readExternal(in));
    }
    
    @Test
    public void testWriteExternal_ValidOutput_ThrowNoException() {
        // Arrange
        ObjectOutput out = null;
        
        // Act & Assert
        assertDoesNotThrow(() -> mapDecorator.writeExternal(out));
    }
}