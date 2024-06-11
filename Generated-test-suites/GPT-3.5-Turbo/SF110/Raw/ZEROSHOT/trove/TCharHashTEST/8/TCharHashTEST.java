import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {
    
    private TCharHash tCharHash;
    
    @Before
    public void setUp() {
        tCharHash = new TCharHash(10);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        assertFalse(tCharHash.contains('A'));
        tCharHash.insertKey('A');
        assertTrue(tCharHash.contains('A'));
    }
    
    @Test
    public void testForEach() {
        TCharProcedure procedure = new TCharProcedure() {
            StringBuilder sb = new StringBuilder();
            
            public boolean execute(char val) {
                sb.append(val);
                return true;
            }
            
            public String toString() {
                return sb.toString();
            }
        };
        
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.forEach(procedure);
        
        assertEquals("AB", procedure.toString());
    }
    
    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.removeAt(0);
        
        assertFalse(tCharHash.contains('A'));
        assertTrue(tCharHash.contains('B'));
    }
    
    @Test
    public void testIndex() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        assertEquals(0, tCharHash.index('A'));
        assertEquals(1, tCharHash.index('B'));
        assertEquals(-1, tCharHash.index('C'));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, tCharHash.insertKey('A'));
        assertEquals(1, tCharHash.insertKey('B'));
        assertEquals(-1, tCharHash.insertKey('A'));
    }
    
    @Test
    public void testIndexRehashed() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        assertEquals(0, tCharHash.indexRehashed('A', 0, 1, (byte) 1));
        assertEquals(1, tCharHash.indexRehashed('B', 1, 2, (byte) 1));
        assertEquals(-1, tCharHash.indexRehashed('C', 2, 3, (byte) 0));
    }
    
    @Test
    public void testInsertKeyRehash() {
        assertEquals(0, tCharHash.insertKeyRehash('A', 0, 1, (byte) 1));
        assertEquals(1, tCharHash.insertKeyRehash('B', 1, 2, (byte) 1));
        assertEquals(-1, tCharHash.insertKeyRehash('A', 2, 3, (byte) 0));
    }
    
    @Test
    public void testInsertKeyAt() {
        tCharHash.insertKeyAt(0, 'A');
        assertEquals('A', tCharHash._set[0]);
        assertEquals(1, tCharHash._states[0]);
    }
}