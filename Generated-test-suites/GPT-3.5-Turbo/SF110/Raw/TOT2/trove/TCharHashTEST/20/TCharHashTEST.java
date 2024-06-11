import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testConstructor1() {
        TCharHash tCharHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        TCharHash tCharHash = new TCharHash(initialCapacity);
        assertEquals(initialCapacity, tCharHash.capacity());
    }

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        char val = 'a';
        tCharHash.insertKey(val);
        assertTrue(tCharHash.contains(val));
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        char val = 'a';
        int index = tCharHash.insertKey(val);
        tCharHash.removeAt(index);
        assertFalse(tCharHash.contains(val));
    }

    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        char val = 'a';
        int index = tCharHash.insertKey(val);
        assertTrue(tCharHash.contains(val));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash();
        char val = 'a';
        for (int i = 0; i < 100; i++) {
            tCharHash.insertKey(val);
        }
        assertTrue(tCharHash.contains(val));
    }

    // Additional edge cases and error scenarios can be added here

}