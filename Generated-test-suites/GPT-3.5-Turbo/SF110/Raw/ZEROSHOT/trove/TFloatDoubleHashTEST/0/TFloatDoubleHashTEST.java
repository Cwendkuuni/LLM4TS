import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    
    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        
        hash = new TFloatDoubleHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        
        hash = new TFloatDoubleHash(10, 0.5f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        
        hash = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0);
        assertEquals(1.0f, hash.getNoEntryKey(), 0.001);
    }
    
    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0, hash.getNoEntryValue(), 0.001);
        
        hash = new TFloatDoubleHash(10);
        assertEquals(0.0, hash.getNoEntryValue(), 0.001);
        
        hash = new TFloatDoubleHash(10, 0.5f);
        assertEquals(0.0, hash.getNoEntryValue(), 0.001);
        
        hash = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0);
        assertEquals(2.0, hash.getNoEntryValue(), 0.001);
    }
    
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(1.0f));
        
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        
        assertFalse(hash.contains(2.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0.0;
            
            public boolean execute(float value) {
                sum += value;
                return true;
            }
            
            public double getResult() {
                return sum;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(3.0, procedure.getResult(), 0.001);
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        assertEquals(1.0f, hash._set[0], 0.001);
        assertEquals(2.0f, hash._set[1], 0.001);
        
        hash.removeAt(0);
        assertEquals(0.0f, hash._set[0], 0.001);
        assertEquals(2.0f, hash._set[1], 0.001);
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(3.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        assertEquals(0, hash.indexRehashed(1.0f, 0, 1, (byte) 1));
        assertEquals(1, hash.indexRehashed(2.0f, 1, 2, (byte) 1));
        assertEquals(-1, hash.indexRehashed(3.0f, 0, 3, (byte) 1));
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        assertEquals(0, hash.insertKeyRehash(1.0f, 0, 1, (byte) 1));
        assertEquals(1, hash.insertKeyRehash(2.0f, 1, 2, (byte) 1));
        assertEquals(1, hash.insertKeyRehash(2.0f, 0, 3, (byte) 2));
        assertEquals(2, hash.insertKeyRehash(3.0f, 2, 4, (byte) 1));
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        hash.insertKeyAt(0, 1.0f);
        hash.insertKeyAt(1, 2.0f);
        
        assertEquals(1.0f, hash._set[0], 0.001);
        assertEquals(2.0f, hash._set[1], 0.001);
    }
    
    @Test
    public void testXInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(1, hash.XinsertKey(2.0f));
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        hash.writeExternal(out);
        out.flush();
        
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInput in = new ObjectInputStream(bis);
        TFloatDoubleHash newHash = new TFloatDoubleHash();
        newHash.readExternal(in);
        
        assertEquals(0.0f, newHash.getNoEntryKey(), 0.001);
        assertEquals(0.0, newHash.getNoEntryValue(), 0.001);
    }
}