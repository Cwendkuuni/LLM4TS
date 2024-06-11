package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {
    
    @Test
    public void testContains() {
        TCharHash obj = new TCharHash();
        obj.insertKey('a');
        obj.insertKey('b');
        obj.insertKey('c');
        
        assertTrue(obj.contains('a'));
        assertTrue(obj.contains('b'));
        assertTrue(obj.contains('c'));
        assertFalse(obj.contains('d'));
    }
    
    @Test
    public void testForEach() {
        TCharHash obj = new TCharHash();
        obj.insertKey('a');
        obj.insertKey('b');
        obj.insertKey('c');
        
        TCharProcedure proc = new TCharProcedure() {
            StringBuilder sb = new StringBuilder();
            
            public boolean execute(char value) {
                sb.append(value);
                return true;
            }
            
            public String getValues() {
                return sb.toString();
            }
        };
        
        assertTrue(obj.forEach(proc));
        assertEquals("abc", proc.getValues());
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash obj = new TCharHash();
        obj.insertKey('a');
        obj.insertKey('b');
        
        obj.removeAt(0);
        assertFalse(obj.contains('a'));
        assertTrue(obj.contains('b'));
    }
    
    @Test
    public void testIndex() {
        TCharHash obj = new TCharHash();
        obj.insertKey('a');
        obj.insertKey('b');
        obj.insertKey('c');
        
        assertEquals(0, obj.index('a'));
        assertEquals(1, obj.index('b'));
        assertEquals(2, obj.index('c'));
        assertEquals(-1, obj.index('d'));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash obj = new TCharHash();
        
        assertEquals(0, obj.insertKey('a'));
        assertEquals(1, obj.insertKey('b'));
        assertEquals(2, obj.insertKey('c'));
    }
}