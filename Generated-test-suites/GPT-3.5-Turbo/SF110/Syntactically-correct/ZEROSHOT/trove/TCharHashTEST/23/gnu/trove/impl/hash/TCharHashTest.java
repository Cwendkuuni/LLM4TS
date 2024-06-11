import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');

        assertTrue(tCharHash.contains('a'));
        assertTrue(tCharHash.contains('b'));
        assertTrue(tCharHash.contains('c'));
        assertFalse(tCharHash.contains('d'));
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');

        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println("Value: " + value);
                return true;
            }
        };

        assertTrue(tCharHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');

        tCharHash.removeAt(1);

        assertFalse(tCharHash.contains('b'));
        assertTrue(tCharHash.contains('a'));
        assertTrue(tCharHash.contains('c'));
    }

    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');

        assertEquals(tCharHash.index('a'), 0);
        assertEquals(tCharHash.index('b'), 1);
        assertEquals(tCharHash.index('c'), 2);
        assertEquals(tCharHash.index('d'), -1);
    }

    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        int index1 = tCharHash.insertKey('a');
        int index2 = tCharHash.insertKey('b');

        assertEquals(tCharHash._set[index1], 'a');
        assertEquals(tCharHash._set[index2], 'b');
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TCharHash tCharHash = new TCharHash(2, 0.75f);
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
    }

}