import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash();
    }

    @Test
    public void testNoEntryValue() {
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        int capacity = tShortHash.setUp(initialCapacity);

        assertEquals(initialCapacity, capacity);
        assertEquals(initialCapacity, tShortHash._set.length);
        assertEquals(initialCapacity, tShortHash._states.length);
    }

    @Test
    public void testContains() {
        tShortHash._set = new short[]{1, 2, 3, 4};
        tShortHash._states = new byte[]{1, 1, 1, 1};

        assertTrue(tShortHash.contains((short) 2));
        assertFalse(tShortHash.contains((short) 5));
    }

    @Test
    public void testForEach() {
        tShortHash._set = new short[]{1, 2, 3, 4};
        tShortHash._states = new byte[]{1, 1, 1, 1};

        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short value) {
                if (value == 2) {
                    return false;
                }
                return true;
            }
        };

        assertFalse(tShortHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        tShortHash._set = new short[]{1, 2, 3, 4};
        tShortHash._states = new byte[]{1, 1, 1, 1};

        tShortHash.removeAt(2);

        assertEquals(0, tShortHash._set[2]);
        assertEquals(0, tShortHash._states[2]);
    }

    @Test
    public void testIndex() {
        tShortHash._set = new short[]{1, 2, 3, 4};
        tShortHash._states = new byte[]{1, 1, 1, 1};

        assertEquals(1, tShortHash.index((short) 2));
        assertEquals(-1, tShortHash.index((short) 5));
    }

    @Test
    public void testInsertKey() {
        tShortHash._set = new short[]{1, 2, 3, 4};
        tShortHash._states = new byte[]{1, 1, 1, 1};

        int index = tShortHash.insertKey((short) 5);

        assertEquals(4, index);
        assertEquals(5, tShortHash._set[4]);
        assertEquals(1, tShortHash._states[4]);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        tShortHash._set = new short[]{1, 2, 3, 4};
        tShortHash._states = new byte[]{1, 1, 1, 1};

        tShortHash.insertKey((short) 5);
        tShortHash.insertKey((short) 6);
    }

    @Test
    public void testInsertKeyRehash() {
        tShortHash._set = new short[]{1, 2, 0, 4};
        tShortHash._states = new byte[]{1, 1, 2, 1};

        int index = tShortHash.insertKeyRehash((short) 3, 2, 2, (byte) 2);

        assertEquals(2, index);
        assertEquals(3, tShortHash._set[2]);
        assertEquals(1, tShortHash._states[2]);
    }

    @Test
    public void testInsertKeyAt() {
        tShortHash._set = new short[]{1, 0, 3, 4};
        tShortHash._states = new byte[]{1, 0, 1, 1};

        tShortHash.insertKeyAt(1, (short) 2);

        assertEquals(2, tShortHash._set[1]);
        assertEquals(1, tShortHash._states[1]);
    }
}