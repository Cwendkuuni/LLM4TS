
package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import gnu.trove.procedure.TShortProcedure;
import org.junit.Before;
import org.junit.Test;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash() {
            @Override
            protected int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            protected void insertKeyAt(int index, short val) {
                // Do nothing
            }
        };
    }

    @Test
    public void testConstructor1() {
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
        assertEquals(10, tShortHash._set.length);
    }

    @Test
    public void testConstructor2() {
        tShortHash = new TShortHash(20);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
        assertEquals(20, tShortHash._set.length);
    }

    @Test
    public void testConstructor3() {
        tShortHash = new TShortHash(20, 0.75f);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
        assertEquals(20, tShortHash._set.length);
    }

    @Test
    public void testConstructor4() {
        short noEntryValue = 99;
        tShortHash = new TShortHash(20, 0.75f, noEntryValue);
        assertEquals(noEntryValue, tShortHash.getNoEntryValue());
        assertEquals(20, tShortHash._set.length);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int capacity = 20;
        assertEquals(capacity, tShortHash.setUp(capacity));
        assertEquals(capacity, tShortHash._set.length);
    }

    @Test
    public void testContains() {
        assertFalse(tShortHash.contains((short) 2));
        tShortHash._set[0] = 2;
        assertTrue(tShortHash.contains((short) 2));
    }

    @Test
    public void testForEach() {
        final short[] values = {2, 4, 6, 8};
        tShortHash._set = values;
        tShortHash._states = new byte[]{1, 1, 0, 1};

        assertTrue(tShortHash.forEach(value -> {
            for (short val : values) {
                if (val == value) {
                    return true;
                }
            }
            return false;
        }));

        assertFalse(tShortHash.forEach(value -> value == 10));
    }

    @Test
    public void testRemoveAt() {
        tShortHash._set[0] = 2;
        tShortHash._states[0] = 1;
        assertEquals(1, tShortHash._states[0]);
        tShortHash.removeAt(0);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash._set[0]);
        assertEquals(0, tShortHash._states[0]);
    }

    @Test
    public void testIndex() {
        assertEquals(-1, tShortHash.index((short) 2));
        tShortHash._set[0] = 2;
        tShortHash._states[0] = 1;
        assertEquals(0, tShortHash.index((short) 2));
    }

    @Test
    public void testInsertKey() {
        short noEntryValue = 99;
        tShortHash = new TShortHash(20, 0.75f, noEntryValue);

        int index = tShortHash.insertKey((short) 2);
        assertEquals(0, index);
        assertEquals((short) 2, tShortHash._set[0]);
        assertEquals((byte) 1, tShortHash._states[0]);

        tShortHash._states[0] = 2; // Set state to REMOVED
        index = tShortHash.insertKey((short) 2);
        assertEquals(0, index);
        assertEquals((short) 2, tShortHash._set[0]);
        assertEquals((byte) 1, tShortHash._states[0]);

        tShortHash._states[0] = 1;
        assertEquals(0, tShortHash.insertKey((short) 2));
        assertEquals(-1, tShortHash.insertKey((short) 3));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        int length = tShortHash._set.length;
        tShortHash.insertKeyRehash((short) 2, length - 1, 1, (byte) 1);
    }

    @Test
    public void testInsertKeyAt() {
        tShortHash.insertKeyAt(0, (short) 2);
        assertEquals((short) 2, tShortHash._set[0]);
        assertEquals((byte) 1, tShortHash._states[0]);
    }
}
