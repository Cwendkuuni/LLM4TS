package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TShortHashTest {
    @Test
    public void testConstructor() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertNull(hash._set);
        
        hash = new TShortHash(10);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertNotNull(hash._set);
        assertEquals(10, hash._set.length);
        
        hash = new TShortHash(10, 0.5f);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertNotNull(hash._set);
        assertEquals(10, hash._set.length);
        
        hash = new TShortHash(10, 0.5f, (short) 0);
        assertEquals(0, hash.getNoEntryValue());
        assertNotNull(hash._set);
        assertEquals(10, hash._set.length);
    }
    
    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        assertNotNull(hash._set);
        assertEquals(10, hash._set.length);
    }
    
    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash._set = new short[]{2, 4, 6};
        assertTrue(hash.contains((short) 2));
        assertFalse(hash.contains((short) 1));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash._set = new short[]{2, 4, 6};
        
        TShortProcedure procedure = (short value) -> {
            System.out.println(value);
            return true;
        };
        
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash._set = new short[]{2, 4, 6};
        
        hash.removeAt(1);
        assertEquals((short) 0, hash._set[1]);
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash._set = new short[]{2, 4, 6};
        hash._states = new byte[]{0, 1, 0};
        
        assertEquals(-1, hash.index((short) 1));
        assertEquals(1, hash.index((short) 4));
    }
    
    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();
        hash._set = new short[]{2, 4, 6};
        hash._states = new byte[]{0, 1, 0};
        
        assertEquals(-1, hash.indexRehashed((short) 1, 0, 0, (byte) 0));
        assertEquals(1, hash.indexRehashed((short) 4, 1, 10, (byte) 1));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        hash._set = new short[]{2, 4, 6};
        hash._states = new byte[]{0, 1, 0};
        
        assertEquals(0, hash.insertKey((short) 2));
        assertEquals(-2, hash.insertKey((short) 1));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        hash._set = new short[]{2, 4, 6};
        hash._states = new byte[]{0, 1, 0};
        
        assertEquals(-2, hash.insertKeyRehash((short) 1, 0, 2, (byte) 0));
        assertEquals(1, hash.insertKeyRehash((short) 4, 1, 10, (byte) 1));
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash();
        hash._set = new short[]{2, 4, 6};
        hash._states = new byte[]{0, 1, 0};
        
        hash.insertKeyAt(1, (short) 3);
        assertEquals((short) 3, hash._set[1]);
        assertEquals((byte) 1, hash._states[1]);
    }
}