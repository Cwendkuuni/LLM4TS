package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {

    private TCharHash tCharHash;

    @Before
    public void setUp() {
        tCharHash = new TCharHash();
    }

    @Test
    public void testContains() {
        assertFalse(tCharHash.contains('a'));
        assertFalse(tCharHash.contains('z'));
    }

    @Test
    public void testForEach() {
        TCharProcedureMock procedure = new TCharProcedureMock();
        assertTrue(tCharHash.forEach(procedure));
        assertFalse(procedure.isExecuted());
    }

    @Test
    public void testRemoveAt() {
        tCharHash._set = new char[]{};
        tCharHash._states = new byte[]{};
        tCharHash.removeAt(0);
    }

    @Test
    public void testIndex() {
        tCharHash._set = new char[]{};
        tCharHash._states = new byte[]{};
        assertEquals(-1, tCharHash.index('a'));
        assertEquals(-1, tCharHash.index('z'));
    }

    @Test
    public void testInsertKey() {
        tCharHash._set = new char[]{};
        tCharHash._states = new byte[]{};
        assertEquals(0, tCharHash.insertKey('a'));
    }

    class TCharProcedureMock implements TCharProcedure {

        private boolean executed;

        public boolean execute(char value) {
            executed = true;
            return true;
        }

        public boolean isExecuted() {
            return executed;
        }
    }
}