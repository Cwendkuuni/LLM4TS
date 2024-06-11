
package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatDoubleHashTest {
    
    @Test
    public void testConstructor1() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        // Test initial capacity
        assertEquals(10, hash.capacity());
        
        // Test load factor
        assertEquals(0.5f, hash.loadFactor(), 0.001);
        
        // Test no entry key
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        
        // Test no entry value
        assertEquals(0.0, hash.getNoEntryValue(), 0.001);
    }
    
    @Test
    public void testConstructor2() {
        TFloatDoubleHash hash = new TFloatDoubleHash(20);
        
        // Test initial capacity
        assertEquals(20, hash.capacity());
        
        // Test load factor
        assertEquals(0.5f, hash.loadFactor(), 0.001);
        
        // Test no entry key
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        
        // Test no entry value
        assertEquals(0.0, hash.getNoEntryValue(), 0.001);
    }
    
    @Test
    public void testConstructor3() {
        TFloatDoubleHash hash = new TFloatDoubleHash(20, 0.8f);
        
        // Test initial capacity
        assertEquals(20, hash.capacity());
        
        // Test load factor
        assertEquals(0.8f, hash.loadFactor(), 0.001);
        
        // Test no entry key
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        
        // Test no entry value
        assertEquals(0.0, hash.getNoEntryValue(), 0.001);
    }
    
    @Test
    public void testConstructor4() {
        TFloatDoubleHash hash = new TFloatDoubleHash(20, 0.8f, 1.0f, 2.0);
        
        // Test initial capacity
        assertEquals(20, hash.capacity());
        
        // Test load factor
        assertEquals(0.8f, hash.loadFactor(), 0.001);
        
        // Test no entry key
        assertEquals(1.0f, hash.getNoEntryKey(), 0.001);
        
        // Test no entry value
        assertEquals(2.0, hash.getNoEntryValue(), 0.001);
    }
    
    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        int capacity = hash.setUp(20);
        
        // Test capacity
        assertEquals(20, capacity);
        
        // Test _set array size
        assertEquals(20, hash._set.length);
    }
    
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(20);
        
        hash.insertKey(1.0f);
        
        // Test negative case
        assertFalse(hash.contains(2.0f));
        
        // Test positive case
        assertTrue(hash.contains(1.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(20);
        
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        // Test procedure execution
        hash.forEach(value -> {
            assertTrue(value == 1.0f || value == 2.0f);
            return true;
        });
        
        // Test procedure termination
        hash.forEach(value -> {
            assertTrue(value == 1.0f || value == 2.0f);
            return false;
        });
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(20);
        
        hash.insertKey(1.0f);
        
        // Test remove key
        hash.removeAt(0);
        
        // Test no entry key
        assertEquals(0.0f, hash._set[0], 0.001);
        
        // Test remove state
        assertEquals(0, hash._states[0]);
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(20);
        
        hash.insertKey(1.0f);
        
        // Test key not found
        assertEquals(-1, hash.index(2.0f));
        
        // Test key found
        assertEquals(0, hash.index(1.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash(20);
        
        hash.insertKey(1.0f);
        
        // Test key not found
        assertEquals(-1, hash.indexRehashed(2.0f, 0, 1, (byte)1));
        
        // Test key found
        assertEquals(0, hash.indexRehashed(1.0f, 0, 1, (byte)1));
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(20);
        
        // Test insert key
        assertEquals(0, hash.insertKey(1.0f));
        
        // Test insert key already present
        assertEquals(-1, hash.insertKey(1.0f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(20);
        
        // Test insert key
        assertEquals(0, hash.insertKeyRehash(1.0f, 0, 1, (byte)1));
        
        // Test no free slot
        try {
            hash.insertKeyRehash(2.0f, 0, 1, (byte)1);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(20);
        
        // Test insert key
        hash.insertKeyAt(0, 1.0f);
        
        // Test key
        assertEquals(1.0f, hash._set[0], 0.001);
        
        // Test state
        assertEquals(1, hash._states[0]);
    }
    
    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(20);
        
        // Test Xinsert key
        assertEquals(0, hash.XinsertKey(1.0f));
        
        // Test Xinsert key already present
        assertEquals(-1, hash.XinsertKey(1.0f));
    }
    
    @Test
    public void testWriteExternal() {
        // Not tested
    }
    
    @Test
    public void testReadExternal() {
        // Not tested
    }
}
