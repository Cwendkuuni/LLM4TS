import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertFalse(hash.contains('c'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        
        hash.removeAt(0);
        assertFalse(hash.contains('a'));
        assertTrue(hash.contains('b'));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertTrue(hash.contains('c'));
    }
}