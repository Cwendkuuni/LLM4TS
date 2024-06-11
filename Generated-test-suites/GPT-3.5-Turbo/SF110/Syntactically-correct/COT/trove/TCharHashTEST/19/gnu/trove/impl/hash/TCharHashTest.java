import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    // Basic test for contains() method
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        
        assertTrue(hash.contains('a'));
        assertFalse(hash.contains('b'));
    }
    
    // Test for forEach() method with a procedure that returns true
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
    }
    
    // Test for forEach() method with a procedure that returns false
    @Test
    public void testForEachWithFalseProcedure() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                return false;
            }
        };
        
        assertFalse(hash.forEach(procedure));
    }
    
    // Test for removeAt() method
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        
        hash.removeAt(0);
        
        assertFalse(hash.contains('a'));
        assertTrue(hash.contains('b'));
    }
    
    // Test for index() method with an existing value
    @Test
    public void testIndexExistingValue() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        
        int index = hash.index('a');
        assertEquals(0, index);
    }
    
    // Test for index() method with a non-existing value
    @Test
    public void testIndexNonExistingValue() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        
        int index = hash.index('b');
        assertEquals(-1, index);
    }
    
    // Test for insertKey() method with an existing value
    @Test
    public void testInsertKeyExistingValue() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        
        int index = hash.insertKey('a');
        assertEquals(-1, index);
    }
    
    // Test for insertKey() method with a non-existing value
    @Test
    public void testInsertKeyNonExistingValue() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        
        int index = hash.insertKey('b');
        assertTrue(index >= 0);
    }
    
    // Test for insertKey() method with a full key set
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFullSet() {
        TCharHash hash = new TCharHash(1, 1);
        hash.insertKey('a');
        hash.insertKey('b');
    }
}