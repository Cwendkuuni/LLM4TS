package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        
        assertFalse(tShortHash.contains((short) 0));
        assertFalse(tShortHash.contains((short) 1));
    }

    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        
        assertTrue(tShortHash.forEach(new TShortProcedure() {
            public boolean execute(short value) {
                return true;
            }
        }));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        
        tShortHash.removeAt(0);
        tShortHash.removeAt(1);
    }
    
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        
        assertEquals(-1, tShortHash.index((short) 0));
        assertEquals(-1, tShortHash.index((short) 1));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash();
        
        assertEquals(0, tShortHash.insertKey((short) 0));
        assertEquals(1, tShortHash.insertKey((short) 1));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TShortHash tShortHash = new TShortHash();
        
        tShortHash.insertKeyRehash((short) 0, 0, 0, (byte) 0);
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash tShortHash = new TShortHash();
        
        tShortHash.insertKeyAt(0, (short) 0);
        tShortHash.insertKeyAt(1, (short) 1);
    }
}