import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testGetNoEntryValue() {
        TCharHash tCharHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.setUp(10);
        assertEquals(10, tCharHash._set.length);
    }

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        assertFalse(tCharHash.contains('A'));

        tCharHash.insertKey('A');
        assertTrue(tCharHash.contains('A'));
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');

        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };

        assertTrue(tCharHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('A'));
    }

    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');

        assertEquals(0, tCharHash.index('A'));
        assertEquals(1, tCharHash.index('B'));
        assertEquals(-1, tCharHash.index('C'));
    }

    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        assertEquals(0, tCharHash.insertKey('A'));
        assertEquals(-1, tCharHash.insertKey('A'));
        assertEquals(1, tCharHash.insertKey('B'));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash(1, 1.0f);
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKeyAt(0, 'A');
        tCharHash.insertKeyAt(1, 'B');
        assertEquals('A', tCharHash._set[0]);
        assertEquals('B', tCharHash._set[1]);
    }
}