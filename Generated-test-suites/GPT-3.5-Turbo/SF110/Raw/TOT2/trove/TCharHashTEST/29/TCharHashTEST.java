import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {

    TCharHash tCharHash = new TCharHash(5);

    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', tCharHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        assertFalse(tCharHash.contains('A'));
    }

    @Test
    public void testForEach() {
        assertTrue(tCharHash.forEach((val) -> true));
    }

    @Test
    public void testRemoveAt() {
        tCharHash.removeAt(1);
        assertEquals('\0', tCharHash._set[1]);
    }

    @Test
    public void testInsertKey() {
        assertEquals(1, tCharHash.insertKey('A'));
    }

    @Test
    public void testIndex() {
        assertEquals(-1, tCharHash.index('B'));
    }

}