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
        char noEntryValue = tCharHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        int capacity = tCharHash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertEquals(10, tCharHash._set.length);
    }

    @Test
    public void testContains() {
        char val = 'a';
        boolean contains = tCharHash.contains(val);
        assertFalse(contains);
    }

    @Test
    public void testForEach() {
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return false;
            }
        };
        boolean result = tCharHash.forEach(procedure);
        assertFalse(result);
    }

    @Test
    public void testRemoveAt() {
        int index = 0;
        tCharHash.removeAt(index);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash._set[index]);
        assertEquals(0, tCharHash._states[index]);
    }

    @Test
    public void testIndex() {
        char val = 'a';
        int index = tCharHash.index(val);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        char val = 'a';
        int index = tCharHash.insertKey(val);
        assertEquals(-1, index);
        assertFalse(tCharHash.consumeFreeSlot);
    }

    @Test
    public void testInsertKeyRehash() {
        char val = 'a';
        int index = 0;
        int hash = HashFunctions.hash(val) & Integer.MAX_VALUE;
        byte state = tCharHash._states[index];
        int insertIndex = tCharHash.insertKeyRehash(val, index, hash, state);
        assertEquals(-2, insertIndex);
    }

    @Test
    public void testInsertKeyAt() {
        int index = 0;
        char val = 'a';
        tCharHash.insertKeyAt(index, val);
        assertEquals(val, tCharHash._set[index]);
        assertEquals(1, tCharHash._states[index]);
    }
}