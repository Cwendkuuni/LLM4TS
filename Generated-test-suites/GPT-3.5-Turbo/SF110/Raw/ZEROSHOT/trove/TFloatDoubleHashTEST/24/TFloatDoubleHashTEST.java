package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float result = hash.getNoEntryKey();
        assertEquals(0.0f, result, 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        double result = hash.getNoEntryValue();
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int result = hash.setUp(10);
        assertEquals(10, result);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        boolean result = hash.contains(5.0f);
        assertFalse(result);
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        boolean result = hash.forEach((float val) -> {
            System.out.println(val);
            return true;
        });
        assertTrue(result);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.removeAt(0);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int result = hash.index(5.0f);
        assertEquals(-1, result);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int result = hash.indexRehashed(5.0f, 0, 0, (byte) 0);
        assertEquals(-1, result);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int result = hash.insertKey(5.0f);
        assertEquals(0, result);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int result = hash.insertKeyRehash(5.0f, 0, 0, (byte) 0);
        assertEquals(-1, result);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKeyAt(0, 5.0f);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int result = hash.XinsertKey(5.0f);
        assertEquals(0, result);
    }

    @Test
    public void testWriteExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        try {
            hash.writeExternal(null);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testReadExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        try {
            hash.readExternal(null);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}