package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TShortHashTest {
    @Test
    public void testGetNoEntryValue() {
        TShortHash tShortHash = new TShortHash();
        assertEquals(0, tShortHash.getNoEntryValue());
        
        tShortHash = new TShortHash(10);
        assertEquals(0, tShortHash.getNoEntryValue());
        
        tShortHash = new TShortHash(10, 0.5f);
        assertEquals(0, tShortHash.getNoEntryValue());
        
        tShortHash = new TShortHash(10, 0.5f, (short) -1);
        assertEquals(-1, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);
        
        assertTrue(tShortHash.contains((short) 1));
        assertTrue(tShortHash.contains((short) 2));
        assertFalse(tShortHash.contains((short) 3));
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);
        tShortHash.insertKey((short) 3);
        
        TShortProcedure procedure = new TShortProcedure() {
            
            int sum = 0;
            
            @Override
            public boolean execute(short value) {
                sum += value;
                return true;
            }
        };
        
        assertTrue(tShortHash.forEach(procedure));
        assertEquals(6, procedure.sum);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        
        tShortHash.removeAt(0);
        assertFalse(tShortHash.contains((short) 1));
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
        TShortHash tShortHash = new TShortHash();
        
        assertEquals(0, tShortHash.insertKey((short) 1));
        assertEquals(-1, tShortHash.insertKey((short) 1)); // Duplicates are not inserted
        
        assertEquals(1, tShortHash.insertKey((short) 2));
        
        tShortHash.removeAt(0);
        
        assertEquals(0, tShortHash.insertKey((short) 2)); // Slot is reused
    }
}