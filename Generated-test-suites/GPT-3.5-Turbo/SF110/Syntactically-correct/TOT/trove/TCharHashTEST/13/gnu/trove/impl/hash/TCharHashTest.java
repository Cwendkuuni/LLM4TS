import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testConstructor_NoArgs_Success() {
        // Arrange
        TCharHash tCharHash = new TCharHash();
        // Act
        
        // Assert
        assertNotNull(tCharHash);
    }
    
    @Test
    public void testConstructor_WithInitialCapacity_Success() {
        // Arrange
        int initialCapacity = 10;
        
        // Act
        TCharHash tCharHash = new TCharHash(initialCapacity);
        
        // Assert
        assertNotNull(tCharHash);
    }
    
    @Test
    public void testConstructor_WithInitialCapacityAndLoadFactor_Success() {
        // Arrange
        int initialCapacity = 10;
        float loadFactor = 0.8f;
        
        // Act
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor);
        
        // Assert
        assertNotNull(tCharHash);
    }
    
    @Test
    public void testConstructor_WithInitialCapacityAndLoadFactorAndNoEntryValue_Success() {
        // Arrange
        int initialCapacity = 10;
        float loadFactor = 0.8f;
        char noEntryValue = 'X';
        
        // Act
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor, noEntryValue);
        
        // Assert
        assertNotNull(tCharHash);
    }
    
    @Test
    public void testGetNoEntryValue_Success() {
        // Arrange
        TCharHash tCharHash = new TCharHash();
        char expectedNoEntryValue = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        
        // Act
        char actualNoEntryValue = tCharHash.getNoEntryValue();
        
        // Assert
        assertEquals(expectedNoEntryValue, actualNoEntryValue);
    }
    
    @Test
    public void testSetUp_Success() {
        // Arrange
        TCharHash tCharHash = new TCharHash();
        int initialCapacity = 10;
        int expectedCapacity = initialCapacity;
        
        // Act
        int actualCapacity = tCharHash.setUp(initialCapacity);
        
        // Assert
        assertEquals(expectedCapacity, actualCapacity);
    }
    
    @Test
    public void testContains_WhenValueExistsInSet_ReturnsTrue() {
        // Arrange
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        tCharHash.insertKey(val);
        
        // Act
        boolean result = tCharHash.contains(val);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testContains_WhenValueDoesNotExistInSet_ReturnsFalse() {
        // Arrange
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        char differentVal = 'B';
        tCharHash.insertKey(val);
        
        // Act
        boolean result = tCharHash.contains(differentVal);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testForEach_WhenProcedureReturnsTrueForAllValues_ReturnsTrue() {
        // Arrange
        TCharHash tCharHash = new TCharHash();
        char val1 = 'A';
        char val2 = 'B';
        tCharHash.insertKey(val1);
        tCharHash.insertKey(val2);
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                return true;
            }
        };
        
        // Act
        boolean result = tCharHash.forEach(procedure);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testForEach_WhenProcedureReturnsFalseForAnyValue_ReturnsFalse() {
        // Arrange
        TCharHash tCharHash = new TCharHash();
        char val1 = 'A';
        char val2 = 'B';
        tCharHash.insertKey(val1);
        tCharHash.insertKey(val2);
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                return value != 'B';
            }
        };
        
        // Act
        boolean result = tCharHash.forEach(procedure);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testRemoveAt_Success() {
        // Arrange
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        tCharHash.insertKey(val);
        int index = 0;
        
        // Act
        tCharHash.removeAt(index);
        boolean result = tCharHash.contains(val);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testInsertKey_WhenSlotIsAvailable_Success() {
        // Arrange
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        
        // Act
        int index = tCharHash.insertKey(val);
        boolean result = tCharHash.contains(val) && index >= 0;
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testInsertKey_WhenSlotIsNotAvailable_Success() {
        // Arrange
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        for (int i = 0; i < tCharHash._set.length; i++) {
            tCharHash._set[i] = val;
            tCharHash._states[i] = 1;
        }
        
        // Act
        try {
            tCharHash.insertKey(val);
            fail("Expected an IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Assert
            assertTrue(true);
        }
    }
    
}