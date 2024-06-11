import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testConstructorNoArg() {
        TCharHash tCharHash = new TCharHash();
        assertNotNull(tCharHash);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        TCharHash tCharHash = new TCharHash(initialCapacity);
        assertNotNull(tCharHash);
        assertEquals(initialCapacity, tCharHash.capacity());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.8f;
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor);
        assertNotNull(tCharHash);
        assertEquals(initialCapacity, tCharHash.capacity());
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        int initialCapacity = 10;
        float loadFactor = 0.8f;
        char noEntryValue = 'N';
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor, noEntryValue);
        assertNotNull(tCharHash);
        assertEquals(noEntryValue, tCharHash.getNoEntryValue());
    }

    // Add more test cases for other methods to cover typical use cases, edge cases, and error scenarios

}