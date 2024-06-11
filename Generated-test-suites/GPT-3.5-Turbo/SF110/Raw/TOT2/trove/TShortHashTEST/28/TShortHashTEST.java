package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testGetNoEntryValue() {
        TShortHash tShortHash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 5);
        assertTrue(tShortHash.contains((short) 5));
        assertFalse(tShortHash.contains((short) 10));
    }

    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 2);
        tShortHash.insertKey((short) 4);
        tShortHash.insertKey((short) 6);
        TShortProcedure tShortProcedure = (value) -> {
            if (value % 2 == 0) {
                return true;
            } else {
                return false;
            }
        };
        assertFalse(tShortHash.forEach(tShortProcedure));
    }
    
    // Additional test cases for other methods can be added here

}