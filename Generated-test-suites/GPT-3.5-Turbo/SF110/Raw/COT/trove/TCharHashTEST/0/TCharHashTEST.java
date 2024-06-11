
import org.junit.Test;

import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertFalse(hash.contains('c'));
    }

    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        TCharHash h = hash;
        assertTrue(h.forEach(val -> {
            assertTrue(val == 'a' || val == 'b' || val == 'c');
            return true;
        }));
    }

    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        hash.removeAt(1);
        assertFalse(hash.contains('b'));
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('c'));
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        int index1 = hash.insertKey('a');
        int index2 = hash.insertKey('b');
        int index3 = hash.insertKey('c');
        assertEquals(index1, hash.index('a'));
        assertEquals(index2, hash.index('b'));
        assertEquals(index3, hash.index('c'));
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        assertEquals(0, hash.index('a'));
        assertEquals(1, hash.index('b'));
        assertEquals(2, hash.index('c'));
        assertEquals(-1, hash.index('d'));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        assertEquals(2, hash.indexRehashed('c', 0, 0, (byte) 1));
        assertEquals(-1, hash.indexRehashed('d', 0, 0, (byte) 1));
    }
}
