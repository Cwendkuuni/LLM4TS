package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class TCharHashTest {
    
    @Test
    public void testConstructor() {
        TCharHash hash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(10, hash._set.length);
        
        hash = new TCharHash(20);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(20, hash._set.length);
        
        hash = new TCharHash(30, 0.5f);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(30, hash._set.length);
        
        hash = new TCharHash(40, 0.6f, 'x');
        assertEquals('x', hash.getNoEntryValue());
        assertEquals(40, hash._set.length);
    }
    
    @Test
    public void testSetUp() {
        TCharHash hash = new TCharHash();
        hash.setUp(50);
        assertEquals(50, hash._set.length);
    }
    
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash._set = new char[] {'a', 'b', 'c'};
        hash._states = new byte[] {1, 1, 1};
        
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertTrue(hash.contains('c'));
        assertFalse(hash.contains('d'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash._set = new char[] {'a', 'b', 'c'};
        hash._states = new byte[] {1, 1, 0};
        
        final StringBuilder result = new StringBuilder();
        hash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                result.append(value);
                return true;
            }
        });
        assertEquals("ab", result.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash._set = new char[] {'a', 'b', 'c'};
        hash._states = new byte[] {1, 1, 1};
        
        hash.removeAt(1);
        assertEquals('a', hash._set[0]);
        assertEquals('c', hash._set[1]);
    }
    
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash._set = new char[] {'a', 'b', 'c'};
        hash._states = new byte[] {1, 1, 1};
        
        assertEquals(0, hash.index('a'));
        assertEquals(1, hash.index('b'));
        assertEquals(2, hash.index('c'));
        assertEquals(-1, hash.index('d'));
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        hash._set = new char[] {'a', 'b', 'c'};
        hash._states = new byte[] {1, 1, 1};
        
        assertEquals(-1, hash.indexRehashed('d', 0, 0, (byte) 0));
        assertEquals(-1, hash.indexRehashed('a', 1, 1, (byte) 0));
        assertEquals(0, hash.indexRehashed('a', 1, 1, (byte) 1));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        hash._set = new char[] {'a', 'b', 'c'};
        hash._states = new byte[] {1, 1, 1};
        
        assertEquals(1, hash.insertKey('a'));
        assertEquals(3, hash.insertKey('d'));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash();
        hash._set = new char[] {'a', 'b', 'c'};
        hash._states = new byte[] {1, 1, 1};
        
        assertEquals(3, hash.insertKeyRehash('d', 0, 0, (byte) 0));
        assertEquals(5, hash.insertKeyRehash('d', 0, 0, (byte) 2));
        assertEquals(0, hash.insertKeyRehash('a', 0, 0, (byte) 1));
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        hash._set = new char[] {'a', 'b', 'c'};
        hash._states = new byte[] {1, 1, 1};
        
        hash.insertKeyAt(1, 'x');
        assertEquals('x', hash._set[1]);
        assertEquals(1, hash._states[1]);
    }
}