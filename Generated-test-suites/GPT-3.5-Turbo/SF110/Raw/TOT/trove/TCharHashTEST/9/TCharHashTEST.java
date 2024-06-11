import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    // Test case for default constructor
    @Test
    public void testDefaultConstructor() {
        TCharHash charHash = new TCharHash();
        char noEntryValue = charHash.getNoEntryValue();

        // Test default values
        assertArrayEquals(new char[0], charHash._set);
        assertEquals('\0', noEntryValue);
    }

    // Test case for constructor with initial capacity
    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 5;
        TCharHash charHash = new TCharHash(initialCapacity);
        char noEntryValue = charHash.getNoEntryValue();

        // Test initial capacity
        assertEquals(initialCapacity, charHash._set.length);
        // Test default values
        for (char value : charHash._set) {
            assertEquals(noEntryValue, value);
        }
    }

    // Test case for constructor with initial capacity and load factor
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 5;
        float loadFactor = 0.75f;
        TCharHash charHash = new TCharHash(initialCapacity, loadFactor);
        char noEntryValue = charHash.getNoEntryValue();

        // Test initial capacity
        assertEquals(initialCapacity, charHash._set.length);
        // Test default values
        for (char value : charHash._set) {
            assertEquals(noEntryValue, value);
        }
    }

    // Test case for constructor with initial capacity, load factor and no_entry_value
    @Test
    public void testConstructorWithAllParams() {
        int initialCapacity = 5;
        float loadFactor = 0.75f;
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash(initialCapacity, loadFactor, noEntryValue);

        // Test initial capacity
        assertEquals(initialCapacity, charHash._set.length);
        // Test default values
        for (char value : charHash._set) {
            assertEquals(noEntryValue, value);
        }
        // Test no_entry_value
        assertEquals(noEntryValue, charHash.getNoEntryValue());
    }

    // Test case for setUp method
    @Test
    public void testSetUp() {
        int initialCapacity = 5;
        TCharHash charHash = new TCharHash();
        int capacity = charHash.setUp(initialCapacity);

        // Test capacity
        assertEquals(initialCapacity, capacity);
        // Test _set array
        assertEquals(initialCapacity, charHash._set.length);
        // Test default values
        for (char value : charHash._set) {
            assertEquals('\0', value);
        }
    }

    // Test case for contains method (positive case)
    @Test
    public void testContains_Positive() {
        char value = 'A';
        TCharHash charHash = new TCharHash();
        charHash.insertKey(value);

        assertTrue(charHash.contains(value));
    }

    // Test case for contains method (negative case)
    @Test
    public void testContains_Negative() {
        char value1 = 'A';
        char value2 = 'B';
        TCharHash charHash = new TCharHash();
        charHash.insertKey(value1);

        assertFalse(charHash.contains(value2));
    }

    // Test case for forEach method (positive case)
    @Test
    public void testForEach_Positive() {
        char[] values = {'A', 'B', 'C'};
        TCharHash charHash = new TCharHash();
        for (char value : values) {
            charHash.insertKey(value);
        }

        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                // Perform some action
                return true;
            }
        };

        assertTrue(charHash.forEach(procedure));
    }

    // Test case for forEach method (negative case)
    @Test
    public void testForEach_Negative() {
        char[] values = {'A', 'B', 'C'};
        TCharHash charHash = new TCharHash();
        for (char value : values) {
            charHash.insertKey(value);
        }

        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                // Perform some action
                return false;  // return false to stop iteration
            }
        };

        assertFalse(charHash.forEach(procedure));
    }

    // Test case for removeAt method
    @Test
    public void testRemoveAt() {
        char value = 'A';
        TCharHash charHash = new TCharHash();
        charHash.insertKey(value);

        // Test _set array
        assertEquals(value, charHash._set[0]);
        // Test _states array
        assertEquals(1, charHash._states[0]);

        charHash.removeAt(0);

        // Test _set array
        assertEquals('\0', charHash._set[0]);
        // Test _states array
        assertEquals(0, charHash._states[0]);
    }

    // Test case for index method (positive case)
    @Test
    public void testIndex_Positive() {
        char value = 'A';
        TCharHash charHash = new TCharHash();
        charHash.insertKey(value);

        int index = charHash.index(value);

        assertEquals(0, index);
    }

    // Test case for index method (negative case)
    @Test
    public void testIndex_Negative() {
        char value1 = 'A';
        char value2 = 'B';
        TCharHash charHash = new TCharHash();
        charHash.insertKey(value1);

        int index = charHash.index(value2);

        assertEquals(-1, index);
    }

    // Test case for indexRehashed method
    @Test
    public void testIndexRehashed() {
        char value = 'A';
        int index = 2;
        int hash = 5;
        byte state = 1;
        TCharHash charHash = new TCharHash();
        charHash._set = new char[]{'X', 'Y', value, 'Z'};
        charHash._states = new byte[]{0, 1, 2, 1};

        int result = charHash.indexRehashed(value, index, hash, state);

        assertEquals(index, result);
    }

    // Test case for insertKey method (positive case)
    @Test
    public void testInsertKey_Positive() {
        char value = 'A';
        TCharHash charHash = new TCharHash();

        int index = charHash.insertKey(value);

        assertEquals(0, index);
        assertEquals(value, charHash._set[0]);
        assertEquals(1, charHash._states[0]);
    }

    // Test case for insertKey method (negative case)
    @Test
    public void testInsertKey_Negative() {
        char value1 = 'A';
        char value2 = 'B';
        TCharHash charHash = new TCharHash();
        charHash.insertKey(value1);

        int index = charHash.insertKey(value1);

        assertEquals(-1, index);

        index = charHash.insertKey(value2);

        assertEquals(1, index);
        assertEquals(value2, charHash._set[1]);
        assertEquals(1, charHash._states[1]);
    }

    // Test case for insertKeyRehash method
    @Test
    public void testInsertKeyRehash() {
        char value = 'A';
        int index = 2;
        int hash = 5;
        byte state = 2;
        TCharHash charHash = new TCharHash();
        charHash._set = new char[]{'X', 'Y', value, 'Z'};
        charHash._states = new byte[]{0, 1, 2, 1};

        int result = charHash.insertKeyRehash(value, index, hash, state);

        assertEquals(index, result);
    }

    // Test case for insertKeyAt method
    @Test
    public void testInsertKeyAt() {
        char value = 'A';
        int index = 2;
        TCharHash charHash = new TCharHash();
        charHash._set = new char[3];
        charHash._states = new byte[3];

        charHash.insertKeyAt(index, value);

        assertEquals(value, charHash._set[index]);
        assertEquals(1, charHash._states[index]);
    }
}