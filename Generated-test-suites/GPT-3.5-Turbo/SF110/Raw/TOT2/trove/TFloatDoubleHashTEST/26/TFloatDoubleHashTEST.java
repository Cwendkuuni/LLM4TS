import org.junit.*;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    TFloatDoubleHash tFloatDoubleHash;

    @Before
    public void setUp() {
        tFloatDoubleHash = new TFloatDoubleHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testContains() {
        assertFalse(tFloatDoubleHash.contains(5.5f));
        tFloatDoubleHash.insertKey(5.5f);
        assertTrue(tFloatDoubleHash.contains(5.5f));
    }

    @Test
    public void testRemoveAt() {
        tFloatDoubleHash.insertKey(3.2f);
        tFloatDoubleHash.removeAt(0);
        assertEquals(0.0f, tFloatDoubleHash._set[0], 0.0);
    }

    @Test
    public void testIndex() {
        tFloatDoubleHash.insertKey(10.0f);
        assertEquals(0, tFloatDoubleHash.index(10.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        tFloatDoubleHash.insertKey(3.5f);
        tFloatDoubleHash.insertKey(7.5f);
        tFloatDoubleHash.insertKey(11.5f);
        tFloatDoubleHash.insertKey(15.5f);
        tFloatDoubleHash.insertKey(19.5f);
        tFloatDoubleHash.insertKey(23.5f);
        tFloatDoubleHash.insertKey(27.5f);
        tFloatDoubleHash.insertKey(31.5f);
        tFloatDoubleHash.insertKey(35.5f);
        tFloatDoubleHash.insertKey(39.5f);
        tFloatDoubleHash.insertKey(43.5f);
        tFloatDoubleHash.insertKey(47.5f);
        tFloatDoubleHash.insertKey(51.5f);
        tFloatDoubleHash.insertKey(55.5f);
        tFloatDoubleHash.insertKey(59.5f);
        tFloatDoubleHash.insertKey(63.5f);
        tFloatDoubleHash.insertKey(67.5f);
        tFloatDoubleHash.insertKey(71.5f);
        tFloatDoubleHash.insertKey(75.5f);
        tFloatDoubleHash.insertKey(79.5f);
        tFloatDoubleHash.insertKey(83.5f);
        tFloatDoubleHash.insertKey(87.5f);
        tFloatDoubleHash.insertKey(91.5f);
        tFloatDoubleHash.insertKey(95.5f);
        tFloatDoubleHash.insertKey(99.5f);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashFull() {
        tFloatDoubleHash.insertKeyRehash(3.5f, 0, 0, (byte) 1);
    }

    @Test
    public void testInsertKeyAt() {
        tFloatDoubleHash.insertKeyAt(0, 5.0f);
        assertEquals(5.0f, tFloatDoubleHash._set[0], 0.0);
    }

    @Test
    public void testXinsertKey() {
        assertEquals(0, tFloatDoubleHash.XinsertKey(5.0f));
        assertEquals(-1, tFloatDoubleHash.XinsertKey(5.0f));
    }

    @Test
    public void testWriteExternal() {
        // To be implemented
    }

    @Test
    public void testReadExternal() {
        // To be implemented
    }

}