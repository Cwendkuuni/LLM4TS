import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {
    
    @Test
    public void testConstructor1() {
        TCharHash tCharHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor2() {
        TCharHash tCharHash = new TCharHash(10);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor3() {
        TCharHash tCharHash = new TCharHash(10, 0.5f);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor4() {
        char customValue = 'A';
        TCharHash tCharHash = new TCharHash(10, 0.5f, customValue);
        assertEquals(customValue, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TCharHash tCharHash = new TCharHash();
        int capacity = 10;
        tCharHash.setUp(capacity);
        assertEquals(capacity, tCharHash._set.length);
    }
    
    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        char value = 'A';
        assertFalse(tCharHash.contains(value));
        tCharHash.insertKey(value);
        assertTrue(tCharHash.contains(value));
    }
    
    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        char value = 'A';
        tCharHash.insertKey(value);
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return value != 'B';
            }
        };
        assertFalse(tCharHash.forEach(procedure));
    }
    
    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        char value = 'A';
        assertEquals(-1, tCharHash.index(value));
        tCharHash.insertKey(value);
        assertEquals(0, tCharHash.index(value));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        char value = 'A';
        tCharHash.insertKey(value);
        assertTrue(tCharHash.contains(value));
    }
}