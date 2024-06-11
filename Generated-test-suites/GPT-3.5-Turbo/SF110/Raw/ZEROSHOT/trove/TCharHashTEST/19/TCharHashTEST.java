import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    @Test
    public void testGetNoEntryValue() {
        TCharHash tCharHash = new TCharHash();
        char noEntryValue = tCharHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
    }
    
    @Test
    public void testSetUp() {
        TCharHash tCharHash = new TCharHash();
        int capacity = tCharHash.setUp(10);
        assertEquals(10, capacity);
        char[] set = tCharHash._set;
        assertEquals(10, set.length);
    }
    
    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash(5);
        tCharHash.insertKey('a');
        boolean contains = tCharHash.contains('a');
        assertTrue(contains);
    }
    
    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash(5);
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };
        boolean success = tCharHash.forEach(procedure);
        assertTrue(success);
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash(5);
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('a'));
    }
    
    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash(5);
        tCharHash.insertKey('a');
        int index = tCharHash.index('a');
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash(5);
        int index = tCharHash.insertKey('a');
        assertEquals(0, index);
    }
    
}