import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testGetNoEntryValue() {
        TCharHash tCharHash = new TCharHash();
        assertEquals('\0', tCharHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TCharHash tCharHash = new TCharHash();
        assertEquals(10, tCharHash.setUp(10));
    }

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        assertFalse(tCharHash.contains('A'));
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        assertTrue(tCharHash.forEach(value -> true));
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.removeAt(2);
        assertEquals('\0', tCharHash._set[2]);
    }

    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        assertEquals(-1, tCharHash.index('A'));
    }

    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        assertEquals(0, tCharHash.insertKey('A'));
    }

}