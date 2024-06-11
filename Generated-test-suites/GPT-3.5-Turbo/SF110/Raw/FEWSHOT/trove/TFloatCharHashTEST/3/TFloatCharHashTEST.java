package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TFloatCharHashTest {
    @Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
        
        TFloatCharHash hash2 = new TFloatCharHash(10);
        assertEquals(0.0f, hash2.getNoEntryKey(), 0.001);
        assertEquals('\0', hash2.getNoEntryValue());
        
        TFloatCharHash hash3 = new TFloatCharHash(10, 0.75f);
        assertEquals(0.0f, hash3.getNoEntryKey(), 0.001);
        assertEquals('\0', hash3.getNoEntryValue());
        
        TFloatCharHash hash4 = new TFloatCharHash(10, 0.75f, 1.5f, 'a');
        assertEquals(1.5f, hash4.getNoEntryKey(), 0.001);
        assertEquals('a', hash4.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertEquals(10, hash._set.length);
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
        hash.insertKey(4.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0;
            
            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(10.0f, procedure.sum, 0.001);
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(0);
        
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }
    
    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(3.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        assertEquals(0, hash.indexRehashed(1.0f, 0, 0, (byte)1));
        assertEquals(1, hash.indexRehashed(2.0f, 1, 0, (byte)1));
        assertEquals(-1, hash.indexRehashed(3.0f, 2, 0, (byte)2));
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        assertEquals(2, hash.size());
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        
        assertEquals(4, hash.size());
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 1.0f);
        hash.insertKeyAt(1, 2.0f);
        
        assertEquals(2, hash.size());
    }
}