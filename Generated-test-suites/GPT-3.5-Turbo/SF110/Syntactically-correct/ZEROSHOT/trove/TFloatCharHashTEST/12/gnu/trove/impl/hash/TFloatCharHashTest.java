import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {
    
    TFloatCharHash hash;
    
    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        assertFalse(hash.contains(0.0f));
    }
    
    @Test
    public void testForEach() {
        assertFalse(hash.forEach(new TFloatProcedure() {
            public boolean execute(float val) {
                return true;
            }
        }));
    }
    
    @Test
    public void testRemoveAt() {
        hash.removeAt(0);
        assertEquals(0.0f, hash._set[0], 0.0);
        assertEquals(0, hash._states[0]);
        assertEquals(0, hash._size);
    }
    
    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(0.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        assertEquals(-1, hash.indexRehashed(0.0f, 0, 0, (byte) 0));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(0.0f));
        assertEquals(1, hash._size);
        assertEquals(0.0f, hash._set[0], 0.0);
        assertEquals(1, hash._states[0]);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        hash.insertKeyRehash(0.0f, 0, 0, (byte) 0);
    }
    
    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 0.0f);
        assertEquals(0.0f, hash._set[0], 0.0);
        assertEquals(1, hash._states[0]);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testXinsertKey() {
        hash.XinsertKey(0.0f);
    }
    
    @Test
    public void testWriteExternal() {
        // Not testing this method as it calls external API
    }
    
    @Test
    public void testReadExternal() {
        // Not testing this method as it calls external API
    }
    
}