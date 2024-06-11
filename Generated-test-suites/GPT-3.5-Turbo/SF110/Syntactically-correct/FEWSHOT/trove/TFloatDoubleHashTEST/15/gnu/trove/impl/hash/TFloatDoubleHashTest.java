package gnu.trove.impl.hash;

import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        assertFalse(hash.contains(1.0f));
        
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        
        hash.insertKey(2.0f);
        assertTrue(hash.contains(2.0f));
        
        hash.insertKey(3.0f);
        assertTrue(hash.contains(3.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        // Empty hash
        assertFalse(hash.forEach(val -> {
            fail("Should not execute procedure on empty hash");
            return true;
        }));
        
        // Non-empty hash
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        assertTrue(hash.forEach(val -> {
            assertTrue(val == 1.0f || val == 2.0f);
            return true;
        }));
        
        assertFalse(hash.forEach(val -> {
            fail("Should not execute procedure again");
            return false;
        }));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        hash.removeAt(1);
        assertFalse(hash.contains(2.0f));
        assertEquals(2, hash.size());
        
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        assertEquals(1, hash.size());
        
        hash.removeAt(0);
        assertFalse(hash.contains(3.0f));
        assertEquals(0, hash.size());
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(2, hash.index(3.0f));
        
        assertEquals(-1, hash.index(4.0f));
        assertEquals(-1, hash.index(0.5f));
    }
    
    // Additional test cases for remaining methods
    
    // TODO: insertKey()
    
    // TODO: indexRehashed()
    
    // TODO: XinsertKey()
    
    // TODO: writeExternal()
    
    // TODO: readExternal()
}