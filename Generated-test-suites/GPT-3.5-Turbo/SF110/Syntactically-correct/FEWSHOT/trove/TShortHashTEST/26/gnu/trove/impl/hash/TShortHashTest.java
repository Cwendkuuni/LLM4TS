package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TShortHashTest {
    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 1);
        assertTrue(hash.contains((short) 1));
        assertFalse(hash.contains((short) 2));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        
        TShortProcedure procedure = new TShortProcedure() {
            int count = 0;
            
            public boolean execute(short value) {
                count++;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(3, procedure.count);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.removeAt(0);
        assertFalse(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        assertEquals(0, hash.index((short) 1));
        assertEquals(1, hash.index((short) 2));
        assertEquals(-1, hash.index((short) 3));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.insertKey((short) 1));
        assertEquals(-1, hash.insertKey((short) 1));
    }
}