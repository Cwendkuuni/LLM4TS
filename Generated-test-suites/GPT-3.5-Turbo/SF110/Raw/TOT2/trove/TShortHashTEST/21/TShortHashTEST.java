import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testGetNoEntryValue() {
        TShortHash tShortHash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.setUp(10);
        assertEquals(10, tShortHash._set.length);
    }

    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 5);
        assertTrue(tShortHash.contains((short) 5));
        assertFalse(tShortHash.contains((short) 10));
    }

    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 5);
        assertTrue(tShortHash.contains((short) 5));
        assertEquals(0, tShortHash.insertKey((short) 5));
    }
}