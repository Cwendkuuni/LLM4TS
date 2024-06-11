import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {
    
    @Test
    public void testConstructor() {
        TShortHash tShortHash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash(initialCapacity);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short noEntryValue = 0;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor, noEntryValue);
        assertEquals(noEntryValue, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash();
        int capacity = tShortHash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
    }
    
    @Test
    public void testContains() {
        short val = 5;
        TShortHash tShortHash = new TShortHash();
        boolean contains = tShortHash.contains(val);
        assertFalse(contains);
    }
    
    @Test
    public void testForEach() {
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return true;
            }
        };
        TShortHash tShortHash = new TShortHash();
        boolean result = tShortHash.forEach(procedure);
        assertTrue(result);
    }
    
    @Test
    public void testRemoveAt() {
        int index = 5;
        short noEntryValue = 0;
        TShortHash tShortHash = new TShortHash();
        tShortHash.removeAt(index);
        assertEquals(noEntryValue, tShortHash._set[index]);
    }
    
    @Test
    public void testIndex() {
        short val = 5;
        TShortHash tShortHash = new TShortHash();
        int index = tShortHash.index(val);
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexRehashed() {
        short key = 5;
        int index = 5;
        int hash = 10;
        byte state = 1;
        TShortHash tShortHash = new TShortHash();
        int result = tShortHash.indexRehashed(key, index, hash, state);
        assertEquals(-1, result);
    }
    
    @Test
    public void testInsertKey() {
        short val = 5;
        TShortHash tShortHash = new TShortHash();
        int index = tShortHash.insertKey(val);
        assertEquals(-1, index);
        assertFalse(tShortHash.consumeFreeSlot);
    }
    
    @Test
    public void testInsertKeyRehash() {
        short val = 5;
        int index = 5;
        int hash = 10;
        byte state = 1;
        TShortHash tShortHash = new TShortHash();
        int result = tShortHash.insertKeyRehash(val, index, hash, state);
        assertEquals(-1, result);
    }
    
    @Test
    public void testInsertKeyAt() {
        int index = 5;
        short val = 10;
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKeyAt(index, val);
        assertEquals(val, tShortHash._set[index]);
        assertEquals(1, tShortHash._states[index]);
    }
}