import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testConstructor1() {
        TShortHash tShortHash = new TShortHash();
        
        // Test that no_entry_value is set to DEFAULT_SHORT_NO_ENTRY_VALUE
        assertEquals(tShortHash.getNoEntryValue(), Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
        // Test that _set array is initialized with no_entry_value
        for (short val : tShortHash._set) {
            assertEquals(val, Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
        }
    }
    
    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash(initialCapacity);
        
        // Test that no_entry_value is set to DEFAULT_SHORT_NO_ENTRY_VALUE
        assertEquals(tShortHash.getNoEntryValue(), Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
        // Test that _set array is initialized with no_entry_value
        for (short val : tShortHash._set) {
            assertEquals(val, Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
        }
        // Test that initialCapacity is set correctly
        assertEquals(tShortHash.capacity(), initialCapacity);
    }
    
    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor);
        
        // Test that no_entry_value is set to DEFAULT_SHORT_NO_ENTRY_VALUE
        assertEquals(tShortHash.getNoEntryValue(), Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
        // Test that _set array is initialized with no_entry_value
        for (short val : tShortHash._set) {
            assertEquals(val, Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
        }
        // Test that initialCapacity and loadFactor are set correctly
        assertEquals(tShortHash.capacity(), initialCapacity);
        assertEquals(tShortHash.loadFactor(), loadFactor, 0.01f);
    }
    
    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short no_entry_value = 0;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor, no_entry_value);
        
        // Test that no_entry_value is set correctly
        assertEquals(tShortHash.getNoEntryValue(), no_entry_value);
        // Test that _set array is initialized with no_entry_value
        for (short val : tShortHash._set) {
            assertEquals(val, no_entry_value);
        }
        // Test that initialCapacity and loadFactor are set correctly
        assertEquals(tShortHash.capacity(), initialCapacity);
        assertEquals(tShortHash.loadFactor(), loadFactor, 0.01f);
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash();
        
        // Test that setUp method returns the correct capacity
        assertEquals(tShortHash.setUp(initialCapacity), initialCapacity);
        // Test that _set array is initialized with the correct capacity
        assertEquals(tShortHash._set.length, initialCapacity);
    }
    
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        
        // Test when value exists in the hash
        tShortHash.insertKey((short) 10);
        assertTrue(tShortHash.contains((short) 10));
        
        // Test when value does not exist in the hash
        assertFalse(tShortHash.contains((short) 20));
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        
        // Test when procedure returns true for all values and hash is not empty
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 20);
        assertTrue(tShortHash.forEach(value -> value > 0));
        
        // Test when procedure returns false for one value
        tShortHash.insertKey((short) -10);
        assertFalse(tShortHash.forEach(value -> value > 0));
    }
    
    @Test
    public void testRemoveAt() {
        int index = 0;
        TShortHash tShortHash = new TShortHash();
        
        // Test when index exists in the hash
        tShortHash.insertKey((short) 10);
        tShortHash.removeAt(index);
        assertEquals(tShortHash._set[index], Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
        
        // Test when index does not exist in the hash
        tShortHash.insertKey((short) 20);
        tShortHash.removeAt(index);
        assertNotEquals(tShortHash._set[index], Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
    }
    
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        
        // Test when index exists in the hash
        tShortHash.insertKey((short) 10);
        assertEquals(tShortHash.index((short) 10), 0);
        
        // Test when index does not exist in the hash
        assertEquals(tShortHash.index((short) 20), -1);
    }
    
    @Test
    public void testIndexRehashed() {
        int index = 0;
        int hash = 12345;
        byte state = 1;
        TShortHash tShortHash = new TShortHash();
        
        // Test when loopIndex is not reached
        for (int i = 0; i < 10; i++) {
            tShortHash._states[i] = 1;
            tShortHash._set[i] = (short) i;
        }
        assertEquals(tShortHash.indexRehashed((short) 5, index, hash, state), 5);
        
        // Test when loopIndex is reached
        tShortHash.insertKey((short) 10);
        assertEquals(tShortHash.indexRehashed((short) 10, index, hash, state), -1);
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash();
        
        // Test when inserting key into a free slot
        tShortHash.insertKey((short) 10);
        assertEquals(tShortHash.insertKey((short) 20), 1);
        
        // Test when inserting key into a slot with the same value
        assertEquals(tShortHash.insertKey((short) 10), -2);
        
        // Test when no free slot available
        for (int i = 0; i < 10; i++) {
            tShortHash._states[i] = 1;
        }
        assertThrows(IllegalStateException.class, () -> tShortHash.insertKey((short) 30));
    }
    
    @Test
    public void testInsertKeyRehash() {
        int index = 0;
        int hash = 12345;
        byte state = 1;
        TShortHash tShortHash = new TShortHash();
        
        // Test when loopIndex is not reached
        for (int i = 0; i < 10; i++) {
            tShortHash._states[i] = 1;
            tShortHash._set[i] = (short) i;
        }
        assertEquals(tShortHash.insertKeyRehash((short) 5, index, hash, state), 5);
        
        // Test when loopIndex is reached and firstRemoved is not -1
        tShortHash._states[5] = 2;
        assertEquals(tShortHash.insertKeyRehash((short) 10, index, hash, state), 5);
        
        // Test when loopIndex is reached and firstRemoved is -1
        for (int i = 0; i < 10; i++) {
            tShortHash._states[i] = 1;
        }
        assertThrows(IllegalStateException.class, () -> tShortHash.insertKeyRehash((short) 15, index, hash, state));
    }
    
    @Test
    public void testInsertKeyAt() {
        int index = 0;
        short val = 10;
        TShortHash tShortHash = new TShortHash();
        
        // Test when inserting key at a free slot
        tShortHash.insertKeyAt(index, val);
        assertEquals(tShortHash._set[index], val);
        
        // Test when inserting key at a slot with the same value
        assertNotEquals(tShortHash._set[index], val + 5);
    }
}