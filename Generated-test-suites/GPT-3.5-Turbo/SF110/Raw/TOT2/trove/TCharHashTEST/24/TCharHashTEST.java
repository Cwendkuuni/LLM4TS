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
        assertEquals(10, tCharHash.size());
    }
    
    @Test
    public void testConstructor3() {
        TCharHash tCharHash = new TCharHash(10, 0.5f);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
        assertEquals(10, tCharHash.size());
    }
    
    @Test
    public void testConstructor4() {
        TCharHash tCharHash = new TCharHash(10, 0.5f, 'a');
        assertEquals('a', tCharHash.getNoEntryValue());
        assertEquals(10, tCharHash.size());
    }
    
    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        assertTrue(tCharHash.contains('a'));
        assertFalse(tCharHash.contains('b'));
    }
    
    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        tCharHash.insertKey('d');
        
        TCharProcedure procedure = value -> {
            if(value == 'c' || value == 'd') {
                return false;
            }
            return true;
        };
        
        assertFalse(tCharHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('a'));
        assertTrue(tCharHash.contains('b'));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        assertEquals(0, tCharHash.insertKey('a'));
        assertEquals(1, tCharHash.insertKey('b'));
        assertEquals(2, tCharHash.insertKey('c'));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash(10, 0.5f, 'a');
        for(int i = 0; i < 10; i++) {
            tCharHash.insertKey((char)(i + 'a'));
        }
        assertEquals(10, tCharHash.size());
        assertEquals(-1, tCharHash.insertKeyRehash('z', 0, 0, (byte)1));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashFull() {
        TCharHash tCharHash = new TCharHash(1, 0.5f, 'a');
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
    }
}