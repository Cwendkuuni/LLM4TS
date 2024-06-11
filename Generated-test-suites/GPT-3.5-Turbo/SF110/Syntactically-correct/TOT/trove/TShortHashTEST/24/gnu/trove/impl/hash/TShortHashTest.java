import junit.framework.TestCase;

public class TShortHashTest extends TestCase {

    private TShortHash tShortHash;

    protected void setUp() {
        tShortHash = new TShortHash();
    }

    // Test case for TShortHash() constructor
    public void testConstructor() {
        assertNotNull(tShortHash);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    // Test case for TShortHash(int initialCapacity) constructor
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        tShortHash = new TShortHash(initialCapacity);
        assertNotNull(tShortHash);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
        assertEquals(initialCapacity, tShortHash._set.length);
        assertEquals(initialCapacity, tShortHash._states.length);
        for (int i = 0; i < initialCapacity; i++) {
            assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash._set[i]);
            assertEquals(0, tShortHash._states[i]);
        }
    }

    // Test case for TShortHash(int initialCapacity, float loadFactor) constructor
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        tShortHash = new TShortHash(initialCapacity, loadFactor);
        assertNotNull(tShortHash);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
        assertEquals(initialCapacity, tShortHash._set.length);
        assertEquals(initialCapacity, tShortHash._states.length);
        for (int i = 0; i < initialCapacity; i++) {
            assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash._set[i]);
            assertEquals(0, tShortHash._states[i]);
        }
    }

    // Test case for TShortHash(int initialCapacity, float loadFactor, short no_entry_value) constructor
    public void testConstructorWithParameters() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short no_entry_value = 0;
        tShortHash = new TShortHash(initialCapacity, loadFactor, no_entry_value);
        assertNotNull(tShortHash);
        assertEquals(no_entry_value, tShortHash.getNoEntryValue());
        assertEquals(initialCapacity, tShortHash._set.length);
        assertEquals(initialCapacity, tShortHash._states.length);
        for (int i = 0; i < initialCapacity; i++) {
            assertEquals(no_entry_value, tShortHash._set[i]);
            assertEquals(0, tShortHash._states[i]);
        }
    }

    // Test case for getNoEntryValue() method
    public void testGetNoEntryValue() {
        short no_entry_value = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
        assertEquals(no_entry_value, tShortHash.getNoEntryValue());
    }

    // Test case for setUp(int initialCapacity) method
    public void testSetUp() {
        int initialCapacity = 10;
        int capacity = tShortHash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertEquals(initialCapacity, tShortHash._set.length);
        assertEquals(initialCapacity, tShortHash._states.length);
        for (int i = 0; i < initialCapacity; i++) {
            assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash._set[i]);
            assertEquals(0, tShortHash._states[i]);
        }
    }

    // Test case for contains(short val) method - returning true
    public void testContains() {
        short val = 5;
        tShortHash._set[0] = val;
        tShortHash._states[0] = 1;
        assertTrue(tShortHash.contains(val));
    }

    // Test case for contains(short val) method - returning false
    public void testContainsNotFound() {
        short val = 10;
        assertFalse(tShortHash.contains(val));
    }

    // Test case for forEach(TShortProcedure procedure) method - returning true
    public void testForEach() {
        short val1 = 5;
        short val2 = 10;
        tShortHash._set[0] = val1;
        tShortHash._states[0] = 1;
        tShortHash._set[1] = val2;
        tShortHash._states[1] = 1;
        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short value) {
                return value == val1 || value == val2;
            }
        };
        assertTrue(tShortHash.forEach(procedure));
    }

    // Test case for forEach(TShortProcedure procedure) method - returning false
    public void testForEachWithFailure() {
        short val = 5;
        tShortHash._set[0] = val;
        tShortHash._states[0] = 1;
        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short value) {
                return value != val;
            }
        };
        assertFalse(tShortHash.forEach(procedure));
    }

    // Test case for removeAt(int index) method
    public void testRemoveAt() {
        short val = 5;
        int index = 0;
        tShortHash._set[index] = val;
        tShortHash._states[index] = 1;
        tShortHash.removeAt(index);
        assertEquals(0, tShortHash._set[index]);
        assertEquals(0, tShortHash._states[index]);
    }

    // Test case for index(short val) method - returning non-negative index
    public void testIndex() {
        short val = 5;
        int index = 0;
        tShortHash._set[index] = val;
        tShortHash._states[index] = 1;
        assertEquals(index, tShortHash.index(val));
    }

    // Test case for index(short val) method - returning -1
    public void testIndexNotFound() {
        short val = 10;
        assertEquals(-1, tShortHash.index(val));
    }

    // Test case for indexRehashed(short key, int index, int hash, byte state) method - returning non-negative index
    public void testIndexRehashed() {
        short key = 5;
        int index = 0;
        int hash = key % 10;
        byte state = 1;
        tShortHash._set[index] = key;
        tShortHash._states[index] = state;
        assertEquals(index, tShortHash.indexRehashed(key, index, hash, state));
    }


    // Test case for indexRehashed(short key, int index, int hash, byte state) method - returning -1
    public void testIndexRehashedNotFound() {
        short key = 10;
        int index = 0;
        int hash = key % 10;
        byte state = 1;
        assertEquals(-1, tShortHash.indexRehashed(key, index, hash, state));
    }

    // Test case for insertKey(short val) method - inserting key at free slot
    public void testInsertKey() {
        short val = 5;
        int hash = val % 10;
        int index = hash;
        tShortHash._states[index] = 0;
        tShortHash.insertKey(val);
        assertEquals(val, tShortHash._set[index]);
        assertEquals(1, tShortHash._states[index]);
    }

    // Test case for insertKey(short val) method - key already exists
    public void testInsertKeyAlreadyExists() {
        short val = 5;
        int hash = val % 10;
        int index = hash;
        tShortHash._set[index] = val;
        tShortHash._states[index] = 1;
        assertEquals(-index - 1, tShortHash.insertKey(val));
    }

    // Test case for insertKeyRehash(short val, int index, int hash, byte state) method - inserting key at free slot after rehashing
    public void testInsertKeyRehash() {
        short val = 5;
        int hash = val % 10;
        int index = hash;
        byte state = 1;
        int probe = 1 + hash % (10 - 2);
        int loopIndex = index;
        tShortHash._states[index] = 2;
        index -= probe;
        if (index < 0) {
            index += 10;
        }
        tShortHash._states[index] = 0;
        tShortHash.insertKeyRehash(val, index, hash, state);
        assertEquals(val, tShortHash._set[index]);
        assertEquals(1, tShortHash._states[index]);
    }

    // Test case for insertKeyRehash(short val, int index, int hash, byte state) method - key already exists
    public void testInsertKeyRehashAlreadyExists() {
        short val = 5;
        int hash = val % 10;
        int index = hash;
        byte state = 1;
        int probe = 1 + hash % (10 - 2);
        int loopIndex = index;
        tShortHash._states[index] = state;
        tShortHash._set[index] = val;
        assertEquals(-index - 1, tShortHash.insertKeyRehash(val, index, hash, state));
    }

    // Test case for insertKeyRehash(short val, int index, int hash, byte state) method - failed to find free slot
    public void testInsertKeyRehashNoFreeSlot() {
        short val = 5;
        int hash = val % 10;
        int index = hash;
        byte state = 1;
        int probe = 1 + hash % (10 - 2);
        int loopIndex = index;
        while (index != loopIndex) {
            index -= probe;
            if (index < 0) {
                index += 10;
            }
            tShortHash._states[index] = 1;
            tShortHash._set[index] = val;
        }
        try {
            tShortHash.insertKeyRehash(val, index, hash, state);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
        }
    }

    // Test case for insertKeyAt(int index, short val) method
    public void testInsertKeyAt() {
        short val = 5;
        int index = 0;
        tShortHash.insertKeyAt(index, val);
        assertEquals(val, tShortHash._set[index]);
        assertEquals(1, tShortHash._states[index]);
    }
}