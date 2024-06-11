import static org.junit.Assert.*;
import org.junit.*;

public class TShortHashTest {

    TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash();
    }

    @Test
    public void getNoEntryValueTest() {
        short expected = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
        assertEquals(expected, tShortHash.getNoEntryValue());
    }

    @Test
    public void setUpTest() {
        int initialCapacity = 10;
        int capacity = tShortHash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        short[] expectedSet = new short[initialCapacity];
        assertArrayEquals(expectedSet, tShortHash._set);
    }

    @Test
    public void containsTest() {
        short val = 5;
        assertFalse(tShortHash.contains(val));
    }

    @Test
    public void forEachTest() {
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return value > 0;
            }
        };
        assertTrue(tShortHash.forEach(procedure));
    }

    @Test
    public void removeAtTest() {
        int index = 5;
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        tShortHash._states = new byte[]{1, 1, 1, 1, 1};
        short noEntryValue = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
        tShortHash.no_entry_value = noEntryValue;
        tShortHash.removeAt(index);
        assertEquals(noEntryValue, tShortHash._set[index]);
        assertEquals(0, tShortHash._states[index]);
    }

    @Test
    public void indexTest() {
        short val = 5;
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        tShortHash._states = new byte[]{1, 1, 1, 1, 1};
        int expectedIndex = 4;
        assertEquals(expectedIndex, tShortHash.index(val));
    }

    @Test
    public void indexRehashedTest() {
        short key = 5;
        int index = 4;
        int hash = 20;
        byte state = 0;
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        tShortHash._states = new byte[]{1, 1, 1, 1, 1};
        int expectedIndex = -1;
        assertEquals(expectedIndex, tShortHash.indexRehashed(key, index, hash, state));
    }

    @Test
    public void insertKeyTest() {
        short val = 5;
        int index = 4;
        int hash = 20;
        byte state = 1;
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        tShortHash._states = new byte[]{1, 1, 1, 1, 1};
        assertFalse(tShortHash.consumeFreeSlot);
        int expectedIndex = -index - 1;
        assertEquals(expectedIndex, tShortHash.insertKey(val));
    }

    @Test
    public void insertKeyRehashTest() {
        short val = 5;
        int index = 4;
        int hash = 20;
        byte state = 0;
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        tShortHash._states = new byte[]{1, 1, 1, 1, 1};
        assertFalse(tShortHash.consumeFreeSlot);
        try {
            tShortHash.insertKeyRehash(val, index, hash, state);
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
        }
    }

    @Test
    public void insertKeyAtTest() {
        int index = 4;
        short val = 5;
        tShortHash._set = new short[]{1, 2, 3, 4, 0};
        tShortHash._states = new byte[]{1, 1, 0, 0, 0};
        tShortHash.insertKeyAt(index, val);
        assertEquals(val, tShortHash._set[index]);
        assertEquals(1, tShortHash._states[index]);
    }

}