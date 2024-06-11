import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TShortHashTest {

    private TShortHash tShortHash;
    
    @Before
    public void setUp() {
        tShortHash = new TShortHash();
    }
    
    @Test
    public void testGetNoEntryValue() {
        short noEntryValue = tShortHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        int capacity = tShortHash.setUp(initialCapacity);
        assertEquals(capacity, tShortHash._set.length);
    }
    
    @Test
    public void testContains() {
        short val = 10;
        assertFalse(tShortHash.contains(val));
        
        tShortHash.insertKey(val);
        
        assertTrue(tShortHash.contains(val));
    }
    
    @Test
    public void testRemoveAt() {
        short val = 10;
        int index = tShortHash.insertKey(val);
        
        tShortHash.removeAt(index);
        
        assertFalse(tShortHash.contains(val));
    }
    
    @Test
    public void testIndex() {
        short val = 10;
        int index = tShortHash.insertKey(val);
        
        int returnedIndex = tShortHash.index(val);
        
        assertEquals(index, returnedIndex);
    }
    
    @Test
    public void testInsertKey() {
        short val = 10;
        int index = tShortHash.insertKey(val);
        
        assertTrue(tShortHash.contains(val));
        assertEquals(index, -tShortHash.getNoEntryValue() - 1);
    }
    
    @Test
    public void testInsertKeyRehash() {
        short val = 10;
        int index = tShortHash.insertKey(val);
        
        index = tShortHash.insertKeyRehash(val, index, 0, (byte) 1);
        
        assertTrue(tShortHash.contains(val));
        assertEquals(index, -tShortHash.getNoEntryValue() - 1);
    }
    
    @Test
    public void testInsertKeyAt() {
        short val = 10;
        int index = 5;
        
        tShortHash.insertKeyAt(index, val);
        
        assertTrue(tShortHash.contains(val));
        assertEquals(tShortHash._set[index], val);
    }
    
}