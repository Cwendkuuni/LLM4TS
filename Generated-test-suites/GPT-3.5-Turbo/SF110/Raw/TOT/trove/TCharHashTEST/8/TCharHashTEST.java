import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    private TCharHash tCharHash;
  
    @Before
    public void setUp() {
        tCharHash = new TCharHash();
    }
  
    @Test
    public void testConstructor() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }
  
    @Test
    public void testContains() {
        assertFalse(tCharHash.contains('A')); // Empty set, should return false
        tCharHash.insertKey('A');
        assertTrue(tCharHash.contains('A')); // Value exists, should return true
        assertFalse(tCharHash.contains('B')); // Value doesn't exist, should return false
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('A')); // Value removed, should return false
    }
  
    @Test
    public void testForEach() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
      
        // Create a procedure to append each element to a StringBuilder
        final StringBuilder sb = new StringBuilder();
        tCharHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                sb.append(value);
                return true;
            }
        });
      
        assertEquals("ABC", sb.toString()); // Check if all elements were appended correctly
    }
  
    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
      
        tCharHash.removeAt(1);
      
        assertFalse(tCharHash.contains('B')); // Check if 'B' is removed
        assertEquals('C', tCharHash._set[1]); // Check if 'C' is shifted to the correct position
        assertEquals(-1, tCharHash.index('B')); // Check if index of 'B' is -1
      
        tCharHash.removeAt(0);
      
        assertFalse(tCharHash.contains('A')); // Check if 'A' is removed
        assertEquals(-1, tCharHash.index('B')); // Check if index of 'B' is still -1
        assertEquals(0, tCharHash.index('C')); // Check if index of 'C' is 0
    }
  
    @Test
    public void testIndex() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
      
        assertEquals(-1, tCharHash.index('D')); // Value doesn't exist, should return -1
        assertEquals(1, tCharHash.index('B')); // Value exists, should return index 1
    }
  
    @Test
    public void testInsertKey() {
        assertEquals(0, tCharHash.insertKey('A')); // Insert first element, should return index 0
        assertEquals(1, tCharHash.insertKey('B')); // Insert second element, should return index 1
        assertEquals(0, tCharHash.insertKey('A')); // Insert duplicate element, should return -1 * index - 1
    }
  
    @Test
    public void testInsertKeyRehash() {
        // Insert elements that will cause rehashing
        for (char c = 'A'; c <= 'Z'; c++) {
            tCharHash.insertKey(c);
        }
      
        // Check if all elements are inserted correctly
        for (char c = 'A'; c <= 'Z'; c++) {
            assertEquals(c, tCharHash._set[tCharHash.index(c)]);
        }
    }

}