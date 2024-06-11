import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.01f);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0, hash.getNoEntryValue(), 0.01);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int capacity = hash.setUp(100);
        assertEquals(100, capacity);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(4.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            int count = 0;
            public boolean execute(float value) {
                count++;
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(3, procedure.count);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.removeAt(1);
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(4.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertTrue(hash.insertKey(1.0f) >= 0);
        assertEquals(-1, hash.insertKey(1.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertTrue(hash.insertKeyRehash(1.0f, 0, 0, (byte) 0) >= 0);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertTrue(hash.XinsertKey(1.0f) >= 0);
        assertEquals(-1, hash.XinsertKey(1.0f));
    }

    @Test
    public void testWriteExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(baos);
            hash.writeExternal(out);
            out.flush();
            byte[] data = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInput in = new ObjectInputStream(bais);
            TFloatDoubleHash newHash = new TFloatDoubleHash();
            newHash.readExternal(in);
            assertEquals(hash.getNoEntryKey(), newHash.getNoEntryKey(), 0.01f);
            assertEquals(hash.getNoEntryValue(), newHash.getNoEntryValue(), 0.01);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}