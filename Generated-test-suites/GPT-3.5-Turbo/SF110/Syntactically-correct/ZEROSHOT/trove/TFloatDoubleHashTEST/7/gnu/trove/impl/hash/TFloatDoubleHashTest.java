import org.junit.*;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    
    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.no_entry_key = 4.5f;
        
        float result = hash.getNoEntryKey();
        
        assertEquals(4.5f, result, 0.0);
    }
    
    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.no_entry_value = 6.7;
        
        double result = hash.getNoEntryValue();
        
        assertEquals(6.7, result, 0.0);
    }
    
    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        int result = hash.setUp(10);
        
        assertEquals(10, result);
        assertEquals(10, hash._set.length);
    }
    
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        
        boolean result = hash.contains(2.0f);
        
        assertTrue(result);
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._states = new byte[] {1, 1, 1};
        hash._set = new float[] {1.1f, 2.2f, 3.3f};
        
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float val) {
                if (val == 2.2f) {
                    return false;
                }
                return true;
            }
        };
        
        boolean result = hash.forEach(procedure);
        
        assertFalse(result);
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        hash._states = new byte[] {1, 1, 1};
        
        hash.removeAt(1);
        
        assertEquals(0.0f, hash._set[1], 0.0);
        assertEquals(2, hash._states[1]);
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        hash._states = new byte[] {1, 1, 1};
        
        int result = hash.index(2.0f);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._set = new float[] {1.0f, 2.0f};
        hash._states = new byte[] {1, 1};
        
        int result = hash.indexRehashed(2.0f, 1, 1234, (byte)1);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._states = new byte[] {0, 0, 0, 0};
        hash._set = new float[] {0.0f, 0.0f, 0.0f, 0.0f};
        
        int result = hash.insertKey(2.0f);
        
        assertEquals(1, result);
        assertEquals(2.0f, hash._set[1], 0.0);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._states = new byte[] {1, 2, 0, 0};
        hash._set = new float[] {1.0f, 2.0f, 0.0f, 0.0f};
        
        int result = hash.insertKeyRehash(3.0f, 1, 1234, (byte)2);
        
        assertEquals(3, result);
        assertEquals(3.0f, hash._set[3], 0.0);
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._states = new byte[] {0, 0, 0, 0};
        hash._set = new float[] {0.0f, 0.0f, 0.0f, 0.0f};
        
        hash.insertKeyAt(1, 2.0f);
        
        assertEquals(2.0f, hash._set[1], 0.0);
        assertEquals(1, hash._states[1]);
    }
    
    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash._states = new byte[] {0, 0, 0, 0};
        hash._set = new float[] {0.0f, 0.0f, 0.0f, 0.0f};
        
        int result = hash.XinsertKey(2.0f);
        
        assertEquals(1, result);
        assertEquals(2.0f, hash._set[1], 0.0);
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        
        hash.writeExternal(out);
        out.flush();
        byte[] byteArray = baos.toByteArray();
        
        assertArrayEquals(new byte[] {0, 0, 0, 0, 0, 0, 0}, byteArray);
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {0, 0, 0, 0, 0, 0, 0});
        ObjectInput in = new ObjectInputStream(bais);
        
        hash.readExternal(in);
        
        assertEquals(0.0f, hash.no_entry_key, 0.0);
        assertEquals(0.0, hash.no_entry_value, 0.0);
    }
    
}