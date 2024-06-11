import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {
    
    private TFloatCharHash hash;
    
    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }
    
    @Test
    public void testContains() {
        assertFalse(hash.contains(1.0f));
        
        hash.insertKey(1.0f);
        
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }
    
    @Test
    public void testForEach() {
        final float[] values = {1.0f, 2.0f, 3.0f, 4.0f};
        
        for (float value : values) {
            hash.insertKey(value);
        }
        
        final float[] sum = {0.0f};
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                sum[0] += value;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(10.0f, sum[0], 0.0f);
    }
    
    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        
        assertTrue(hash.contains(1.0f));
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }
    
    @Test
    public void testIndex() {
        hash.insertKey(1.0f);
        
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(2.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        assertEquals(1, hash.indexRehashed(2.0f, 1, 2, (byte)1));
        assertEquals(2, hash.indexRehashed(3.0f, 1, 3, (byte)1));
        assertEquals(-1, hash.indexRehashed(4.0f, 1, 4, (byte)1));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
        assertEquals(-1, hash.insertKey(1.0f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        assertEquals(1, hash.insertKeyRehash(2.0f, 1, 2, (byte)1));
        assertEquals(2, hash.insertKeyRehash(3.0f, 1, 3, (byte)1));
        assertEquals(3, hash.insertKeyRehash(4.0f, 1, 4, (byte)1));
    }
    
    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, hash._set[0], 0.0f);
        assertEquals(1, hash._states[0]);
        
        hash.insertKeyAt(1, 2.0f);
        assertEquals(2.0f, hash._set[1], 0.0f);
        assertEquals(1, hash._states[1]);
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testWriteExternal() {
        // TODO: Implement test case
    }
    
    @Test
    public void testReadExternal() {
        // TODO: Implement test case
    }
}