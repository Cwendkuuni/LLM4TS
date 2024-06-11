package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {
    private TCharHash hash;
    
    @Before
    public void setUp() {
        hash = new TCharHash();
    }
    
    @Test
    public void testContains() {
        assertFalse(hash.contains('a'));
        assertFalse(hash.contains('b'));
        assertFalse(hash.contains('c'));
        
        hash.insertKey('a');
        assertTrue(hash.contains('a'));
        assertFalse(hash.contains('b'));
        assertFalse(hash.contains('c'));
        
        hash.insertKey('b');
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertFalse(hash.contains('c'));
        
        hash.insertKey('c');
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertTrue(hash.contains('c'));
    }
    
    @Test
    public void testForEach() {
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        StringBuilder sb = new StringBuilder();
        hash.forEach(val -> {
            sb.append(val);
            return true;
        });
        
        assertEquals("abc", sb.toString());
    }
    
    @Test
    public void testRemoveAt() {
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        hash.removeAt(0);
        assertFalse(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertTrue(hash.contains('c'));
        
        hash.removeAt(1);
        assertFalse(hash.contains('a'));
        assertFalse(hash.contains('b'));
        assertTrue(hash.contains('c'));
        
        hash.removeAt(2);
        assertFalse(hash.contains('a'));
        assertFalse(hash.contains('b'));
        assertFalse(hash.contains('c'));
    }
    
    @Test
    public void testIndex() {
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        assertEquals(0, hash.index('a'));
        assertEquals(1, hash.index('b'));
        assertEquals(2, hash.index('c'));
    }
    
    @Test
    public void testInsertKey() {
        int index = hash.insertKey('a');
        assertEquals(0, index);
        
        index = hash.insertKey('b');
        assertEquals(1, index);
        
        index = hash.insertKey('c');
        assertEquals(2, index);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }
}