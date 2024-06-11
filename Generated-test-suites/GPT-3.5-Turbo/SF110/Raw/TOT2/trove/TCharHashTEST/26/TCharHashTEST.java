
import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    @Test
    public void testConstructorNoArgs() {
        TCharHash charHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        TCharHash charHash = new TCharHash(initialCapacity);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.5f;
        TCharHash charHash = new TCharHash(initialCapacity, loadFactor);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        int initialCapacity = 10;
        float loadFactor = 0.5f;
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash(initialCapacity, loadFactor, noEntryValue);
        assertEquals(noEntryValue, charHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TCharHash charHash = new TCharHash();
        charHash.setUp(initialCapacity);
        assertEquals(initialCapacity, charHash._set.length);
    }
    
    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        char val = 'A';
        assertTrue(charHash.contains(val));
    }
    
    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        charHash._set = new char[]{'A', 'B', 'C'};
        
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return value != 'A';
            }
        };
        
        assertFalse(charHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        char val = 'A';
        int index = charHash.insertKey(val);
        charHash.removeAt(index);
        assertFalse(charHash.contains(val));
    }
    
    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash();
        char val = 'A';
        int index = charHash.insertKey(val);
        assertEquals(index, charHash.index(val));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        char val = 'A';
        int index = charHash.insertKey(val);
        assertEquals(index, charHash.index(val));
    }
}

