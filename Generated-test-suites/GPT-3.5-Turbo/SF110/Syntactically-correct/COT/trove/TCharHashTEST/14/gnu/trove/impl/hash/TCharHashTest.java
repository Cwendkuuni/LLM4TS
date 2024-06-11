import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testGetNoEntryValue() {
        TCharHash tCharHash = new TCharHash();
        char expected = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        char actual = tCharHash.getNoEntryValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetUp() {
        TCharHash tCharHash = new TCharHash();
        int initialCapacity = 10;
        int expected = initialCapacity;
        int actual = tCharHash.setUp(initialCapacity);
        assertEquals(expected, actual);
    }

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        boolean expected = true;
        boolean actual = tCharHash.contains('a');
        assertEquals(expected, actual);
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };
        boolean expected = true;
        boolean actual = tCharHash.forEach(procedure);
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        int index = 0;
        tCharHash.removeAt(index);
        char expected = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        char actual = tCharHash._set[index];
        assertEquals(expected, actual);
    }

    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        int expected = 0;
        int actual = tCharHash.index('a');
        assertEquals(expected, actual);
    }

    @Test
    public void testIndexRehashed() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        int key = 'a';
        int index = 0;
        int hash = HashFunctions.hash(key) & Integer.MAX_VALUE;
        byte state = tCharHash._states[index];
        int expected = index;
        int actual = tCharHash.indexRehashed(key, index, hash, state);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        int key = 'a';
        int expected = 0;
        int actual = tCharHash.insertKey(key);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        int key = 'c';
        int index = 0;
        int hash = HashFunctions.hash(key) & Integer.MAX_VALUE;
        byte state = tCharHash._states[index];
        int expected = 2;
        int actual = tCharHash.insertKeyRehash(key, index, hash, state);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash tCharHash = new TCharHash();
        int index = 0;
        char key = 'a';
        tCharHash.insertKeyAt(index, key);
        char expected = key;
        char actual = tCharHash._set[index];
        assertEquals(expected, actual);
    }
}