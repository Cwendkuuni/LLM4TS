import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    private TCharHash tCharHash;
    
    @Before
    public void setUp() {
        tCharHash = new TCharHash();
    }
    
    @Test
    public void testConstructorWithNoArgs() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        tCharHash = new TCharHash(10);
        assertEquals(10, tCharHash._set.length);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        tCharHash = new TCharHash(10, 0.7f);
        assertEquals(10, tCharHash._set.length);
        assertEquals(0.7f, tCharHash.loadFactor, 0.001);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        tCharHash = new TCharHash(10, 0.7f, '#');
        assertEquals(10, tCharHash._set.length);
        assertEquals(0.7f, tCharHash.loadFactor, 0.001);
        assertEquals('#', tCharHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        int capacity = tCharHash.setUp(10);
        assertEquals(10, capacity);
        assertEquals(10, tCharHash._set.length);
    }
    
    @Test
    public void testContains() {
        assertFalse(tCharHash.contains('a'));
        
        tCharHash._set[0] = 'a';
        assertTrue(tCharHash.contains('a'));
        
        tCharHash._states[0] = 2;
        assertFalse(tCharHash.contains('a'));
    }
    
    @Test
    public void testForEach() {
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                if (value == 'a') {
                    return false;
                }
                return true;
            }
        };
        
        assertFalse(tCharHash.forEach(procedure));
        
        tCharHash._set[0] = 'a';
        assertFalse(tCharHash.forEach(procedure));
        
        tCharHash._set[0] = 'b';
        assertTrue(tCharHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        tCharHash._set[0] = 'a';
        tCharHash.removeAt(0);
        assertEquals('#', tCharHash._set[0]);
        
        tCharHash._states[0] = 2;
        tCharHash.removeAt(0);
        assertEquals('#', tCharHash._set[0]);
    }
    
    @Test
    public void testIndex() {
        tCharHash._set[0] = 'a';
        assertEquals(0, tCharHash.index('a'));
        
        tCharHash._states[0] = 2;
        assertEquals(-1, tCharHash.index('a'));
    }
    
    @Test
    public void testIndexRehashed() {
        tCharHash._set[0] = 'a';
        assertEquals(-1, tCharHash.indexRehashed('a', 0, 0, (byte) 0));
        
        tCharHash._set[0] = 'a';
        tCharHash._states[0] = 1;
        assertEquals(0, tCharHash.indexRehashed('a', 0, 0, (byte) 1));
        
        tCharHash._set[0] = 'a';
        tCharHash._set[1] = 'b';
        tCharHash._states[0] = 1;
        tCharHash._states[1] = 1;
        assertEquals(1, tCharHash.indexRehashed('b', 0, 1, (byte) 1));
        
        tCharHash._set[0] = 'a';
        tCharHash._set[1] = 'b';
        tCharHash._states[0] = 1;
        tCharHash._states[1] = 2;
        assertEquals(1, tCharHash.indexRehashed('b', 0, 1, (byte) 2));
    }
    
    @Test
    public void testInsertKey() {
        tCharHash._set[0] = 'a';
        tCharHash._states[0] = 1;
        assertEquals(-1, tCharHash.insertKey('a'));
        
        tCharHash._set[0] = 'a';
        assertEquals(0, tCharHash.insertKey('a'));
        
        tCharHash._set[0] = 'a';
        tCharHash._set[1] = 'b';
        tCharHash._states[0] = 1;
        tCharHash._states[1] = 2;
        assertEquals(1, tCharHash.insertKey('b'));
        
        tCharHash._set[0] = 'a';
        tCharHash._set[1] = 'b';
        tCharHash._states[0] = 2;
        tCharHash._states[1] = 1;
        assertEquals(-1, tCharHash.insertKey('b'));
    }
    
    @Test
    public void testInsertKeyRehash() {
        tCharHash._set[0] = 'a';
        tCharHash._states[0] = 1;
        assertEquals(-1, tCharHash.insertKeyRehash('a', 0, 0, (byte) 0));
        
        tCharHash._set[0] = 'a';
        tCharHash._states[0] = 1;
        assertEquals(0, tCharHash.insertKeyRehash('a', 0, 0, (byte) 1));
        
        tCharHash._set[0] = 'a';
        tCharHash._set[1] = 'b';
        tCharHash._states[0] = 1;
        tCharHash._states[1] = 1;
        assertEquals(1, tCharHash.insertKeyRehash('b', 0, 1, (byte) 1));
        
        tCharHash._set[0] = 'a';
        tCharHash._set[1] = 'b';
        tCharHash._states[0] = 2;
        tCharHash._states[1] = 1;
        assertEquals(1, tCharHash.insertKeyRehash('b', 0, 1, (byte) 2));
        
        tCharHash._set[0] = 'a';
        tCharHash._set[1] = 'b';
        tCharHash._states[0] = 0;
        tCharHash._states[1] = 1;
        assertEquals(0, tCharHash.insertKeyRehash('b', 0, 1, (byte) 0));
        
        tCharHash._set[0] = 'a';
        tCharHash._set[1] = 'b';
        tCharHash._states[0] = 1;
        tCharHash._states[1] = 2;
        assertEquals(-1, tCharHash.insertKeyRehash('b', 0, 1, (byte) 2));
    }
    
    @Test
    public void testInsertKeyAt() {
        tCharHash.insertKeyAt(0, 'a');
        assertEquals('a', tCharHash._set[0]);
        assertEquals(1, tCharHash._states[0]);
    }
}