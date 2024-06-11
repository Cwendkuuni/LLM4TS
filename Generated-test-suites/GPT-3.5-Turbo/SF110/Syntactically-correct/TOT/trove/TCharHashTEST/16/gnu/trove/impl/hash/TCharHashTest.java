import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.impl.hash.TCharHash;

public class TCharHashTest {
    
    private TCharHash tCharHash;
    
    @Before
    public void setUp() {
        tCharHash = new TCharHash();
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        assertEquals(10, tCharHash.setUp(10));
        assertEquals(0, tCharHash.setUp(0));
        assertEquals(1000000, tCharHash.setUp(1000000));
    }
    
    @Test
    public void testContains() {
        assertFalse(tCharHash.contains('a'));
        tCharHash.insertKey('a');
        assertTrue(tCharHash.contains('a'));
    }
    
    @Test
    public void testForEach() {
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                if (value == 'a' || value == 'b') {
                    return true;
                }
                return false;
            }
        };
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        assertTrue(tCharHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('a'));
        assertTrue(tCharHash.contains('b'));
    }
    
    @Test
    public void testIndex() {
        assertEquals(-1, tCharHash.index('a'));
        tCharHash.insertKey('a');
        assertEquals(0, tCharHash.index('a'));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, tCharHash.insertKey('a'));
        assertEquals(-1, tCharHash.insertKey('a'));
        tCharHash.insertKey('b');
        assertEquals(1, tCharHash.insertKey('a'));
    }
    
}