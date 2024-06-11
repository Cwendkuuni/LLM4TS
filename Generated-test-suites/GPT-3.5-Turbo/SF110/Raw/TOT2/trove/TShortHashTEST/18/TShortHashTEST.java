package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {
    
    @Test
    public void testConstructorDefaultNoEntryValue() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    // Proceed to the next method...
}