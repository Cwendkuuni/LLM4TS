import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash() {
            @Override
            protected void insertKeyAt(int index, short val) {
                super.insertKeyAt(index, val);
            }
        };
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        int actualCapacity = tShortHash.setUp(initialCapacity);
        assertEquals(initialCapacity, actualCapacity);
        assertNotNull(tShortHash._set);
    }

    @Test
    public void testContains() {
        short val = 5;
        assertFalse(tShortHash.contains(val));

        tShortHash.insertKeyAt(0, val);
        assertTrue(tShortHash.contains(val));
    }

    @Test
    public void testForEach() {
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return value > 0;
            }
        };

        assertFalse(tShortHash.forEach(procedure));

        tShortHash.insertKeyAt(0, (short) 1);
        assertTrue(tShortHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        int index = 0;

        tShortHash.insertKeyAt(index, (short) 5);
        tShortHash.removeAt(index);

        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash._set[index]);
    }

    @Test
    public void testIndex() {
        short val = 5;
        int expectedIndex = 2;

        tShortHash.insertKeyAt(expectedIndex, val);

        int actualIndex = tShortHash.index(val);
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testIndexRehashed() {
        short key = 5;
        int index = 3;
        int hash = 100;
        byte state = 1;

        int expectedIndex = 0;
        int actualIndex = tShortHash.indexRehashed(key, index, hash, state);

        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testInsertKey() {
        short val = 5;

        int expectedIndex = 3;
        int actualIndex = tShortHash.insertKey(val);

        assertEquals(expectedIndex, actualIndex);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashWhenNoSlotsAvailable() {
        short val = 5;

        int index = 0;
        int hash = 100;
        byte state = 2;

        tShortHash.insertKeyAt(index, val);
        tShortHash.insertKeyRehash(val, index, hash, state);
    }

    @Test
    public void testInsertKeyRehash() {
        short val = 5;

        int index = 1;
        int hash = 100;
        byte state = 1;

        int expectedIndex = 0;
        int actualIndex = tShortHash.insertKeyRehash(val, index, hash, state);

        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testInsertKeyAt() {
        int index = 0;
        short val = 5;

        tShortHash.insertKeyAt(index, val);

        assertEquals(val, tShortHash._set[index]);
        assertEquals((byte) 1, tShortHash._states[index]);
    }
}