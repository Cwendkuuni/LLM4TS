package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');

        assertTrue(hash.contains('a'));
        assertFalse(hash.contains('b'));
    }

    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');

        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');

        hash.removeAt(0);
        assertFalse(hash.contains('a'));
        assertTrue(hash.contains('b'));
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');

        assertEquals(0, hash.index('a'));
        assertEquals(-1, hash.index('b'));
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        int index1 = hash.insertKey('a');
        int index2 = hash.insertKey('b');

        assertEquals(0, index1);
        assertEquals(1, index2);
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash(2, 0.5f, '-');
        int index1 = hash.insertKeyRehash('a', 0, 11, (byte) 1);
        int index2 = hash.insertKeyRehash('b', 1, 22, (byte) 1);

        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals('-', hash._set[2]);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash_full() {
        TCharHash hash = new TCharHash(2, 0.5f, '-');
        hash.insertKeyRehash('a', 0, 11, (byte) 1);
        hash.insertKeyRehash('b', 1, 22, (byte) 1);
        hash.insertKeyRehash('c', 2, 33, (byte) 1);
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        hash.insertKeyAt(0, 'a');
        hash.insertKeyAt(1, 'b');

        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
    }

}