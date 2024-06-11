package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {

    private TCharHash tCharHash;
    
    @Before
    public void setUp() {
        tCharHash = new TCharHash(10);
    }
    
    @Test
    public void testContains() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        
        assertTrue(tCharHash.contains('a'));
        assertTrue(tCharHash.contains('b'));
        assertTrue(tCharHash.contains('c'));
        assertFalse(tCharHash.contains('d'));
    }
    
    @Test
    public void testForEach() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');

        StringBuilder sb = new StringBuilder();
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                sb.append(value);
                return true;
            }
        };

        tCharHash.forEach(procedure);
        
        assertEquals("ab", sb.toString());
    }
    
    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('a'));
        assertTrue(tCharHash.contains('b'));
        assertTrue(tCharHash.contains('c'));
        
        tCharHash.removeAt(1);
        assertFalse(tCharHash.contains('b'));
        assertTrue(tCharHash.contains('c'));
        
        tCharHash.removeAt(1);
        assertFalse(tCharHash.contains('c'));
    }
    
    @Test
    public void testIndex() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        
        assertEquals(0, tCharHash.index('a'));
        assertEquals(1, tCharHash.index('b'));
        assertEquals(2, tCharHash.index('c'));
        assertEquals(-1, tCharHash.index('d'));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, tCharHash.insertKey('a'));
        assertEquals(1, tCharHash.insertKey('b'));
        assertEquals(2, tCharHash.insertKey('c'));
        assertEquals(-3, tCharHash.insertKey('a'));
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }
    
}