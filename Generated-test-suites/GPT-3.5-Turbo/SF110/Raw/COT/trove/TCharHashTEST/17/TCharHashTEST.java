import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        
        // Test if hash contains a value
        hash.insertKey('a');
        assertTrue(hash.contains('a'));
        
        // Test if hash does not contain a value
        assertFalse(hash.contains('b'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        
        // Test if forEach method executes for each element
        hash.insertKey('a');
        hash.insertKey('b');
        
        StringBuilder result = new StringBuilder();
        hash.forEach((val) -> {
            result.append(val);
            return true;
        });
        
        assertEquals("ab", result.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        
        // Test if removeAt method removes an element at the specified index
        hash.insertKey('a');
        hash.removeAt(0);
        assertFalse(hash.contains('a'));
    }
    
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        
        // Test if index method returns the correct index for a value
        hash.insertKey('a');
        assertEquals(0, hash.index('a'));
        
        // Test if index method returns -1 for a value not present in the hash
        assertEquals(-1, hash.index('b'));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        
        // Test if insertKey method inserts a new key and returns the index
        int index = hash.insertKey('a');
        assertTrue(hash.contains('a'));
        assertEquals(0, index);
        
        // Test if insertKey method returns the negative index if key already exists
        index = hash.insertKey('a');
        assertEquals(-1, index);
    }
}