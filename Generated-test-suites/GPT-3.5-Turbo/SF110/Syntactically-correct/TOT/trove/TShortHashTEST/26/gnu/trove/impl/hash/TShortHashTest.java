import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        short noEntryValue = hash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 5);
        boolean contains = hash.contains((short) 5);
        assertTrue(contains);
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 5);
        hash.insertKey((short) 10);
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                System.out.println(value);
                return true;
            }
        };
        boolean success = hash.forEach(procedure);
        assertTrue(success);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 5);
        hash.removeAt(0);
        assertFalse(hash.contains((short) 5));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 5);
        int index = hash.index((short) 5);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        int index = hash.insertKey((short) 5);
        assertEquals(0, index);

        index = hash.insertKey((short) 10);
        assertEquals(1, index);

        index = hash.insertKey((short) 5); // Duplicate key
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 5);
        hash.insertKey((short) 10);
        int index = hash.insertKeyRehash((short) 15, 0,
                HashFunctions.hash((short) 15) & Integer.MAX_VALUE, (byte) 2);
        assertEquals(0, index);

        index = hash.insertKeyRehash((short) 15, 0,
                HashFunctions.hash((short) 15) & Integer.MAX_VALUE, (byte) 1); // Duplicate key
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash();
        hash.insertKeyAt(0, (short) 5);
        assertTrue(hash.contains((short) 5));
        assertFalse(hash.contains((short) 0));
    }
}