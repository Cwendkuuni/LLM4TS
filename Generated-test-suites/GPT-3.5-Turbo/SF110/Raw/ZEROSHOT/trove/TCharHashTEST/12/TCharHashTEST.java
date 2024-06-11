import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        assertFalse(hash.contains('A'));
        
        hash.insertKey('A');
        assertTrue(hash.contains('A'));
        
        hash.insertKey('B');
        assertTrue(hash.contains('B'));
        
        hash.insertKey('C');
        assertTrue(hash.contains('C'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        StringBuilder result = new StringBuilder();
        
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                result.append(value);
                return true;
            }
        };
        
        hash.forEach(procedure);
        
        assertEquals("ABC", result.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        assertTrue(hash.contains('A'));
        hash.removeAt(0);
        assertFalse(hash.contains('A'));
        
        assertTrue(hash.contains('B'));
        hash.removeAt(1);
        assertFalse(hash.contains('B'));
        
        assertTrue(hash.contains('C'));
        hash.removeAt(0);
        assertFalse(hash.contains('C'));
    }
    
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        assertEquals(0, hash.index('A'));
        assertEquals(1, hash.index('B'));
        assertEquals(2, hash.index('C'));
        assertEquals(-1, hash.index('D'));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        
        assertEquals(0, hash.insertKey('A'));
        assertEquals(1, hash.insertKey('B'));
        assertEquals(2, hash.insertKey('C'));
        assertEquals(-1, hash.insertKey('A'));
    }
}