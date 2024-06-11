import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TCharHashTest {

    private TCharHash tCharHash;

    @Before
    public void setUp() {
        tCharHash = new TCharHash();
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        assertFalse(tCharHash.contains('a'));
        tCharHash.insertKey('a');
        assertTrue(tCharHash.contains('a'));
    }

    @Test
    public void testForEach() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        TCharProcedure procedure = value -> {
            if (value == 'a') {
                return false;
            }
            return true;
        };
        assertFalse(tCharHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('a');
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('a'));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, tCharHash.insertKey('a'));
        assertEquals(1, tCharHash.insertKey('b'));
        assertNotEquals(-1, tCharHash.insertKey('a'));
    }

    // Edge Cases and Exception Handling

    @Test
    public void testSetUpEdgeCase() {
        tCharHash.setUp(0);
        assertEquals(10, tCharHash._set.length);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashExceptionHandling() {
        try {
            for (int i = 0; i < 10; i++) {
                tCharHash.insertKey((char) i);
            }
        } catch (IllegalStateException e) {
            fail("InsertKeyRehash should not throw IllegalStateException");
        }
        tCharHash.insertKey((char) 10); // This should throw IllegalStateException
    }

}