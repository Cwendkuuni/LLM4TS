package gnu.trove.impl.hash;

import static org.junit.Assert.*;
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
        hash.insertKey(1.0f);
        
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.removeAt(0);
        
        assertFalse(hash.contains(1.0f));
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(2.0f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int index = hash.insertKey(1.0f);
        
        assertEquals(0, index);
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        hash.insertKey(1.0f);
        int index = hash.indexRehashed(1.0f, 0, 0, (byte) 1);
        
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        int index = hash.insertKeyRehash(1.0f, 0, 0, (byte) 1);
        
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        hash.insertKeyAt(0, 1.0f);
        
        assertTrue(hash.contains(1.0f));
    }
    
    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int index = hash.XinsertKey(1.0f);
        
        assertEquals(0, index);
    }
    
    @Test
    public void testWriteExternal() {
        // TODO
    }
    
    @Test
    public void testReadExternal() {
        // TODO
    }
}