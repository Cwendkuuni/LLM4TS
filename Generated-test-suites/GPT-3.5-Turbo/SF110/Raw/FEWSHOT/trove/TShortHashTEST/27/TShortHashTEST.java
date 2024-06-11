package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TShortHashTest {
    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 5);
        hash.insertKey((short) 10);
        hash.insertKey((short) 15);
        
        assertTrue(hash.contains((short) 5));
        assertTrue(hash.contains((short) 10));
        assertTrue(hash.contains((short) 15));
        assertFalse(hash.contains((short) 20));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 5);
        hash.insertKey((short) 10);
        hash.insertKey((short) 15);
        
        TShortProcedure procedure = new TShortProcedure() {
            short sum = 0;
            
            public boolean execute(short value) {
                sum += value;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(30, procedure.sum);
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 5);
        hash.insertKey((short) 10);
        hash.insertKey((short) 15);
        
        assertEquals(0, hash.index((short) 5));
        assertEquals(1, hash.index((short) 10));
        assertEquals(2, hash.index((short) 15));
        assertEquals(-1, hash.index((short) 20));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 5);
        hash.insertKey((short) 10);
        hash.insertKey((short) 15);
        
        assertTrue(hash.contains((short) 5));
        assertTrue(hash.contains((short) 10));
        assertTrue(hash.contains((short) 15));
    }
}