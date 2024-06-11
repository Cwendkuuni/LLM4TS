package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testGetNoEntryValue() {
        TShortHash t = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, t.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TShortHash t = new TShortHash();
        assertEquals(10, t.setUp(10));
    }

    @Test
    public void testContains() {
        TShortHash t = new TShortHash();
        assertFalse(t.contains((short) 5));
        t.insertKey((short) 5);
        assertTrue(t.contains((short) 5));
    }

    @Test
    public void testForEach() {
        TShortHash t = new TShortHash();
        assertFalse(t.forEach((val) -> false));
        t.insertKey((short) 1);
        t.insertKey((short) 2);
        assertTrue(t.forEach((val) -> true));
    }

    @Test
    public void testRemoveAt() {
        TShortHash t = new TShortHash();
        t.insertKey((short) 1);
        t.insertKey((short) 2);
        t.removeAt(0);
        assertFalse(t.contains((short) 1));
        assertTrue(t.contains((short) 2));
    }

    @Test
    public void testIndex() {
        TShortHash t = new TShortHash();
        assertEquals(-1, t.index((short) 5));
        t.insertKey((short) 5);
        assertEquals(5, t.index((short) 5));
    }

    @Test
    public void testInsertKey() {
        TShortHash t = new TShortHash();
        assertEquals(0, t.insertKey((short) 5));
        assertEquals(1, t.insertKey((short) 6));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash t = new TShortHash();
        assertEquals(4, t.insertKeyRehash((short) 1, 4, 12, (byte) 1));
        assertEquals(5, t.insertKeyRehash((short) 2, 5, 13, (byte) 2));
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash t = new TShortHash();
        t.insertKeyAt(3, (short) 7);
        assertTrue(t.contains((short) 7));
    }
}