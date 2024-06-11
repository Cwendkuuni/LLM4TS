
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    private TFloatDoubleHash tFloatDoubleHash;

    @Before
    public void setUp() {
        tFloatDoubleHash = new TFloatDoubleHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.001);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.001);
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 100;
        int capacity = tFloatDoubleHash.setUp(initialCapacity);
        float[] set = tFloatDoubleHash._set;

        assertEquals(initialCapacity, capacity);
        assertNotNull(set);
        assertEquals(initialCapacity, set.length);
    }

    @Test
    public void testContains() {
        tFloatDoubleHash.insertKey(1.0f);
        assertTrue(tFloatDoubleHash.contains(1.0f));
        assertFalse(tFloatDoubleHash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);
        tFloatDoubleHash.insertKey(3.0f);

        TFloatProcedure procedure = (val) -> {
            // Do something with val
            return true;
        };

        assertTrue(tFloatDoubleHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);
        int oldSize = tFloatDoubleHash.size();
        tFloatDoubleHash.removeAt(0);

        assertEquals(oldSize-1, tFloatDoubleHash.size());
        assertEquals(0.0f, tFloatDoubleHash._set[0], 0.001);
    }

    @Test
    public void testIndex() {
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);

        assertEquals(0, tFloatDoubleHash.index(1.0f));
        assertEquals(1, tFloatDoubleHash.index(2.0f));
        assertEquals(-1, tFloatDoubleHash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        int index1 = tFloatDoubleHash.insertKey(1.0f);
        int index2 = tFloatDoubleHash.insertKey(2.0f);
        int index3 = tFloatDoubleHash.insertKey(3.0f);

        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(2, index3);
        assertEquals(1.0f, tFloatDoubleHash._set[0], 0.001);
        assertEquals(2.0f, tFloatDoubleHash._set[1], 0.001);
        assertEquals(3.0f, tFloatDoubleHash._set[2], 0.001);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKey_RehashFull() {
        int initialCapacity = 2;
        tFloatDoubleHash.setUp(initialCapacity);
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);
        tFloatDoubleHash.insertKey(3.0f);
    }

    @Test
    public void testInsertKeyAt() {
        tFloatDoubleHash.insertKeyAt(0, 1.0f);
        tFloatDoubleHash.insertKeyAt(1, 2.0f);

        assertEquals(1.0f, tFloatDoubleHash._set[0], 0.001);
        assertEquals(2.0f, tFloatDoubleHash._set[1], 0.001);
    }

    @Test
    public void testWriteExternal() {
        // TODO: Implement this test
    }

    @Test
    public void testReadExternal() {
        // TODO: Implement this test
    }
}
