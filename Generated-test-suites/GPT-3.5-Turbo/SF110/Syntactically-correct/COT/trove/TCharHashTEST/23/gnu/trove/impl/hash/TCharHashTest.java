
import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        assertFalse(hash.contains('A'));
        
        hash.insertKey('A');
        assertTrue(hash.contains('A'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        
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
        hash.insertKey('A');
        hash.insertKey('B');
        
        hash.removeAt(0);
        assertFalse(hash.contains('A'));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        assertTrue(hash.contains('A'));
        
        assertEquals(1, hash.insertKey('B'));
        assertFalse(hash.insertKey('B'));
    }
}
