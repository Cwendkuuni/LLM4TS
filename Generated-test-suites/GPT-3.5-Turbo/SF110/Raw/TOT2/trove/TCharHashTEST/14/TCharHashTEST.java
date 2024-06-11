import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testDefaultConstructor() {
        TCharHash tCharHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        TCharHash tCharHash = new TCharHash(10);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TCharHash tCharHash = new TCharHash(10, 0.75f);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }

    @Test
    public void insertKey() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        assertTrue(tCharHash.contains('a'));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash();
        for (int i = 0; i < 50; i++) {
            tCharHash.insertKey((char)i);
        }
        assertTrue(tCharHash.contains((char)25));
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        assertTrue(tCharHash.contains('a'));
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('a'));
    }

    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        assertEquals(0, tCharHash.index('a'));
    }

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        assertTrue(tCharHash.contains('a'));
    }
}