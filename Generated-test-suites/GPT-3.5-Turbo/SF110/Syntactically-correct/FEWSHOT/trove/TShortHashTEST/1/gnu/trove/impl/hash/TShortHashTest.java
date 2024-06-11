package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import gnu.trove.procedure.TShortProcedure;
import org.junit.Test;

public class TShortHashTest {
    
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);
        
        assertTrue(tShortHash.contains((short) 1));
        assertFalse(tShortHash.contains((short) 3));
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);
        
        final int[] sum = {0};
        tShortHash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                sum[0] += value;
                return true;
            }
        });
        
        assertEquals(3, sum[0]);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);
        assertTrue(tShortHash.contains((short) 2));
        
        tShortHash.removeAt(1);
        assertFalse(tShortHash.contains((short) 2));
    }
    
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);
        
        assertEquals(0, tShortHash.index((short) 1));
        assertEquals(1, tShortHash.index((short) 2));
        assertEquals(-1, tShortHash.index((short) 3));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash(2, 0.75f, (short) -1);
        
        assertEquals(0, tShortHash.insertKey((short) 1));
        assertEquals(1, tShortHash.insertKey((short) 2));
        assertEquals(0, tShortHash.insertKey((short) 3));
        assertEquals(2, tShortHash.insertKey((short) 4));
    }
}