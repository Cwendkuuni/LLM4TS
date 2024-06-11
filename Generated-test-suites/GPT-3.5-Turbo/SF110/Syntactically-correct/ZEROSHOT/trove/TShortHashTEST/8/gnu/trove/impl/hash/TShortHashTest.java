package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setup() {
        tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            protected int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {
                
            }
        };
    }

    @Test
    public void testContains() {
        assertFalse(tShortHash.contains((short) 1));
        assertFalse(tShortHash.contains((short) 2));
    }

    @Test
    public void testForEach() {
        assertFalse(tShortHash.forEach((val) -> {
            return false;
        }));
    }

    @Test
    public void testGetNoEntryValue() {
        short no_entry_value = tShortHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, no_entry_value);
    }
}