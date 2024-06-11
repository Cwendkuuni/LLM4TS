import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHash() {
        TCharHash tCharHash = new TCharHash();
        assertNotNull(tCharHash);
    }

    @Test
    public void testTCharHashWithInitialCapacity() {
        TCharHash tCharHash = new TCharHash(10);
        assertNotNull(tCharHash);
    }

    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactor() {
        TCharHash tCharHash = new TCharHash(10, 0.75f);
        assertNotNull(tCharHash);
    }

    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        TCharHash tCharHash = new TCharHash(10, 0.75f, 'X');
        assertNotNull(tCharHash);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash tCharHash = new TCharHash();
        char noEntryValue = tCharHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
    }

    @Test
    public void testSetUp() {
        TCharHash tCharHash = new TCharHash();
        int capacity = tCharHash.setUp(10);
        char[] set = tCharHash._set;
        assertEquals(10, capacity);
        assertNotNull(set);
        assertEquals(10, set.length);
    }

    @Test
    public void testContainsWhenValueExists() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        boolean contains = tCharHash.contains('A');
        assertTrue(contains);
    }

    @Test
    public void testContainsWhenValueDoesNotExist() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        boolean contains = tCharHash.contains('B');
        assertFalse(contains);
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        tCharHash.insertKey('D');

        TCharProcedure procedure = new TCharProcedure() {
            StringBuilder stringBuilder = new StringBuilder();

            public boolean execute(char value) {
                stringBuilder.append(value);
                return true;
            }

            public String getResult() {
                return stringBuilder.toString();
            }
        };

        boolean success = tCharHash.forEach(procedure);
        assertTrue(success);
        assertEquals("ABCD", procedure.getResult());
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        tCharHash.insertKey('D');

        tCharHash.removeAt(1);

        boolean contains = tCharHash.contains('B');
        assertFalse(contains);
    }

    @Test
    public void testIndexWhenValueExists() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        int index = tCharHash.index('A');
        assertEquals(0, index);
    }

    @Test
    public void testIndexWhenValueDoesNotExist() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        int index = tCharHash.index('B');
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyWhenSlotAvailable() {
        TCharHash tCharHash = new TCharHash();
        int index = tCharHash.insertKey('A');
        assertEquals(0, index);
        boolean contains = tCharHash.contains('A');
        assertTrue(contains);
    }

    @Test
    public void testInsertKeyWhenSlotNotAvailable() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        tCharHash.insertKey('D');
        tCharHash.insertKey('E');
        tCharHash.insertKey('F');
        tCharHash.insertKey('G');
        tCharHash.insertKey('H');
        tCharHash.insertKey('I');
        tCharHash.insertKey('J');
        tCharHash.insertKey('K');
        int index = tCharHash.insertKey('L');
        assertEquals(11, index);
        boolean contains = tCharHash.contains('L');
        assertTrue(contains);
    }

}