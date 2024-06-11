package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TCharHashTest {
    @Test
    public void testConstructor() {
        TCharHash hash = new TCharHash();
        assertEquals(0, hash.size());
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TCharHash(10);
        assertEquals(0, hash.size());
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TCharHash(10, 0.5f);
        assertEquals(0, hash.size());
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TCharHash(10, 0.5f, 'x');
        assertEquals(0, hash.size());
        assertEquals('x', hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TCharHash hash = new TCharHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertEquals(10, hash._set.length);
        
        hash = new TCharHash(5);
        capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertEquals(10, hash._set.length);
    }
    
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        
        assertTrue(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertFalse(hash.contains('c'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        
        TCharProcedure procedure = new TCharProcedure() {
            StringBuilder sb = new StringBuilder();
            
            @Override
            public boolean execute(char value) {
                sb.append(value);
                return true;
            }
            
            public String toString() {
                return sb.toString();
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals("ab", procedure.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        
        hash.removeAt(0);
        
        assertFalse(hash.contains('a'));
        assertTrue(hash.contains('b'));
        assertEquals('\0', hash._set[0]);
        assertEquals(1, hash.size());
    }
    
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        
        assertEquals(0, hash.index('a'));
        assertEquals(1, hash.index('b'));
        assertEquals(-1, hash.index('c'));
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        
        assertEquals(0, hash.indexRehashed('a', 0, 0, (byte) 1));
        assertEquals(1, hash.indexRehashed('b', 1, 0, (byte) 1));
        assertEquals(-1, hash.indexRehashed('c', 0, 0, (byte) 0));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        int index = hash.insertKey('a');
        
        assertEquals(0, index);
        assertTrue(hash.contains('a'));
        assertEquals(1, hash.size());
        
        index = hash.insertKey('b');
        
        assertEquals(1, index);
        assertTrue(hash.contains('b'));
        assertEquals(2, hash.size());
        
        index = hash.insertKey('a');
        
        assertEquals(-1, index);
        assertTrue(hash.contains('a'));
        assertEquals(2, hash.size());
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        
        assertEquals(0, hash.insertKeyRehash('a', 0, 0, (byte) 1));
        assertEquals(1, hash.insertKeyRehash('b', 1, 0, (byte) 1));
        assertEquals(-1, hash.insertKeyRehash('d', 0, 0, (byte) 0));
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        
        hash.insertKeyAt(0, 'a');
        assertTrue(hash.contains('a'));
        assertEquals(1, hash.size());
        
        hash.insertKeyAt(1, 'b');
        assertTrue(hash.contains('b'));
        assertEquals(2, hash.size());
    }
}