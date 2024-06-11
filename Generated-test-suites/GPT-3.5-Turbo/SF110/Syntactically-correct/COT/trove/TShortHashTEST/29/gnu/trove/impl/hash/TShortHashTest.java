import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        assertFalse(hash.contains((short) 1));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        assertTrue(hash.forEach(null));
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.removeAt(0);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        assertEquals(-1, hash.index((short) 1));
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();
        assertEquals(-1, hash.indexRehashed((short) 1, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.insertKey((short) 1));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.insertKeyRehash((short) 1, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash();
        hash.insertKeyAt(0, (short) 1);
    }

}