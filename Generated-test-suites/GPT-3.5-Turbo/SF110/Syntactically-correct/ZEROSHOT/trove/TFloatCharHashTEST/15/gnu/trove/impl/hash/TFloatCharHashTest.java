import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
    }
    
    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
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
            float sum = 0;
            
            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(6.0f, procedure.sum, 0.0);
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertTrue(hash.contains(1.0f));
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }
    
    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(3.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertEquals(1, hash.indexRehashed(2.0f, 1, 12345, (byte)1));
        assertEquals(-1, hash.indexRehashed(3.0f, 0, 54321, (byte)0));
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(-1, hash.insertKey(1.0f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertEquals(0, hash.insertKeyRehash(2.0f, 5, 12345, (byte)1));
        assertEquals(2, hash.insertKeyRehash(3.0f, 0, 54321, (byte)0));
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 1.0f);
        hash.insertKeyAt(1, 2.0f);
        
        assertEquals(1.0, hash._set[0], 0.0);
        assertEquals(2.0, hash._set[1], 0.0);
        assertEquals(1, hash._states[0]);
        assertEquals(1, hash._states[1]);
    }
    
    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(-1, hash.XinsertKey(1.0f));
    }
    
    @Test
    public void testWriteExternal() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            hash.writeExternal(oos);
            oos.flush();
            
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            
            TFloatCharHash newHash = new TFloatCharHash();
            newHash.readExternal(ois);
            
            assertEquals(hash.getNoEntryKey(), newHash.getNoEntryKey(), 0.0);
            assertEquals(hash.getNoEntryValue(), newHash.getNoEntryValue());
            assertEquals(hash.index(1.0f), newHash.index(1.0f));
            assertEquals(hash.index(2.0f), newHash.index(2.0f));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}