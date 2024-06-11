import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    private TFloatDoubleHash hash;
    
    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, hash.getNoEntryValue(), 0);
    }
    
    @Test
    public void testSetUp() {
        assertEquals(16, hash.setUp(16));
    }
    
    @Test
    public void testContains() {
        assertFalse(hash.contains(0.0f));
        hash.insertKey(0.0f);
        assertTrue(hash.contains(0.0f));
    }
    
    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0.0;
            
            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(3.0, procedure.sum, 0);
    }
    
    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        int indexToRemove = hash.insertKey(3.0f);
        assertEquals(3, hash._set[indexToRemove], 0);
        
        hash.removeAt(indexToRemove);
        assertEquals(hash.getNoEntryKey(), hash._set[indexToRemove], 0);
    }
    
    @Test
    public void testIndex() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        int index = hash.index(2.0f);
        assertEquals(1, index);
        
        index = hash.index(3.0f);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(1.0f));
        
        int index = hash.insertKey(1.0f);
        assertEquals(-1, index);
        
        index = hash.insertKey(2.0f);
        assertEquals(2, index);
    }
    
    @Test
    public void testXinsertKey() {
        assertEquals(0, hash.XinsertKey(1.0f));
        
        int index = hash.XinsertKey(1.0f);
        assertEquals(-1, index);
        
        index = hash.XinsertKey(2.0f);
        assertEquals(2, index);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashFull() {
        for (int i = 0; i < 16; i++) {
            hash.insertKey(i);
        }
        
        hash.insertKeyRehash(16.0f, 0, 0, (byte)0);
    }
    
    @Test
    public void testWriteAndReadExternal() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        hash.writeExternal(oos);
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        
        TFloatDoubleHash fromExternal = new TFloatDoubleHash();
        fromExternal.readExternal(ois);
        
        assertEquals(hash.getNoEntryKey(), fromExternal.getNoEntryKey(), 0);
        assertEquals(hash.getNoEntryValue(), fromExternal.getNoEntryValue(), 0);
    }
}