import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {
    private TCharHash testHash;

    @Before
    public void setUp() {
        testHash = new TCharHash();
    }

    @Test
    public void testContains() {
        assertFalse(testHash.contains('a'));
        testHash.insertKey('a');
        assertTrue(testHash.contains('a'));
    }

    @Test
    public void testForEach() {
        testHash.insertKey('a');
        testHash.insertKey('b');
        testHash.insertKey('c');

        StringBuilder sb = new StringBuilder();
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char val) {
                sb.append(val);
                return true;
            }
        };

        assertTrue(testHash.forEach(procedure));
        assertEquals("abc", sb.toString());
    }

    @Test
    public void testRemoveAt() {
        testHash.insertKey('a');
        testHash.removeAt(0);
        assertFalse(testHash.contains('a'));
    }

    @Test
    public void testIndex() {
        assertEquals(-1, testHash.index('a'));
        testHash.insertKey('a');
        assertEquals(0, testHash.index('a'));
        assertEquals(-1, testHash.index('b'));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, testHash.insertKey('a'));
        assertEquals(-1, testHash.insertKey('a'));
    }
    
}