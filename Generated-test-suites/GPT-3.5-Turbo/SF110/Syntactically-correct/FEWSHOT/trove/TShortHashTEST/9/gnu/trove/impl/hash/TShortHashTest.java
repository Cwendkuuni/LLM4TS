package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {
    
    @Test
    public void testConstructorWithoutParameters() {
        TShortHash th = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, th.getNoEntryValue());
        assertNotNull(th._set);
        for (int i = 0; i < th._set.length; i++) {
            assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, th._set[i]);
        }
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        TShortHash th = new TShortHash(initialCapacity);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, th.getNoEntryValue());
        assertNotNull(th._set);
        for (int i = 0; i < th._set.length; i++) {
            assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, th._set[i]);
        }
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TShortHash th = new TShortHash(initialCapacity, loadFactor);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, th.getNoEntryValue());
        assertNotNull(th._set);
        for (int i = 0; i < th._set.length; i++) {
            assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, th._set[i]);
        }
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short noEntryValue = 0;
        TShortHash th = new TShortHash(initialCapacity, loadFactor, noEntryValue);
        assertEquals(noEntryValue, th.getNoEntryValue());
        assertNotNull(th._set);
        for (int i = 0; i < th._set.length; i++) {
            assertEquals(noEntryValue, th._set[i]);
        }
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TShortHash th = new TShortHash();
        int capacity = th.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertNotNull(th._set);
        assertEquals(initialCapacity, th._set.length);
    }

    @Test
    public void testContains() {
        TShortHash th = new TShortHash();
        assertFalse(th.contains((short) 0));
        th.insertKeyAt(0, (short) 0);
        assertTrue(th.contains((short) 0));
    }

    @Test
    public void testForEach() {
        TShortHash th = new TShortHash();
        TShortProcedure procedure = new TShortProcedure() {
            int counter = 0;
            @Override
            public boolean execute(short value) {
                counter++;
                return true;
            }
        };
        assertTrue(th.forEach(procedure));
        assertEquals(0, procedure.counter);
        th.insertKeyAt(0, (short) 1);
        th.insertKeyAt(1, (short) 2);
        assertTrue(th.forEach(procedure));
        assertEquals(2, procedure.counter);
    }

    @Test
    public void testRemoveAt() {
        TShortHash th = new TShortHash();
        th.insertKeyAt(0, (short) 1);
        assertEquals((short) 1, th._set[0]);
        th.removeAt(0);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, th._set[0]);
    }

    @Test
    public void testIndex() {
        TShortHash th = new TShortHash();
        assertEquals(-1, th.index((short) 1));
        th.insertKeyAt(0, (short) 1);
        assertEquals(0, th.index((short) 1));
        th.insertKeyAt(1, (short) 2);
        assertEquals(1, th.index((short) 2));
        assertEquals(-1, th.index((short) 3));
    }

    @Test
    public void testIndexRehashed() {
        TShortHash th = new TShortHash();
        assertEquals(-1, th.indexRehashed((short) 1, 0, 0, (byte) 0));
        th.insertKeyAt(0, (short) 1);
        assertEquals(0, th.indexRehashed((short) 1, 0, 0, (byte) 1));
        th.insertKeyAt(1, (short) 2);
        assertEquals(1, th.indexRehashed((short) 2, 1, 0, (byte) 1));
        assertEquals(-1, th.indexRehashed((short) 2, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        TShortHash th = new TShortHash();
        assertEquals(0, th.insertKey((short) 1));
        assertEquals(-1, th.insertKey((short) 1));
        assertEquals(1, th.insertKey((short) 2));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash th = new TShortHash();
        assertEquals(0, th.insertKeyRehash((short) 1, 0, 0, (byte) 0));
        assertEquals(-1, th.insertKeyRehash((short) 1, 0, 0, (byte) 1));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashFullKeySet() {
        TShortHash th = new TShortHash();
        th.insertKeyAt(0, (short) 1);
        th.insertKeyAt(1, (short) 2);
        th.insertKeyRehash((short) 3, 0, 0, (byte) 2);
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash th = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, th._set[0]);
        th.insertKeyAt(0, (short) 1);
        assertEquals((short) 1, th._set[0]);
    }

}