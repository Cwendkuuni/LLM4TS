import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(5.0f);
        assertTrue(hash.contains(5.0f));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(2.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(10.0f);
        assertEquals(0, hash.index(10.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(3.0f);
        TFloatProcedure procedure = value -> {
            assertTrue(value == 3.0f);
            return true;
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(1, 0.5f, 0.0f, 0.0);
        hash.insertKeyRehash(2.0f, 0, 5, (byte) 1);
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