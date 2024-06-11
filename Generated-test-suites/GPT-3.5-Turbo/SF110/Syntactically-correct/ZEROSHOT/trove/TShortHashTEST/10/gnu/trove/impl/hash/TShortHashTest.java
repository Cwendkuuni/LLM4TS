package gnu.trove.impl.hash;

import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {
    
    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        short noEntryValue = hash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
    }
    
    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        short[] set = hash._set;
        assertEquals(10, set.length);
    }
    
    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash._set = new short[]{1, 2, 3};
        assertTrue(hash.contains(2));
        assertFalse(hash.contains(5));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash._states = new byte[]{1, 1, 1};
        hash._set = new short[]{1, 2, 3};
        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short value) {
                if (value == 2) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        assertTrue(hash.forEach(procedure));
        
        hash._states = new byte[]{1, 1, 2};
        assertFalse(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash._set = new short[]{1, 2, 3};
        hash._states = new byte[]{1, 1, 1};
        hash.removeAt(1);
        assertEquals(0, hash._set[1]);
        assertEquals(0, hash._states[1]);
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash._set = new short[]{1, 2, 3};
        hash._states = new byte[]{1, 1, 1};
        int index = hash.index(2);
        assertEquals(1, index);
        
        index = hash.index(4);
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();
        hash._set = new short[]{1, 2, 3};
        hash._states = new byte[]{1, 1, 1};
        int index = hash.indexRehashed(2, 1, 1, (byte)1);
        assertEquals(1, index);
        
        index = hash.indexRehashed(4, 1, 1, (byte)1);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        hash._states = new byte[]{1, 1, 1};
        hash._set = new short[]{1, 2, 3};
        int index = hash.insertKey(4);
        assertEquals(0, hash._set[0]);
        assertEquals(1, hash._set[1]);
        assertEquals(2, hash._set[2]);
        assertEquals(4, hash._set[3]);
        assertEquals(1, hash._states[0]);
        assertEquals(1, hash._states[1]);
        assertEquals(1, hash._states[2]);
        assertEquals(1, hash._states[3]);
        assertEquals(3, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        hash._states = new byte[]{1, 1, 1};
        hash._set = new short[]{1, 2, 3};
        int index = hash.insertKeyRehash(4, 1, 1, (byte)1);
        assertEquals(0, hash._set[0]);
        assertEquals(2, hash._set[1]);
        assertEquals(3, hash._set[2]);
        assertEquals(4, hash._set[3]);
        assertEquals(1, hash._states[0]);
        assertEquals(1, hash._states[1]);
        assertEquals(1, hash._states[2]);
        assertEquals(1, hash._states[3]);
        assertEquals(1, index);
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash();
        hash._set = new short[]{1, 2, 3};
        hash._states = new byte[]{1, 1, 1};
        hash.insertKeyAt(1, (short)4);
        assertEquals(1, hash._set[0]);
        assertEquals(4, hash._set[1]);
        assertEquals(3, hash._set[2]);
        assertEquals(1, hash._states[0]);
        assertEquals(1, hash._states[1]);
        assertEquals(1, hash._states[2]);
    }
}