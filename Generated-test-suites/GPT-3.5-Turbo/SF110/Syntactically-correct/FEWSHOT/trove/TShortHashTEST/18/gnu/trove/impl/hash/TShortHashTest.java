package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TShortHashTest {
    @Test
    public void testContains() {
        TShortHash tsh = new TShortHash();
        tsh._set = new short[10];
        tsh._states = new byte[10];
        
        tsh._set[0] = 1;
        tsh._states[0] = 1;
        
        assertFalse(tsh.contains((short)0));
        assertTrue(tsh.contains((short)1));
    }
    
    @Test
    public void testForEach() {
        TShortHash tsh = new TShortHash();
        tsh._set = new short[3];
        tsh._states = new byte[3];
        
        tsh._set[0] = 1;
        tsh._states[0] = 1;
        
        tsh._set[1] = 2;
        tsh._states[1] = 1;
        
        tsh._set[2] = 3;
        tsh._states[2] = 0;
        
        TIntProcedure procedure = new TIntProcedure();
        
        assertFalse(tsh.forEach(procedure));
        
        assertEquals(1, procedure.count);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tsh = new TShortHash();
        tsh._set = new short[3];
        tsh._states = new byte[3];
        
        tsh._set[0] = 1;
        tsh._states[0] = 1;
        
        tsh.removeAt(0);
        
        assertEquals(0, tsh._states[0]);
        assertEquals(0, tsh._set[0]);
    }
    
    @Test
    public void testIndex() {
        TShortHash tsh = new TShortHash();
        tsh._set = new short[3];
        tsh._states = new byte[3];
        
        tsh._set[0] = 1;
        tsh._states[0] = 1;
        
        tsh._set[1] = 2;
        tsh._states[1] = 1;
        
        tsh._set[2] = 3;
        tsh._states[2] = 0;
        
        assertEquals(-1, tsh.index((short)0));
        assertEquals(0, tsh.index((short)1));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tsh = new TShortHash();
        tsh._set = new short[6];
        tsh._states = new byte[6];
        
        tsh._states[0] = 0;
        tsh._states[1] = 1;
        
        assertEquals(0, tsh.insertKey((short)2));
        assertEquals(1, tsh.insertKeyRehash((short)2, 1, 0, (byte)1));
    }
}