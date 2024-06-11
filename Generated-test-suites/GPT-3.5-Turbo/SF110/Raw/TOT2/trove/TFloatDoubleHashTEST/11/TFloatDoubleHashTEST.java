package gnu.trove.impl.hash;

import gnu.trove.procedure.TFloatProcedure;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor1() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructor2() {
        TFloatDoubleHash hash = new TFloatDoubleHash(100);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructor3() {
        TFloatDoubleHash hash = new TFloatDoubleHash(100, 0.5f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructor4() {
        TFloatDoubleHash hash = new TFloatDoubleHash(100, 0.5f, 10.0f, 20.0);
        assertEquals(10.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(20.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(100);
        assertNotNull(hash._set);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(5.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertTrue(hash.forEach(value -> true));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.removeAt(0);
        assertEquals(0.0f, hash._set[0], 0.0);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(-1, hash.index(5.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(5.0f);
        assertEquals(5.0f, hash._set[0], 0.0);
    }

    @Test
    public void testXInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.XinsertKey(5.0f);
        assertEquals(5.0f, hash._set[0], 0.0);
    }

    @Test
    public void testWriteExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        try {
            hash.writeExternal(null);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testReadExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        try {
            hash.readExternal(null);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }
}