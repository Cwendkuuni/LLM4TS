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
        assertFalse(tCharHash.contains('a'));
        
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
        assertFalse(tCharHash.forEach(new TCharProcedure() {
            
            @Override
            public boolean execute(char value) {
                return true;
            }
        }));
        
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        
        assertTrue(tCharHash.forEach(new TCharProcedure() {
            
            @Override
            public boolean execute(char value) {
                return true;
            }
        }));
    }
    
    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        
        assertTrue(tCharHash.contains('a'));
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('a'));
        
        assertTrue(tCharHash.contains('b'));
        tCharHash.removeAt(1);
        assertFalse(tCharHash.contains('b'));
        
        assertTrue(tCharHash.contains('c'));
        tCharHash.removeAt(2);
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
        assertEquals(-1, tCharHash.insertKey('a'));
    }

    // Additional test cases can be added here to further test the TCharHash class
    
    // ...

}