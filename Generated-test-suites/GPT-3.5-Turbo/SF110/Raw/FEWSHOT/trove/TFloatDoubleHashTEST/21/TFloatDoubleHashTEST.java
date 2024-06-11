package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TFloatDoubleHashTest {
    
    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        
        hash = new TFloatDoubleHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        
        hash = new TFloatDoubleHash(10, 0.5f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        
        hash = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0);
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(2.0, hash.getNoEntryValue(), 0.0);
    }
    
    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        assertEquals(10, hash._set.length);
    }
    
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertFalse(hash.contains(4.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        
        TFloatProcedure procedure = new TFloatProcedure() {
            private boolean executed = false;
            
            @Override
            public boolean execute(float value) {
                executed = true;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertTrue(procedure.executed);
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        hash._states = new byte[] {1, 1, 1};
        
        hash.removeAt(1);
        
        assertTrue(hash._set[1] == hash.getNoEntryKey());
        assertTrue(hash._states[1] == 0);
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        hash._states = new byte[] {1, 2, 1};
        
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(4.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        hash._states = new byte[] {1, 2, 1};
        
        int index = hash.indexRehashed(2.0f, 1, 1, (byte) 2);
        
        assertEquals(1, index);
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        hash._states = new byte[] {1, 2, 1};
        
        int index = hash.insertKey(4.0f);
        
        assertEquals(1, index);
        assertEquals(4.0f, hash._set[1], 0.0);
        assertEquals((byte) 1, hash._states[1]);
        
        index = hash.insertKey(2.0f);
        
        assertEquals(-2, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        hash._states = new byte[] {1, 2, 1};
        
        int index = hash.insertKeyRehash(4.0f, 1, 1, (byte) 2);
        
        assertEquals(1, index);
        
        index = hash.insertKeyRehash(2.0f, 1, 1, (byte) 1);
        
        assertEquals(-2, index);
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[3];
        hash._states = new byte[3];
        
        hash.insertKeyAt(1, 2.0f);
        
        assertEquals(2.0f, hash._set[1], 0.0);
        assertEquals((byte) 1, hash._states[1]);
    }
    
    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        hash._states = new byte[] {1, (byte) 2, 1};
        
        int index = hash.XinsertKey(4.0f);
        
        assertEquals(1, index);
        assertEquals(4.0f, hash._set[1], 0.0);
        assertEquals((byte) 1, hash._states[1]);
        
        index = hash.XinsertKey(2.0f);
        
        assertEquals(-2, index);
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.no_entry_key = 1.0f;
        hash.no_entry_value = 2.0;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        hash.writeExternal(oos);
        oos.close();
        
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        byte version = ois.readByte();
        assertEquals(0, version);
        hash.readExternal(ois);
        
        assertEquals(1.0f, hash.no_entry_key, 0.0);
        assertEquals(2.0, hash.no_entry_value, 0.0);
    }

    // Additional test cases for other methods can be added here

}