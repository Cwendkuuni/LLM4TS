import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {

    private TCharHash hash;

    @Before
    public void setUp() {
        hash = new TCharHash();
    }

    @Test
    public void testConstructor() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        hash = new TCharHash(initialCapacity);
        assertEquals(initialCapacity, hash._set.length);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.5f;
        hash = new TCharHash(initialCapacity, loadFactor);
        assertEquals(initialCapacity, hash._set.length);
        assertEquals(loadFactor, hash._loadFactor, 0.001);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        int initialCapacity = 10;
        float loadFactor = 0.5f;
        char noEntryValue = '$';
        hash = new TCharHash(initialCapacity, loadFactor, noEntryValue);
        assertEquals(initialCapacity, hash._set.length);
        assertEquals(loadFactor, hash._loadFactor, 0.001);
        assertEquals(noEntryValue, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        assertEquals(initialCapacity, hash.setUp(initialCapacity));
        assertEquals(initialCapacity, hash._set.length);
    }

    @Test
    public void testContains() {
        char val = 'A';
        assertFalse(hash.contains(val));
        hash.insertKey(val);
        assertTrue(hash.contains(val));
    }

    @Test
    public void testForEach() {
        // Test procedure that returns false for specific values
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                return (value != 'A');
            }
        };

        hash.insertKey('A');
        hash.insertKey('B');
        assertTrue(hash.forEach(procedure));

        hash.insertKey('C');
        assertFalse(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        char val = 'A';
        hash.insertKey(val);
        assertTrue(hash.contains(val));
        hash.removeAt(0);
        assertFalse(hash.contains(val));
    }

    @Test
    public void testIndex() {
        char val = 'A';
        assertEquals(-1, hash.index(val));
        hash.insertKey(val);
        assertEquals(0, hash.index(val));
    }

    @Test
    public void testIndexRehashed() {
        char val = 'A';
        int index = 0;
        int hashValue = 1;
        byte state = 1;
        assertEquals(-1, hash.indexRehashed(val, index, hashValue, state));
        hash.insertKey(val);
        assertEquals(0, hash.indexRehashed(val, index, hashValue, state));
    }

    @Test
    public void testInsertKey() {
        char val = 'A';
        assertTrue(hash.insertKey(val) >= 0);
    }

    @Test
    public void testInsertKeyRehash() {
        char val = 'A';
        int index = 0;
        int hashValue = 1;
        byte state = 1;
        assertTrue(hash.insertKeyRehash(val, index, hashValue, state) >= 0);
    }

    @Test
    public void testInsertKeyAt() {
        char val = 'A';
        int index = 0;
        hash.insertKeyAt(index, val);
        assertEquals(val, hash._set[index]);
        assertEquals(1, hash._states[index]);
    }

}