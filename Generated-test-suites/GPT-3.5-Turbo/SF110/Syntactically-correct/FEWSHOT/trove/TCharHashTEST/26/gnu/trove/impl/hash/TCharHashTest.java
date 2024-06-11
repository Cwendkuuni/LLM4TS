package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {
    
    @Test
    public void testConstructor1() {
        TCharHash hash = new TCharHash();
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor2() {
        TCharHash hash = new TCharHash(10);
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor3() {
        TCharHash hash = new TCharHash(10, 0.5f);
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor4() {
        TCharHash hash = new TCharHash(10, 0.5f, 'x');
        assertEquals('x', hash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('a');
        assertTrue(hash.contains('a'));
        assertFalse(hash.contains('b'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        final StringBuilder sb = new StringBuilder();
        hash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                sb.append(value);
                return true;
            }
        });
        assertEquals("abc", sb.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        hash.removeAt(1);
        assertFalse(hash.contains('b'));
    }
    
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('a');
        hash.insertKey('b');
        assertEquals(0, hash.index('a'));
        assertEquals(1, hash.index('b'));
        assertEquals(-1, hash.index('c'));
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('a');
        hash.insertKey('b');
        assertEquals(1, hash.indexRehashed('b', 0, 0, (byte) 1));
        assertEquals(-1, hash.indexRehashed('c', 0, 0, (byte) 1));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash(10);
        assertEquals(0, hash.insertKey('a'));
        assertEquals(1, hash.insertKey('b'));
        assertEquals(-2, hash.insertKey('b'));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('a');
        hash.insertKey('b');
        assertEquals(1, hash.insertKeyRehash('b', 0, 0, (byte) 1));
        assertEquals(-1, hash.insertKeyRehash('c', 0, 0, (byte) 1));
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash(10);
        hash.insertKeyAt(0, 'a');
        hash.insertKeyAt(1, 'b');
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
    }
}