import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {

    // Test case for TCharHash()
    @Test
    public void testDefaultConstructor() {
        TCharHash charHash = new TCharHash();
        
        // Check if the initial capacity is set correctly
        assertEquals(10, charHash.capacity());
        
        // Check if the no_entry_value is set correctly
        assertEquals('\u0000', charHash.getNoEntryValue());
    }
    
    // Test case for TCharHash(int initialCapacity)
    @Test
    public void testConstructorWithCapacity() {
        TCharHash charHash = new TCharHash(20);
        
        // Check if the initial capacity is set correctly
        assertEquals(20, charHash.capacity());
        
        // Check if the no_entry_value is set correctly
        assertEquals('\u0000', charHash.getNoEntryValue());
    }
    
    // Test case for TCharHash(int initialCapacity, float loadFactor)
    @Test
    public void testConstructorWithCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(20, 0.75f);
        
        // Check if the initial capacity is set correctly
        assertEquals(20, charHash.capacity());
        
        // Check if the load factor is set correctly
        assertEquals(0.75f, charHash.loadFactor(), 0.01);
        
        // Check if the no_entry_value is set correctly
        assertEquals('\u0000', charHash.getNoEntryValue());
    }
    
    // Test case for TCharHash(int initialCapacity, float loadFactor, char no_entry_value)
    @Test
    public void testConstructorWithCapacityLoadFactorAndNoEntryValue() {
        TCharHash charHash = new TCharHash(20, 0.75f, 'X');
        
        // Check if the initial capacity is set correctly
        assertEquals(20, charHash.capacity());
        
        // Check if the load factor is set correctly
        assertEquals(0.75f, charHash.loadFactor(), 0.01);
        
        // Check if the no_entry_value is set correctly
        assertEquals('X', charHash.getNoEntryValue());
    }
    
    // Test case for contains(char val)
    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        
        // Test when the set is empty
        assertFalse(charHash.contains('A'));
        
        // Test when the set contains the value
        charHash.insertKey('A');
        assertTrue(charHash.contains('A'));
        
        // Test when the set does not contain the value
        assertFalse(charHash.contains('B'));
    }
    
    // Test case for forEach(TCharProcedure procedure)
    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        
        // Test when the set is empty
        assertFalse(charHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                return false;
            }
        }));
        
        // Test when the set contains values
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.insertKey('C');
        
        assertTrue(charHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                return true;
            }
        }));
    }
    
    // Test case for removeAt(int index)
    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        
        // Test when the index is valid and contains a value
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.insertKey('C');
        
        charHash.removeAt(1);
        
        assertFalse(charHash.contains('B'));
        
        // Test when the index is invalid
        assertEquals('\u0000', charHash._set[1]);
    }
    
    // Test case for index(char val)
    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash();
        
        // Test when the set is empty
        assertEquals(-1, charHash.index('A'));
        
        // Test when the set contains the value
        charHash.insertKey('A');
        charHash.insertKey('B');
        
        assertEquals(0, charHash.index('A'));
        
        // Test when the set does not contain the value
        assertEquals(-1, charHash.index('C'));
    }
    
    // Test case for indexRehashed(char key, int index, int hash, byte state)
    @Test
    public void testIndexRehashed() {
        TCharHash charHash = new TCharHash();
        
        // Test when the set is empty
        assertEquals(-1, charHash.indexRehashed('A', 0, 0, (byte) 0));
        
        // Test when the set contains the value
        charHash.insertKey('A');
        charHash.insertKey('B');
        
        assertEquals(0, charHash.indexRehashed('A', 0, 0, (byte) 1));
        
        // Test when the set does not contain the value
        assertEquals(-1, charHash.indexRehashed('C', 0, 0, (byte) 1));
    }
    
    // Test case for insertKey(char val)
    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash(2);
        
        // Test when the index is available
        assertEquals(0, charHash.insertKey('A'));
        assertEquals(1, charHash.insertKey('B'));
        
        // Test when the index is not available
        assertEquals(0, charHash.insertKey('C'));
        assertEquals(1, charHash.insertKey('D'));
        
        // Test when the set is full
        assertEquals(2, charHash.insertKey('E'));
    }
    
    // Test case for insertKeyRehash(char val, int index, int hash, byte state)
    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash(2);
        
        // Test when the index is available
        assertEquals(0, charHash.insertKeyRehash('A', 0, 0, (byte) 0));
        assertEquals(1, charHash.insertKeyRehash('B', 1, 1, (byte) 0));
        
        // Test when the index is not available
        assertEquals(0, charHash.insertKeyRehash('C', 0, 0, (byte) 0));
        assertEquals(1, charHash.insertKeyRehash('D', 1, 1, (byte) 0));
        
        // Test when the set is full
        try {
            charHash.insertKeyRehash('E', 2, 2, (byte) 0);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Exception expected
        }
    }
    
    // Test case for insertKeyAt(int index, char val)
    @Test
    public void testInsertKeyAt() {
        TCharHash charHash = new TCharHash(2);
        
        charHash.insertKeyAt(0, 'A');
        charHash.insertKeyAt(1, 'B');
        
        assertEquals('A', charHash._set[0]);
        assertEquals('B', charHash._set[1]);
    }
    
}