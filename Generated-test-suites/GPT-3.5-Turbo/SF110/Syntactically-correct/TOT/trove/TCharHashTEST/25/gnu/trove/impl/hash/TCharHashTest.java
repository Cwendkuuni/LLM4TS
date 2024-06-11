
import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    // Test case for TCharHash constructor without parameters
    @Test
    public void testDefaultConstructor() {
        TCharHash hash = new TCharHash();
        
        // Test that default capacity is set correctly
        int expectedCapacity = 10;
        assertEquals(expectedCapacity, hash.capacity());
        
        // Test that default load factor is set correctly
        float expectedLoadFactor = 0.5f;
        assertEquals(expectedLoadFactor, hash.loadFactor(), 0.0001);
        
        // Test that no_entry_value is set correctly
        char expectedNoEntryValue = '\0';
        assertEquals(expectedNoEntryValue, hash.getNoEntryValue());
    }
    
    // Test case for TCharHash constructor with initial capacity
    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 20;
        TCharHash hash = new TCharHash(initialCapacity);
        
        // Test that capacity is set correctly
        assertEquals(initialCapacity, hash.capacity());
        
        // Test that default load factor is set correctly
        float expectedLoadFactor = 0.5f;
        assertEquals(expectedLoadFactor, hash.loadFactor(), 0.0001);
        
        // Test that no_entry_value is set correctly
        char expectedNoEntryValue = '\0';
        assertEquals(expectedNoEntryValue, hash.getNoEntryValue());
    }
    
    // Test case for TCharHash constructor with initial capacity and load factor
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 20;
        float loadFactor = 0.7f;
        TCharHash hash = new TCharHash(initialCapacity, loadFactor);
        
        // Test that capacity is set correctly
        assertEquals(initialCapacity, hash.capacity());
        
        // Test that load factor is set correctly
        assertEquals(loadFactor, hash.loadFactor(), 0.0001);
        
        // Test that no_entry_value is set correctly
        char expectedNoEntryValue = '\0';
        assertEquals(expectedNoEntryValue, hash.getNoEntryValue());
    }
    
    // Test case for TCharHash constructor with initial capacity, load factor and no_entry_value
    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        int initialCapacity = 20;
        float loadFactor = 0.7f;
        char noEntryValue = '!';
        TCharHash hash = new TCharHash(initialCapacity, loadFactor, noEntryValue);
        
        // Test that capacity is set correctly
        assertEquals(initialCapacity, hash.capacity());
        
        // Test that load factor is set correctly
        assertEquals(loadFactor, hash.loadFactor(), 0.0001);
        
        // Test that no_entry_value is set correctly
        assertEquals(noEntryValue, hash.getNoEntryValue());
    }
    
    // Test case for contains method with existing value
    @Test
    public void testContainsExistingValue() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        
        // Test that contains returns true for an existing value
        assertTrue(hash.contains('A'));
    }
    
    // Test case for contains method with non-existing value
    @Test
    public void testContainsNonExistingValue() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        
        // Test that contains returns false for a non-existing value
        assertFalse(hash.contains('B'));
    }
    
    // Test case for forEach method with procedure returning true for all values
    @Test
    public void testForEachProcedureAllTrue() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        // Test that forEach method returns true when procedure returns true for all values
        boolean result = hash.forEach(value -> {
            return true;
        });
        assertTrue(result);
    }
    
    // Test case for forEach method with procedure returning false for some values
    @Test
    public void testForEachProcedureSomeFalse() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        // Test that forEach method stops iteration and returns false when procedure returns false
        boolean result = hash.forEach(value -> {
            if (value == 'B') {
                return false;
            }
            return true;
        });
        assertFalse(result);
    }
    
    // Test case for removeAt method
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        
        // Test that removeAt method removes the correct value
        hash.removeAt(1);
        assertFalse(hash.contains('B'));
        
        // Test that size is updated correctly after removal
        int expectedSize = 1;
        assertEquals(expectedSize, hash.size());
    }
    
    // Test case for index method with existing value
    @Test
    public void testIndexExistingValue() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        
        // Test that index method returns the correct index for an existing value
        int expectedIndex = 0;
        assertEquals(expectedIndex, hash.index('A'));
    }
    
    // Test case for index method with non-existing value
    @Test
    public void testIndexNonExistingValue() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        
        // Test that index method returns -1 for a non-existing value
        int expectedIndex = -1;
        assertEquals(expectedIndex, hash.index('B'));
    }
    
    // Test case for indexRehashed method
    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        // Test that indexRehashed method returns the correct index for an existing value
        int expectedIndex = 1;
        assertEquals(expectedIndex, hash.indexRehashed('B', 0, 1, (byte) 1));
    }
    
    // Test case for insertKey method with an empty slot available
    @Test
    public void testInsertKeyWithEmptySlot() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        
        // Test that insertKey method inserts the key in the empty slot
        int expectedIndex = 1;
        assertEquals(expectedIndex, hash.insertKey('B'));
        
        // Test that size is updated correctly after insertion
        int expectedSize = 2;
        assertEquals(expectedSize, hash.size());
    }
    
    // Test case for insertKey method with an existing key
    @Test
    public void testInsertKeyWithExistingKey() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        
        // Test that insertKey method returns the negative index for an existing key
        int expectedIndex = -2;
        assertEquals(expectedIndex, hash.insertKey('B'));
    }
    
    // Test case for insertKey method with no empty slots available
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyNoEmptySlots() {
        TCharHash hash = new TCharHash();
        for (int i = 0; i < 10; i++) {
            hash.insertKey((char) ('A' + i));
        }
        
        // Test that insertKey method throws IllegalStateException when no empty slots available
        hash.insertKey('K');
    }
    
    // Test case for insertKeyAt method
    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        
        // Test that insertKeyAt method inserts the value at the correct index
        hash.insertKeyAt(0, 'A');
        assertTrue(hash.contains('A'));
    }
}