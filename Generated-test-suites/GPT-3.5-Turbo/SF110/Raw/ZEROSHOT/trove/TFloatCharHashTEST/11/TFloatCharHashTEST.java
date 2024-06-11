import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testTFloatCharHash() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testTFloatCharHash_initialCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testTFloatCharHash_initialCapacity_loadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testTFloatCharHash_initialCapacity_loadFactor_noEntryKey_noEntryValue() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f, 1.0f, 'a');
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(1.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('a', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        float[] set = hash._set;
        assertNotNull(set);
        assertEquals(10, set.length);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0.0f;
            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(6.0f, procedure.sum, 0.001);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertFalse(hash.contains(3.0f));
        hash.removeAt(1);
        assertFalse(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        int index = hash.index(1.0f);
        assertEquals(0, index);
        index = hash.index(2.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        int index = hash.indexRehashed(2.0f, 0, 0, (byte) 0);
        assertEquals(1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.consumeFreeSlot);
        index = hash.insertKey(1.0f);
        assertEquals(-1, index);
        assertFalse(hash.consumeFreeSlot);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        int index = hash.insertKeyRehash(2.0f, 0, 0, (byte) 2);
        assertEquals(1, index);
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.consumeFreeSlot);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 1.0f);
        assertTrue(hash.contains(1.0f));
        assertEquals(1, hash._states[0]);
        assertEquals(1.0f, hash._set[0], 0.001);
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.consumeFreeSlot);
        index = hash.XinsertKey(1.0f);
        assertEquals(-1, index);
        assertFalse(hash.consumeFreeSlot);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        hash.writeExternal(out);
        out.flush();
        byte[] bytes = byteOut.toByteArray();
        assertNotNull(bytes);
        assertFalse(bytes.length == 0);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        hash.writeExternal(out);
        out.flush();
        byte[] bytes = byteOut.toByteArray();
        assertNotNull(bytes);
        assertFalse(bytes.length == 0);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(byteIn);
        hash.readExternal(in);
        assertTrue(hash.contains(1.0f));
    }
}