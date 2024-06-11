package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TCharHashTest {
    
    @Test
    public void testConstructor() {
        TCharHash hash = new TCharHash();
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash._set.length);
        assertTrue(hash.consumeFreeSlot);
        
        hash = new TCharHash(20);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(20, hash._set.length);
        assertTrue(hash.consumeFreeSlot);
        
        hash = new TCharHash(30, 0.75f);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(30, hash._set.length);
        assertTrue(hash.consumeFreeSlot);
        
        hash = new TCharHash(40, 0.75f, 'x');
        assertEquals('x', hash.getNoEntryValue());
        assertEquals(40, hash._set.length);
        assertTrue(hash.consumeFreeSlot);
    }
    
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{};
        hash._states = new byte[]{};
        assertTrue(!hash.contains('a'));
        
        hash._set = new char[]{'a'};
        hash._states = new byte[]{1};
        assertTrue(hash.contains('a'));
        
        hash._set = new char[]{'a', 'b'};
        hash._states = new byte[]{1, 0};
        assertTrue(hash.contains('a'));
        
        hash._set = new char[]{'a', 'b', 'c'};
        hash._states = new byte[]{1, 1, 0};
        assertTrue(hash.contains('a'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{'a', 'b', 'c'};
        hash._states = new byte[]{1, 2, 1};
        
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                return false;
            }
        };
        
        assertTrue(!hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{'a'};
        hash._states = new byte[]{1};
        
        hash.removeAt(0);
        assertEquals('\0', hash._set[0]);
    }
    
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{'a', 'b'};
        hash._states = new byte[]{1, 0};
        
        assertEquals(0, hash.index('a'));
        assertEquals(-1, hash.index('c'));
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{'a', 'b'};
        hash._states = new byte[]{1, 2};
        
        int result = hash.indexRehashed('a', 0, 1234, (byte)1);
        assertEquals(0, result);
        
        result = hash.indexRehashed('b', 0, 1234, (byte)2);
        assertEquals(0, result);
        
        result = hash.indexRehashed('c', 0, 1234, (byte)0);
        assertEquals(-1, result);
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{};
        hash._states = new byte[]{};
        
        int result = hash.insertKey('a');
        assertEquals(0, result);
        assertEquals('a', hash._set[0]);
        assertEquals((byte)1, hash._states[0]);
        
        result = hash.insertKey('b');
        assertEquals(1, result);
        assertEquals('b', hash._set[1]);
        assertEquals((byte)1, hash._states[1]);
        
        result = hash.insertKey('a');
        assertEquals(-1, result);
        
        result = hash.insertKey('c');
        assertEquals(2, result);
        assertEquals('c', hash._set[2]);
        assertEquals((byte)1, hash._states[2]);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{'a', 'b'};
        hash._states = new byte[]{1, 0};
        
        int result = hash.insertKeyRehash('c', 0, 1234, (byte)1);
        assertEquals(2, result);
        assertEquals('c', hash._set[2]);
        assertEquals((byte)1, hash._states[2]);
        
        result = hash.insertKeyRehash('a', 0, 1234, (byte)1);
        assertEquals(1, result);
        
        result = hash.insertKeyRehash('d', 0, 1234, (byte)0);
        assertEquals(2, result);
        assertEquals('d', hash._set[2]);
        assertEquals((byte)1, hash._states[2]);
        
        result = hash.insertKeyRehash('e', 0, 1234, (byte)1);
        assertEquals(3, result);
        assertEquals('e', hash._set[3]);
        assertEquals((byte)1, hash._states[3]);
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        hash._set = new char[]{};
        hash._states = new byte[]{};
        
        hash.insertKeyAt(0, 'a');
        assertEquals('a', hash._set[0]);
        assertEquals((byte)1, hash._states[0]);
    }
}