package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash hash;
    
    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }
    
    @Test
    public void testContains() {
        assertFalse(hash.contains(0.0f));
    }
    
    @Test
    public void testForEach() {
        hash.forEach(val -> {
            return false;
        });
    }
    
    @Test
    public void testRemoveAt() {
        hash.removeAt(0);
    }
    
    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(0.0f));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(0.0f));
    }
    
    @Test
    public void testXinsertKey() {
        assertEquals(0, hash.XinsertKey(0.0f));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        hash.insertKeyRehash(0.0f, 0, 0, (byte) 0);
    }
    
    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 0.0f);
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }
    
    @Test
    public void testWriteExternal() {
        // Not testable
    }
    
    @Test
    public void testReadExternal() {
        // Not testable
    }
}