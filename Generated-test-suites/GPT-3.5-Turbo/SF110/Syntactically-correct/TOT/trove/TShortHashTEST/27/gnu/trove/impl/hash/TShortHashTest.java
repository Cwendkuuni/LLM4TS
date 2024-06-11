import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash();
    }

    @Test
    public void testNoEntryValue() {
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testInsertKey() {
        short val = 10;
        int index = tShortHash.insertKey(val);
        assertEquals(0, index);
        assertTrue(tShortHash.contains(val));

        val = 20;
        index = tShortHash.insertKey(val);
        assertEquals(1, index);
        assertTrue(tShortHash.contains(val));

        val = 30;
        index = tShortHash.insertKey(val);
        assertEquals(2, index);
        assertTrue(tShortHash.contains(val));
    }

    @Test
    public void testInsertKey_Rehash() {
        short val = 10;
        int index = tShortHash.insertKey(val);
        assertEquals(0, index);
        assertTrue(tShortHash.contains(val));

        val = 20;
        index = tShortHash.insertKey(val);
        assertEquals(1, index);
        assertTrue(tShortHash.contains(val));

        val = 30;
        index = tShortHash.insertKey(val);
        assertEquals(2, index);
        assertTrue(tShortHash.contains(val));

        // Rehashing
        val = 40;
        index = tShortHash.insertKey(val);
        assertEquals(3, index);
        assertTrue(tShortHash.contains(val));
    }

    @Test
    public void testIndex() {
        short val = 10;
        tShortHash.insertKey(val);
        int index = tShortHash.index(val);
        assertEquals(0, index);

        val = 20;
        tShortHash.insertKey(val);
        index = tShortHash.index(val);
        assertEquals(1, index);

        val = 30;
        tShortHash.insertKey(val);
        index = tShortHash.index(val);
        assertEquals(2, index);
    }

    @Test
    public void testIndex_NotFound() {
        short val = 10;
        tShortHash.insertKey(val);
        int index = tShortHash.index(val);
        assertEquals(0, index);

        val = 20;
        tShortHash.insertKey(val);
        index = tShortHash.index(val);
        assertEquals(1, index);

        // Value not in the hash
        val = 30;
        index = tShortHash.index(val);
        assertEquals(-1, index);
    }

    @Test
    public void testConsumeFreeSlot() {
        short val = 10;
        tShortHash.insertKey(val);
        val = 20;
        int index = tShortHash.insertKey(val);

        assertTrue(tShortHash.contains(val));

        // Remove the element at index = 1
        tShortHash.removeAt(1);
        assertFalse(tShortHash.contains(val));

        // Insert a new value and it should take the empty slot at index = 1
        val = 30;
        int newIndex = tShortHash.insertKey(val);
        assertEquals(1, newIndex);
        assertTrue(tShortHash.contains(val));
    }

    @Test
    public void testForEach() {
        final short[] expectedValues = {10, 20, 30, 40, 50};
        for (short val : expectedValues) {
            tShortHash.insertKey(val);
        }

        final short[] actualValues = new short[expectedValues.length];
        TShortProcedure procedure = new TShortProcedure() {
            int currentIndex = 0;

            @Override
            public boolean execute(short value) {
                actualValues[currentIndex++] = value;
                return true;
            }
        };

        assertTrue(tShortHash.forEach(procedure));

        assertArrayEquals(expectedValues, actualValues);
    }

}