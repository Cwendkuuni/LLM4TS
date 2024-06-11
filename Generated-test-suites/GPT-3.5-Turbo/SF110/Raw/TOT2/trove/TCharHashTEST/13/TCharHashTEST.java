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
        tCharHash.insertKey('a');
        assertTrue(tCharHash.contains('a'));
        assertFalse(tCharHash.contains('b'));
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        TCharProcedure procedure = value -> {
            System.out.println(value);
            return true;
        };
        assertTrue(tCharHash.forEach(procedure));
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
        assertEquals(0, tCharHash.insertKey('a'));
        assertEquals(-1, tCharHash.insertKey('a'));
        assertEquals(1, tCharHash.insertKey('b'));
    }
}