
package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {
    
    private TCharHash tCharHash;
    
    @Before
    public void setUp() {
        tCharHash = new TCharHash();
    }
    
    @Test
    public void testContains() {
        assertFalse(tCharHash.contains('A'));
        tCharHash.insertKey('A');
        assertTrue(tCharHash.contains('A'));
    }
    
    @Test
    public void testContains_duplicate() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('A');
        assertTrue(tCharHash.contains('A'));
    }
    
    @Test
    public void testContains_notAdded() {
        assertFalse(tCharHash.contains('A'));
        assertFalse(tCharHash.contains('B'));
    }
    
    @Test
    public void testForEach() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        
        StringBuilder sb = new StringBuilder();
        tCharHash.forEach(val -> {
            sb.append(val);
            return true;
        });
        
        assertEquals("ABC", sb.toString());
    }
    
    @Test
    public void testForEach_empty() {
        StringBuilder sb = new StringBuilder();
        tCharHash.forEach(val -> {
            sb.append(val);
            return true;
        });
        
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        
        tCharHash.removeAt(1);
        
        assertFalse(tCharHash.contains('B'));
        
        StringBuilder sb = new StringBuilder();
        tCharHash.forEach(val -> {
            sb.append(val);
            return true;
        });
        
        assertEquals("AC", sb.toString());
    }
    
    @Test
    public void testRemoveAt_notAdded() {
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('A'));
        
        StringBuilder sb = new StringBuilder();
        tCharHash.forEach(val -> {
            sb.append(val);
            return true;
        });
        
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testIndex() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        
        assertEquals(0, tCharHash.index('A'));
        assertEquals(1, tCharHash.index('B'));
        assertEquals(2, tCharHash.index('C'));
    }
    
    @Test
    public void testIndex_notAdded() {
        assertEquals(-1, tCharHash.index('A'));
        
        tCharHash.insertKey('B');
        assertEquals(-1, tCharHash.index('A'));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, tCharHash.insertKey('A'));
        assertEquals(1, tCharHash.insertKey('B'));
        assertEquals(2, tCharHash.insertKey('C'));
    }
    
    @Test
    public void testInsertKey_duplicate() {
        tCharHash.insertKey('A');
        assertEquals(-1, tCharHash.insertKey('A'));
    }
    
    @Test
    public void testInsertKey_full() {
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        tCharHash.insertKey('D');
        tCharHash.insertKey('E');
        
        try {
            tCharHash.insertKey('F');
            fail("Expected IllegalStateException was not thrown");
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
        }
    }
    
}
