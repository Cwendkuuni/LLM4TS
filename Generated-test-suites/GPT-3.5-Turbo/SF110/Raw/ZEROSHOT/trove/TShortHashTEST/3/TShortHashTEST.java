package gnu.trove.impl.hash;

import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testConstructor() {
        TShortHash tShortHash = new TShortHash();
        short no_entry_value = tShortHash.getNoEntryValue();
        short[] set = tShortHash._set;
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, no_entry_value);
        assertNotNull(set);
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash(initialCapacity);
        short no_entry_value = tShortHash.getNoEntryValue();
        short[] set = tShortHash._set;
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, no_entry_value);
        assertNotNull(set);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor);
        short no_entry_value = tShortHash.getNoEntryValue();
        short[] set = tShortHash._set;
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, no_entry_value);
        assertNotNull(set);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short no_entry_value = 0;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor, no_entry_value);
        assertEquals(no_entry_value, tShortHash.getNoEntryValue());
        assertNotNull(tShortHash._set);
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash();
        int capacity = tShortHash.setUp(initialCapacity);
        short[] set = tShortHash._set;
        assertNotNull(set);
        assertEquals(initialCapacity, set.length);
        assertEquals(initialCapacity, capacity);
    }

    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        short[] set = tShortHash._set;
        set[0] = 1;
        set[1] = 2;
        set[2] = 3;
        assertTrue(tShortHash.contains(1));
        assertTrue(tShortHash.contains(2));
        assertTrue(tShortHash.contains(3));
        assertFalse(tShortHash.contains(4));
    }

    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        short[] set = tShortHash._set;
        set[0] = 1;
        set[1] = 2;
        set[2] = 3;
        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short value) {
                System.out.println(value);
                return true;
            }
        };
        assertTrue(tShortHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        short[] set = tShortHash._set;
        set[0] = 1;
        set[1] = 2;
        set[2] = 3;
        tShortHash.removeAt(1);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, set[1]);
    }

    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        short[] set = tShortHash._set;
        set[0] = 1;
        set[1] = 2;
        set[2] = 3;
        assertEquals(0, tShortHash.index(1));
        assertEquals(1, tShortHash.index(2));
        assertEquals(2, tShortHash.index(3));
        assertEquals(-1, tShortHash.index(4));
    }

    @Test
    public void testIndexRehashed() {
        TShortHash tShortHash = new TShortHash();
        short[] set = tShortHash._set;
        set[0] = 1;
        set[1] = 2;
        set[2] = 3;
        short key = 2;
        int index = tShortHash.index(key);
        int hash = HashFunctions.hash(key) & Integer.MAX_VALUE;
        byte state = tShortHash._states[index];
        assertEquals(1, tShortHash.indexRehashed(key, index, hash, state));
    }

    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash();
        short[] set = tShortHash._set;
        tShortHash.insertKey((short)1);
        assertEquals(1, set[0]);
        tShortHash.insertKey((short)2);
        assertEquals(2, set[1]);
        tShortHash.insertKey((short)3);
        assertEquals(3, set[2]);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash tShortHash = new TShortHash();
        short[] set = tShortHash._set;

        tShortHash.insertKeyRehash((short)1, 0, 1, (byte)1);
        assertEquals(1, set[0]);
        
        tShortHash.insertKeyRehash((short)2, 1, 2, (byte)1);
        assertEquals(2, set[1]);
        
        tShortHash.insertKeyRehash((short)3, 2, 3, (byte)1);
        assertEquals(3, set[2]);
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash tShortHash = new TShortHash();
        short[] set = tShortHash._set;

        tShortHash.insertKeyAt(0, (short)1);
        assertEquals(1, set[0]);

        tShortHash.insertKeyAt(1, (short)2);
        assertEquals(2, set[1]);

        tShortHash.insertKeyAt(2, (short)3);
        assertEquals(3, set[2]);
    }
}