
package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(0.0f));
        
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        TFloatProcedureMock procedure = new TFloatProcedureMock();
        assertTrue(hash.forEach(procedure));
        assertTrue(procedure.executed);
        assertEquals(2.0f, procedure.value, 0.0);
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        hash.removeAt(1);
        assertFalse(hash.contains(2.0f));
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        assertEquals(1, hash.index(2.0f));
        
        // Test edge case: negative index
        assertEquals(-1, hash.index(0.0f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
        
        // Test edge case: insert key that already exists
        assertEquals(-2, hash.insertKey(2.0f));
        
        // Test edge case: insert key to a full set
        for (int i = 3; i <= hash._set.length; i++) {
            hash.insertKey(i);
        }
        try {
            hash.insertKey(hash._set.length + 1);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
        }
    }
    
    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(1, hash.XinsertKey(2.0f));
        
        // Test edge case: insert key that already exists
        assertEquals(-2, hash.XinsertKey(2.0f));
        
        // Test edge case: insert key to a full set
        for (int i = 3; i <= hash._set.length; i++) {
            hash.XinsertKey(i);
        }
        try {
            hash.XinsertKey(hash._set.length + 1);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
        }
    }
    
    class TFloatProcedureMock implements TFloatProcedure {
        boolean executed = false;
        float value = 0.0f;
        
        public boolean execute(float value) {
            this.executed = true;
            this.value = value;
            return true;
        }
    }
}
