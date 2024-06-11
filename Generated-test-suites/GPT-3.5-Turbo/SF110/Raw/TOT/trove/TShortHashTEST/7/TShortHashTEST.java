
import static org.junit.Assert.*;
import org.junit.*;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash();
    }

    @Test
    public void testNoEntryValue() {
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testContainsWhenNotPresent() {
        assertFalse(tShortHash.contains((short) 10));
    }

    @Test
    public void testContainsWhenPresent() {
        tShortHash.insertKey((short) 10);
        assertTrue(tShortHash.contains((short) 10));
    }

    @Test
    public void testInsertKeyWhenNotPresent() {
        int index = tShortHash.insertKey((short) 10);
        assertEquals(0, index);
        assertTrue(tShortHash.contains((short) 10));
    }

    @Test
    public void testInsertKeyWhenAlreadyPresent() {
        tShortHash.insertKey((short) 10);
        int index = tShortHash.insertKey((short) 10);
        assertEquals(-1, index);
    }

    @Test
    public void testForEachProcedure() {
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 20);
        TShortProcedure procedure = new TShortProcedure() {
            int count = 0;

            public boolean execute(short value) {
                count++;
                return true;
            }

            public int getCount() {
                return count;
            }
        };
        assertTrue(tShortHash.forEach(procedure));
        assertEquals(2, procedure.getCount());
    }

    @Test
    public void testRemoveAt() {
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 20);
        tShortHash.removeAt(0);
        assertFalse(tShortHash.contains((short) 10));
        assertTrue(tShortHash.contains((short) 20));
    }

    @Test
    public void testIndexWhenNotPresent() {
        int index = tShortHash.index((short) 10);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexWhenPresent() {
        tShortHash.insertKey((short) 10);
        int index = tShortHash.index((short) 10);
        assertEquals(0, index);
    }

    @Test
    public void testIndexRehashedWhenNotPresent() {
        int index = tShortHash.indexRehashed((short) 10, 0, 0, (byte) 0);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashedWhenPresent() {
        tShortHash.insertKey((short) 10);
        int index = tShortHash.indexRehashed((short) 10, 0, 0, (byte) 1);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyRehashWhenNotPresent() {
        int index = tShortHash.insertKeyRehash((short) 10, 0, 0, (byte) 0);
        assertEquals(0, index);
        assertTrue(tShortHash.contains((short) 10));
    }

    @Test
    public void testInsertKeyRehashWhenAlreadyPresent() {
        tShortHash.insertKey((short) 10);
        int index = tShortHash.insertKeyRehash((short) 10, 0, 0, (byte) 1);
        assertEquals(-1, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashWhenSetFull() {
        // Filling the set with elements
        for (short i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
            tShortHash.insertKey(i);
        }
        // Trying to insert one more element when set is already full
        tShortHash.insertKeyRehash(1000, 0, 0, (byte) 2);
    }

    @Test
    public void testInsertKeyAt() {
        tShortHash.insertKeyAt(0, (short) 10);
        assertTrue(tShortHash.contains((short) 10));
    }
}

