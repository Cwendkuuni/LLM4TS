package gnu.trove.impl.hash;

import org.junit.*;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        // Test empty hash
        assertFalse(hash.contains(0.0f));
        
        // Test non-empty hash
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(0.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        final StringBuilder sb = new StringBuilder();
        
        // Test empty hash
        assertTrue(hash.forEach(new TFloatProcedure() {
            public boolean execute(float value) {
                sb.append(value);
                return true;
            }
        }));
        assertEquals("", sb.toString());
        
        // Test non-empty hash
        hash.insertKey(1.0f);
        assertTrue(hash.forEach(new TFloatProcedure() {
            public boolean execute(float value) {
                sb.append(value);
                return true;
            }
        }));
        assertEquals("1.0", sb.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        // Test removing from empty hash
        hash.removeAt(0);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        
        // Test removing from non-empty hash
        hash.insertKey(1.0f);
        hash.removeAt(0);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        // Test empty hash
        assertEquals(-1, hash.index(1.0f));
        
        // Test non-empty hash
        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(0.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        // Test empty hash
        assertEquals(-1, hash.indexRehashed(1.0f, 0, 0, (byte)0));
        
        // Test non-empty hash
        hash.insertKey(1.0f);
        assertEquals(0, hash.indexRehashed(1.0f, 0, 0, (byte)1));
        assertEquals(-1, hash.indexRehashed(1.0f, 0, 0, (byte)2));
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        // Test inserting into empty hash
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(0, hash.index(1.0f));
        
        // Test inserting existing key
        assertEquals(-1, hash.insertKey(1.0f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        // Test inserting into empty hash
        assertEquals(0, hash.insertKeyRehash(1.0f, 0, 0, (byte)0));
        assertEquals(0, hash.index(1.0f));
        
        // Test inserting existing key
        assertEquals(-1, hash.insertKeyRehash(1.0f, 0, 0, (byte)1));
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        // Test inserting at index 0
        hash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, hash._set[0], 0.001);
        assertEquals((byte)1, hash._states[0]);
    }
    
    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        // Test inserting into empty hash
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(0, hash.index(1.0f));
        
        // Test inserting existing key
        assertEquals(-1, hash.XinsertKey(1.0f));
    }
    
    @Test
    public void testWriteExternal() throws Exception {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        hash.writeExternal(oos);
        oos.flush();
        byte[] output = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(output);
        ObjectInputStream ois = new ObjectInputStream(bais);
        
        assertEquals(0, ois.readByte());
        ois.close();
    }
    
    @Test
    public void testReadExternal() throws Exception {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{0});
        ObjectInputStream ois = new ObjectInputStream(bais);
        
        hash.readExternal(ois);
        assertEquals(0, hash.getNoEntryKey(), 0.001);
        ois.close();
    }
}