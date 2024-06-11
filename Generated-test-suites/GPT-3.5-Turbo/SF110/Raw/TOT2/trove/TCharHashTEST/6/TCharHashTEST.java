import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    @Test
    public void testDefaultConstructor() {
        TCharHash hash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 100;
        TCharHash hash = new TCharHash(initialCapacity);
        assertEquals(initialCapacity, hash._set.length);
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 100;
        float loadFactor = 0.8f;
        TCharHash hash = new TCharHash(initialCapacity, loadFactor);
        assertEquals(initialCapacity, hash._set.length);
        assertEquals(loadFactor, hash._loadFactor, 0.001);
    }
    
    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        int initialCapacity = 100;
        float loadFactor = 0.8f;
        char noEntryValue = 'X';
        TCharHash hash = new TCharHash(initialCapacity, loadFactor, noEntryValue);
        assertEquals(initialCapacity, hash._set.length);
        assertEquals(loadFactor, hash._loadFactor, 0.001);
        assertEquals(noEntryValue, hash.getNoEntryValue());
    }

    // Additional test cases for other methods can be added here
    
    @Test
    public void testEdgeCases() {
        // Test for edge cases if applicable
    }
    
    @Test
    public void testExceptionHandling() {
        // Test for exception handling scenarios if applicable
    }
}