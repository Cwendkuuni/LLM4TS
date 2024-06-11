package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatDoubleHashTest {
    
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.2f);
        hash.insertKey(2.3f);
        hash.insertKey(3.4f);
        
        assertTrue(hash.contains(1.2f));
        assertTrue(hash.contains(2.3f));
        assertTrue(hash.contains(3.4f));
        
        assertFalse(hash.contains(4.5f));
        assertFalse(hash.contains(0.1f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.2f);
        hash.insertKey(2.3f);
        hash.insertKey(3.4f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            private boolean executed = false;
            
            public boolean execute(float value) {
                assertTrue(value == 1.2f || value == 2.3f || value == 3.4f);
                executed = true;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertTrue(procedure.executed);
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.2f);
        hash.insertKey(2.3f);
        hash.insertKey(3.4f);
        
        assertEquals(0, hash.index(1.2f));
        assertEquals(1, hash.index(2.3f));
        assertEquals(2, hash.index(3.4f));
        
        assertEquals(-1, hash.index(4.5f));
        assertEquals(-1, hash.index(0.1f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.2f);
        hash.insertKey(2.3f);
        
        assertEquals(0, hash.index(1.2f));
        assertEquals(1, hash.index(2.3f));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.2f);
        
        assertEquals(0, hash.index(1.2f));
        
        hash.removeAt(0);
        
        assertEquals(-1, hash.index(1.2f));
    }
    
}