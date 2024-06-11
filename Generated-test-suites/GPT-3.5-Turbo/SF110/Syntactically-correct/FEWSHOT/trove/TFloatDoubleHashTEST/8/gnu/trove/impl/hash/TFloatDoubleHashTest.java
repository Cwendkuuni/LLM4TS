package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TFloatDoubleHashTest {
    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        
        hash = new TFloatDoubleHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        
        hash = new TFloatDoubleHash(10, 0.5f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        
        hash = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0);
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0f);
    }
    
    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        
        hash = new TFloatDoubleHash(10);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        
        hash = new TFloatDoubleHash(10, 0.5f);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        
        hash = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0);
        assertEquals(2.0, hash.getNoEntryValue(), 0.0);
    }
    
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        assertFalse(hash.contains(1.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        assertFalse(hash.forEach((float val) -> { return true; }));
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        assertEquals(-1, hash.index(1.0f));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        hash.removeAt(0);
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        assertEquals(0, hash.insertKey(1.0f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        assertEquals(0, hash.insertKeyRehash(1.0f, 0, 0, (byte) 0));
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        hash.insertKeyAt(0, 1.0f);
    }
    
    @Test
    public void testWriteExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        hash.writeExternal(null);
    }
    
    @Test
    public void testReadExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        hash.readExternal(null);
    }
}