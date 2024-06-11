package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        assertFalse(hash.contains('a'));
        
        hash.insertKey('a');
        assertTrue(hash.contains('a'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        
        StringBuilder sb = new StringBuilder();
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                sb.append(value);
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals("ab", sb.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        
        hash.removeAt(0);
        assertFalse(hash.contains('a'));
    }
    
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        assertEquals(-1, hash.index('a'));
        
        hash.insertKey('a');
        assertEquals(0, hash.index('a'));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        assertEquals(0, hash.insertKey('a'));
        
        assertEquals(-1, hash.insertKey('a'));
    }
}