package gnu.trove.impl.hash;

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

        TShortHash hash2 = new TShortHash(20);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash2.getNoEntryValue());
        assertEquals(20, hash2._set.length);

        TShortHash hash3 = new TShortHash(20, 0.5f);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash3.getNoEntryValue());
        assertEquals(20, hash3._set.length);

        TShortHash hash4 = new TShortHash(20, 0.5f, (short) 0);
        assertEquals(0, hash4.getNoEntryValue());
        assertEquals(20, hash4._set.length);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash._set[0] = 1;
        hash._states[0] = 1;
        assertTrue(hash.contains((short) 1));
        assertFalse(hash.contains((short) 2));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash._set[0] = 1;
        hash._states[0] = 1;

        TShortProcedure procedure = new TShortProcedure() {
            boolean executed = false;
            
            @Override
            public boolean execute(short value) {
                executed = true;
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertTrue(procedure.executed);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash._set[0] = 1;
        hash._states[0] = 1;

        hash.removeAt(0);
        assertEquals(0, hash._set[0]);
        assertEquals(0, hash._states[0]);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash._set[0] = 1;
        hash._states[0] = 1;

        assertEquals(0, hash.index((short) 1));
        assertEquals(-1, hash.index((short) 2));
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();
        hash._set[1] = 1;
        hash._set[2] = 2;
        hash._states[1] = 1;
        hash._states[2] = 1;

        assertEquals(1, hash.indexRehashed((short) 1, 1, 1, (byte) 1));
        assertEquals(-1, hash.indexRehashed((short) 1, 1, 1, (byte) 2));
        assertEquals(-1, hash.indexRehashed((short) 2, 2, 2, (byte) 1));
        assertEquals(2, hash.indexRehashed((short) 2, 2, 2, (byte) 2));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        hash._set[0] = 1;
        hash._states[0] = 1;

        assertEquals(-1, hash.insertKey((short) 1));
        assertEquals(0, hash.insertKey((short) 2));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        hash._set[1] = 1;
        hash._states[1] = 1;

        assertEquals(1, hash.insertKeyRehash((short) 1, 1, 1, (byte) 1));
        assertEquals(-2, hash.insertKeyRehash((short) 1, 1, 1, (byte) 2));
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash();

        hash.insertKeyAt(0, (short) 1);
        assertEquals(1, hash._set[0]);
        assertEquals(1, hash._states[0]);
    }
}