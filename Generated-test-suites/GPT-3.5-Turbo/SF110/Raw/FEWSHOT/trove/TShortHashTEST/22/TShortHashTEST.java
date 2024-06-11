import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testConstructor() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(10, hash._set.length);

        hash = new TShortHash(20);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(20, hash._set.length);

        hash = new TShortHash(30, 0.5f);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(30, hash._set.length);

        short noEntryValue = 10;
        hash = new TShortHash(40, 0.6f, noEntryValue);
        assertEquals(noEntryValue, hash.getNoEntryValue());
        assertEquals(40, hash._set.length);
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash();
        assertEquals(10, hash.setUp(10));
        assertEquals(20, hash._set.length);

        hash = new TShortHash();
        assertEquals(100, hash.setUp(100));
        assertEquals(100, hash._set.length);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 10);
        assertTrue(hash.contains((short) 10));
        assertFalse(hash.contains((short) 20));

        hash.insertKey((short) 20);
        assertTrue(hash.contains((short) 20));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 10);
        hash.insertKey((short) 20);
        hash.insertKey((short) 30);

        TShortProcedure procedure = new TShortProcedure() {
            long sum = 0;

            public boolean execute(short value) {
                sum += value;
                return true;
            }

            public long getSum() {
                return sum;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(60, procedure.getSum());
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 10);
        hash.insertKey((short) 20);
        hash.insertKey((short) 30);

        hash.removeAt(1);
        assertTrue(hash.contains((short) 10));
        assertFalse(hash.contains((short) 20));
        assertTrue(hash.contains((short) 30));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 10);
        hash.insertKey((short) 20);
        hash.insertKey((short) 30);

        assertEquals(1, hash.index((short) 20));
        assertEquals(-1, hash.index((short) 40));
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 10);
        hash.insertKey((short) 20);
        hash.insertKey((short) 30);

        assertEquals(1, hash.indexRehashed((short) 20, 1, 123, (byte) 1));
        assertEquals(-1, hash.indexRehashed((short) 40, 1, 123, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 10);
        hash.insertKey((short) 20);
        hash.insertKey((short) 30);

        assertEquals(-2, hash.insertKey((short) 10));
        assertEquals(-3, hash.insertKey((short) 40));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();

        hash.insertKey((short) 10);
        hash.insertKey((short) 20);
        hash.insertKey((short) 30);

        assertEquals(3, hash.insertKeyRehash((short) 40, 3, 123, (byte) 1));
        assertEquals(-1, hash.insertKeyRehash((short) 40, 1, 123, (byte) 1));
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash();
        hash.insertKeyAt(1, (short) 20);
        assertEquals((short) 20, hash._set[1]);
        assertEquals((byte) 1, hash._states[1]);
    }
}