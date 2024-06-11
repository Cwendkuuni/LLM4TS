import static org.junit.Assert.*;
import org.junit.*;

public class TCharHashTest {
    
    @Test
    public void testConstructor1() {
        TCharHash tCharHash = new TCharHash();
        assertNotNull(tCharHash);
    }
    
    @Test
    public void testConstructor2() {
        TCharHash tCharHash = new TCharHash(10);
        assertNotNull(tCharHash);
    }
    
    @Test
    public void testConstructor3() {
        TCharHash tCharHash = new TCharHash(10, 0.75f);
        assertNotNull(tCharHash);
    }
    
    @Test
    public void testConstructor4() {
        TCharHash tCharHash = new TCharHash(10, 0.75f, '0');
        assertNotNull(tCharHash);
    }
    
    @Test
    public void testGetNoEntryValue() {
        TCharHash tCharHash = new TCharHash();
        char noEntryValue = tCharHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
    }
    
    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        boolean contains = tCharHash.contains('A');
        assertTrue(contains);
    }
    
    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };
        boolean result = tCharHash.forEach(procedure);
        assertTrue(result);
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('A'));
    }
    
    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        int index = tCharHash.index('A');
        assertEquals(0, index);
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        int index = tCharHash.indexRehashed('A', 0, 0, (byte)1);
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        int index = tCharHash.insertKey('A');
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash();
        int index = tCharHash.insertKeyRehash('B', 0, 0, (byte)1);
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKeyAt(0, 'A');
        assertTrue(tCharHash.contains('A'));
    }
}