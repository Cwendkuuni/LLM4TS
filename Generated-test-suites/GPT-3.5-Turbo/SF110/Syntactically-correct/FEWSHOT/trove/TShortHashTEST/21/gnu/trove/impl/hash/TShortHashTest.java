package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testConstructor() {
        TShortHash tShortHash = new TShortHash();
        assertNotNull(tShortHash._set);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.no_entry_value);
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash(initialCapacity);
        assertNotNull(tShortHash._set);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.no_entry_value);
        assertEquals(initialCapacity, tShortHash._set.length);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor);
        assertNotNull(tShortHash._set);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.no_entry_value);
        assertEquals(initialCapacity, tShortHash._set.length);
        assertEquals(loadFactor, tShortHash._loadFactor, 0.001);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short noEntryValue = 0;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor, noEntryValue);
        assertNotNull(tShortHash._set);
        assertEquals(noEntryValue, tShortHash.no_entry_value);
        assertEquals(initialCapacity, tShortHash._set.length);
        assertEquals(loadFactor, tShortHash._loadFactor, 0.001);
    }

    @Test
    public void testGetNoEntryValue() {
        short noEntryValue = 0;
        TShortHash tShortHash = new TShortHash();
        assertEquals(noEntryValue, tShortHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash();
        int capacity = tShortHash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertEquals(capacity, tShortHash._set.length);
    }

    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        tShortHash._set = new short[] {1, 2, 3};
        assertTrue(tShortHash.contains((short)1));
        assertFalse(tShortHash.contains((short)4));
    }

    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        tShortHash._states = new byte[] {1, 1, 1};
        tShortHash._set = new short[] {1, 2, 3};
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return value == 1;
            }
        };
        assertTrue(tShortHash.forEach(procedure));
        tShortHash._states[1] = 2;
        assertFalse(tShortHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        int index = 1;
        TShortHash tShortHash = new TShortHash();
        tShortHash._set = new short[] {1, 2, 3};
        tShortHash._states = new byte[] {1, 1, 1};
        tShortHash.removeAt(index);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash._set[index]);
        assertEquals(0, tShortHash._states[index]);
    }

    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        tShortHash._set = new short[] {1, 2, 3};
        tShortHash._states = new byte[] {1, 1, 1};
        int index = tShortHash.index((short)2);
        assertEquals(1, index);
        index = tShortHash.index((short)4);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        int key = 2;
        int index = 2;
        int hash = 3;
        byte state = 1;
        int[] set = {1, 2, 3};
        byte[] states = {1, 1, 1};
        TShortHash tShortHash = new TShortHash() {
            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return super.indexRehashed(key, index, hash, state);
            }
        };
        tShortHash._set = set;
        tShortHash._states = states;
        int result = tShortHash.indexRehashed((short)key, index, hash, state);
        assertEquals(-1, result);
    }

    @Test
    public void testInsertKey() {
        int val = 1;
        int hash = 2;
        int index = 1;
        byte state = 0;
        TShortHash tShortHash = new TShortHash() {
            @Override
            int insertKeyAt(int index, short val) {
                return super.insertKeyAt(index, val);
            }
        };
        tShortHash._states = new byte[] {state};
        int result = tShortHash.insertKey((short)val);
        assertEquals(index, result);
    }

    @Test
    public void testInsertKeyRehash() {
        int val = 1;
        int index = 2;
        int hash = 3;
        byte state = 1;
        TShortHash tShortHash = new TShortHash() {
            @Override
            int insertKeyAt(int index, short val) {
                return super.insertKeyAt(index, val);
            }
        };
        int result = tShortHash.insertKeyRehash((short)val, index, hash, state);
        assertEquals(-1, result);
    }

    @Test
    public void testInsertKeyAt() {
        int index = 1;
        int val = 2;
        byte state = 1;
        TShortHash tShortHash = new TShortHash();
        tShortHash._set = new short[5];
        tShortHash._states = new byte[5];
        tShortHash.insertKeyAt(index, (short)val);
        assertEquals(val, tShortHash._set[index]);
        assertEquals(state, tShortHash._states[index]);
    }

}