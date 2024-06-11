package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TCharHashTest {
    
    @Test
    public void testConstructor() {
        TCharHash hash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        
        hash = new TCharHash(10);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        
        hash = new TCharHash(10, 0.5f);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        
        char noEntryValue = '!';
        hash = new TCharHash(10, 0.5f, noEntryValue);
        assertEquals(noEntryValue, hash.getNoEntryValue());
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
        
        TCharProcedure proc = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                if (value == 'a') {
                    return true;
                } else if (value == 'b') {
                    return false;
                }
                return false;
            }
        };
        
        assertFalse(hash.forEach(proc));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        
        assertEquals('a', hash._set[0]);
        assertEquals('b', hash._set[1]);
        assertEquals(1, hash._states[0]);
        assertEquals(1, hash._states[1]);
        
        hash.removeAt(0);
        
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash._set[0]);
        assertEquals('b', hash._set[1]);
        assertEquals(0, hash._states[0]);
        assertEquals(1, hash._states[1]);
    }
    
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        
        assertEquals(0, hash.index('a'));
        assertEquals(-1, hash.index('b'));
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        
        assertEquals(0, hash.indexRehashed('a', 0, HashFunctions.hash('a') & Integer.MAX_VALUE, (byte)1));
        assertEquals(1, hash.indexRehashed('b', 0, HashFunctions.hash('a') & Integer.MAX_VALUE, (byte)1));
        assertEquals(-1, hash.indexRehashed('c', 0, HashFunctions.hash('a') & Integer.MAX_VALUE, (byte)1));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        
        assertEquals(0, hash.insertKey('a'));
        assertEquals(-1, hash.insertKey('a'));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash(3);
        
        hash.insertKey('a');
        hash.insertKey('b');
        
        assertEquals(0, hash.insertKeyRehash('a', 0, HashFunctions.hash('a') & Integer.MAX_VALUE, (byte)1));
        assertEquals(1, hash.insertKeyRehash('b', 0, HashFunctions.hash('a') & Integer.MAX_VALUE, (byte)1));
        assertEquals(-1, hash.insertKeyRehash('c', 0, HashFunctions.hash('a') & Integer.MAX_VALUE, (byte)1));
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        hash.insertKeyAt(0, 'a');
        
        assertEquals('a', hash._set[0]);
        assertEquals(1, hash._states[0]);
    }
}