import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        assertFalse(tCharHash.contains('A'));
        assertFalse(tCharHash.contains('B'));
        assertFalse(tCharHash.contains('C'));

        tCharHash.insertKey('A');
        assertTrue(tCharHash.contains('A'));
        assertFalse(tCharHash.contains('B'));
        assertFalse(tCharHash.contains('C'));

        tCharHash.insertKey('B');
        assertTrue(tCharHash.contains('A'));
        assertTrue(tCharHash.contains('B'));
        assertFalse(tCharHash.contains('C'));

        tCharHash.insertKey('C');
        assertTrue(tCharHash.contains('A'));
        assertTrue(tCharHash.contains('B'));
        assertTrue(tCharHash.contains('C'));
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');

        StringBuilder result = new StringBuilder();
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                result.append(value);
                return true;
            }
        };

        assertTrue(tCharHash.forEach(procedure));
        assertEquals("ABC", result.toString());
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');

        assertTrue(tCharHash.contains('A'));
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('A'));

        assertTrue(tCharHash.contains('B'));
        tCharHash.removeAt(1);
        assertFalse(tCharHash.contains('B'));

        assertTrue(tCharHash.contains('C'));
        tCharHash.removeAt(2);
        assertFalse(tCharHash.contains('C'));
    }

    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');

        assertTrue(tCharHash.contains('A'));
        assertTrue(tCharHash.contains('B'));
        assertTrue(tCharHash.contains('C'));
    }
}