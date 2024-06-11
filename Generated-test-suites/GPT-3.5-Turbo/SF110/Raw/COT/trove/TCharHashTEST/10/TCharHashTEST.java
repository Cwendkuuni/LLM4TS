import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testContains() {
        TCharHash testHash = new TCharHash();
        testHash.insertKey('a');
        assertTrue(testHash.contains('a'));
        assertFalse(testHash.contains('b'));
    }

    @Test
    public void testForEach() {
        TCharHash testHash = new TCharHash();
        testHash.insertKey('a');
        testHash.insertKey('b');
        testHash.insertKey('c');
        TCharProcedureImpl procedure = new TCharProcedureImpl();
        assertTrue(testHash.forEach(procedure));
        assertEquals("abc", procedure.getResult());
    }

    @Test
    public void testRemoveAt() {
        TCharHash testHash = new TCharHash();
        testHash.insertKey('a');
        testHash.insertKey('b');
        testHash.removeAt(0);
        assertFalse(testHash.contains('a'));
        assertTrue(testHash.contains('b'));
    }

    @Test
    public void testIndex() {
        TCharHash testHash = new TCharHash();
        testHash.insertKey('a');
        testHash.insertKey('b');
        assertEquals(0, testHash.index('a'));
        assertEquals(1, testHash.index('b'));
        assertEquals(-1, testHash.index('c'));
    }

    @Test
    public void testInsertKey() {
        TCharHash testHash = new TCharHash();
        assertEquals(0, testHash.insertKey('a'));
        assertEquals(1, testHash.insertKey('b'));
        assertEquals(-1, testHash.insertKey('b'));
    }

    class TCharProcedureImpl implements TCharProcedure {

        private StringBuilder result = new StringBuilder();

        @Override
        public boolean execute(char value) {
            result.append(value);
            return true;
        }

        public String getResult() {
            return result.toString();
        }
    }

}