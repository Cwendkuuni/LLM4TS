package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.procedure.*;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash() {
            @Override
            public int capacity() {
                return 10;
            }
        };
    }

    @Test
    public void testGetNoEntryValue() {
        short expected = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
        short result = tShortHash.getNoEntryValue();

        assertEquals(expected, result);
    }

    @Test
    public void testSetUp() {
        int expected = 10;
        int result = tShortHash.setUp(10);

        assertEquals(expected, result);
    }

    @Test
    public void testContains() {
        assertFalse(tShortHash.contains((short) 0));
    }

    @Test
    public void testForEach() {
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return true;
            }
        };

        assertTrue(tShortHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        tShortHash._states = new byte[]{1, 0};
        tShortHash._set = new short[]{1, 0};

        tShortHash.removeAt(0);

        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash._set[0]);
        assertEquals(0, tShortHash._states[0]);
    }

    @Test
    public void testIndex() {
        tShortHash._states = new byte[]{1, 1};
        tShortHash._set = new short[]{1, 2};

        int expected = 0;
        int result = tShortHash.index((short) 1);

        assertEquals(expected, result);
    }

    @Test
    public void testInsertKey() {
        int expected = -1;
        int result = tShortHash.insertKey((short) 1);

        assertEquals(expected, result);
    }

    @Test
    public void testInsertKeyRehash() {
        tShortHash._states = new byte[]{1, 1};
        tShortHash._set = new short[]{1, 2};

        int expected = -1;
        int result = tShortHash.insertKeyRehash((short) 1, 0, 0, (byte) 0);

        assertEquals(expected, result);
    }

    @Test
    public void testInsertKeyAt() {
        tShortHash._set = new short[1];
        tShortHash._states = new byte[1];

        tShortHash.insertKeyAt(0, (short) 1);

        assertEquals((short) 1, tShortHash._set[0]);
        assertEquals((byte) 1, tShortHash._states[0]);
    }

}