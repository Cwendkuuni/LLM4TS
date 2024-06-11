package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TFloatDoubleHashTest {
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
        assertEquals(0, hash.setUp(10));
    }
    
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(0.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertTrue(hash.forEach(new TFloatProcedure() {
            public boolean execute(float val) {
                return true;
            }
        }));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.removeAt(0);
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(-1, hash.index(0.0f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.insertKey(0.0f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.insertKeyRehash(0.0f, 0, 0, (byte) 0));
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKeyAt(0, 0.0f);
    }
    
    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.XinsertKey(0.0f));
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outStream);
        hash.writeExternal(out);
        out.flush();
        byte[] bytes = outStream.toByteArray();
        assertEquals(14, bytes.length);
    }
    
    @Test
    public void testReadExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        byte[] bytes = new byte[] {0, -84, -19, 0, 5, 115, 114, 0, 33, 103, 110, 117, 46, 116, 114, 111, 118, 101, 46, 105, 109, 112, 108, 46, 104, 97, 115, 104, 46, 84, 70, 108, 111, 97, 116, 68, 111, 117, 98, 108, 101, 72, 97, 115, 104, -33, -9, -99, 64, 12, 0, 0, 120, 112, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0};
        ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(inStream);
        hash.readExternal(in);
        assertEquals(0.0f, hash.no_entry_key, 0.0);
        assertEquals(0.0, hash.no_entry_value, 0.0);
    }
}