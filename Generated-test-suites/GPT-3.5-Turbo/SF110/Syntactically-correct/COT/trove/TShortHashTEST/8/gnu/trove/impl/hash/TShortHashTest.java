
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash._set = new short[]{1, 2, 3, 4};
        assertTrue(hash.contains(2));
        assertFalse(hash.contains(5));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash._states = new byte[]{1, 1, 1, 1};
        hash._set = new short[]{1, 2, 3, 4};
        TShortProcedure procedure = new TShortProcedure() {
            int sum = 0;

            public boolean execute(short value) {
                sum += value;
                return true;
            }

            public int getSum() {
                return sum;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(10, procedure.getSum());
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash._states = new byte[]{1, 1, 1, 1};
        hash._set = new short[]{1, 2, 3, 4};
        hash.no_entry_value = -1;
        hash.removeAt(2);
        assertEquals(-1, hash._set[2]);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash._states = new byte[]{1, 1, 1, 1};
        hash._set = new short[]{1, 2, 3, 4};
        assertEquals(2, hash.index(3));
        assertEquals(-1, hash.index(5));
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();
        hash._states = new byte[]{1, 1, 1, 1, 0, 1};
        hash._set = new short[]{1, 2, 3, 4, 0, 5};
        hash.no_entry_value = -1;
        assertEquals(5, hash.indexRehashed(5, 5, 5, (byte) 0));
        assertEquals(-1, hash.indexRehashed(5, 5, 5, (byte) 2));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        hash._states = new byte[]{1, 0, 1, 0, 0, 0};
        hash._set = new short[]{1, 0, 3, 0, 0, 0};
        hash.no_entry_value = -1;
        hash.insertKey((short) 2);
        assertEquals((short) 2, hash._set[1]);
        assertTrue(hash.consumeFreeSlot);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        hash._states = new byte[]{1, 1, 1, 1, 0, 2};
        hash._set = new short[]{1, 2, 3, 4, 0, 5};
        hash.no_entry_value = -1;
        hash.insertKeyRehash((short) 8, 5, 5, (byte) 2);
        assertEquals((short) 8, hash._set[4]);
        assertFalse(hash.consumeFreeSlot);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyAtFullCapacity() {
        TShortHash hash = new TShortHash();
        hash._states = new byte[]{1, 1, 1, 1};
        hash._set = new short[]{1, 2, 3, 4};
        hash.no_entry_value = -1;
        hash.insertKeyAt(2, (short) 5);
    }
}
