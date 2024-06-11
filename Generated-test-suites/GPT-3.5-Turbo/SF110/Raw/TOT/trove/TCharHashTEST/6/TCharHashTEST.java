
package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {
  
    private TCharHash tCharHash;
    
    @Before
    public void setup() {
        tCharHash = new TCharHash(10, 0.75f, (char)0);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        assertEquals(10, tCharHash.setUp(10));
    }
    
    @Test
    public void testContains() {
        assertFalse(tCharHash.contains((char)123));
        
        tCharHash.insertKey((char)123);
        assertTrue(tCharHash.contains((char)123));
    }
    
    @Test
    public void testForEach() {
        assertFalse(tCharHash.forEach(value -> {
            // execute procedure
            return true;
        }));
        
        tCharHash.insertKey((char)123);
        assertTrue(tCharHash.forEach(value -> {
            // execute procedure
            return true;
        }));
    }
    
    @Test
    public void testRemoveAt() {
        tCharHash.insertKey((char)123);
        
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains((char)123));
    }
    
    @Test
    public void testIndex() {
        tCharHash.insertKey((char)123);
        assertEquals(0, tCharHash.index((char)123));
        
        assertEquals(-1, tCharHash.index((char)321));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, tCharHash.insertKey((char)123));
        assertFalse(tCharHash.insertKey((char)123) >= 0); // Already inserted
        
        for (int i = 1; i <= 8; i++) {
            assertTrue(tCharHash.insertKey((char)(100 + i)) >= 0);
        }
        assertThrows(IllegalStateException.class, () -> tCharHash.insertKey((char)321)); // No free slots
    }
    
    @Test
    public void testInsertKeyRehash() {
        tCharHash.insertKey((char)123);
        tCharHash.insertKey((char)321);
        
        assertEquals(1, tCharHash.insertKeyRehash((char)123, 0, 0, (byte)0));
        assertEquals(-1, tCharHash.insertKeyRehash((char)123, 0, 0, (byte)1));
        assertEquals(2, tCharHash.insertKeyRehash((char)123, 0, 0, (byte)2));
        
        assertEquals(3, tCharHash.insertKeyRehash((char)432, 1, 1, (byte)1));
        assertEquals(-2, tCharHash.insertKeyRehash((char)432, 1, 1, (byte)2));
    }
    
    @Test
    public void testInsertKeyAt() {
        tCharHash.insertKeyAt(0, (char)123);
        assertTrue(tCharHash.contains((char)123));
    }
}

