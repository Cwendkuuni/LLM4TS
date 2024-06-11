package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {
    
    @Test
    public void testConstructor() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        
        hash = new TShortHash(10);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        
        hash = new TShortHash(10, 0.5f);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        
        hash = new TShortHash(10, 0.5f, (short)123);
        assertEquals(123, hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        assertEquals(10, hash._set.length);
        
        hash = new TShortHash(10);
        hash.setUp(20);
        assertEquals(20, hash._set.length);
    }
    
    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        hash._set[0] = 123;
        
        assertTrue(hash.contains((short)123));
        assertFalse(hash.contains((short)456));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        hash._set[0] = 123;
        
        assertTrue(hash.forEach(new TShortProcedure() {
            public boolean execute(short value) {
                assertEquals(123, value);
                return true;
            }
        }));
        
        assertFalse(hash.forEach(new TShortProcedure() {
            public boolean execute(short value) {
                return false;
            }
        }));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        hash._set[0] = 123;
        
        hash.removeAt(0);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash._set[0]);
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        hash._set[0] = 123;
        
        int index = hash.index((short)123);
        assertEquals(0, index);
        
        index = hash.index((short)456);
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        hash._set[0] = 123;
        
        int index = hash.indexRehashed((short)123, 0, 1, (byte)1);
        assertEquals(0, index);
        
        index = hash.indexRehashed((short)456, 0, 1, (byte)1);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        
        int index = hash.insertKey((short)123);
        assertEquals(0, index);
        
        index = hash.insertKey((short)123);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        hash._set[0] = 123;
        
        int index = hash.insertKeyRehash((short)123, 0, 1, (byte)1);
        assertEquals(0, index);
        
        index = hash.insertKeyRehash((short)456, 0, 1, (byte)1);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash();
        hash.setUp(10);
        
        hash.insertKeyAt(0, (short)123);
        assertEquals(123, hash._set[0]);
    }
}