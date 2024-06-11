
package gnu.trove.impl.hash;

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
    public void testGetNoEntryValue() {
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        int capacity = tShortHash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertEquals(initialCapacity, tShortHash._set.length);
    }

    @Test
    public void testContains() {
        short val = 5;
        assertFalse(tShortHash.contains(val));

        tShortHash.insertKey(val);
        assertTrue(tShortHash.contains(val));
    }

    @Test
    public void testForEach() {
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return value < 10;
            }
        };

        assertFalse(tShortHash.forEach(procedure));

        for (int i = 0; i < 10; i++) {
            tShortHash.insertKey((short) i);
        }

        assertTrue(tShortHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        short val = 5;
        tShortHash.insertKey(val);
        assertEquals(val, tShortHash._set[0]);

        tShortHash.removeAt(0);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash._set[0]);
    }

    @Test
    public void testIndex() {
        short val = 5;
        assertEquals(-1, tShortHash.index(val));

        tShortHash.insertKey(val);
        assertEquals(0, tShortHash.index(val));
    }

    @Test
    public void testInsertKey() {
        short val = 5;
        int index = tShortHash.insertKey(val);
        assertEquals(0, index);

        index = tShortHash.insertKey(val);
        assertEquals(-1, index);
    }
}
