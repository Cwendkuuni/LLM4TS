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

        hash.insertKey('a');
        assertTrue(hash.contains('a'));

        hash.remove('a');
        assertFalse(hash.contains('a'));
    }

    @Test
    public void testForEach() {
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');

        StringBuilder sb = new StringBuilder();
        hash.forEach((char val) -> {
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

        hash.removeAt(1);
        assertFalse(hash.contains('b'));
    }

    @Test
    public void testIndex() {
        assertEquals(-1, hash.index('a'));

        hash.insertKey('a');
        assertEquals(0, hash.index('a'));

        hash.remove('a');
        assertEquals(-1, hash.index('a'));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey('a'));
        assertTrue(hash.contains('a'));

        assertEquals(1, hash.insertKey('b'));
        assertTrue(hash.contains('b'));

        assertEquals(2, hash.insertKey('c'));
        assertTrue(hash.contains('c'));
    }
}