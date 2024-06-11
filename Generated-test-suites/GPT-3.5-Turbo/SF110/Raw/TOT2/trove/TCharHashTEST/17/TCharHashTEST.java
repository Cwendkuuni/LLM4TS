import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testConstructor1() {
        TCharHash charHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }

    @Test
    public void testConstructor3() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }

    @Test
    public void testConstructor4() {
        char customNoEntryValue = '#';
        TCharHash charHash = new TCharHash(10, 0.75f, customNoEntryValue);
        assertEquals(customNoEntryValue, charHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        assertTrue(charHash.contains('A'));
        assertFalse(charHash.contains('B'));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.insertKey('C');
        TCharProcedure procedure = value -> {
            System.out.println(value);
            return true;
        };
        assertTrue(charHash.forEach(procedure));
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        assertEquals(0, charHash.insertKey('A')); // duplicate key
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        charHash.removeAt(0);
        assertFalse(charHash.contains('A'));
    }

    // Additional test cases can be added for edge cases and exception handling scenarios

}