import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testNoEntryValue() {
        TCharHash tCharHash = new TCharHash();
        assertEquals('\0', tCharHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TCharHash tCharHash = new TCharHash();
        int capacity = tCharHash.setUp(10);
        assertEquals(10, capacity);
    }

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');

        assertTrue(tCharHash.contains('a'));
        assertFalse(tCharHash.contains('b'));
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.removeAt(0);

        assertFalse(tCharHash.contains('a'));
    }

    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');

        assertTrue(tCharHash.contains('a'));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash(10, 0.75f, 'z');
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        tCharHash.insertKey('d');
        tCharHash.insertKey('e');
        tCharHash.insertKey('f');
        tCharHash.insertKey('g');
        tCharHash.insertKey('h');
        tCharHash.insertKey('i');
        tCharHash.insertKey('j');
        tCharHash.insertKey('k');

        assertTrue(tCharHash.contains('k'));
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKeyAt(0, 'a');

        assertTrue(tCharHash.contains('a'));
    }
}