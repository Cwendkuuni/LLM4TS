import static org.junit.Assert.*;
import org.junit.*;

public class TCharHashTest {

    @Test
    public void testConstructorWithDefault() {
        TCharHash tCharHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        TCharHash tCharHash = new TCharHash(initialCapacity);
        assertEquals(initialCapacity, tCharHash.size());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor);
        assertEquals(initialCapacity, tCharHash.size());
        assertEquals(loadFactor, tCharHash.getLoadFactor(), 0.001);
        assertTrue(tCharHash.isEmpty());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        char no_entry_value = 'x';
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor, no_entry_value);
        assertEquals(initialCapacity, tCharHash.size());
        assertEquals(loadFactor, tCharHash.getLoadFactor(), 0.001);
        assertEquals(no_entry_value, tCharHash.getNoEntryValue());
        assertTrue(tCharHash.isEmpty());
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TCharHash tCharHash = new TCharHash();
        assertEquals(initialCapacity, tCharHash.setUp(initialCapacity));
    }

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        char val = 'x';
        assertFalse(tCharHash.contains(val));
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return true;
            }
        };
        assertTrue(tCharHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        int index = 0;
        TCharHash tCharHash = new TCharHash();
        tCharHash.removeAt(index);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash._set[index]);
    }

    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        char val = 'x';
        assertEquals(-1, tCharHash.index(val));
    }

    @Test
    public void testIndexRehashed() {
        char key = 'x';
        int index = 0;
        int hash = 1;
        byte state = 1;
        TCharHash tCharHash = new TCharHash();
        assertEquals(-1, tCharHash.indexRehashed(key, index, hash, state));
    }

    @Test
    public void testInsertKey() {
        char val = 'x';
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey(val);
        assertEquals(val, tCharHash._set[0]);
    }

    @Test
    public void testInsertKeyRehash() {
        char val = 'x';
        int index = 0;
        int hash = 1;
        byte state = 1;
        TCharHash tCharHash = new TCharHash();
        try {
            tCharHash.insertKeyRehash(val, index, hash, state);
            fail("Exception not thrown");
        } catch (IllegalStateException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testInsertKeyAt() {
        int index = 0;
        char val = 'x';
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKeyAt(index, val);
        assertEquals(val, tCharHash._set[index]);
        assertEquals(1, tCharHash._states[index]);
    }
}