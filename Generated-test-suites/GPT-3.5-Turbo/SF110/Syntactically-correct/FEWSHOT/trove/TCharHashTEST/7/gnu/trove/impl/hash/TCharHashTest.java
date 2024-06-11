package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertTrue(tCharHash.contains('a'));
    }

    @Test
    public void testInsertKey() {
        int index = tCharHash.insertKey('a');
        assertEquals(0, index);
        assertEquals('a', tCharHash._set[index]);
        assertEquals(1, tCharHash._states[index]);
    }

    @Test
    public void testInsertKeyRehash() {
        int index = tCharHash.insertKeyRehash('a', 0, 123, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyAt() {
        tCharHash.insertKeyAt(0, 'a');
        assertEquals('a', tCharHash._set[0]);
        assertEquals(1, tCharHash._states[0]);
    }

    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('a');
        tCharHash.removeAt(0);
        assertEquals('\u0000', tCharHash._set[0]);
        assertEquals(0, tCharHash._states[0]);
    }

    @Test
    public void testIndex() {
        tCharHash.insertKey('a');
        int index = tCharHash.index('a');
        assertEquals(0, index);
        tCharHash.insertKeyRehash('a', 0, 123, (byte) 1);
        index = tCharHash.index('a');
        assertEquals(0, index);
        index = tCharHash.index('b');
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        tCharHash.insertKeyAt(0, 'a');
        int index = tCharHash.indexRehashed('a', 0, 123, (byte) 1);
        assertEquals(0, index);
        index = tCharHash.indexRehashed('b', 0, 123, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testSetUp() {
        int capacity = tCharHash.setUp(10);
        assertEquals(10, capacity);
        assertEquals(10, tCharHash._set.length);
    }

    @Test
    public void testForEach() {
        tCharHash.insertKey('a');
        assertTrue(tCharHash.forEach(new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                assertEquals('a', value);
                return true;
            }
        }));
    }

    @Test
    public void testGetNoEntryValue() {
        char noEntryValue = tCharHash.getNoEntryValue();
        assertEquals('\0', noEntryValue);
    }
}