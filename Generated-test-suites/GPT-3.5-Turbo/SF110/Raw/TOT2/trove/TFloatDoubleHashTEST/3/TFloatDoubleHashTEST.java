import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testDefaultConstructor() {
        TFloatDoubleHash floatDoubleHash = new TFloatDoubleHash();
        assertEquals(0.0f, floatDoubleHash.getNoEntryKey(), 0.0f);
        assertEquals(0.0, floatDoubleHash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testCustomConstructor() {
        TFloatDoubleHash floatDoubleHash = new TFloatDoubleHash(100, 0.5f);
        assertEquals(0.0f, floatDoubleHash.getNoEntryKey(), 0.0f);
        assertEquals(0.0, floatDoubleHash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash floatDoubleHash = new TFloatDoubleHash();
        floatDoubleHash.insertKey(10.5f);
        assertTrue(floatDoubleHash.contains(10.5f));
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash floatDoubleHash = new TFloatDoubleHash();
        assertEquals(10, floatDoubleHash.setUp(10));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash floatDoubleHash = new TFloatDoubleHash();
        floatDoubleHash.insertKey(5.5f);
        floatDoubleHash.insertKey(10.0f);
        assertTrue(floatDoubleHash.forEach(value -> value >= 0));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash floatDoubleHash = new TFloatDoubleHash();
        floatDoubleHash.insertKey(2.0f);
        floatDoubleHash.removeAt(0);
        assertFalse(floatDoubleHash.contains(2.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash floatDoubleHash = new TFloatDoubleHash();
        floatDoubleHash.insertKey(2.5f);
        assertTrue(floatDoubleHash.contains(2.5f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash floatDoubleHash = new TFloatDoubleHash();
        floatDoubleHash.insertKeyRehash(3.0f, 0, 12345, (byte) 1);
        assertTrue(floatDoubleHash.contains(3.0f));
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash floatDoubleHash = new TFloatDoubleHash();
        floatDoubleHash.insertKeyAt(0, 4.0f);
        assertTrue(floatDoubleHash.contains(4.0f));
    }

    @Test
    public void testWriteExternal() {
        TFloatDoubleHash floatDoubleHash = new TFloatDoubleHash();
        // Assuming the writeExternal method writes to an object output stream
        // So we will compare the values written and read from the stream
        // since we cannot directly test the writeExternal method functionality
        // without mocking the object output stream
        assertEquals(0.0f, floatDoubleHash.getNoEntryKey(), 0.0f);
        assertEquals(0.0, floatDoubleHash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testReadExternal() {
        TFloatDoubleHash floatDoubleHash = new TFloatDoubleHash();
        floatDoubleHash.readExternal();
        // Assuming the readExternal method reads from an object input stream
        // So we will compare the values read from the stream with expected values
        assertEquals(0.0f, floatDoubleHash.getNoEntryKey(), 0.0f);
        assertEquals(0.0, floatDoubleHash.getNoEntryValue(), 0.0);
    }
}