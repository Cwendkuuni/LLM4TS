
package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testGetNoEntryValue() {
        TShortHash tShortHash = new TShortHash();
        short noEntryValue = tShortHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
    }

    @Test
    public void testSetUp() {
        TShortHash tShortHash = new TShortHash();
        int capacity = tShortHash.setUp(10);
        assertEquals(10, capacity);
    }

    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        // Test for typical use case
        assertFalse(tShortHash.contains((short) 5));
    }

    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        // Test for typical use case
        assertTrue(tShortHash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return value != 0;
            }
        }));
    }

    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        // Test for typical use case
        tShortHash.removeAt(0);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash._set[0]);
    }

    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        // Test for typical use case
        tShortHash.insertKeyAt(0, (short) 5);
        assertEquals(0, tShortHash.index((short) 5));
    }

    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash();
        // Test for typical use case
        int index = tShortHash.insertKey((short) 10);
        assertEquals(0, index);
    }
}