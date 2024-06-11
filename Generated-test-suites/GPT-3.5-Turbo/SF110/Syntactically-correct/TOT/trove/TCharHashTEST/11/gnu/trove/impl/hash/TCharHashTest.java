import org.junit.Test;

import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testConstructor1() {
        TCharHash hash = new TCharHash();
        
        char noEntryValue = hash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
        
        assertFalse(hash.contains('a'));
    }
    
    @Test
    public void testConstructor2() {
        TCharHash hash = new TCharHash(10);
        
        char noEntryValue = hash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
        
        assertFalse(hash.contains('a'));
    }
    
    @Test
    public void testConstructor3() {
        TCharHash hash = new TCharHash(10, 0.75f);
        
        char noEntryValue = hash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
        
        assertFalse(hash.contains('a'));
    }
    
    @Test
    public void testConstructor4() {
        char noEntryValue = 'X';
        TCharHash hash = new TCharHash(10, 0.75f, noEntryValue);
        
        char retrievedNoEntryValue = hash.getNoEntryValue();
        assertEquals(noEntryValue, retrievedNoEntryValue);
        
        assertFalse(hash.contains('a'));
    }
    
    @Test
    public void testSetUp() {
        TCharHash hash = new TCharHash();
        
        int capacity = hash.setUp(100);
        assertEquals(100, capacity);
        
        hash.insertKey('a');
        assertTrue(hash.contains('a'));
    }
    
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        
        hash.insertKey('a');
        assertTrue(hash.contains('a'));
        
        assertFalse(hash.contains('b'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        
        hash.insertKey('a');
        hash.insertKey('b');
        
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                // Perform some action or assertion here
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        
        hash.insertKey('a');
        assertTrue(hash.contains('a'));
        
        hash.removeAt(0);
        assertFalse(hash.contains('a'));
    }
    
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        
        hash.insertKey('a');
        int index = hash.index('a');
        assertEquals(0, index);
        
        int nonExistentIndex = hash.index('b');
        assertEquals(-1, nonExistentIndex);
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        
        hash.insertKey('a');
        int index = hash.indexRehashed('a', 0, 10, (byte)1);
        assertEquals(0, index);
        
        int nonExistentIndex = hash.indexRehashed('b', 0, 10, (byte)1);
        assertEquals(-1, nonExistentIndex);
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        
        int index = hash.insertKey('a');
        assertEquals(0, index);
        assertTrue(hash.contains('a'));
        
        int existingIndex = hash.insertKey('a');
        assertEquals(-1, existingIndex);
        
        hash.removeAt(0);
        int newIndex = hash.insertKey('a');
        assertEquals(0, newIndex);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash();
        
        int index = hash.insertKeyRehash('a', 0, 10, (byte)1);
        assertEquals(0, index);
        assertTrue(hash.contains('a'));
        
        int existingIndex = hash.insertKeyRehash('a', 0, 10, (byte)1);
        assertEquals(-1, existingIndex);
        
        hash.removeAt(0);
        int newIndex = hash.insertKeyRehash('a', 0, 10, (byte)1);
        assertEquals(0, newIndex);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashNoSlotsAvailable() {
        TCharHash hash = new TCharHash(1);
        
        hash.insertKeyRehash('a', 0, 10, (byte)1);
        hash.insertKeyRehash('b', 0, 10, (byte)1);
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        
        hash.insertKeyAt(0, 'a');
        assertTrue(hash.contains('a'));
    }
}