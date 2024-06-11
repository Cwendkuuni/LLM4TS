package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {

    private TCharHash hash;
    
    @Before
    public void setup() {
        hash = new TCharHash();
    }
    
    @Test
    public void testConstructor() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertFalse(hash.contains('a'));
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        hash = new TCharHash(10);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertFalse(hash.contains('a'));
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        hash = new TCharHash(10, 0.5f);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertFalse(hash.contains('a'));
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        hash = new TCharHash(10, 0.5f, 'z');
        assertEquals('z', hash.getNoEntryValue());
        assertFalse(hash.contains('a'));
    }
    
    @Test
    public void testSetUp() {
        hash.setUp(10);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertFalse(hash.contains('a'));
    }
    
    @Test
    public void testContains() {
        hash.insertKey('a');
        assertTrue(hash.contains('a'));
        assertFalse(hash.contains('b'));
    }
    
    @Test
    public void testForEach() {
        hash.insertKey('a');
        hash.insertKey('b');
        
        TCharProcedure procedure = (val) -> {
            System.out.println(val);
            return true;
        };
        
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        hash.insertKey('a');
        hash.removeAt(0);
        assertFalse(hash.contains('a'));
    }
    
    @Test
    public void testIndex() {
        hash.insertKey('a');
        assertEquals(0, hash.index('a'));
        assertEquals(-1, hash.index('b'));
    }
    
    @Test
    public void testIndexRehashed() {
        hash.insertKey('a');
        assertEquals(-1, hash.indexRehashed('b', 0, 0, (byte) 0));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey('a'));
        assertFalse(hash.contains('b'));
    }
    
    @Test
    public void testInsertKeyRehash() {
        hash.insertKey('a');
        assertEquals(-1, hash.insertKeyRehash('b', 0, 0, (byte) 0));
    }
    
    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 'a');
        assertTrue(hash.contains('a'));
    }
}