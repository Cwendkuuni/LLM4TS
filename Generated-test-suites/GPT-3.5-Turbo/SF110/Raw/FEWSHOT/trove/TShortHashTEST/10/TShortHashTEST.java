package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;
//import any additional packages needed

public class TShortHashTest {
    
    @Test
    public void testConstructorNoArgs() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(0, hash._set.length);
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        TShortHash hash = new TShortHash(10);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(10, hash._set.length);
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TShortHash hash = new TShortHash(10, 0.5f);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(10, hash._set.length);
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        short noEntryValue = 1;
        TShortHash hash = new TShortHash(10, 0.5f, noEntryValue);
        assertEquals(noEntryValue, hash.getNoEntryValue());
        assertEquals(10, hash._set.length);
    }
    
    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash();
        int capacity = 10;
        int result = hash.setUp(capacity);
        assertEquals(capacity, result);
        assertEquals(capacity, hash._set.length);
    }
    
    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        short val = 1;
        hash._set = new short[]{val};
        assertTrue(hash.contains(val));
        
        hash._set = new short[]{2};
        assertFalse(hash.contains(val));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        short val = 1;
        hash._set = new short[]{val};
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        
        procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return false;
            }
        };
        assertFalse(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        short val = 1;
        int index = 0;
        hash._set = new short[]{val};
        hash._states = new byte[]{1};
        
        hash.removeAt(index);
        
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash._set[index]);
        assertEquals(0, hash._states[index]);
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        short val = 1;
        int index = 0;
        int hashValue = 2;
        byte state = 1;
        hash._set = new short[]{val};
        hash._states = new byte[]{state};
        
        int result = hash.index(val);
        
        assertEquals(index, result);
        
        result = hash.indexRehashed(val, index, hashValue, state);
        
        assertEquals(index, result);
        
        result = hash.index(val + 1);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        short val = 1;
        int index = 0;
        int hashValue = 2;
        byte state = 1;
        hash._set = new short[]{val};
        hash._states = new byte[]{state};
        hash.consumeFreeSlot = false;
        
        int result = hash.insertKey(val);
        
        assertEquals(index, result);
        assertTrue(hash.consumeFreeSlot);
        
        result = hash.insertKeyRehash(val, index, hashValue, state);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash();
        short val = 1;
        int index = 0;
        hash._set = new short[]{0};
        hash._states = new byte[]{0};
        
        hash.insertKeyAt(index, val);
        
        assertEquals(val, hash._set[index]);
        assertEquals(1, hash._states[index]);
    }
}