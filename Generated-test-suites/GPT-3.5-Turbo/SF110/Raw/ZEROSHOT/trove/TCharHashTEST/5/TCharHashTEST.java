import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;

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
        assertFalse(tCharHash.contains('b'));
    }

    @Test
    public void testForEach() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        final StringBuilder result = new StringBuilder();
        tCharHash.forEach(new TCharProcedure() {
            public boolean execute(final char value) {
                result.append(value);
                return true;
            }
        });
        assertEquals("abc", result.toString());
    }

    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('a');
        assertEquals(1, tCharHash.size());
        tCharHash.removeAt(0);
        assertEquals(0, tCharHash.size());
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
    public void testIndexRehashed() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        assertEquals(0, tCharHash.indexRehashed('a', 0, HashFunctions.hash('a') & Integer.MAX_VALUE, (byte) 1));
        assertEquals(1, tCharHash.indexRehashed('b', 1, HashFunctions.hash('b') & Integer.MAX_VALUE, (byte) 1));
        assertEquals(2, tCharHash.indexRehashed('c', 2, HashFunctions.hash('c') & Integer.MAX_VALUE, (byte) 1));
        assertEquals(-1, tCharHash.indexRehashed('d', 0, HashFunctions.hash('d') & Integer.MAX_VALUE, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, tCharHash.insertKey('a'));
        assertFalse(tCharHash.consumeFreeSlot);
        assertEquals(-1, tCharHash.insertKey('a'));
        assertTrue(tCharHash.consumeFreeSlot);
        assertEquals(1, tCharHash.insertKey('b'));
    }

    @Test
    public void testInsertKeyRehash() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        assertEquals(0, tCharHash.insertKeyRehash('a', 0, HashFunctions.hash('a') & Integer.MAX_VALUE, (byte) 2));
        assertEquals(1, tCharHash.insertKeyRehash('b', 1, HashFunctions.hash('b') & Integer.MAX_VALUE, (byte) 1));
        assertEquals(2, tCharHash.insertKeyRehash('c', 2, HashFunctions.hash('c') & Integer.MAX_VALUE, (byte) 1));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashFull() {
        char val = 'a';
        for (int i = 0; i < Constants.DEFAULT_CAPACITY; i++) {
            tCharHash.insertKeyRehash(val, i, HashFunctions.hash(val) & Integer.MAX_VALUE, (byte) 1);
            val++;
        }
        tCharHash.insertKeyRehash('z', 0, HashFunctions.hash('z') & Integer.MAX_VALUE, (byte) 1);
    }

    @Test
    public void testInsertKeyAt() {
        tCharHash.insertKeyAt(0, 'a');
        assertEquals(1, tCharHash.size());
        assertEquals('a', tCharHash._set[0]);
        assertEquals((byte) 1, tCharHash._states[0]);
    }

    @Test
    public void testNoEntryValue() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }

}