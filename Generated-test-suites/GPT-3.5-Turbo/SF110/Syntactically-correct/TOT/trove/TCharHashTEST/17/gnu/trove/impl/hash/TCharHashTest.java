import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testConstructor1() {
        TCharHash h = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, h.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        TCharHash h = new TCharHash(10);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, h.getNoEntryValue());
        // test initial capacity
        assertEquals(10, h._states.length);
    }

    @Test
    public void testConstructor3() {
        TCharHash h = new TCharHash(10, 0.5f);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, h.getNoEntryValue());
        // test initial capacity and load factor
        assertEquals(10, h._states.length);
        assertEquals(0.5f, h.loadFactor, 0.001f);
    }

    @Test
    public void testConstructor4() {
        char no_entry_value = 'X';
        TCharHash h = new TCharHash(10, 0.5f, no_entry_value);
        assertEquals(no_entry_value, h.getNoEntryValue());
        // test initial capacity, load factor, and no_entry_value
        assertEquals(10, h._states.length);
        assertEquals(0.5f, h.loadFactor, 0.001f);
    }

    @Test
    public void testContains_Positive() {
        TCharHash h = new TCharHash();
        h.insertKey('A');
        assertTrue(h.contains('A'));
    }

    @Test
    public void testContains_Negative() {
        TCharHash h = new TCharHash();
        h.insertKey('A');
        assertFalse(h.contains('B'));
    }

    @Test
    public void testForEach() {
        TCharHash h = new TCharHash();
        h.insertKey('A');
        h.insertKey('B');
        h.insertKey('C');

        StringBuilder sb = new StringBuilder();
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                sb.append(value);
                return true;
            }
        };

        h.forEach(procedure);
        assertEquals("ABC", sb.toString());
    }

    @Test
    public void testRemoveAt() {
        TCharHash h = new TCharHash();
        h.insertKey('A');
        h.insertKey('B');
        h.insertKey('C');

        h.removeAt(1);
        assertFalse(h.contains('B'));
    }

    @Test
    public void testIndex() {
        TCharHash h = new TCharHash();
        h.insertKey('A');
        h.insertKey('B');
        h.insertKey('C');

        assertEquals(1, h.index('B'));
        assertEquals(-1, h.index('D'));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash h = new TCharHash();
        h.insertKey('A');
        h.insertKey('B');
        h.insertKey('C');

        // test indexRehashed for existing key
        assertEquals(2, h.indexRehashed('B', 1, 123, (byte) 1));

        // test indexRehashed for non-existing key
        assertEquals(-1, h.indexRehashed('D', 1, 123, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        TCharHash h = new TCharHash();
        h.insertKey('A');
        h.insertKey('B');
        h.insertKey('C');

        // test insertKey for existing key
        assertEquals(-2, h.insertKey('B'));

        // test insertKey for non-existing key
        assertEquals(3, h.insertKey('D'));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash h = new TCharHash();
        h.insertKey('A');
        h.insertKey('B');
        h.insertKey('C');

        // test insertKeyRehash for existing key
        assertEquals(2, h.insertKeyRehash('B', 1, 123, (byte) 1));

        // test insertKeyRehash for non-existing key
        assertEquals(3, h.insertKeyRehash('D', 1, 123, (byte) 1));
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash h = new TCharHash();
        h.insertKeyAt(1, 'A');
        assertEquals('A', h._set[1]);
        assertEquals(1, h._states[1]);
    }
}