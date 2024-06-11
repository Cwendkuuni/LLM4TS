import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testConstructorDefault() {
        TCharHash tCharHash = new TCharHash();
        char expected = '\0';
        assertEquals(expected, tCharHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        TCharHash tCharHash = new TCharHash(initialCapacity);
        char expected = '\0';
        assertEquals(expected, tCharHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor);
        char expected = '\0';
        assertEquals(expected, tCharHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        char noEntryValue = 'X';
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor, noEntryValue);
        assertEquals(noEntryValue, tCharHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TCharHash tCharHash = new TCharHash();
        tCharHash.setUp(initialCapacity);
        assertEquals(initialCapacity, tCharHash._set.length);
    }

    // Add more test cases here...

}