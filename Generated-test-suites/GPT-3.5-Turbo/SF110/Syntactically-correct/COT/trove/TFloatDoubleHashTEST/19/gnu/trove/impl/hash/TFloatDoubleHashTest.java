
import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash t = new TFloatDoubleHash();
        assertEquals(0.0f, t.getNoEntryKey(), 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash t = new TFloatDoubleHash();
        assertEquals(0.0, t.getNoEntryValue(), 0.0);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash t = new TFloatDoubleHash();
        assertFalse(t.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash t = new TFloatDoubleHash();
        assertFalse(t.forEach((float val) -> {
            return true;
        }));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash t = new TFloatDoubleHash();
        t.removeAt(0);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash t = new TFloatDoubleHash();
        assertEquals(-1, t.index(1.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash t = new TFloatDoubleHash();
        assertEquals(-1, t.indexRehashed(1.0f, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash t = new TFloatDoubleHash();
        assertEquals(0, t.insertKey(1.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash t = new TFloatDoubleHash();
        try {
            t.insertKeyRehash(1.0f, 0, 0, (byte) 0);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
        }
    }
}
