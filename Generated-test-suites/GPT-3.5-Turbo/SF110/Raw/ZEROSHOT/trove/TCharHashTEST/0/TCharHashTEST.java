package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        assertFalse(tCharHash.contains('a'));
        assertFalse(tCharHash.contains('b'));
        assertFalse(tCharHash.contains('c'));
    }
    
    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        
        tCharHash.insertKeyAt(0, 'a');
        tCharHash.insertKeyAt(1, 'b');
        tCharHash.insertKeyAt(2, 'c');
        
        final StringBuilder stringBuilder = new StringBuilder();
        
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                stringBuilder.append(value);
                return true;
            }
        };
        
        assertTrue(tCharHash.forEach(procedure));
        assertEquals("abc", stringBuilder.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        
        tCharHash.insertKeyAt(0, 'a');
        tCharHash.insertKeyAt(1, 'b');
        tCharHash.insertKeyAt(2, 'c');
        
        tCharHash.removeAt(1);
        
        assertFalse(tCharHash.contains('b'));
    }
    
    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        
        tCharHash.insertKeyAt(0, 'a');
        tCharHash.insertKeyAt(1, 'b');
        tCharHash.insertKeyAt(2, 'c');
        
        assertEquals(1, tCharHash.index('b'));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        
        int index1 = tCharHash.insertKey('a');
        int index2 = tCharHash.insertKey('b');
        int index3 = tCharHash.insertKey('c');
        
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(2, index3);
        
        int index4 = tCharHash.insertKey('d');
        assertEquals(3, index4);
    }
}