package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testConstructor1() {
        TShortHash tShortHash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash(initialCapacity);
        assertEquals(initialCapacity, tShortHash.size());
    }

    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        short val = 5;
        tShortHash.insertKey(val);
        assertTrue(tShortHash.contains(val));
    }

    // Add more test cases for other methods

}