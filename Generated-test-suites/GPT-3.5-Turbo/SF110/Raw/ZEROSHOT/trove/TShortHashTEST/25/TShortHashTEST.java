import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testConstructor() {
        TShortHash hash = new TShortHash();
        assertNotNull(hash);

        hash = new TShortHash(10);
        assertNotNull(hash);

        hash = new TShortHash(10, 0.75f);
        assertNotNull(hash);

        hash = new TShortHash(10, 0.75f, (short) 0);
        assertNotNull(hash);
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.setUp(10));
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        assertFalse(hash.contains((short) 0));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.removeAt(0);
        assertFalse(hash.contains((short) 0));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        assertEquals(-1, hash.index((short) 0));
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();
        assertEquals(-1, hash.indexRehashed((short) 0, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.insertKey((short) 0));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.insertKeyRehash((short) 0, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash();
        hash.insertKeyAt(0, (short) 0);
        assertTrue(hash.contains((short) 0));
    }
}