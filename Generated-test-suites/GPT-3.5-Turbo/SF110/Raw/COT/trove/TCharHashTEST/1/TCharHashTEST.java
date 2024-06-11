
package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        assertFalse(hash.contains('a'));
        hash.insertKey('a');
        assertTrue(hash.contains('a'));
        assertFalse(hash.contains('b'));
    }

    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash(2);
        hash.insertKey('a');
        hash.insertKey('b');
        TCharProcedure procedure = new TCharProcedure() {
            int count = 0;

            public boolean execute(char value) {
                count++;
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(2, procedure.count);
    }

    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        assertTrue(hash.contains('a'));
        hash.removeAt(0);
        assertFalse(hash.contains('a'));
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        assertEquals(0, hash.index('a'));
        assertEquals(-1, hash.index('b'));
    }
}
