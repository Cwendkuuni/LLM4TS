package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testContains() {
        assertFalse(hash.contains(1.0f));
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        assertFalse(hash.forEach(val -> val == 1.0f));
        hash.insertKey(1.0f);
        assertTrue(hash.forEach(val -> val == 1.0f));
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        assertEquals(1.0f, hash._set[0], 0.0001);
        hash.removeAt(0);
        assertEquals(0.0f, hash._set[0], 0.0001);
    }

    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(1.0f));
        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
    }

    @Test
    public void testIndexRehashed() {
        assertEquals(-1, hash.indexRehashed(1.0f, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(-1, hash.insertKey(1.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        assertEquals(0, hash.insertKeyRehash(1.0f, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, hash._set[0], 0.0001);
    }

    @Test
    public void testXinsertKey() {
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(-1, hash.XinsertKey(1.0f));
    }

    @Test
    public void testWriteExternal() {
        // Need to implement this test
    }

    @Test
    public void testReadExternal() {
        // Need to implement this test
    }

}