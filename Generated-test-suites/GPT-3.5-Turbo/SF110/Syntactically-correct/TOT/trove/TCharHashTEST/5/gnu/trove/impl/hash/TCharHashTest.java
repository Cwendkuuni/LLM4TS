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
    public void testGetNoEntryValue() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 5;
        int capacity = tCharHash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertEquals(initialCapacity, tCharHash._set.length);
    }

    @Test
    public void testContains() {
        char[] set = {'a', 'b', 'c', 'd'};
        tCharHash._set = set;

        assertTrue(tCharHash.contains('a'));
        assertTrue(tCharHash.contains('b'));
        assertTrue(tCharHash.contains('c'));
        assertTrue(tCharHash.contains('d'));
        assertFalse(tCharHash.contains('e'));
    }

    @Test
    public void testForEach() {
        char[] set = {'a', 'b', 'c', 'd'};
        tCharHash._set = set;

        StringBuilder output = new StringBuilder();
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                output.append(value);
                return true;
            }
        };

        assertTrue(tCharHash.forEach(procedure));
        assertEquals("abcd", output.toString());
    }

    @Test
    public void testRemoveAt() {
        char[] set = {'a', 'b', 'c', 'd'};
        tCharHash._set = set;

        tCharHash.removeAt(2);

        assertTrue(tCharHash.contains('a'));
        assertTrue(tCharHash.contains('b'));
        assertFalse(tCharHash.contains('c'));
        assertTrue(tCharHash.contains('d'));
    }

    @Test
    public void testIndex() {
        char[] set = {'a', 'b', 'c', 'd'};
        tCharHash._set = set;

        assertEquals(0, tCharHash.index('a'));
        assertEquals(1, tCharHash.index('b'));
        assertEquals(2, tCharHash.index('c'));
        assertEquals(3, tCharHash.index('d'));
        assertEquals(-1, tCharHash.index('e'));
    }

    @Test
    public void testInsertKey() {
        char[] set = {'a', 'b'};
        tCharHash._set = set;

        assertEquals(-1, tCharHash.insertKey('a')); // Already exists
        assertEquals(-3, tCharHash.insertKey('c'));
        assertEquals('c', tCharHash._set[2]);
        assertEquals(2, tCharHash.insertKey('d'));
        assertEquals('d', tCharHash._set[3]);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash_exception() {
        char[] set = {'a', 'b', 'c', 'd'};
        tCharHash._set = set;

        tCharHash.insertKeyRehash('e', 0, 0, (byte) 1);
    }

}