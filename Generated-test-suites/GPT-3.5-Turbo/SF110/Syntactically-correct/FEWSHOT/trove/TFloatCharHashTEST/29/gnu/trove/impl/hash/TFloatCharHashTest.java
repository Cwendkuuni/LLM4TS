package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {
    private TFloatCharHash hash;

    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        assertFalse(hash.contains(1.0f));
        
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }
    
    @Test
    public void testForEach() {
        assertFalse(hash.forEach((val) -> {
            // do nothing
            return true;
        }));
        
        hash.insertKey(1.0f);
        assertTrue(hash.forEach((val) -> {
            return false;
        }));
    }
    
    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.removeAt(0);
        
        assertFalse(hash.contains(1.0f));
    }
    
    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(1.0f));
        
        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
    }
    
    @Test
    public void testInsertKey() {
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));
        
        index = hash.insertKey(1.0f);
        assertEquals(-1, index);
    }
    
    @Test
    public void testXInsertKey() {
        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));
        
        index = hash.XinsertKey(1.0f);
        assertEquals(-1, index);
    }
    
    @Test
    public void testWriteExternal() {
        // not testable
    }
    
    @Test
    public void testReadExternal() {
        // not testable
    }
}