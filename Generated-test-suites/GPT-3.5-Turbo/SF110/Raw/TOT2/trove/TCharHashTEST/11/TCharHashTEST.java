import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    @Test
    public void testConstructorNoArgs() {
        TCharHash charHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
        // Add more assertions if needed
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        TCharHash charHash = new TCharHash(initialCapacity);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
        // Add more assertions if needed
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.6f;
        TCharHash charHash = new TCharHash(initialCapacity, loadFactor);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
        // Add more assertions if needed
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        int initialCapacity = 10;
        float loadFactor = 0.6f;
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash(initialCapacity, loadFactor, noEntryValue);
        assertEquals(noEntryValue, charHash.getNoEntryValue());
        // Add more assertions if needed
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TCharHash charHash = new TCharHash();
        int result = charHash.setUp(initialCapacity);
        assertEquals(initialCapacity, charHash._set.length);
        assertEquals(initialCapacity, result);
        // Add more assertions if needed
    }
    
    // Add more test cases for other methods
    
}