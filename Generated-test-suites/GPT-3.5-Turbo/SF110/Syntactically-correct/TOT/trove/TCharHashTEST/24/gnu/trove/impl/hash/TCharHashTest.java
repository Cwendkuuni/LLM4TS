import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testConstructor() {
        TCharHash hash = new TCharHash();
        assertNotNull(hash);
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        TCharHash hash = new TCharHash(10);
        assertNotNull(hash);
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TCharHash hash = new TCharHash(10, 0.75f);
        assertNotNull(hash);
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        TCharHash hash = new TCharHash(10, 0.75f, '\0');
        assertNotNull(hash);
    }
    
    @Test
    public void testGetNoEntryValue() {
        TCharHash hash = new TCharHash();
        char noEntryValue = hash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
    }
    
    @Test
    public void testSetUp() {
        TCharHash hash = new TCharHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertNotNull(hash._set);
    }
    
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        assertTrue(hash.contains('A'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.removeAt(0);
        assertFalse(hash.contains('A'));
    }
    
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        int index = hash.index('A');
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        int index = hash.insertKey('A');
        assertEquals(0, index);
        assertTrue(hash.contains('A'));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash(5);
        hash.insertKey('A');
        hash.insertKey('B');
        
        int index = hash.insertKeyRehash('C', 2, 2, (byte)1);
        assertEquals(0, index);
        assertTrue(hash.contains('C'));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashFull() {
        TCharHash hash = new TCharHash(1);
        hash.insertKey('A');
        
        hash.insertKeyRehash('B', 0, 0, (byte)1);
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        hash.insertKeyAt(0, 'A');
        assertTrue(hash.contains('A'));
    }
}