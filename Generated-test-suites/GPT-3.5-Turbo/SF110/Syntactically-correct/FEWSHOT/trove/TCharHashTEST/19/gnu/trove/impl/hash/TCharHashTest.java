
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

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

        hash.insertKey('a');
        assertTrue(hash.contains('a'));
        assertFalse(hash.contains('b'));

        hash.insertKey('b');
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));

        hash.removeAt(1);
        assertTrue(hash.contains('a'));
        assertFalse(hash.contains('b'));
    }

    @Test
    public void testForEach() {
        hash.insertKey('a');
        hash.insertKey('b');

        final StringBuilder result = new StringBuilder();
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                result.append(value);
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals("ab", result.toString());
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey('a');
        hash.insertKey('b');

        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));

        hash.removeAt(0);
        assertFalse(hash.contains('a'));
        assertTrue(hash.contains('b'));

        hash.removeAt(0);
        assertFalse(hash.contains('a'));
        assertFalse(hash.contains('b'));
    }

    @Test
    public void testIndex() {
        hash.insertKey('a');
        hash.insertKey('b');

        assertEquals(0, hash.index('a'));
        assertEquals(1, hash.index('b'));
        assertEquals(-1, hash.index('c'));
    }

    @Test
    public void testIndexRehashed() {
        hash.insertKey('a');
        hash.insertKey('b');

        assertEquals(0, hash.indexRehashed('a', 0, 0, (byte) 1));
        assertEquals(1, hash.indexRehashed('b', 1, 0, (byte) 1));
        assertEquals(-1, hash.indexRehashed('c', -1, 0, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey('a'));
        assertEquals(1, hash.insertKey('b'));
        assertEquals(-2, hash.insertKey('a'));
    }

    @Test
    public void testInsertKeyRehash() {
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');

        assertEquals(0, hash.insertKeyRehash('a', 0, 0, (byte) 2));
        assertEquals(1, hash.insertKeyRehash('b', 1, 0, (byte) 2));
        assertEquals(3, hash.insertKeyRehash('d', 2, 0, (byte) 0));
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 'a');
        hash.insertKeyAt(1, 'b');

        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertFalse(hash.contains('c'));
    }
}

