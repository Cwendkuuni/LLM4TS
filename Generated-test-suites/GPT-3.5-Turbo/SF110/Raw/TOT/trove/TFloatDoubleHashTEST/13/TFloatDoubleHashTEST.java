
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
    }

    @Test
    public void testNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0, hash.getNoEntryValue(), 0.001);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
        assertEquals(2, hash.insertKey(3.0f));
        assertEquals(3, hash.insertKey(4.0f));
        assertEquals(4, hash.insertKey(5.0f));
        assertFalse(hash.contains(6.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        assertEquals(0, hash.insertKeyRehash(1.0f, 0, 1, (byte) 1));
        assertEquals(1, hash.insertKeyRehash(2.0f, 1, 2, (byte) 1));
        assertEquals(2, hash.insertKeyRehash(3.0f, 2, 3, (byte) 1));
        assertEquals(3, hash.insertKeyRehash(4.0f, 1, 1, (byte) 2));
        assertEquals(5, hash.insertKeyRehash(5.0f, 1, 1, (byte) 2));
        assertEquals(7, hash.insertKeyRehash(6.0f, 1, 1, (byte) 2));
        assertFalse(hash.contains(7.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testForEeach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        final StringBuilder sb = new StringBuilder();
        hash.forEach(val -> sb.append(val));
        assertEquals("123", sb.toString());
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.removeAt(1);
        assertFalse(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        assertEquals(-1, hash.index(1.0f));
        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        assertEquals(-1, hash.indexRehashed(1.0f, 0, 1, (byte) 1));
        hash.insertKey(3.0f);
        assertEquals(0, hash.indexRehashed(1.0f, 0, 1, (byte) 1));
    }
}
