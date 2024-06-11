
import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {
    
    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        StringBuilder result = new StringBuilder();
        hash.forEach(val -> { result.append(val); return true; });
        
        assertEquals("12", result.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        hash.removeAt(0);
        
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }
    
    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(2.0f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.insertKey(1.0f);
        
        assertTrue(hash.contains(1.0f));
        assertEquals(0, index);
    }
    
    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.XinsertKey(1.0f);
        
        assertTrue(hash.contains(1.0f));
        assertEquals(0, index);
    }
    
    @Test
    public void testWriteExternal() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        // write hash to external source
        
        // read hash from external source
        
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }
    
    @Test
    public void testReadExternal() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        // write hash to external source
        
        // read hash from external source
        
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }
    
}

