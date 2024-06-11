import gnu.trove.procedure.TShortProcedure;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {
    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash(10, 0.75f, (short) -1);
    }

    @Test
    public void testContains() {
        assertFalse(tShortHash.contains((short) 5));
        tShortHash.insertKey((short) 5);
        assertTrue(tShortHash.contains((short) 5));
    }

    @Test
    public void testForEach() {
        final short[] values = new short[]{1, 2, 3};
        for (short value : values) {
            tShortHash.insertKey(value);
        }

        final short[] procedureValues = new short[3];
        tShortHash.forEach(new TShortProcedure() {
            private int i = 0;

            @Override
            public boolean execute(short value) {
                procedureValues[i++] = value;
                return true;
            }
        });

        assertArrayEquals(values, procedureValues);
    }

    @Test
    public void testRemoveAt() {
        final short[] values = new short[]{1, 2, 3};
        for (short value : values) {
            tShortHash.insertKey(value);
        }

        tShortHash.removeAt(0);
        assertFalse(tShortHash.contains(values[0]));

        tShortHash.removeAt(1);
        assertFalse(tShortHash.contains(values[1]));

        tShortHash.removeAt(2);
        assertFalse(tShortHash.contains(values[2]));
    }

    @Test
    public void testIndex() {
        final short[] values = new short[]{1, 2, 3};
        for (short value : values) {
            tShortHash.insertKey(value);
        }

        assertEquals(1, tShortHash.index(2));
        assertEquals(-1, tShortHash.index(4));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, tShortHash.insertKey((short) 5));
        assertEquals(1, tShortHash.insertKey((short) 10));
        assertEquals(-2, tShortHash.insertKey((short) 5));
    }

    @Test
    public void testInsertKeyRehash() {
        final short[] values = new short[]{1, 2, 3, 4, 5, 6};
        for (short value : values) {
            tShortHash.insertKey(value);
        }

        final short[] rehashedValues = new short[]{7, 8, 9};
        for (short value : rehashedValues) {
            tShortHash.insertKeyRehash(value, 5, 123, (byte) 1);
        }

        for (short value : rehashedValues) {
            assertTrue(tShortHash.contains(value));
        }
    }

    @Test
    public void testInsertKeyAt() {
        tShortHash.insertKeyAt(0, (short) 5);
        tShortHash.insertKeyAt(1, (short) 10);

        assertTrue(tShortHash.contains((short) 5));
        assertTrue(tShortHash.contains((short) 10));
    }
}