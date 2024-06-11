import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNotNull(hash);
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
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
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int index = hash.insertKey(5.0f);
        assertTrue(index >= 0);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int index = hash.insertKeyRehash(5.0f, 0, 12345, (byte) 1);
        assertTrue(index >= 0);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKeyAt(0, 5.0f);
        assertEquals(5.0f, hash._set[0], 0.0);
        assertEquals((byte) 1, hash._states[0]);
    }

    @Test
    public void testWriteExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        hash.writeExternal(out);
        byte[] result = bos.toByteArray();
        assertTrue(result.length > 0);
    }

    @Test
    public void testReadExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        hash.writeExternal(out);
        byte[] result = bos.toByteArray();

        ByteArrayInputStream bis = new ByteArrayInputStream(result);
        ObjectInput in = new ObjectInputStream(bis);
        try {
            hash.readExternal(in);
        } catch (IOException | ClassNotFoundException e) {
            fail("Exception thrown");
        }
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }
}