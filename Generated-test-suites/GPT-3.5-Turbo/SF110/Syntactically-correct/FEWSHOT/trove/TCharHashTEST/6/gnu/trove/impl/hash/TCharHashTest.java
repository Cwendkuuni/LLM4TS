package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {

    private TCharHash tcharHash;

    @Before
    public void setUp() {
        tcharHash = new TCharHash();
    }

    @Test
    public void testContains() {
        assertFalse(tcharHash.contains('a'));
        tcharHash.insertKey('a');
        assertTrue(tcharHash.contains('a'));
    }

    @Test
    public void testForEach() {
        TCharProcedureImpl procedure = new TCharProcedureImpl();
        tcharHash.insertKey('a');
        tcharHash.insertKey('b');
        assertTrue(tcharHash.forEach(procedure));
        assertTrue(procedure.isExecuted('a'));
        assertTrue(procedure.isExecuted('b'));
    }

    @Test
    public void testRemoveAt() {
        tcharHash.insertKey('a');
        tcharHash.removeAt(0);
        assertFalse(tcharHash.contains('a'));
    }

    @Test
    public void testIndex() {
        tcharHash.insertKey('a');
        assertEquals(0, tcharHash.index('a'));
        assertEquals(-1, tcharHash.index('b'));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, tcharHash.insertKey('a'));
        assertEquals(-1, tcharHash.insertKey('a'));
    }

    class TCharProcedureImpl implements TCharProcedure {

        private boolean executedA;
        private boolean executedB;

        @Override
        public boolean execute(char value) {
            if (value == 'a') {
                executedA = true;
            } else if (value == 'b') {
                executedB = true;
            }
            return true;
        }

        public boolean isExecuted(char value) {
            if (value == 'a') {
                return executedA;
            } else if (value == 'b') {
                return executedB;
            }
            return false;
        }

    }
}