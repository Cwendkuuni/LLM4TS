package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TShortHashTest {
    
    @Test
    public void testConstructorWithNoArgs() {
        TShortHash tShortHash = new TShortHash() {};
        assertEquals(0, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        TShortHash tShortHash = new TShortHash(10) {};
        assertEquals(0, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TShortHash tShortHash = new TShortHash(10, 0.75f) {};
        assertEquals(0, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        TShortHash tShortHash = new TShortHash(10, 0.75f, (short) -1) {};
        assertEquals(-1, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        assertTrue(tShortHash.contains(1));
        assertTrue(tShortHash.contains(2));
        assertTrue(tShortHash.contains(3));
        assertTrue(tShortHash.contains(4));
        assertTrue(tShortHash.contains(5));
        assertFalse(tShortHash.contains(6));
        assertFalse(tShortHash.contains(0));
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash._states = new byte[]{1, 1, 2, 1, 0};
        tShortHash._set = new short[]{1, 2, 3, 4, 0};
        
        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short value) {
                return value != 4;
            }
        };
        
        assertTrue(tShortHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        tShortHash._states = new byte[]{1, 1, 1, 1, 1};
        
        tShortHash.removeAt(2);
        assertArrayEquals(new short[]{1, 2, 0, 4, 5}, tShortHash._set);
        assertArrayEquals(new byte[]{1, 1, 2, 1, 1}, tShortHash._states);
    }
    
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        tShortHash._states = new byte[]{1, 1, 1, 1, 1};
        
        assertEquals(2, tShortHash.index(3));
        assertEquals(-1, tShortHash.index(6));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash._set = new short[]{1, 2, 0, 4, 5};
        tShortHash._states = new byte[]{1, 1, 2, 1, 1};
        
        assertEquals(2, tShortHash.insertKey(3));
        assertEquals(5, tShortHash._set[2]);
        assertEquals(5, tShortHash._states[2]);
        assertFalse(tShortHash.consumeFreeSlot);
        
        assertEquals(2, tShortHash.insertKey(5));
        assertEquals(-5, tShortHash._set[2]);
        assertEquals(1, tShortHash._states[2]);
        
        assertEquals(3, tShortHash.insertKey(6));
        assertEquals(6, tShortHash._set[3]);
        assertEquals(1, tShortHash._states[3]);
    }
}