import static org.junit.Assert.*;
import org.junit.Test;

// Class under test
public class TCharHashTest {
    
    // Test contains() method
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash(5);
        
        // Adding elements to hash
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        // Assert that contains() returns true for existing elements
        assertTrue(hash.contains('A'));
        assertTrue(hash.contains('B'));
        assertTrue(hash.contains('C'));
        
        // Assert that contains() returns false for non-existing elements
        assertFalse(hash.contains('D'));
        assertFalse(hash.contains('E'));
    }
    
    // Test forEach() method
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash(5);
        
        // Adding elements to hash
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        // Test procedure implementation that returns true for all elements
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char val) {
                return true;
            }
        };
        
        // Assert that forEach() returns true for all elements
        assertTrue(hash.forEach(procedure));
    }
    
    // Test removeAt() method
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash(5);
        
        // Adding elements to hash
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        // Remove element at index 1 (element 'B')
        hash.removeAt(1);
        
        // Assert that contains() returns false for removed element
        assertFalse(hash.contains('B'));
    }
    
    // Test index() method
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash(5);
        
        // Adding elements to hash
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        
        // Assert that index() returns correct index for existing elements
        assertEquals(0, hash.index('A'));
        assertEquals(1, hash.index('B'));
        assertEquals(2, hash.index('C'));
        
        // Assert that index() returns -1 for non-existing elements
        assertEquals(-1, hash.index('D'));
        assertEquals(-1, hash.index('E'));
    }
    
    // Test insertKey() method
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash(5);
        
        // Inserting elements
        int indexA = hash.insertKey('A');
        int indexB = hash.insertKey('B');
        int indexC = hash.insertKey('C');
        
        // Assert that insertKey() returns correct index for inserted elements
        assertEquals(0, indexA);
        assertEquals(1, indexB);
        assertEquals(2, indexC);
        
        // Assert that contains() returns true for inserted elements
        assertTrue(hash.contains('A'));
        assertTrue(hash.contains('B'));
        assertTrue(hash.contains('C'));
    }
}