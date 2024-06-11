import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testConstructor1() {
        TCharHash tCharHash = new TCharHash();
        assertNotNull(tCharHash);

        char no_entry_value = tCharHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, no_entry_value);
    }

    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        TCharHash tCharHash = new TCharHash(initialCapacity);
        assertNotNull(tCharHash);

        char no_entry_value = tCharHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, no_entry_value);
    }

    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.8f;
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor);
        assertNotNull(tCharHash);

        char no_entry_value = tCharHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, no_entry_value);
    }

    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.8f;
        char no_entry_value = 'X';
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor, no_entry_value);
        assertNotNull(tCharHash);

        char actualNoEntryValue = tCharHash.getNoEntryValue();
        assertEquals(no_entry_value, actualNoEntryValue);
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TCharHash tCharHash = new TCharHash();
        int capacity = tCharHash.setUp(initialCapacity);

        assertEquals(initialCapacity, capacity);
        assertNotNull(tCharHash._set);
    }

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[]{ 'a', 'b', 'c', 'd' };

        assertTrue(tCharHash.contains('a'));
        assertTrue(tCharHash.contains('b'));
        assertFalse(tCharHash.contains('z'));
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[]{ 'a', 'b', 'c', 'd' };

        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };

        assertTrue(tCharHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[]{ 'a', 'b', 'c', 'd' };
        tCharHash._states = new byte[]{ 1, 1, 1, 1 };

        tCharHash.removeAt(2);
        assertEquals('a', tCharHash._set[0]);
        assertEquals('b', tCharHash._set[1]);
        assertEquals(0, tCharHash._set[2]);
        assertEquals('d', tCharHash._set[3]);

        assertEquals(1, tCharHash._states[0]);
        assertEquals(1, tCharHash._states[1]);
        assertEquals(0, tCharHash._states[2]);
        assertEquals(1, tCharHash._states[3]);
    }

    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._states = new byte[]{ 1, 1, 0, 0, 1 };
        tCharHash._set = new char[]{ 'a', 'b', ' ', ' ', 'c' };

        int index = tCharHash.index('a');
        assertEquals(0, index);

        index = tCharHash.index('b');
        assertEquals(1, index);

        index = tCharHash.index('c');
        assertEquals(4, index);

        index = tCharHash.index('z');
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[]{ 'a', 'b', 'c', 'd', 'e' };
        tCharHash._states = new byte[]{ 1, 1, 1, 1, 1 };

        int index = tCharHash.indexRehashed('a', 0, 123, (byte)1);
        assertEquals(0, index);

        index = tCharHash.indexRehashed('d', 1, 456, (byte)1);
        assertEquals(1, index);

        index = tCharHash.indexRehashed('c', 2, 789, (byte)1);
        assertEquals(2, index);

        index = tCharHash.indexRehashed('z', 0, 123, (byte)1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._states = new byte[]{ 0, 1, 1, 0, 0 };
        tCharHash._set = new char[]{ ' ', 'a', 'b', ' ', ' ' };

        int index = tCharHash.insertKey('a');
        assertEquals(-2, index);

        index = tCharHash.insertKey('c');
        assertEquals(0, index);
        assertEquals('c', tCharHash._set[0]);

        index = tCharHash.insertKey('d');
        assertEquals(3, index);
        assertEquals('d', tCharHash._set[3]);
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._states = new byte[]{ 1, 1, 0, 0, 0 };
        tCharHash._set = new char[]{ 'a', 'b', ' ', ' ', ' ' };

        int index = tCharHash.insertKeyRehash('a', 0, 123, (byte)1);
        assertEquals(0, index);

        index = tCharHash.insertKeyRehash('c', 1, 456, (byte)1);
        assertEquals(1, index);

        index = tCharHash.insertKeyRehash('d', 1, 456, (byte)2);
        assertEquals(3, index);
        assertEquals('d', tCharHash._set[3]);

        index = tCharHash.insertKeyRehash('x', 0, 789, (byte)2);
        assertEquals(2, index);
        assertEquals('x', tCharHash._set[2]);

        try {
            tCharHash.insertKeyRehash('y', 0, 789, (byte)1);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
        }
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._states = new byte[]{ 0, 1, 0 };
        tCharHash._set = new char[]{ ' ', 'a', ' ' };

        tCharHash.insertKeyAt(0, 'x');
        assertEquals('x', tCharHash._set[0]);
        assertEquals(1, tCharHash._states[0]);

        tCharHash.insertKeyAt(2, 'y');
        assertEquals('y', tCharHash._set[2]);
        assertEquals(1, tCharHash._states[2]);
    }
}