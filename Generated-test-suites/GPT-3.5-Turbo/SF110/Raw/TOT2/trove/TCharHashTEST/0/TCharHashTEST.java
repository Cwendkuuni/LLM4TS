import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    @Test
    public void testConstructor1() {
        TCharHash tCharHash = new TCharHash();
        assertNotNull(tCharHash);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor2() {
        TCharHash tCharHash = new TCharHash(10);
        assertNotNull(tCharHash);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor3() {
        TCharHash tCharHash = new TCharHash(10, 0.5f);
        assertNotNull(tCharHash);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor4() {
        char noEntryValue = 'X';
        TCharHash tCharHash = new TCharHash(10, 0.5f, noEntryValue);
        assertNotNull(tCharHash);
        assertEquals(noEntryValue, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TCharHash tCharHash = new TCharHash();
        int capacity = tCharHash.setUp(10);
        assertEquals(10, capacity);
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
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        
        StringBuffer buffer = new StringBuffer();
        TCharProcedure charProcedure = (val) -> {
            buffer.append(val);
            return true;
        };
        
        assertTrue(tCharHash.forEach(charProcedure));
        assertEquals("AB", buffer.toString());
    }
    
    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        char value = 'A';
        int index = tCharHash.insertKey(value);
        assertEquals(0, index);
        assertEquals(value, tCharHash._set[0]);
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        char value = 'A';
        tCharHash.insertKey(value);
        tCharHash.removeAt(0);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash._set[0]);
    }

    // Add more test cases for other methods as needed

}